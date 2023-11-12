<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span>CRUD代码生成 </span>
                <el-icon class="show-info-btn" @click="templateSyntaxDocVisible = true">
                    <InfoFilled />
                </el-icon>
                <el-button style="float: right;margin-left: 1rem;" @click="openDataSourceManage">数据源管理</el-button>
                <el-button style="float: right;" @click="openTemplateManage">模板管理</el-button>
                <el-button style="float: right;" @click="dbLangTypeDialogVisible = true">类型映射配置</el-button>
            </div>
        </template>
        <div>
            <div style="margin-bottom: 1rem;">
                <span>数据源：</span>
                <el-select v-model="genForm.dataSourceId" @change="changeDatasource" placeholder="选择数据源">
                    <el-option v-for="item in dataSourceList" :key="item._id" :label="item.uniqueName" :value="item._id" />
                </el-select>
                <span style="margin-left: 1rem;">语言：</span>
                <el-select v-model="genForm.language" placeholder="选择语言">
                    <el-option v-for="lang in constant.languages" :key="lang" :label="lang" :value="lang" />
                </el-select>
                <div style="margin-top:1rem">
                    <el-input v-model="tableSearchKeyWord" @input="tableSearch" placeholder="搜索" />
                </div>
            </div>

            <el-table ref="tableListRef" :data="tableList" border style="width: 100%" height="300">
                <el-table-column type="selection" width="60" align="center" />
                <el-table-column prop="name" label="表名称" :show-overflow-tooltip="true" />
                <el-table-column prop="comment" :show-overflow-tooltip="true" label="表注释" />
                <el-table-column label="操作" align="center" width="100">
                    <template #header>
                        <el-button size="small" type="primary" @click="openGenDialog(null)">批量</el-button>
                    </template>
                    <template #default="scope">
                        <el-button size="small" type="primary" @click="openGenDialog(scope.row.name)">生成</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- gen form -->
        <el-dialog v-model="genDialogVisible" title="生成配置" width="38%" :close-on-click-modal="false"
            :destroy-on-close="true">
            <el-form :model="genForm" label-width="120px">
                <el-form-item label="选择模板" required>
                    <el-select v-model="genForm.templateId" @change="templateChanged">
                        <el-option v-for="(template, id) in templateMap" :key="id" :label="template.key" :value="id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="作者">
                    <el-input v-model="genForm.author" />
                </el-form-item>
                <el-form-item v-if="templateMap[genForm.templateId]"
                    v-for="param in templateMap[genForm.templateId].extraParams" :label="param.name">
                    <el-input v-if="param.type === 'input'" v-model="genForm.extraParams[param.key]" />
                    <el-switch v-if="param.type === 'switch'" v-model="genForm.extraParams[param.key]" />
                </el-form-item>
                <div class="content-right">
                    <el-button type="primary" @click="submitGen">生成</el-button>
                </div>
            </el-form>
        </el-dialog>

        <!-- 类型映射配置 -->
        <el-dialog v-model="dbLangTypeDialogVisible" title="类型映射配置" width="40%" :close-on-click-modal="false"
            :destroy-on-close="true">
            <type-mapping-config></type-mapping-config>
        </el-dialog>


        <!-- 模板管理 -->
        <el-dialog v-model="templateManageDialogVisible" title="模板管理" width="50%" :close-on-click-modal="false"
            :destroy-on-close="true">
            <template-manage></template-manage>
        </el-dialog>


        <!-- 数据源管理 -->
        <el-dialog v-model="datasourceManageDialogVisible" title="数据源管理" width="50%" :close-on-click-modal="false"
            :destroy-on-close="true">
            <datasource-manage></datasource-manage>
        </el-dialog>

        <!-- 模板语法doc -->
        <el-drawer v-model="templateSyntaxDocVisible" title="模板语法" :with-header="false" direction="rtl" size="750px">
            <h3>模板引擎为 <a href="https://handlebarsjs.com/zh/guide" target="_blank">Handlebars</a></h3>
            <h3>除内置外新增以下助手: </h3>
            <el-table :data="constant.doc.helpers" style="width: 100%">
                <el-table-column prop="name" label="名称" width="150" />
                <el-table-column prop="description" label="说明" width="150" />
                <el-table-column prop="demo" label="示例" />
            </el-table>
            <h3>上下文变量</h3>
            <el-tree :data="docContextTree" :render-content="renderDocContext" :default-expand-all="true" />
        </el-drawer>
    </el-card>
</template>
<script setup>
import { ref, onMounted } from "vue";
import { listAll, queryTable } from "../../api/db";
import { findTemplate, genCrud } from "../../api/gen";
import { toElTree } from "../../util/object-utils.js";
import { hasText } from "../../util/string-utils.js";
import { downloadFile } from "../../util/web-file-utils.js";
import templateManage from "./template-manage.vue";
import datasourceManage from "./datasource-manage.vue";
import typeMappingConfig from "./type-mapping-config.vue";
import { ElMessage } from 'element-plus'
import { prefixLocalStore } from "../../util/local-store";
import * as constant from "../../constant/code-gen-constant.json";

const localStore = prefixLocalStore('crudCodeGen');

const docContextTree = toElTree(constant.doc.context, (key, value, isLeafNode) => {
    return key;
})

function renderDocContext(h, ctx) {
    const content = [h('code', null, ctx.data.label)];
    if ((typeof ctx.data.value) === 'string') {
        const descType = ctx.data.value.split('|');
        content.push(h('span', { style: 'margin: 0 8px' }, ':'));
        content.push(h('span', null, descType[0]));
        content.push(h('span', { style: 'margin: 0 24px;color: #fcd3d3;user-select: none' }, descType[1]));
    }
    return h('div', null, content);
}

var tableListCopy = [];
const tableListRef = ref();
const tableList = ref([]);
const dataSourceList = ref([]);
const templateMap = ref({});

const templateSyntaxDocVisible = ref(false);
const genDialogVisible = ref(false);
const dbLangTypeDialogVisible = ref(false);
const datasourceManageDialogVisible = ref(false);
const templateManageDialogVisible = ref(false);

const tableSearchKeyWord = ref('');
const genFormRef = ref();


const genFormModel = localStore.getJsonOrDefault('genForm', {
    dataSourceId: '',
    tableNames: [],
    language: constant.languages[0],
    author: 'clboy',
    templateId: '',
    extraParams: {}
})
genFormModel.dataSourceId = '';
const genForm = ref(genFormModel)


function fetchDataSource() {
    listAll().then(res => {
        dataSourceList.value = res.data;
    })
}

function tableSearch() {
    if (hasText(tableSearchKeyWord.value)) {
        tableList.value = tableList.value.filter(tb => tb.name.includes(tableSearchKeyWord.value) || tb.comment.includes(tableSearchKeyWord.value));
    } else {
        tableList.value = tableListCopy;
    }
}

function openDataSourceManage() {
    datasourceManageDialogVisible.value = true;
}

function openTemplateManage() {
    templateManageDialogVisible.value = true;
}

function changeDatasource() {
    queryTable(genForm.value.dataSourceId, '').then(res => {
        tableList.value = res.data;
        tableListCopy = res.data;
    })
}

function openGenDialog(tableName) {
    if (hasText(tableName)) {
        genForm.value.tableNames = [tableName];
    } else {
        let tbs = tableListRef.value.getSelectionRows();
        if (tbs.length === 0) {
            ElMessage.warning('未选择表');
            return
        }
        genForm.value.tableNames = tbs.map(tb => tb.name);
    }
    findTemplate({ language: genForm.value.language }).then(res => {
        const toMap = {};
        res.data.forEach(element => {
            toMap[element._id] = element;
        });
        templateMap.value = toMap;
        genDialogVisible.value = true;
    })
}

function templateChanged() {
    const extraParams = {};
    templateMap.value[genForm.value.templateId].extraParams.forEach(param => {
        if (param.type === 'select') {
            extraParams[param.key] = [];
        } else if (param.type === 'switch') {
            extraParams[param.key] = false;
        } else {
            extraParams[param.key] = '';
        }
    })
    genForm.value.extraParams = extraParams;
}

function submitGen() {
    if (!hasText(genForm.value.templateId)) {
        ElMessage.error('请先选择模板');
        return;
    }
    localStore.set('genForm', genForm.value);
    genCrud(genForm.value).then(res => {
        downloadFile(res.data, { type: res.headers['content-type'] }, res.headers['content-disposition'].substring(21));
    })
}

onMounted(() => {
    fetchDataSource();
})

</script>

<style>
.el-tree-node {
    margin: 8px 0;
}
</style>