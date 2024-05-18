package cn.clboy.clkit.app.service.impl;

import cn.clboy.clkit.app.dto.ExportDataDTO;
import cn.clboy.clkit.app.handler.AppDataHandler;
import cn.clboy.clkit.app.service.AppService;
import cn.clboy.clkit.common.constants.DictConstant;
import cn.clboy.clkit.common.constants.DictDeclare;
import cn.clboy.clkit.common.constants.enums.IValueLabelEnum;
import cn.clboy.clkit.common.vo.ValueLabelVO;
import cn.clboy.clkit.config.AppProperties;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.net.URLEncodeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 应用服务实施
 *
 * @author clboy
 * @date 2024/04/28 16:51:48
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService, InitializingBean {

    private final ObjectMapper objectMapper;
    private final AppProperties appProperties;
    private final TransactionTemplate transactionTemplate;

    @Override
    @SneakyThrows
    public void exportData(List<ExportDataDTO> dtoList, HttpServletResponse response) {
        Map<String, List> modelExportDataMap = new HashMap<>();
        if (CollectionUtils.isEmpty(dtoList)) {
            //导出全站数据
            List<AppDataHandler> handlers = AppDataHandler.HOLDER.getHandlers();
            for (AppDataHandler handler : handlers) {
                List moduleData = handler.exportData(null);
                if (!CollectionUtils.isEmpty(moduleData)) {
                    modelExportDataMap.put(handler.getModuleName(), moduleData);
                }
            }
        } else {
            for (ExportDataDTO dto : dtoList) {
                AppDataHandler<?> handler = AppDataHandler.HOLDER.getHandler(dto.getModule());
                List moduleData = handler.exportData(dto.getIds());
                if (CollectionUtils.isEmpty(moduleData)) {
                    modelExportDataMap.put(handler.getModuleName(), moduleData);
                }
            }
        }
        byte[] data = objectMapper.writeValueAsBytes(modelExportDataMap);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        String time = LocalDateTime.now().format(DatePattern.PURE_DATETIME_FORMATTER);
        String filename = URLEncodeUtil.encode(String.format("clkit-%s.json", time));
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=" + filename);
        response.getOutputStream().write(data);
    }

    @Override
    @SneakyThrows
    public void importData(MultipartFile file) {
        JsonNode jsonNode = objectMapper.readTree(file.getBytes());
        Map<AppDataHandler, List> handlerDataListMap = new HashMap<>();
        jsonNode.fieldNames().forEachRemaining(module -> {
            JsonNode moduleNode = jsonNode.get(module);
            Assert.isTrue(moduleNode.isArray(), "导入数据格式有无");
            AppDataHandler handler = AppDataHandler.HOLDER.getHandler(module);
            if (handler == null) {
                return;
            }
            ArrayNode arrayModuleNode = (ArrayNode) moduleNode;
            List dataList = new ArrayList(arrayModuleNode.size());
            arrayModuleNode.forEach(dataNode -> {
                try {
                    dataList.add(objectMapper.treeToValue(dataNode, handler.getExportModelClass()));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
            if (!CollectionUtils.isEmpty(dataList)) {
                handlerDataListMap.put(handler, dataList);
            }
        });

        transactionTemplate.executeWithoutResult(ac -> {
            handlerDataListMap.forEach(AppDataHandler::importData);
        });
    }

    @Override
    public String getVersion() {
        return appProperties.getVersion();
    }

    @Override
    public Map<String, List<ValueLabelVO>> getDict(String types) {
        if (!StringUtils.hasText(types)) {
            return Collections.emptyMap();
        }
        Set<String> typeSet = StringUtils.commaDelimitedListToSet(types);
        Map<String, List<ValueLabelVO>> dict = CollectionUtils.newHashMap(typeSet.size());
        for (String type : typeSet) {
            DictDeclare dictDeclare = DictConstant.DICT_MAP.get(type);
            if (dictDeclare != null) {
                dict.put(type, IValueLabelEnum.toValueLabelVO(dictDeclare.getEnumClass()));
            }
        }
        return dict;
    }

    @Override
    public String getDictJsConstDeclare() {
        List<String> statement = new ArrayList<>();
        DictConstant.DICT_MAP.forEach((type, dc) -> {
            StringJoiner joiner = new StringJoiner("<br/>");
            joiner.add("/**");
            joiner.add(" * " + dc.getDesc());
            joiner.add(" */");
            joiner.add(String.format("export const DICT_%s = \"%s\";", type.toUpperCase(), type));
            statement.add(joiner.toString());
        });
        return String.join("<br/><br/>", statement);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //扫描所有字典加载类
        log.debug("开始扫描字典");
        String enumPackage = IValueLabelEnum.class.getPackage().getName();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(IValueLabelEnum.class));
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(enumPackage);
        for (BeanDefinition component : candidateComponents) {
            log.debug("加载字典类枚举: {}", component.getBeanClassName());
            Class.forName(component.getBeanClassName());
        }
    }
}
