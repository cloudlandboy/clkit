package cn.clboy.clkit.extension.handler;

import cn.clboy.clkit.common.constants.enums.ExtensionTypeEnum;
import cn.clboy.clkit.common.util.AppUtils;
import cn.clboy.clkit.extension.entity.Extension;
import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.File;

/**
 * 抽象扩展处理程序
 *
 * @author clboy
 * @date 2024/05/07 09:17:31
 */
@Getter
public abstract class AbstractExtensionHandler implements ExtensionHandler, InitializingBean {

    private final ExtensionTypeEnum type;

    public AbstractExtensionHandler(ExtensionTypeEnum type) {
        this.type = type;
    }

    @Override
    public String install(Extension extension) {
        return extension.getIndex();
    }

    /**
     * 空目录
     *
     * @param id ID
     */
    protected File emptyDirectory(Long id) {
        File file = AppUtils.getExtensionDirFile(id);
        Assert.isTrue(FileUtil.del(file), "删除扩展文件夹出错");
        Assert.isTrue(file.mkdirs(), "创建扩展文件夹出错");
        return file;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExtensionHandler.HOLDER.addHandler(this.getType().getValue(), this);
    }
}
