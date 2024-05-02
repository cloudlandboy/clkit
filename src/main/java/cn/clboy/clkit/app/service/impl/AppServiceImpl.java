package cn.clboy.clkit.app.service.impl;

import cn.clboy.clkit.app.dto.ExportDataDTO;
import cn.clboy.clkit.app.handler.AppDataHandler;
import cn.clboy.clkit.app.service.AppService;
import cn.clboy.clkit.config.AppProperties;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.net.URLEncodeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用服务实施
 *
 * @author clboy
 * @date 2024/04/28 16:51:48
 */
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

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
}
