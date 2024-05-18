package cn.clboy.clkit.job.service.impl;

import cn.clboy.clkit.common.constants.enums.JobReminderTimeEnum;
import cn.clboy.clkit.common.constants.enums.JobRepeatModeEnum;
import cn.clboy.clkit.common.constants.enums.TodoStatusEnum;
import cn.clboy.clkit.common.service.AppDataHandlerCrudServiceImpl;
import cn.clboy.clkit.job.entity.Todo;
import cn.clboy.clkit.job.query.TodoQuery;
import cn.clboy.clkit.job.repository.TodoRepository;
import cn.clboy.clkit.job.service.TodoService;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 待办服务impl
 *
 * @author clboy
 * @date 2024/05/10 15:30:09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl extends AppDataHandlerCrudServiceImpl<Todo, TodoRepository> implements TodoService {

    private final TransactionTemplate transactionTemplate;

    @Override
    public Page<Todo> getPageByQuery(Pageable page, TodoQuery query) {
        return this.repository.findAll(builder -> {
            if (StringUtils.hasText(query.getStatus())) {
                builder.equal(Todo::getStatus, query.getStatus());
            }
            if (query.getStartDeadlineDate() != null) {
                builder.greaterThanOrEqualTo(Todo::getDeadlineTime, query.getStartDeadlineDate().atTime(LocalTime.MIN));
            }
            if (query.getEndDeadlineDate() != null) {
                builder.lessThan(Todo::getDeadlineTime, query.getEndDeadlineDate().plusDays(1).atTime(LocalTime.MIN));
            }
        }, page);
    }

    @Override
    public Todo save(Todo dto) {
        dto.setReminderTime(dto.getReminder().getCalculator().calculate(dto.getDeadlineTime()).orElse(null));
        dto.setFamilyId(IdUtil.fastSimpleUUID());
        dto.setStatus(this.initialStatus(dto.getDeadlineTime()));
        return super.save(dto);
    }

    @Override
    public Todo updateById(Todo dto) {
        dto.setReminderTime(dto.getReminder().getCalculator().calculate(dto.getDeadlineTime()).orElse(null));
        return super.updateById(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateStatus(Long id, Boolean isDone) {
        TodoStatusEnum status = Boolean.TRUE.equals(isDone) ? TodoStatusEnum.DONE : TodoStatusEnum.UNDONE;
        Todo todo = getById(id);
        this.updateStatus(todo, status);
        return todo.getStatus().getValue();
    }

    /**
     * 扫描提醒
     */
    @Scheduled(cron = "0 * * * * ?")
    public void scanReminder() {
        List<Todo> reminderList = this.repository.findAll(builder -> {
            LocalTime nowTime = LocalTime.now();
            LocalDateTime now = LocalDate.now().atTime(LocalTime.of(nowTime.getHour(), nowTime.getMinute()));
            builder.equal(Todo::getStatus, TodoStatusEnum.UNDONE);
            builder.equal(Todo::getReminderTime, now);
        });
        log.debug("扫描未完成需提醒的待办，数量：{}", reminderList.size());
        for (Todo todo : reminderList) {
            log.warn("你有未完成的待办：{}", todo.getName());
        }
    }

    /**
     * 扫描已过期
     */
    @Scheduled(cron = "0 * * * * ?")
    public void scanExpired() {
        List<Todo> expiredList = this.repository.findAll(builder -> {
            builder.equal(Todo::getStatus, TodoStatusEnum.UNDONE);
            builder.lessThan(Todo::getDeadlineTime, LocalDateTime.now());
        });
        log.debug("扫描已过期未完成的待办，数量：{}", expiredList.size());
        if (CollectionUtils.isEmpty(expiredList)) {
            return;
        }
        for (Todo todo : expiredList) {
            if (todo.getReminder() != JobReminderTimeEnum.PUNCTUAL) {
                //正点由提醒处理
                log.warn("你的待办 \"{}\" 已过截止日期", todo.getName());
            }
            try {
                transactionTemplate.executeWithoutResult(ac -> {
                    this.updateStatus(todo, TodoStatusEnum.EXPIRED);
                });
            } catch (Exception ex) {
                log.error("处理过期待办失败，待办id：{}", todo.getId(), ex);
            }
        }

    }

    /**
     * 更新状态
     *
     * @param todo   待办
     * @param status 状态
     */
    private void updateStatus(Todo todo, TodoStatusEnum status) {
        TodoStatusEnum beforeStatus = todo.getStatus();
        if (status == beforeStatus) {
            return;
        }

        if (status == TodoStatusEnum.UNDONE) {
            todo.setStatus(this.initialStatus(todo.getDeadlineTime()));
            this.repository.save(todo);
            return;
        }

        todo.setStatus(status);
        JobRepeatModeEnum currentRepeat = todo.getRepeat();
        LocalDateTime currentRepeatStopTime = todo.getRepeatStopTime();
        //当期任务取消重复，重复工作由新产生任务延续
        this.clearRepeat(todo);
        this.repository.save(todo);
        currentRepeat.getCalculator().calculate(todo.getDeadlineTime()).ifPresent(nextDeadlineTime -> {
            Todo nextTodo = new Todo();
            nextTodo.setFamilyId(todo.getFamilyId());
            nextTodo.setName(todo.getName());
            nextTodo.setRemark(todo.getRemark());
            nextTodo.setRepeat(JobRepeatModeEnum.NOT);
            //计算下次任务重复规则
            currentRepeat.getCalculator().calculate(nextDeadlineTime)
                    .filter(time -> currentRepeatStopTime == null || !time.isAfter(todo.getRepeatStopTime()))
                    .ifPresent(time -> {
                        nextTodo.setRepeat(currentRepeat);
                        nextTodo.setRepeatStopTime(todo.getRepeatStopTime());
                    });
            nextTodo.setReminder(todo.getReminder());
            nextTodo.setDeadlineTime(nextDeadlineTime);
            todo.getReminder().getCalculator().calculate(nextDeadlineTime).ifPresent(nextTodo::setReminderTime);
            nextTodo.setStatus(TodoStatusEnum.UNDONE);
            this.repository.save(nextTodo);
        });


    }

    /**
     * 初始状态
     *
     * @param deadlineTime 截止期限时间
     */
    private TodoStatusEnum initialStatus(LocalDateTime deadlineTime) {
        //当前时间>deadlineTime是已过期
        LocalDateTime now = LocalDate.now().atTime(LocalTime.of(deadlineTime.getHour(), deadlineTime.getMinute()));
        return now.isAfter(deadlineTime) ? TodoStatusEnum.EXPIRED : TodoStatusEnum.UNDONE;
    }

    /**
     * 清除重复
     *
     * @param todo 待办
     */
    private void clearRepeat(Todo todo) {
        todo.setRepeat(JobRepeatModeEnum.NOT);
        todo.setRepeatStopTime(null);
    }

}
