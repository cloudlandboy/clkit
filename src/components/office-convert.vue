<template>
    <el-card class="box-card" v-loading.fullscreen="loading" element-loading-text="转换中">
        <template #header>
            <div class="card-header">
                <span>Office转换</span>
            </div>
        </template>
        <div style="text-align: center;">
            <el-upload ref="upload" :action="actionUrl" :data="form" :limit="1" :on-exceed="handleExceed"
                :show-progress="false" :on-success="handleSuccess" :on-error="handleError" :auto-upload="false"
                :before-upload="beforeUpload" :http-request="actionConvert">
                <template #trigger>
                    <el-button>选择文件</el-button>
                </template>
            </el-upload>
            <div style="margin-top: 1rem;">
                <el-select v-model="form.toType">
                    <el-option v-for="type in supportTypes" :key="type" :label="type" :value="type" />
                </el-select>
            </div>
            <div style="margin-top: 1rem;">
                <el-button type="primary" @click="submit">转换</el-button>
            </div>
        </div>
        <el-dialog v-model="filePreviewVisible" title="文件预览" width="100%" center>
            <div v-for="page in pdfView.pages" style="text-align: center;">
                <canvas :id="'pdf-view-' + page"></canvas>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="filePreviewVisible = false">关闭</el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, genFileId } from 'element-plus'
import { convertUrl, listSupportFileType, convert } from '../api/office';
import { downloadFile } from '../util/web-file-utils';

const actionUrl = ref(convertUrl);
const upload = ref();

const form = ref({
    toType: ''
})
const supportTypes = ref([]);
const loading = ref(false);
const filePreviewVisible = ref(false);

const pdfView = ref({
    pages: 10
});

function handleExceed(files) {
    upload.value.clearFiles();
    const file = files[0];
    file.uid = genFileId();
    upload.value.handleStart(file)
}

function beforeUpload(rawFile) {
    const namePart = rawFile.name.split('.');
    const ext = namePart.pop();
    if (!supportTypes.value.includes(ext)) {
        ElMessage.error('不支持的文件类型');
        return false;
    }
    rawFile.baseName = namePart.join('');
    rawFile.suffixnName = ext;
    return true;

}

function submit() {
    upload.value.submit();
}

function handleSuccess(res, file, files) {
    loading.value = false;
    downloadFile(res.data, { type: res.headers['content-type'] }, `${file.raw.baseName}.${form.value.toType}`);
}

function handleError(error, uploadFile, uploadFiles) {
    loading.value = false;
}

async function actionConvert(options) {
    loading.value = true;
    return await convert(options.file, options.data);
}

onMounted(() => {
    listSupportFileType().then(res => {
        supportTypes.value = res.data;
        form.value.toType = supportTypes.value[0];
    })
})
</script>