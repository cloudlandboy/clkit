package cn.clboy.clkit.extension.handler;

import cn.clboy.clkit.common.constants.enums.ExtensionTypeEnum;
import cn.clboy.clkit.extension.entity.Extension;
import org.springframework.stereotype.Component;

/**
 * 在线网址扩展处理程序
 *
 * @author clboy
 * @date 2024/05/07 09:22:00
 */
@Component
public class OnlineUrlExtensionHandler extends AbstractExtensionHandler {
    public OnlineUrlExtensionHandler() {
        super(ExtensionTypeEnum.ONLINE_URL);
    }

    @Override
    public void validate(Extension extension) {
        if (!extension.getUrl().startsWith("http://") && !extension.getUrl().startsWith("https://")) {
            extension.setUrl("http://" + extension.getUrl());
        }
        extension.setPath(extension.getUrl());
    }
}
