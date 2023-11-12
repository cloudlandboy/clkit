<template>
    <el-card class="box-card" style="width: 600px;">
        <template #header>
            <div class="card-header">
                <span>文本加前后缀 </span>
                <el-popover placement="bottom" title="提示" :width="200" trigger="hover">
                    <template #reference>
                        <el-icon>
                            <InfoFilled />
                        </el-icon>
                    </template>
                    <div>
                        前后缀中可使用的占位符
                        <ul style="padding-left: 1rem;">
                            <li style="margin-bottom: 8px;"><code>${index}</code> 从0开始</li>
                            <li><code>${lineNum}</code> 从1开始</li>
                        </ul>
                    </div>
                </el-popover>
            </div>
        </template>
        <el-form :model="form" label-width="3rem">
            <el-form-item>
                <el-select v-model="form.format" class="full-size" @change="formatText">
                    <el-option v-for="(fp, key) in formatPatternList" :key="key" :label="fp.name" :value="key" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-input v-model="form.prefix" style="margin-right: 1rem;"
                    :placeholder="formatPatternList[form.format].prefixPlaceholder"
                    :disabled="formatPatternList[form.format].disableInput === 'prefix' || formatPatternList[form.format].disableInput === 'both'"
                    @input="formatText" />
                <el-input v-model="form.suffix" :placeholder="formatPatternList[form.format].suffixPlaceholder"
                    :disabled="formatPatternList[form.format].disableInput === 'suffix' || formatPatternList[form.format].disableInput === 'both'"
                    @input="formatText" />
            </el-form-item>
            <el-form-item>
                <el-input v-model="form.text" :rows="8" type="textarea" placeholder="请输入文本" @input="formatText"
                    resize="none" class="custom-scrollbar" input-style="white-space: pre" />
            </el-form-item>
            <el-form-item>
                <el-input v-model="form.processAftertext" :rows="8" readonly type="textarea" resize="none"
                    :placeholder="formatPatternList[form.format].pattern" class="custom-scrollbar"
                    input-style="white-space: pre" />
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script setup>
import { ref } from "vue";
import { hasText, placeholderReplace } from "../util/string-utils";

const formatPatternList = {
    custom: {
        name: '自定义',
        pattern: '${prefix}${text}${suffix}',
        prefixPlaceholder: '前缀',
        suffixPlaceholder: '后缀',
        disableInput: 'none'
    },
    mysqldump: {
        name: 'mysql备份库',
        pattern: 'mysqldump -u ${prefix} -p${suffix} ${text} > ${text}.sql',
        prefixPlaceholder: '用户名',
        suffixPlaceholder: '密码',
        disableInput: 'none'
    },
    mysqldumpImport: {
        name: 'mysql备份导入',
        pattern: 'mysql -u ${prefix} -p${suffix} ${text} < ${text}.sql',
        prefixPlaceholder: '用户名',
        suffixPlaceholder: '密码',
        disableInput: 'none'
    },
    truncateTable: {
        name: 'TRUNCATE TABLE',
        pattern: 'TRUNCATE TABLE ${text};',
        prefixPlaceholder: '',
        suffixPlaceholder: '',
        disableInput: 'both'
    },
    deleteTable: {
        name: 'DELETE TABLE',
        pattern: 'DELETE FROM ${text} ${suffix};',
        prefixPlaceholder: '',
        suffixPlaceholder: '后置sql',
        disableInput: 'prefix'
    }
};

const form = ref({
    prefix: '',
    suffix: '',
    text: '',
    processAftertext: '',
    format: 'custom'
});



function formatText() {
    if (!hasText(form.value.text)) {
        form.value.processAftertext = '';
        return
    }
    const pattern = formatPatternList[form.value.format].pattern;
    form.value.processAftertext = form.value.text.split('\n').map((lineText, index) => {
        return placeholderReplace(pattern, {
            prefix: placeholderReplace(form.value.prefix, { index, lineNum: index + 1 }),
            suffix: placeholderReplace(form.value.suffix, { index, lineNum: index + 1 }),
            text: lineText
        });
    }).join('\n');
}

</script>