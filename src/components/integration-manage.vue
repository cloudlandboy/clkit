<template>
    <el-card class="box-card" v-loading.fullscreen="installLoading" element-loading-text="安装集成中...">
        <template #header>
            <div class="card-header">
                <span>集成管理 </span>
                <el-icon>
                    <InfoFilled />
                </el-icon>
                <el-button style="float: right;" @click="openAddOrEdit(null)">新增</el-button>
            </div>
        </template>
        <el-table :data="tableDataList" border style="width: 100%">
            <el-table-column prop="name" label="名称" width="200" />
            <el-table-column prop="url" :show-overflow-tooltip="true" label="下载地址" />
            <el-table-column prop="index" :show-overflow-tooltip="true" label="主页地址" />
            <el-table-column prop="type" label="类型" width="150">
                <template #default="scope">
                    {{ types[scope.row.type] ? types[scope.row.type].label : '未知' }}
                </template>
            </el-table-column>
            <el-table-column label="状态" width="150">
                <template #default="scope">
                    <el-tag v-if="scope.row.installed" type="success" effect="dark">已安装</el-tag>
                    <el-tag v-else type="info" effect="dark">未安装</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template #default="scope">
                    <span v-if="types[scope.row.type] && types[scope.row.type].needInstall" style="margin-right: 12px;">
                        <el-button v-if="scope.row.installed" size="small" type="warning"
                            @click="doInstall(scope.row._id)">重新安装</el-button>
                        <el-button v-else size="small" type="primary" @click="doInstall(scope.row._id)">安装</el-button>
                    </span>
                    <el-button size="small" @click="openAddOrEdit(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="doRemove(scope.row._id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>


        <!-- add or edit -->
        <el-dialog v-model="formDialogVisible" :title="form._id ? '修改' : '新增'" width="750px" :close-on-click-modal="false">
            <el-form ref="formRef" :model="form" :rules="formRules" label-position="right" label-width="120px">
                <el-form-item label="菜单名称" prop="name">
                    <el-input v-model="form.name" placeholder="自定义唯一菜单名称" :validate-event="false" clearable />
                </el-form-item>
                <el-form-item label="地址类型" prop="type">
                    <el-select v-model="form.type" style="width: 100%;">
                        <el-option v-for="(item, key) in types" :key="key" :label="item.label" :value="key" />
                    </el-select>
                </el-form-item>
                <el-form-item label="地址" prop="url">
                    <el-input v-model="form.url" placeholder="下载地址" :validate-event="false" clearable />
                </el-form-item>
                <el-form-item label="排序值" prop="sortValue">
                    <el-input-number v-model="form.sortValue" :validate-event="false" />
                </el-form-item>
                <el-form-item label="index路径" prop="index" v-if="form.type !== 'onlineUrl'">
                    <el-input v-model="form.index" placeholder="主页路径" :validate-event="false" clearable />
                </el-form-item>
                <el-form-item label="插入脚本" prop="insertScript" v-if="form.type !== 'onlineUrl'">
                    <codemirror v-model="form.insertScript" placeholder="菜单激活时插入到iframe的script"
                        :extensions="scriptExtensions" style="min-height: 150px;border: 1px dotted #b1b3b8;width: 100%;"
                        class="custom-scrollbar hiden-cm-lineNumbers" />
                </el-form-item>
                <el-form-item class="el-form-right-btn-group">
                    <el-button type="primary" @click="submitSaveOrUpdate">{{ form._id ? '更新' : '保存' }}</el-button>
                    <el-button @click="formDialogVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </el-card>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { useAppConfigStore } from "@/stores/app-config";
import { FieldDef } from "../util/object-utils";
import { hasText } from "../util/string-utils";
import { types, create, update, remove, findAll, install } from "../api/integration";
import { ElNotification } from "element-plus";
import { javascript, javascriptLanguage } from '@codemirror/lang-javascript'

const scriptExtensions = [javascript(), javascriptLanguage];
const appConfigStore = useAppConfigStore();

const installLoading = ref(false);
const formRules = ref({
    name: [{ required: true, message: '菜单名称不能为空' }],
    type: [{ required: true, message: '类型必选' }],
    url: [{ required: true, message: '下载地址不能为空' }]
});

const tableDataList = ref([]);

const formFieldDef = new FieldDef({
    name: '',
    type: 'onlineUrl',
    url: '',
    index: '',
    sortValue: 0,
    insertScript: '',
    installed: false,
});

const formRef = ref();
const form = ref(formFieldDef.getObj());


const formDialogVisible = ref(false);

function fetchData() {
    findAll().then(res => {
        tableDataList.value = res.data;
    })
}

function openAddOrEdit(data) {
    if (data) {
        form.value = data;
    } else {
        form.value = formFieldDef.getObj();
    }
    formDialogVisible.value = true;
}

function submitSaveOrUpdate() {
    formRef.value.validate(isValid => {
        if (!isValid) {
            return;
        }
        if (form.value.type === 'onlineUrl' && !hasText(form.value.index)) {
            form.value.index = 'index.html';
        }
        if (form.value._id) {
            update(form.value._id, form.value).then(() => {
                fetchData();
                formDialogVisible.value = false;
                appConfigStore.renderMenu();
            });
            return
        }
        create(form.value).then(() => {
            fetchData();
            formDialogVisible.value = false;
            appConfigStore.renderMenu();
        });

    });
}

function doInstall(id) {
    installLoading.value = true;
    install(id).then(res => {
        fetchData();
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
        fetchData();
        appConfigStore.renderMenu();
    })
}

onMounted(() => {
    fetchData();
});


</script>

<style>
.hiden-cm-lineNumbers .cm-gutters {
    display: none !important;
}
</style>