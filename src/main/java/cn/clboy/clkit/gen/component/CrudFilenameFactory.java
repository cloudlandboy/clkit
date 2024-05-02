package cn.clboy.clkit.gen.component;

import cn.clboy.clkit.gen.vo.CrudDataModelVO;
import cn.hutool.core.text.NamingCase;
import freemarker.template.Template;
import lombok.SneakyThrows;

import java.io.StringWriter;

/**
 * crud文件名工厂
 *
 * @author clboy
 * @date 2024/04/28 10:54:35
 */
public interface CrudFilenameFactory {
    String getFilename(CrudDataModelVO model);

    /**
     * 默认crud文件名工厂
     *
     * @author clboy
     * @date 2024/04/28 10:54:55
     */
    class DefaultCrudFilenameFactory implements CrudFilenameFactory {

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

    class TemplateCrudFilenameFactory implements CrudFilenameFactory {

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