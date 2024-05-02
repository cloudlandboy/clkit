package cn.clboy.clkit.gen.component;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 地图代码编写器
 *
 * @author clboy
 * @date 2024/05/02 17:33:16
 */
public class StringMapCodeWriter extends CodeWriter {
    private final Map<String, ByteArrayOutputStream> osMap = new LinkedHashMap<>();

    @Getter
    private Map<String, String> map;

    public StringMapCodeWriter() {
        this.encoding = StandardCharsets.UTF_8.name();
    }

    @Override
    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        osMap.put(fileName, os);
        return os;
    }

    @Override
    public void close() throws IOException {
        if (CollectionUtils.isEmpty(osMap)) {
            return;
        }
        map = CollectionUtils.newLinkedHashMap(osMap.size());
        osMap.forEach((filename, os) -> {
            try {
                map.put(filename, os.toString(StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
        osMap.clear();
    }
}
