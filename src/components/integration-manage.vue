<template>
    <el-card class="box-card" v-loading.fullscreen="installLoading" element-loading-text="安装集成中...">
        <template #header>
            <div class="card-header">
                <span>集成管理 </span>
                <el-icon>
                    <InfoFilled />
                </el-icon>
                <div style="float: right;">
                    <el-button :icon="Paperclip" color="#85ce61" circle @click="openAddOrEdit(null, '0')" />
                    <el-button :icon="Folder" color="#ffa400" circle
                        @click="openAddOrEdit(null, '0', INTEGRATION_TYPES.FOLDER.value)" />
                </div>
            </div>
        </template>

        <!-- 列表 -->
        <el-tree :data="treeData" node-key="_id" :default-expanded-keys="defaultExpand" :accordion="true"
            @node-expand="nodeExpand" @node-collapse="nodeCollapse">
            <template #default="{ data }">
                <div class="directory-tree-node">
                    <div>
                        <el-icon v-if="isFolder(data.type)" size="16" color="#ffa400" class="directory-tree-icon">
                            <Folder />
                        </el-icon>
                        <el-icon v-else size="16" :color="data.installed ? '#85ce61' : '#909399'"
                            class="directory-tree-icon">
                            <Paperclip />
                        </el-icon>
                        <span>{{ data.name }}</span>
                    </div>
                    <div>
                        <el-button v-if="data.hide" :icon="View" circle color="#fdf6ec" size="small"
                            @click.stop="updateHide(data, false)" />
                        <el-button v-else :icon="Hide" circle color="#73767a" size="small" @click.stop="updateHide(data, true)" />
                        <span v-if="needInstall(data.type)" style="margin: 0 12px;">
                            <el-button v-if="data.installed" size="small" type="success"
                                @click.stop="doInstall(data._id)">重装</el-button>
                            <el-button v-else size="small" type="info" @click.stop="doInstall(data._id)">安装</el-button>
                        </span>
                        <span v-if="isFolder(data.type)" style="margin: 0 12px;">
                            <el-button :icon="Paperclip" color="#85ce61" circle size="small"
                                @click.stop="openAddOrEdit(null, data._id)" />
                            <el-button :icon="Folder" color="#ffa400" circle size="small"
                                @click.stop="openAddOrEdit(null, data._id, INTEGRATION_TYPES.FOLDER.value)" />
                        </span>
                        <el-button :icon="Edit" circle size="small" @click.stop="openAddOrEdit(data, data.folderId)" />
                        <el-button :icon="Right" color="#c0ebd7" circle size="small" @click.stop="doMove(data)" />
                        <el-button v-if="!data.children || data.children.length === 0" type="danger" :icon="Delete" circle
                            size="small" @click.stop="doRemove(data._id)" />
                    </div>
                </div>
            </template>
        </el-tree>

        <!-- add or edit -->
        <el-dialog v-model="formDialogVisible" :title="form._id ? '修改' : '新增'" width="750px" :close-on-click-modal="false">
            <el-form ref="formRef" :model="form" :rules="formRules" :validate-on-rule-change="false" label-position="right"
                label-width="120px">
                <el-form-item :label="isFolder(form.type) ? '文件夹名称' : '菜单名称'" prop="name">
                    <el-input v-model="form.name" placeholder="自定义唯一菜单名称" :validate-event="false" clearable />
                </el-form-item>
                <el-form-item label="排序值" prop="sortValue">
                    <el-input-number v-model="form.sortValue" :validate-event="false" />
                </el-form-item>
                <div v-show="!isFolder(form.type)">
                    <el-form-item label="地址类型" prop="type">
                        <el-select v-model="form.type" style="width: 100%;">
                            <el-option v-show="!item.ve(INTEGRATION_TYPES.FOLDER.value)" v-for="item in INTEGRATION_TYPES"
                                :key="item.value" :label="item.label" :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="地址" prop="url">
                        <el-input v-model="form.url" placeholder="下载地址" :validate-event="false" clearable />
                    </el-form-item>
                    <el-form-item label="index路径" prop="index" v-if="!INTEGRATION_TYPES.ONLINE_URL.ve(form.type)">
                        <el-input v-model="form.index" placeholder="主页路径" :validate-event="false" clearable />
                    </el-form-item>
                    <el-form-item label="插入脚本" prop="insertScript" v-if="!INTEGRATION_TYPES.ONLINE_URL.ve(form.type)">
                        <codemirror v-model="form.insertScript" placeholder="菜单激活时插入到iframe的script"
                            :extensions="scriptExtensions" style="min-height: 150px;border: 1px dotted #b1b3b8;width: 100%;"
                            class="custom-scrollbar hiden-cm-lineNumbers" />
                    </el-form-item>
                </div>
                <el-form-item class="el-form-right-btn-group">
                    <el-button type="primary" @click="submitSaveOrUpdate">{{ form._id ? '更新' : '保存' }}</el-button>
                    <el-button @click="formDialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- move -->
        <el-dialog v-model="moveDialogVisible" title="移动" width="750px">
            <el-tree :data="moveTreeData" node-key="_id" :accordion="true" :props="{ class: getMoveNodeClass }"
                :expand-on-click-node="false" style="user-select: none;" @node-click="submitMove">
                <template #default="{ data }">
                    <div>
                        <el-icon size="16" color="#ffa400" style="vertical-align: middle;margin-right: 16px;">
                            <Folder />
                        </el-icon>
                        <span>{{ data.name }}</span>
                    </div>
                </template>
            </el-tree>
        </el-dialog>

    </el-card>
</template>
<script setup>
import { ref, onMounted, computed } from "vue";
import { useAppConfigStore } from "@/stores/app-config";
import { FieldDef, copyProperties } from "../util/object-utils";
import { INTEGRATION_TYPES, INTEGRATION_TYPE_DICT } from "../constant/dict.constants";
import { create, update, remove, getTree, install } from "../api/integration";
import { ElNotification } from "element-plus";
import { javascript, javascriptLanguage } from '@codemirror/lang-javascript'
import { Delete, Edit, Paperclip, Folder, Right, Hide, View } from '@element-plus/icons-vue'

const expandSet = ref(new Set());
const defaultExpand = computed(() => {
    return Array.from(expandSet.value);
});

const scriptExtensions = [javascript(), javascriptLanguage];
const appConfigStore = useAppConfigStore();

const installLoading = ref(false);

const folderRules = {
    name: [{ required: true, message: '文件夹名称不能为空' }],
}
const menuRules = {
    name: [{ required: true, message: '菜单名称不能为空' }],
    type: [{ required: true, message: '类型必选' }],
    url: [{ required: true, message: '下载地址不能为空' }]
}
const formRules = ref(menuRules);

const treeData = ref([]);
const moveTreeData = computed(() => {
    return [{
        name: "/",
        type: "folder",
        _id: "0",
        children: treeData.value
    }]
})

const formFieldDef = new FieldDef({
    _id: null,
    folderId: '0',
    name: '',
    type: INTEGRATION_TYPES.ONLINE_URL.value,
    url: '',
    index: '',
    sortValue: 0,
    insertScript: '',
    installed: false,
    hide: false
});

const formRef = ref();
const form = ref(formFieldDef.getObj());

const formDialogVisible = ref(false);
const moveDialogVisible = ref(false);

async function fetchData() {
    return getTree().then(res => {
        treeData.value = res.data;
    })
}

function openAddOrEdit(data, folderId, type) {
    if (data) {
        form.value = copyProperties(data, formFieldDef.getObj());
    } else {
        form.value = formFieldDef.getObj();
        if (type) {
            form.value.type = type;
        }
    }
    form.value.folderId = folderId;
    formRules.value = isFolder(form.value.type) ? folderRules : menuRules;
    formDialogVisible.value = true;
}

function submitSaveOrUpdate() {
    formRef.value.validate(isValid => {
        if (!isValid) {
            return;
        }
        if (form.value._id) {
            update(form.value._id, form.value).then(() => fetchData()).then(() => {
                formDialogVisible.value = false;
                appConfigStore.renderMenu();
            });
            return
        }
        create(form.value).then(() => fetchData()).then(() => {
            formDialogVisible.value = false;
            appConfigStore.renderMenu();
        });
    });
}

function doInstall(id) {
    installLoading.value = true;
    install(id).then(res => fetchData()).then(() => {
        appConfigStore.renderMenu();
        ElNotification.success({
            message: '安装成功'
        });
    }).finally(() => {
        installLoading.value = false;
    })
}

function doRemove(id) {
    remove(id).then(res => {
        expandSet.value.delete(id);
        return fetchData();
    }).then(() => appConfigStore.renderMenu())
}

function doMove(data) {
    form.value = copyProperties(data, formFieldDef.getObj());
    moveDialogVisible.value = true;
}

function updateHide(data, hide) {
    form.value = copyProperties(data, formFieldDef.getObj());
    if (form.value.hide !== hide) {
        form.value.hide = hide;
        update(form.value._id, form.value).then(() => fetchData()).then(() => {
            appConfigStore.renderMenu();
        });
    }
}

function isFolder(value) {
    return INTEGRATION_TYPES.FOLDER.ve(value);
}

function needInstall(value) {
    const lt = INTEGRATION_TYPE_DICT.findByValue(value);
    return lt && lt.needInstall;
}

function nodeExpand(data) {
    expandSet.value.add(data._id);
}

function nodeCollapse(data) {
    expandSet.value.delete(data._id);
}

function getMoveNodeClass(data) {
    return (isFolder(data.type) && data._id !== form.value._id) ? '' : 'display-none';
}

function submitMove(data) {
    form.value.folderId = data._id;
    expandSet.value.add(form.value._id);
    update(form.value._id, form.value).then(() => fetchData()).then(() => {
        moveDialogVisible.value = false;
        appConfigStore.renderMenu();
    });
}

onMounted(() => {
    fetchData();
});


</script>

<style>
.hiden-cm-lineNumbers .cm-gutters {
    display: none !important;
}

.directory-tree-node {
    display: flex;
    width: 100%;
    justify-content: space-between;
}

.directory-tree-icon {
    vertical-align: middle;
    margin-right: 16px;
}
</style>