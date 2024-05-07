package cn.clboy.clkit.extension.handler;

import cn.clboy.clkit.common.constants.enums.ExtensionTypeEnum;
import cn.clboy.clkit.extension.entity.Extension;
import cn.hutool.core.io.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * 磁盘文件扩展处理程序
 *
 * @author clboy
 * @date 2024/05/07 09:31:28
 */
@Component
public class DiskFileExtensionHandler extends AbstractExtensionHandler {
    public DiskFileExtensionHandler() {
        super(ExtensionTypeEnum.DISK_FILE);
    }

    @Override
    public void validate(Extension extension) {
        if (!StringUtils.hasText(extension.getIndex())) {
            extension.setIndex("index.html");
        }
        File source = new File(extension.getUrl());
        Assert.isTrue(source.exists(), "磁盘文件不存在");
    }

    @Override
    public String install(Extension extension) {
        File source = new File(extension.getUrl());
        Assert.isTrue(source.exists(), "磁盘文件不存在");
        File directory = this.emptyDirectory(extension.getId());
        FileUtil.copyContent(source, directory, true);
        return source.isDirectory() ? extension.getIndex() : source.getName();
    }
}
