package cn.clboy.clkit.extension.service.impl;

import cn.clboy.clkit.common.constants.enums.ExtensionTypeEnum;
import cn.clboy.clkit.common.service.CrudServiceImpl;
import cn.clboy.clkit.common.util.AppUtils;
import cn.clboy.clkit.extension.entity.Extension;
import cn.clboy.clkit.extension.filter.ExtensionResourceFilter;
import cn.clboy.clkit.extension.handler.ExtensionHandler;
import cn.clboy.clkit.extension.repository.ExtensionRepository;
import cn.clboy.clkit.extension.service.ExtensionService;
import cn.clboy.clkit.extension.vo.ExtensionTypeVO;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 扩展服务实施
 *
 * @author clboy
 * @date 2024/05/06 10:25:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExtensionServiceImpl extends CrudServiceImpl<Extension, Long, ExtensionRepository>
        implements ExtensionService {
    private final Lock extensionOpsLock = new ReentrantLock();

    @Override
    public Extension save(Extension dto) {
        this.validate(dto);
        return super.save(dto);
    }

    @Override
    public Extension updateById(Extension dto) {
        Extension extension = getById(dto.getId());
        dto.setPath(extension.getPath());
        this.validate(dto);
        return super.updateById(dto);
    }

    @Override
    public List<ExtensionTypeVO> getTypes() {
        return Arrays.stream(ExtensionTypeEnum.values()).map(em -> {
            ExtensionTypeVO extensionType = new ExtensionTypeVO();
            extensionType.setValue(em.getValue());
            extensionType.setLabel(em.getLabel());
            extensionType.setNeedInstall(em.isNeedInstall());
            return extensionType;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Tree<Long>> getTree(Boolean filterInstalled) {
        List<Extension> extensionList = this.getAll();
        if (CollectionUtils.isEmpty(extensionList)) {
            return Collections.emptyList();
        }

        List<Long> installedIds = AppUtils.listExistsExtensionIds();
        if (filterInstalled) {
            extensionList = extensionList.stream()
                    .filter(et -> !(et.getType().isNeedInstall()) || installedIds.contains(et.getId()))
                    .collect(Collectors.toList());
        }

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setParentIdKey("folderId");
        return TreeUtil.build(extensionList, 0L, treeNodeConfig, (extension, treeNode) -> {
            treeNode.setId(extension.getId());
            treeNode.setParentId(extension.getFolderId());
            treeNode.setName(extension.getName());
            treeNode.putExtra("type", extension.getType());
            treeNode.putExtra("url", extension.getUrl());
            treeNode.putExtra("index", extension.getIndex());
            treeNode.putExtra("sortValue", extension.getSortValue());
            treeNode.putExtra("hide", extension.getHide());
            treeNode.putExtra("path", extension.getPath());
            boolean needInstall = extension.getType().isNeedInstall();
            treeNode.putExtra("installed", !needInstall || installedIds.contains(extension.getId()));
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        Assert.isTrue(extensionOpsLock.tryLock(), "操作频繁");
        try {
            super.removeById(id);
            File file = AppUtils.getExtensionDirFile(id);
            Assert.isTrue(FileUtil.del(file), "删除扩展文件夹失败");
        } finally {
            extensionOpsLock.unlock();
        }
    }

    @Override
    public void install(Long id) {
        Assert.isTrue(extensionOpsLock.tryLock(), "操作频繁");
        try {
            Extension extension = this.getById(id);
            ExtensionHandler handler = ExtensionHandler.HOLDER.getHandler(extension.getType());
            Assert.notNull(handler, "不支持的扩展的类型");
            String index = handler.install(extension);
            extension.setIndex(index);
            extension.setPath(ExtensionResourceFilter.PATH_PREFIX + id + "/" + index);
            this.repository.save(extension);
        } finally {
            extensionOpsLock.unlock();
        }

    }

    /**
     * 验证
     *
     * @param dto DTO
     */
    private void validate(Extension dto) {
        if (ExtensionTypeEnum.FOLDER == dto.getType()) {
            return;
        }
        Assert.hasText(dto.getUrl(), "invalid url");
        ExtensionHandler handler = ExtensionHandler.HOLDER.getHandler(dto.getType());
        handler.validate(dto);
    }

}
