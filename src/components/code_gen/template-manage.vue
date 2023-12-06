<template>
    <!-- page -->
    <div>
        <div style="padding-bottom: 0.6rem;text-align: right;">
            <el-button type="primary" @click="openSaveOrUpdateTemplate(null)">新增</el-button>
        </div>
        <el-table :data="templateList" border style="width: 100%">
            <el-table-column prop="language" label="语言" />
            <el-table-column prop="key" label="名称" />
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button size="small" @click="copyTemplate(scope.row)">复制</el-button>
                    <el-button size="small" @click="openSaveOrUpdateTemplate(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" v-if="!scope.row.locked"
                        @click="removeTemplateAndRefresh(scope.row._id)">删除</el-button>
                    <el-button size="small" type="warning" v-if="scope.row.locked"
                        @click="openUnlockConfirm(scope.row)">解锁</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- unlock confirm -->
    <el-dialog v-model="unlockConfirmDialogVisible" width="30%">
        <el-input v-model="confirmUnlockTemplateKey" placeholder="确认需要解锁的模板名" />
        <template #footer>
            <span class="dialog-footer">
                <el-button type="primary" @click="unlockTemplateAndRefresh">确认</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- add or edit -->
    <el-dialog v-model="templateAddDialogVisible" :title="templateForm._id ? '修改模板' : '新增模板'" width="60%"
        :close-on-click-modal="false">
        <el-form ref="templateFormRef" :model="templateForm" :inline="true" label-position="right" label-width="80px"
            :disabled="formDisabled">
            <el-form-item label="语言" prop="language" required>
                <el-select v-model="templateForm.language" placeholder="选择语言" :validate-event="false">
                    <el-option v-for="lang in languages" :key="lang" :label="lang" :value="lang" />
                </el-select>
            </el-form-item>
            <el-form-item label="名称" prop="key" required>
                <el-input style="width: 214px;" v-model="templateForm.key" :validate-event="false" placeholder="模板名称"
                    clearable />
            </el-form-item>
            <div>
                <el-form-item label="文件名" prop="key">
                    <el-input style="width: 540px;" v-model="templateForm.moduleFileNameFormat" placeholder="模块文件名格式化"
                        clearable />
                </el-form-item>
            </div>
            <div>
                <el-form-item label="加锁" prop="locked">
                    <el-switch v-model="templateForm.locked" />
                </el-form-item>
                <el-form-item prop="extraParam">
                    <el-button color="#626aef" @click="extraParamVisible = true">自定义参数</el-button>

                    <el-drawer v-model="extraParamVisible" direction="ttb" :show-close="false" :with-header="false"
                        size="480" :before-close="extraParamValidate">
                        <el-table :data="templateForm.extraParams" border style="width: 100%" empty-text=" ">
                            <el-table-column align="center" width="240px">
                                <template #header>
                                    参数名称 <span style="color: red;">*</span>
                                </template>
                                <template #default="scope">
                                    <el-input v-model="scope.row.name" />
                                </template>
                            </el-table-column>
                            <el-table-column align="center" width="240px">
                                <template #header>
                                    参数key <span style="color: red;">*</span>
                                </template>
                                <template #default="scope">
                                    <el-input v-model="scope.row.key" />
                                </template>
                            </el-table-column>
                            <el-table-column align="center" width="180px">
                                <template #header>
                                    参数类型 <span style="color: red;">*</span>
                                </template>
                                <template #default="scope">
                                    <el-select v-model="scope.row.type" size="large">
                                        <el-option v-for="(value, key) in extraParamTypes" :key="value.uk" :label="key"
                                            :value="key" />
                                    </el-select>
                                </template>
                            </el-table-column>
                            <el-table-column label="枚举值" align="center">
                                <template #default="scope">
                                    <el-input v-model="scope.row.enumList"
                                        :placeholder="extraParamTypes[scope.row.type].desc"
                                        :disabled="!extraParamTypes[scope.row.type].hasEnum" />
                                </template>
                            </el-table-column>
                            <el-table-column label="默认值" align="center" width="280px">
                                <template #default="scope">
                                    <el-input v-model="scope.row.defaultValue" />
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center" width="120px">
                                <template #default="scope">
                                    <el-button type="danger" size="small"
                                        @click="templateForm.extraParams = templateForm.extraParams.filter(i => i !== scope.row)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div style="text-align: center;margin-top: 20px;">
                            <el-button type="primary" @click="addExtraParam">添加参数</el-button>
                        </div>
                    </el-drawer>
                </el-form-item>
            </div>
            <div>
                <el-form-item label="添加模块">
                    <el-input style="width: 214px;" v-model="newTemplateModule" placeholder="模块名称"
                        @keydown.enter="changeTotemplateModule(newTemplateModule)" />
                    <el-button style="margin-left: 1rem;" @click="changeTotemplateModule(newTemplateModule)">添加</el-button>
                </el-form-item>
            </div>

            <div class="template-code-container">
                <el-tabs v-model="templateCodeTab" type="border-card" @tab-change="templateModuleChanged">
                    <el-tab-pane v-for="md in templateForm.modules" :label="md.name" :name="md.name" :key="md.name">
                        <template #label>
                            <span>{{ md.name }}</span>
                            <el-dropdown v-if="!formDisabled">
                                <el-icon class="is-icon-close">
                                    <ArrowDown />
                                </el-icon>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item @click="templateModuleRemove(md.name)">删除</el-dropdown-item>
                                        <el-dropdown-item @click="openModuleSetting(md.name)">设置</el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                        </template>
                    </el-tab-pane>
                </el-tabs>
                <codemirror v-model="templateCode" @blur="templateCodeEditorBlur" placeholder="Write template form here"
                    :style="{ height: '380px' }" :autofocus="true" :indent-with-tab="true" :tab-size="2"
                    :extensions="codemirrorExtensions" :disabled="formDisabled" />
            </div>

            <div class="content-right">
                <el-button type="primary" @click="submitSaveOrUpdateTemplate">{{ templateForm._id ? '更新' :
                    '保存' }}</el-button>
                <el-button @click="templateAddDialogVisible = false">取消</el-button>
            </div>
        </el-form>
    </el-dialog>

    <!-- module setting -->
    <el-drawer class="module-setting-drawer" v-model="moduleSettingVisible" direction="ttb" :show-close="false" :size="200"
        :with-header="false" :before-close="saveModuleSetting">
        <el-form :model="moduleSettingForm" ref="moduleSettingFormRef" label-width="120px">
            <el-form-item label="模块名" prop="name" required>
                <el-input v-model="moduleSettingForm.name" />
            </el-form-item>
            <el-form-item label="模块文件名" prop="fileNameFormat">
                <el-input v-model="moduleSettingForm.fileNameFormat" placeholder="模块文件名格式化" clearable />
            </el-form-item>
            <el-form-item label="跳过生成" prop="skip">
                <el-switch v-model="moduleSettingForm.skip"
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" />
            </el-form-item>
        </el-form>
    </el-drawer>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElNotification } from 'element-plus'
import { html } from '@codemirror/lang-html'
import { hasText } from "../../util/string-utils";
import { oneDark } from '@codemirror/theme-one-dark'
import { FieldDef } from "../../util/object-utils";
import { findTemplate, saveTemplate, updateTemplate, removeTemplate, unlockTemplate } from "/src/api/gen";
import { languages } from "../../constant/code-gen-constant.json";

const extraParamTypes = {
    'input': {
        desc: '纯文本',
        hasEnum: false,
    },
    'select': {
        desc: '多选: 英文逗号分割',
        hasEnum: true,
    },
    'radio': {
        desc: '单选: 英文逗号分割',
        hasEnum: true,
    },
    'switch': {
        desc: '开关: true,false',
        hasEnum: false,
    }
}

const codemirrorExtensions = [html(), oneDark];

const templateList = ref([]);

const formDisabled = ref(false);

const templateAddDialogVisible = ref(false);
const moduleSettingVisible = ref(false);
const extraParamVisible = ref(false);

const templateFormRef = ref();
const templateFormFieldDef = new FieldDef({
    key: '',
    locked: false,
    language: languages[0],
    moduleFileNameFormat: '',
    extraParams: [],
    modules: [{ name: 'entity', template: '', fileNameFormat: '', skip: false },
    { name: 'controller', template: '', fileNameFormat: '', skip: false },
    { name: 'service', template: '', fileNameFormat: '', skip: false },
    { name: 'dao', template: '', fileNameFormat: '', skip: false },]
});

const moduleSettingFormRef = ref();
const moduleSettingForm = ref({
    name: '',
    template: '',
    fileNameFormat: '',
    skip: false
});
const templateForm = ref(templateFormFieldDef.getObj());


const templateCodeTab = ref(templateForm.value.modules[0].name);
const templateCode = ref(templateForm.value.modules[0].template);
const newTemplateModule = ref('');

const prepareUnlockTemplate = ref({});
const unlockConfirmDialogVisible = ref(false);
const confirmUnlockTemplateKey = ref('');

function findMduleObjByName(name) {
    return templateForm.value.modules.find(md => md.name.toUpperCase() === name.toUpperCase());
}

function copyTemplate(data) {
    const copyData = JSON.parse(JSON.stringify(data));
    delete copyData._id;
    copyData.key = ""
    openSaveOrUpdateTemplate(copyData);
}

function openSaveOrUpdateTemplate(data) {
    if (data) {
        templateForm.value = data;
        formDisabled.value = data.locked;
    } else {
        templateForm.value = templateFormFieldDef.getObj();
    }
    templateCodeTab.value = templateForm.value.modules[0].name;
    templateCode.value = templateForm.value.modules[0].template;
    templateAddDialogVisible.value = true;
}

function templateModuleChanged(name) {
    templateCode.value = findMduleObjByName(name).template;
}

function changeTotemplateModule(name) {
    if (!name || name.trim().length < 1) {
        return;
    }
    let mdu = findMduleObjByName(name);
    if (!mdu) {
        mdu = { name, template: '', fileNameFormat: '', skip: false };
        templateForm.value.modules.push(mdu);
    }
    templateCodeTab.value = mdu.name;
}

function templateModuleRemove(name) {
    if (templateForm.value.modules.length === 1) {
        ElMessage.warning('至少要有一个模块')
        return;
    }
    templateForm.value.modules = templateForm.value.modules.filter(m => m.name.toUpperCase() !== name.toUpperCase())
    templateCodeTab.value = templateForm.value.modules[0].name;
}

function submitSaveOrUpdateTemplate() {
    if (!hasText(templateForm.value.key)) {
        ElNotification({
            title: '名称不能为空',
            type: 'error',
        })
        return
    }
    if (templateForm.value._id) {
        updateTemplate(templateForm.value._id, templateForm.value).then(res => {
            refreshList();
            templateAddDialogVisible.value = false;
        })
    } else {
        saveTemplate(templateForm.value).then(res => {
            refreshList();
            templateAddDialogVisible.value = false;
        })
    }
}

function templateCodeEditorBlur(viewUpdate) {
    findMduleObjByName(templateCodeTab.value).template = viewUpdate.state.doc.toString();
}

function openModuleSetting(name) {
    if (name !== templateCodeTab.value) {
        changeTotemplateModule(name);
    }
    moduleSettingForm.value = findMduleObjByName(templateCodeTab.value);
    moduleSettingVisible.value = true;
}

function saveModuleSetting(done) {
    const name = moduleSettingForm.value.name;
    if (!hasText(name)) {
        ElNotification({
            title: '模块名不能为空',
            type: 'error',
        })
        return;
    }
    const matchs = templateForm.value.modules.filter(md => md.name.toUpperCase() === name.toUpperCase());
    if (matchs.length > 1) {
        ElNotification({
            title: '模块名已存在',
            type: 'error',
        })
        return;
    }
    done();
}

function extraParamValidate(done) {
    for (let i = 0; i < templateForm.value.extraParams.length; i++) {
        const param = templateForm.value.extraParams[i];
        if (!hasText(param.name) || !hasText(param.key)) {
            ElMessage.error(`第${i + 1}行参数配置存在必填字段未填写`);
            return
        }
    }
    done();
}

function addExtraParam() {
    templateForm.value.extraParams.push({ name: '', key: '', type: 'input', enumList: '', defaultValue: '' });
}

function refreshList() {
    findTemplate().then(res => {
        templateList.value = res.data;
    })
}

function openUnlockConfirm(template) {
    prepareUnlockTemplate.value = template;
    unlockConfirmDialogVisible.value = true;
}

function unlockTemplateAndRefresh() {
    if (confirmUnlockTemplateKey.value !== prepareUnlockTemplate.value.key) {
        ElMessage.error('确认解锁失败');
        return;
    }
    unlockTemplate(prepareUnlockTemplate.value._id).then(res => refreshList());
    confirmUnlockTemplateKey.value = '';
    unlockConfirmDialogVisible.value = false;
}

function removeTemplateAndRefresh(id) {
    removeTemplate(id).then(res => refreshList());
}

onMounted(() => {
    refreshList();
})

</script>

<style>
.template-code-container {
    margin-bottom: 1rem;
    padding: 1rem 2rem;
}

.template-code-container .el-tabs--border-card>.el-tabs__content {
    padding: 0;
}

.el-drawer.btt,
.el-drawer.ttb.module-setting-drawer {
    width: 500px;
    margin: auto;
    padding-right: 60px;
}
</style>