<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span>进程相关</span>
            </div>
        </template>
        <el-form :model="processForm" label-position="right" label-width="60px">
            <el-form-item label="查端口" prop="port">
                <el-input-number placeholder="输入端口号" v-model="processForm.port" :min="0" :max="65535" :controls="false" />
                <el-button class="submit-btn" @click="submitFindPort">查询</el-button>
            </el-form-item>
            <el-form-item label="查名称" prop="name">
                <el-input placeholder="输入进程名称" v-model="processForm.name" input-style="text-align: center;" />
                <el-button class="submit-btn" @click="submitFindName">查询</el-button>
            </el-form-item>
            <el-form-item label="查PID" prop="findPid">
                <el-input-number placeholder="输入pid" v-model="processForm.findPid" :min="0" :controls="false" />
                <el-button class="submit-btn" @click="submitFindPid">查询</el-button>
            </el-form-item>
            <!-- <el-form-item label="杀进程" prop="killPid">
                <el-input-number placeholder="输入pid" v-model="processForm.killPid" :min="0" :controls="false" />
                <el-button type="danger" class="submit-btn" @click="submitKillPid(processForm.killPid)">Kill</el-button>
            </el-form-item> -->
        </el-form>
        <el-dialog v-model="findPidResultVisible" :title="findPidResult.title" width="30%">
            <el-table :data="findPidResult.pidList" :default-sort="{ prop: 'memUsage', order: 'descending' }"
                style="width: 100%" max-height="300">
                <el-table-column prop="imageName" label="程序名称" sortable />
                <el-table-column prop="pid" label="PID" sortable />
                <el-table-column prop="memUsage" label="内存使用" sortable />
                <el-table-column fixed="right" label="操作">
                    <template #default="scope">
                        <el-button type="danger" size="small"
                            @click.prevent="findPidResultKill(scope.row.pid)">Kill</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="findPidResultVisible = false">关闭</el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>

<script setup>

import { ref } from 'vue';
import { findPid, killPid } from '../api/net';
import { prefixLocalStore } from "../util/local-store";
import { ElNotification, ElMessage } from 'element-plus'

const localStore = prefixLocalStore('process');

const processForm = ref({
    port: localStore.getNumberOrDefault('findPort', 8080),
    name: localStore.getOrDefault('findName', ''),
    findPid: null,
    killPid: null
});

const findPidResult = ref({
    title: '查询结果',
    pidList: []
});
const findPidResultVisible = ref(false);

function submitFindPort() {
    const port = processForm.value.port;
    if (port) {
        localStore.set('findPort', port);
        actionFindPid('port', port, '查询端口没有被占用', '查询端口占用进程');
    }
}

function submitFindName() {
    processForm.value.name = processForm.value.name.trim();
    if (processForm.value.name.length > 0) {
        localStore.set('findName', processForm.value.name);
        actionFindPid('name', processForm.value.name, '没有包含查询名称的进程', '包含查询名称的进程');
    }
}

function submitFindPid() {
    const pid = processForm.value.findPid;
    if (pid) {
        actionFindPid('pid', pid, '查询PID没有被占用', '查询PID进程');
    }
}

function submitKillPid(pid, callBack) {
    if (!pid) {
        return;
    }
    killPid({ pid }).then(res => {
        ElNotification.success({ title: '进程已杀死' });
        if (callBack) {
            callBack();
        }
    })
}

function findPidResultKill(pid) {
    submitKillPid(pid, () => {
        const pidList = findPidResult.value.pidList;
        findPidResult.value.pidList = pidList.filter(pi => pi.pid !== pid);
    })
}

function actionFindPid(type, value, noneMessage, resultTitle) {
    findPid(type, value).then(res => {
        if (res.data.length === 0) {
            ElMessage(noneMessage);
        } else {
            findPidResultVisible.value = true;
            findPidResult.value.title = resultTitle;
            findPidResult.value.pidList = res.data;
        }
    });
}
</script>

<style scoped>
.submit-btn {
    margin-left: 1rem;
}

.el-input {
    width: 150px;
}
</style>