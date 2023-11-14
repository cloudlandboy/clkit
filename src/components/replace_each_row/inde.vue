<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span>替换每一行文本 </span>
                <el-popover placement="bottom" title="提示" :width="200" trigger="hover">
                    <template #reference>
                        <el-icon>
                            <InfoFilled />
                        </el-icon>
                    </template>
                    <div>
                        <span>可使用的占位符</span>
                        <ul style="padding-left: 1rem;">
                            <li style="margin-bottom: 8px;"><code>${index}</code> 从0开始</li>
                            <li><code>${lineNum}</code> 从1开始</li>
                        </ul>
                    </div>
                </el-popover>
                <el-button style="float: right;margin-left: 1rem;" @click="presetDialogVisible = true">预设管理</el-button>
            </div>
        </template>
        <el-form :model="form" label-width="3rem">
            <el-form-item label="预设">
                <el-select v-model="preset" class="full-size" @change="formatText">
                    <el-option v-for="preset in presetList" :key="preset._id" :label="preset.label" :value="preset._id" />
                </el-select>
            </el-form-item>
            <el-form-item v-if="!!presetList[preset]">
                <el-input v-for="param in presetList[preset].params" v-model="form.presetParams[param.key]"
                    :placeholder="param.label" style="margin-right:8px" @input="formatText" />
            </el-form-item>
            <el-form-item>
                <el-input v-model="form.text" :rows="8" type="textarea" placeholder="请输入文本" @input="formatText"
                    resize="none" class="custom-scrollbar" input-style="white-space: pre" />
            </el-form-item>
            <el-form-item>
                <el-input v-model="form.processedText" :rows="8" readonly type="textarea" resize="none"
                    :placeholder="presetList[preset] ? presetList[preset].pattern : ''" class="custom-scrollbar"
                    input-style="white-space: pre" />
            </el-form-item>
        </el-form>

        <!-- page -->
        <el-dialog v-model="presetDialogVisible" title="预设管理" width="380px" :destroy-on-close="true">
            <preset-manage></preset-manage>
        </el-dialog>

    </el-card>
</template>

<script setup>
import { ref, watch, onBeforeMount } from "vue";
import PresetManage from "./preset-manage.vue";
import { hasText, placeholderReplace } from "../../util/string-utils";
import { findAll } from "../../api/replace-each-row-preset";

const presetDialogVisible = ref(false);
const presetList = ref([]);;

const preset = ref('');

const form = ref({
    text: '',
    preset: '',
    processedText: '',
    presetParams: {},
});

watch(preset, id => {
    const dv = {};
    for (const param of presetList.value[id].params) {
        dv[param.key] = param.defaultValue || '';
    }
    form.value.presetParams = dv;
})

function formatText() {
    if (!hasText(form.value.text)) {
        form.value.processedText = '';
        return
    }
    const pattern = presetList.value[preset.value].pattern;
    form.value.processedText = form.value.text.split('\n').map((lineText, index) => {
        const globalContext = {
            index,
            lineNum: index + 1
        }
        const context = {
            text: lineText,
        };
        for (const key in form.value.presetParams) {
            context[key] = placeholderReplace(form.value.presetParams[key], globalContext);
        }
        return placeholderReplace(pattern, Object.assign(context, globalContext));
    }).join('\n');
}

onBeforeMount(() => {
    findAll().then(res => {
        const pl = {};
        res.data.sort((n, m) => n.sortValue - m.sortValue);
        res.data.forEach(item => {
            pl[item._id] = item;
        })
        presetList.value = pl;
        preset.value = Object.values(pl)[0]._id;

    })
})

</script>