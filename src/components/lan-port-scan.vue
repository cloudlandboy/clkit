<template>
    <el-card class="box-card" v-loading.fullscreen="loading" element-loading-text="扫描中">
        <template #header>
            <div class="card-header">
                <span>扫描局域网内端口</span>
            </div>
        </template>
        <el-form ref="scanPortForm" :model="form" @keydown..enter="actionScanPort" label-position="right"
            label-width="100px" status-icon>
            <el-form-item label="本机内网ip" prop="ip">
                <el-select v-model="form.ip" class="full-size">
                    <el-option v-for="item in ipOptions" :key="item" :label="item" :value="item" />
                </el-select>
            </el-form-item>
            <el-form-item label="扫描端口" prop="port">
                <el-input-number v-model="form.minPort" :min="0" :max="65535" :controls="false" />
                <el-col class="text-center" :span="1" style="margin: 0 0.5rem">-</el-col>
                <el-input-number v-model="form.maxPort" :min="0" :max="65535" :controls="false" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="actionScanPort">扫描</el-button>
            </el-form-item>
        </el-form>
        <!-- <el-input v-model="port" placeholder="扫描指定端口,为空扫描所有" /> -->
        <el-drawer v-model="scanResultViewer" title="端口扫描结果" direction="rtl">
            <el-collapse v-model="activeNames">
                <el-collapse-item v-for="item in scanResult" :title="item.ip" :name="item.ip">
                    <a style="margin-right: 8px;" v-for="port in item.ports" target="_blank"
                        :href="'http://' + item.ip + ':' + port"><el-tag>{{ port }}</el-tag></a>
                </el-collapse-item>
            </el-collapse>
        </el-drawer>
    </el-card>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { getLanIp, scanLanIp } from '../api/net';
import localStore from "../util/local-store";

let ipOptions = ref([]);
const form = ref(localStore.getJsonOrDefault('lanPortScan', {
    ip: '',
    minPort: 8080,
    maxPort: 8090
}));

const loading = ref(false);
const scanResultViewer = ref(false);
const scanPortForm = ref();
const scanResult = ref('');
const activeNames = ref([]);


function actionScanPort() {
    loading.value = true;
    localStore.set('lanPortScan', form.value);
    scanLanIp(form.value).then(res => {
        const ips = res.data.filter(item => item.ports.length > 0);
        scanResult.value = ips;
        scanResultViewer.value = true;
    }).finally(() => {
        loading.value = false;
    });
}


onMounted(() => {
    getLanIp().then(res => {
        ipOptions.value = res.data;
        form.value.ip = res.data[0];
    })
})


</script>