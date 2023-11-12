<template>
    <el-card class="box-card" style="width: 1000px;" v-loading="loading">
        <template #header>
            <div class="card-header">
                <span>JAVA类处理</span>
            </div>
        </template>
        <el-form :model="form">
            <el-form-item>
                <codemirror v-model="form.javaCode" @change="clearParseResultCache" placeholder="请将java类代码完整粘贴至此处"
                    :extensions="javaExtensions" :style="{ height: '380px', width: '100%' }" class="custom-scrollbar" />
            </el-form-item>
            <el-form-item>
                <el-button @click="doAction('toJson')" color="#3eede7">转JSON</el-button>
                <el-button @click="doAction('toCreateTable')" color="#ffa400">创建表SQL</el-button>
                <el-button @click="doAction('toAddColumn')" color="#ffa400">加字段SQL</el-button>
                <el-button @click="converChineseBytranslate" color="#4b5cc4">翻译属性名</el-button>
            </el-form-item>
        </el-form>

        <!-- convertResult -->
        <el-dialog v-model="convertResult.visible" :title="convertResult.title" width="50%">
            <div style="font-weight: bold;">不包含以下关键字修饰字段：</div>
            <el-checkbox-group v-model="convertResult.filterField" style="padding-left: 2rem;" @change="reDoAction">
                <el-checkbox value="static" label="static" />
                <el-checkbox value="final" label="final" />
                <el-checkbox value="transient" label="transient" />
            </el-checkbox-group>
            <codemirror v-model="convertResult.content" :extensions="converResultExtensions" style="min-height: 400px;"
                class="custom-scrollbar" />
        </el-dialog>

    </el-card>
</template>

<script setup>
import { ref } from "vue";
import { camelCase } from "lodash";
import { hasText } from "../util/string-utils";
import parseJava from "../util/java/chevrotain-java-parse";
import { ElMessage } from 'element-plus'
import { java, javaLanguage } from '@codemirror/lang-java'
import { json, jsonLanguage } from '@codemirror/lang-json'
import { sql, MySQL } from '@codemirror/lang-sql'
import { microsoftTranslator } from "../api/translate";

const javaExtensions = [java(), javaLanguage];
const jsonExtensions = [json(), jsonLanguage];
const sqlExtensions = [sql(), MySQL];
const converResultExtensions = ref(jsonExtensions);

const parseResultCache = {
    classInfo: null,
    parseFail: false
}

const loading = ref(false);

const form = ref({
    javaCode: ''
});

const currentAction = ref('');

const convertResult = ref({
    visible: false,
    title: '',
    content: '',
    filterField: ['static', 'final', 'transient']
}
);

function clearParseResultCache() {
    parseResultCache.classInfo = null;
    parseResultCache.parseFail = false;
}

function surroundDemoClass(code) {
    return `/**\n * demo\n */\npublic class Demo {\n\n${code}\n\n}`;
}

function fieldFilter(f) {
    return convertResult.value.filterField.length === 0 ? true : convertResult.value.filterField.every(ff => !f.modifiers.includes(ff));
}

function converChineseBytranslate() {
    const matchs = {};
    const data = [];
    let match = null;
    const regex = /\b(private|public|protected)?\s+(static\s+)?([a-zA-Z_$][a-zA-Z\d_$<>]*)\s+([\u4e00-\u9fff\w]+)\s*(=.*?;|;)/g
    while ((match = regex.exec(form.value.javaCode)) !== null) {
        if (/[\u4e00-\u9fff]+/.test(match[4])) {
            matchs[match[4]] = match[0];
            data.push({ Text: match[4] });
        }

    }
    if (data.length === 0) {
        ElMessage.warning('没有要翻译的内容');
        return;
    }

    loading.value = true;

    microsoftTranslator('', data).then(res => {
        let code = form.value.javaCode;
        for (let i = 0; i < res.data.length; i++) {
            const key = data[i].Text;
            const fieldDesc = matchs[key];
            let fieldDescRes = fieldDesc.replace(key, camelCase(res.data[i].translations[0].text));
            fieldDescRes = `/**\n     * ${key}\n     */\n    @ApiModelProperty(value = "${key}")\n    ${fieldDescRes}`;
            code = code.replace(fieldDesc, fieldDescRes);
        }
        form.value.javaCode = code;
    }).catch(err => {
        ElMessage.error('翻译接口不可用');
    }).finally(() => loading.value = false);
}

function doAction(action) {
    if (initClassInfo()) {
        actions[action]();
        currentAction.value = action;
        convertResult.value.visible = true;
    }
}

function toSql(mode) {
    convertResult.value.title = '生成SQL';
    convertResult.value.content = parseResultCache.classInfo.toSql(fieldFilter, mode);
    converResultExtensions.value = sqlExtensions;
}

const actions = {
    toJson: () => {
        convertResult.value.title = 'JSON';
        convertResult.value.content = JSON.stringify(parseResultCache.classInfo.toJson(fieldFilter), null, 2);
        converResultExtensions.value = jsonExtensions;
    },
    toCreateTable: () => {
        toSql('ct');
    },
    toAddColumn: () => {
        toSql('ac');
    },
    translate: () => {
    }
}

function reDoAction() {
    doAction(currentAction.value);
}

function initClassInfo() {
    if (!hasText(form.value.javaCode)) {
        form.value.javaCode = surroundDemoClass('');
    }
    if (parseResultCache.classInfo) {
        return true;
    }

    if (parseResultCache.parseFail) {
        ElMessage.error('代码格式有误');
        return false;
    }
    try {
        parseResultCache.classInfo = parseJava(form.value.javaCode);
        return true;
    } catch (e1) {
        try {
            const tryCode = surroundDemoClass(form.value.javaCode);
            parseResultCache.classInfo = parseJava(tryCode);
            form.value.javaCode = tryCode;
            return true;
        } catch (e2) {
            parseResultCache.parseFail = true;
            ElMessage.error('代码格式有误');
        }
        return false;
    }
}


</script>