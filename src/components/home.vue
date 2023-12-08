<template>
    <div class="kit-home-content">
        <ul>
            <li>当前版本: <el-tag type="success" style="margin-left: 1rem;">{{ version }}</el-tag></li>
            <li>博客地址：<a href="https://www.clboy.cn/" target="_blank">https://www.clboy.cn</a></li>
            <li>前端源码：<a href="https://github.com/cloudlandboy/clkit"
                    target="_blank">https://github.com/cloudlandboy/clkit</a></li>
            <li>服务端源码：<a href="https://github.com/cloudlandboy/clkit-server"
                    target="_blank">https://github.com/cloudlandboy/clkit-server</a></li>
        </ul>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getVersion } from "../api/app";

const version = ref('');

onMounted(() => {
    getVersion().then(res => version.value = res.data);
})
</script>

<style>
.kit-home-content {
    padding: 1rem;
}

.kit-home-content ul {
    margin-left: 38px;
    list-style-type: disclosure-closed;
}

.kit-home-content li {
    padding: 1rem;
    color: #337ecc;
}

.kit-home-content li>a {
    text-decoration: none;
    color: #73767a;
    font-size: 0.8rem;
}
</style>