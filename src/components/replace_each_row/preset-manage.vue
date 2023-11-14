<template>
    <div style="padding-bottom: 0.6rem;text-align: right;">
        <el-button type="primary" @click="openSaveOrUpdate(null)">新增</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="label" label="名称" width="180" />
        <el-table-column label="操作">
            <template #default="scope">
                <el-button size="small" @click="openSaveOrUpdate(scope.row)">编辑</el-button>
                <el-button size="small" type="danger" v-if="!scope.row.locked"
                    @click="removeAndRefresh(scope.row._id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-dialog v-model="formDialogVisible" :title="form._id ? '修改' : '新增'" width="950px" :close-on-click-modal="false">
        <el-form ref="formRef" :model="form" label-position="right" :disabled="formDisabled" :rules="formRules"
            :validate-on-rule-change="false">
            <el-form-item label="名称" prop="label">
                <el-input v-model="form.label" />
            </el-form-item>
            <el-form-item label="模板" prop="pattern">
                <el-input v-model="form.pattern" />
            </el-form-item>
            <el-form-item label="排序值" prop="pattern">
                <el-input-number v-model="form.sortValue" />
            </el-form-item>
            <el-form-item style="padding: 8px;">
                <el-button type="primary" @click="addParam" style="position: absolute;right: 8px;">添加参数</el-button>
            </el-form-item>
            <el-form-item prop="params" class="type-mapping-matchs">
                <el-table :data="form.params" border empty-text=" ">
                    <el-table-column label="参数key" align="center" width="240px" heigth="80px" label-class-name="required">
                        <template #default="scope">
                            <el-form-item :prop="'params.' + scope.$index + '.key'">
                                <el-input v-model="scope.row.key" :validate-event="false" />
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="参数名称" align="center" width="240px" heigth="80px" label-class-name="required">
                        <template #default="scope">
                            <el-form-item :prop="'params.' + scope.$index + '.label'">
                                <el-input v-model="scope.row.label" :validate-event="false" />
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="默认值" align="center" width="240px" heigth="80px">
                        <template #default="scope">
                            <el-form-item :prop="'params.' + scope.$index + '.defaultValue'">
                                <el-input v-model="scope.row.defaultValue" :validate-event="false" />
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center">
                        <template #default="scope">
                            <el-button type="danger" size="small" @click="removeParam(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-form-item>

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
import { create, findAll, remove, update } from '../../api/replace-each-row-preset'

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
    key: [{ validator: hasTextValidator, label: '参数key' }],
    label: [{ validator: hasTextValidator, label: '参数名' }]
}

const formRules = ref({})


const fieldDef = new FieldDef({
    label: '',
    pattern: '',
    sortValue: 0,
    params: [],
});

const form = ref(fieldDef.getObj());

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

function removeParam(match) {
    formRef.value.clearValidate();
    const prefix = 'params.' + (form.value.params.length - 1) + '.';
    delete formRules.value[`${prefix}key`];
    delete formRules.value[`${prefix}label`];
    form.value.params = form.value.params.filter(i => i !== match);
}
function addParam() {
    formRef.value.clearValidate();
    form.value.params.push({ key: '', label: '', defaultValue: '' });
    const prefix = 'params.' + (form.value.params.length - 1) + '.';
    formRules.value[`${prefix}key`] = rulesDef.key;
    formRules.value[`${prefix}label`] = rulesDef.label;
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