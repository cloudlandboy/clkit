<template>
    <div style="padding-bottom: 0.6rem;text-align: right;">
        <el-button type="primary" @click="openSaveOrUpdate(null)">新增</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="dbType" label="数据库类型" width="180" />
        <el-table-column prop="langType" label="语言类型" width="180" />
        <el-table-column label="操作">
            <template #default="scope">
                <el-button size="small" @click="copy(scope.row)">复制</el-button>
                <el-button size="small" @click="openSaveOrUpdate(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" v-if="!scope.row.locked"
                    @click="removeAndRefresh(scope.row._id)">删除</el-button>
                <el-button size="small" type="warning" v-if="scope.row.locked"
                    @click="openUnlockConfirm(scope.row)">解锁</el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-dialog v-model="formDialogVisible" :title="form._id ? '修改' : '新增'" width="1280px" :close-on-click-modal="false">
        <el-form ref="formRef" :model="form" :inline="true" label-position="right" :disabled="formDisabled"
            :rules="formRules" :validate-on-rule-change="false">
            <el-form-item label="数据库类型" prop="dbType">
                <el-select v-model="form.dbType" :validate-event="false">
                    <el-option v-for="dbType in dbTypes" :key="dbType" :label="dbType" :value="dbType" />
                </el-select>
            </el-form-item>
            <el-form-item label="语言" prop="langType">
                <el-select v-model="form.langType" :validate-event="false">
                    <el-option v-for="lang in languages" :key="lang" :label="lang" :value="lang" />
                </el-select>
            </el-form-item>
            <el-form-item label="加锁" prop="locked">
                <el-switch v-model="form.locked" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addMacth">添加配置</el-button>
            </el-form-item>
            <el-form-item prop="matchs" class="type-mapping-matchs">
                <el-table :data="form.matchs" border empty-text=" ">
                    <el-table-column label="正则表达式" align="center" width="240px" heigth="80px" label-class-name="required">
                        <template #default="scope">
                            <el-form-item :prop="'matchs.' + scope.$index + '.match'">
                                <el-input v-model="scope.row.match" :readonly="scope.row.required"
                                    :validate-event="false" />
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="语言类型" align="center" width="180px" label-class-name="required">
                        <template #default="scope">
                            <el-form-item :prop="'matchs.' + scope.$index + '.type'">
                                <el-input v-model="scope.row.type" :validate-event="false" />
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="包名" align="center" width="240px" label-class-name="required">
                        <template #default="scope">
                            <el-form-item :prop="'matchs.' + scope.$index + '.package'">
                                <el-input v-model="scope.row.package" :validate-event="false" />
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="需要导入包" align="center" width="150px" label-class-name="required">
                        <template #default="scope">
                            <el-form-item>
                                <el-radio v-model="scope.row.needImport" :label="true">是</el-radio>
                                <el-radio v-model="scope.row.needImport" :label="false">否</el-radio>
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="是数字类型" align="center" width="150px" label-class-name="required">
                        <template #default="scope">
                            <el-form-item>
                                <el-radio v-model="scope.row.isNumber" :label="true">是</el-radio>
                                <el-radio v-model="scope.row.isNumber" :label="false">否</el-radio>
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="是小数类型" align="center" width="150px" label-class-name="required">
                        <template #default="scope">
                            <el-form-item>
                                <el-radio v-model="scope.row.isDecimal" :label="true">是</el-radio>
                                <el-radio v-model="scope.row.isDecimal" :label="false">否</el-radio>
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center">
                        <template #default="scope">
                            <el-button v-if="!scope.row.required" type="danger" size="small"
                                @click="removeMatch(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-form-item>


            <div style="text-align: center;margin-top: 20px;">

            </div>
            <div class="content-right">
                <el-button type="primary" @click="submitSaveOrUpdate">{{ form._id ? '更新' : '保存' }}</el-button>
                <el-button @click="formDialogVisible = false">取消</el-button>
            </div>
        </el-form>
    </el-dialog>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { FieldDef } from "../../util/object-utils";
import { hasText } from "../../util/string-utils";
import { languages, dbTypes } from "../../constant/code-gen-constant.json";
import { create, findAll, remove, update, unlock } from '../../api/db-lang-type'
import { ElMessageBox } from 'element-plus'

const tableData = ref([]);

const formDisabled = ref(false);
const formDialogVisible = ref(false);

const formRef = ref();

const hasTextValidator = function (rule, value, callback) {
    if (hasText(value)) {
        callback();
        return
    }
    callback(new Error(`${rule.label}不能为空`));
}

const rulesDef = {
    match: [{ validator: hasTextValidator, label: '正则表达式' }],
    type: [{ validator: hasTextValidator, label: '语言类型' }]

}

const formRules = ref({
    "matchs.0.match": rulesDef.match,
    "matchs.0.type": rulesDef.type,
    "matchs.0.package": rulesDef.package
})

const matchDef = new FieldDef({
    match: '',
    type: '',
    package: '',
    needImport: false,
    isNumber: false,
    isDecimal: false,
    required: false
});

const defaultMatch = matchDef.getObj();
defaultMatch.match = '.*';
defaultMatch.required = true;
const fieldDef = new FieldDef({
    dbType: dbTypes[0],
    langType: languages[0],
    locked: false,
    matchs: [defaultMatch],
});

const form = ref(fieldDef.getObj());

function copy(data) {
    const copyData = JSON.parse(JSON.stringify(data));
    delete copyData._id;
    copyData.locked = false;
    openSaveOrUpdate(copyData);
}

function openSaveOrUpdate(data) {
    if (data) {
        form.value = data;
        formDisabled.value = data.locked;
    } else {
        form.value = fieldDef.getObj();
    }
    formDialogVisible.value = true;
}

function removeAndRefresh(id) {
    remove(id).then(fetchData);
}

function openUnlockConfirm(data) {
    ElMessageBox.confirm('确认要解锁吗?', '解锁提醒',
        {
            type: 'warning',
            confirmButtonText: '确认',
            cancelButtonText: '取消'
        }
    ).then(() => {
        unlock(data._id).then(res => { fetchData() })
    }).catch(() => { });
}

function submitSaveOrUpdate() {
    formRef.value.validate(isValid => {
        if (!isValid) {
            return;
        }

        if (form.value._id) {
            update(form.value._id, form.value).then(() => {
                fetchData();
                formDialogVisible.value = false;
            });
            return
        }

        create(form.value).then(() => {
            fetchData();
            formDialogVisible.value = false;
        });


    });
}

function removeMatch(match) {
    formRef.value.clearValidate();
    const prefix = 'matchs.' + (form.value.matchs.length - 1) + '.';
    delete formRules.value[`${prefix}match`];
    delete formRules.value[`${prefix}type`];
    delete formRules.value[`${prefix}package`];
    form.value.matchs = form.value.matchs.filter(i => i !== match);
}
function addMacth() {
    formRef.value.clearValidate();
    form.value.matchs.unshift(matchDef.getObj());
    const prefix = 'matchs.' + (form.value.matchs.length - 1) + '.'
    formRules.value[`${prefix}match`] = rulesDef.match;
    formRules.value[`${prefix}type`] = rulesDef.type;
    formRules.value[`${prefix}package`] = rulesDef.package;
}

function clearItemValidate(prop, event) {
    formRef.value.clearValidate([prop]);
}

function fetchData() {
    findAll().then(res => tableData.value = res.data)
}

onMounted(fetchData);
</script>

<style>
.type-mapping-matchs .cell {
    overflow: visible;
}

.type-mapping-matchs .el-form-item__error {
    top: 32px;
    right: 0;
}

.type-mapping-matchs .el-table__cell {
    padding-bottom: 1.2rem;
}
</style>