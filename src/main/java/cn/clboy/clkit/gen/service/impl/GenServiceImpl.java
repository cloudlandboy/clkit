package cn.clboy.clkit.gen.service.impl;

import cn.clboy.clkit.gen.dto.GenCrudDTO;
import cn.clboy.clkit.gen.dto.LangTypeMatchDTO;
import cn.clboy.clkit.gen.entity.CrudTemplate;
import cn.clboy.clkit.gen.entity.Db;
import cn.clboy.clkit.gen.entity.DbLangType;
import cn.clboy.clkit.gen.service.CrudTemplateService;
import cn.clboy.clkit.gen.service.DbLangTypeService;
import cn.clboy.clkit.gen.service.DbService;
import cn.clboy.clkit.gen.service.GenService;
import cn.clboy.clkit.gen.vo.ColumnInfoVO;
import cn.clboy.clkit.gen.vo.CrudDataModelVO;
import cn.clboy.clkit.gen.vo.TableBasicVO;
import cn.clboy.clkit.gen.vo.TableInfoVO;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.ZipUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成服务impl
 *
 * @author clboy
 * @date 2024/04/23 15:30:41
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenServiceImpl implements GenService {

    private final DbService dbService;
    private final DbLangTypeService dbLangTypeService;
    private final CrudTemplateService crudTemplateService;

    private static final String GLOBAL_FILENAME_TP_KEY = "__GLOBAL_FILENAME__";
    private static final String MODULE_FILENAME_TP_KEY = "__%s_FILENAME__";
    private static final String MODULE_TP_KEY = "__%s__";

    @Override
    @SneakyThrows
    public void genCrud(GenCrudDTO dto, HttpServletResponse response) {
        CrudTemplate crudTemplate = crudTemplateService.getById(dto.getTemplateId());

        List<CrudTemplate.CrudTemplateModule> toUseModules;
        if (crudTemplate.getModules() == null) {
            toUseModules = Collections.emptyList();
        } else {
            toUseModules = crudTemplate.getModules().stream()
                    .filter(m -> Boolean.FALSE.equals(m.getSkip())).collect(Collectors.toList());
        }
        Assert.notEmpty(toUseModules, "该模板无可生成模块");

        Configuration cfg = new Configuration(Configuration.getVersion());
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        cfg.setTemplateLoader(templateLoader);

        TemplateCrudFilenameFactory globalFilenameFactory = null;
        if (StringUtils.hasText(crudTemplate.getModuleFileNameFormat())) {
            templateLoader.putTemplate(GLOBAL_FILENAME_TP_KEY, crudTemplate.getModuleFileNameFormat());
            globalFilenameFactory = new TemplateCrudFilenameFactory(cfg.getTemplate(GLOBAL_FILENAME_TP_KEY));
        }

        Map<String, CrudFilenameFactory> filenameFactoryMap = CollectionUtils.newHashMap(toUseModules.size());
        for (CrudTemplate.CrudTemplateModule module : toUseModules) {
            templateLoader.putTemplate(String.format(MODULE_TP_KEY, module.getName()), module.getTemplate());
            if (StringUtils.hasText(module.getFileNameFormat())) {
                String tpk = String.format(MODULE_FILENAME_TP_KEY, module.getName());
                templateLoader.putTemplate(tpk, module.getFileNameFormat());
                Template template = cfg.getTemplate(tpk);
                filenameFactoryMap.put(module.getName(), new TemplateCrudFilenameFactory(template));
            } else if (globalFilenameFactory != null) {
                filenameFactoryMap.put(module.getName(), globalFilenameFactory);
            } else {
                filenameFactoryMap.put(module.getName(), DefaultCrudFilenameFactory.INSTANCE);
            }
        }


        Db db = dbService.getById(dto.getDbId());
        List<TableInfoVO> tableInfoList = dbService.queryTableInfo(db, dto.getTableNames());

        DbLangType dbLangType = dbLangTypeService.getById(dto.getDbLangTypeId());


        int fileCount = tableInfoList.size() * toUseModules.size();
        String[] paths = new String[fileCount];
        ByteArrayInputStream[] iss = new ByteArrayInputStream[fileCount];

        int index = 0;
        for (TableInfoVO tableInfo : tableInfoList) {
            CrudDataModelVO dataModel = new CrudDataModelVO();
            dataModel.setLang(dbLangType.getLangType());
            dataModel.setAuthor(dto.getAuthor());
            dataModel.setExtraParams(dto.getExtraParams());
            CrudDataModelVO.EntityInfo entityInfo = this.buildEntityInfo(tableInfo, dbLangType);
            if (CollectionUtils.isEmpty(entityInfo.getIdAttributeList())) {
                log.error("{} 表中无id列", tableInfo.getName());
            }
            dataModel.setEntity(entityInfo);
            LocalDateTime now = LocalDateTime.now();
            dataModel.setNowDateTime(now);
            dataModel.setNowDateTimeIsoFormat(now.format(DatePattern.ISO8601_FORMATTER));
            dataModel.setNowDateTimeNormFormat(now.format(DatePattern.NORM_DATETIME_FORMATTER));
            for (CrudTemplate.CrudTemplateModule module : toUseModules) {
                dataModel.setModuleName(module.getName());
                Template template = cfg.getTemplate(String.format(MODULE_TP_KEY, module.getName()));
                StringWriter result = new StringWriter();
                template.process(dataModel, result);
                paths[index] = filenameFactoryMap.get(module.getName()).getFilename(dataModel);
                iss[index] = new ByteArrayInputStream(result.toString().getBytes());
                index++;
            }
        }

        String fileName;
        byte[] data;
        String contentType;
        if (fileCount == 1) {
            //直接返回文件不压缩
            contentType = MediaType.TEXT_PLAIN_VALUE;
            data = IoUtil.readBytes(iss[0]);
            fileName = StringUtils.getFilename(paths[0]);
        } else {
            contentType = "application/zip";
            String dirname = tableInfoList.get(0).getName();
            if (tableInfoList.size() > 1) {
                dirname += "等多个文件";
            }
            for (int i = 0; i < paths.length; i++) {
                paths[i] = dirname + "/" + paths[i];
            }
            ByteArrayOutputStream zipOs = new ByteArrayOutputStream((int) DataSize.ofKilobytes(100).toBytes());
            ZipUtil.zip(zipOs, paths, iss);
            data = zipOs.toByteArray();
            fileName = dirname + ".zip";
        }
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=" + URLEncodeUtil.encode(fileName));
        response.getOutputStream().write(data);
    }

    /**
     * 构建实体信息
     *
     * @param tableInfo 表信息
     */
    private CrudDataModelVO.EntityInfo buildEntityInfo(TableInfoVO tableInfo, DbLangType dbLangType) {
        TableBasicVO table = new TableBasicVO();
        table.setName(tableInfo.getName());
        table.setComment(tableInfo.getComment());
        Set<String> importStatementSet = new HashSet<>();
        List<CrudDataModelVO.AttributeInfo> idAttributeList = new ArrayList<>(2);
        List<CrudDataModelVO.AttributeInfo> attributeList = tableInfo.getColumnList().stream().map(columnInfo -> {
            CrudDataModelVO.AttributeInfo attribute = new CrudDataModelVO.AttributeInfo();
            attribute.setColumn(columnInfo);
            attribute.setCamelCaseName(NamingCase.toCamelCase(columnInfo.getName()));
            LangTypeMatchDTO typeMatch = dbLangType.getMatchList().stream()
                    .filter(ltm -> columnInfo.getDataType().matches(ltm.getMatch())).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("%s的%s类型无法转换为%s类型",
                            dbLangType.getDbPlatform(), columnInfo.getDataType(), dbLangType.getLangType())));
            attribute.setType(typeMatch.getType());
            attribute.setPackagePath(typeMatch.getPackagePath());
            if (StringUtils.hasText(typeMatch.getImportStatement())) {
                importStatementSet.add(typeMatch.getImportStatement());
            }
            if (columnInfo.getIdc()) {
                idAttributeList.add(attribute);
            }
            return attribute;
        }).collect(Collectors.toList());

        List<ColumnInfoVO> idColumnList = tableInfo.getColumnList().stream()
                .filter(ColumnInfoVO::getIdc).collect(Collectors.toList());
        CrudDataModelVO.EntityInfo entity = new CrudDataModelVO.EntityInfo();
        entity.setPascalCaseName(NamingCase.toPascalCase(tableInfo.getName()));
        entity.setCamelCaseName(NamingCase.toCamelCase(tableInfo.getName()));
        entity.setCompositeId(idColumnList.size() > 1);
        entity.setTable(table);
        entity.setAttributeList(attributeList);
        entity.setIdAttributeList(idAttributeList);
        entity.setImportStatementList(new ArrayList<>(importStatementSet));
        return entity;
    }


    /**
     * crud文件名工厂
     *
     * @author clboy
     * @date 2024/04/28 10:54:35
     */
    public interface CrudFilenameFactory {
        String getFilename(CrudDataModelVO model);

    }

    /**
     * 默认crud文件名工厂
     *
     * @author clboy
     * @date 2024/04/28 10:54:55
     */
    public static class DefaultCrudFilenameFactory implements CrudFilenameFactory {

        public static final CrudFilenameFactory INSTANCE = new DefaultCrudFilenameFactory();

        private DefaultCrudFilenameFactory() {
        }

        @Override
        public String getFilename(CrudDataModelVO model) {
            return String.format("%s/%s.%s", model.getModuleName(),
                    model.getEntity().getPascalCaseName() + NamingCase.toPascalCase(model.getModuleName()),
                    model.getLang().toLowerCase());
        }
    }

    public static class TemplateCrudFilenameFactory implements CrudFilenameFactory {

        private final Template template;

        public TemplateCrudFilenameFactory(Template template) {
            this.template = template;
        }

        @Override
        @SneakyThrows
        public String getFilename(CrudDataModelVO model) {
            StringWriter stringWriter = new StringWriter();
            template.process(model, stringWriter);
            return stringWriter.toString();
        }
    }
}
