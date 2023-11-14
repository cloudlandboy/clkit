<script setup>
import { ref, onMounted, onBeforeMount, watch, shallowRef } from 'vue';
import Clipboard from 'clipboard';
import Home from './components/home.vue';
import DateTime from './components/date-time.vue';
import LanPortScan from './components/lan-port-scan.vue';
import Process from './components/process.vue';
import Faker from './components/faker.vue';
import OfficeConvert from './components/office-convert.vue';
import CrudCodeGen from './components/code_gen/crud-code-gen.vue';
import ReplaceEachRow from './components/replace_each_row/inde.vue';
import JavaClass from './components/java-class.vue';
import { AutoIncrementKey } from "./util/id-utils";

const idGen = new AutoIncrementKey();

const config = {
  title: 'CLBOY工具箱',
  iconSrc: '/author-avatar.png',
  contextPath: '',
  defaultPath: '/',
  menus: [
    {
      path: idGen.getStringKey(), title: '代码生成', children: [
        { path: '/crud-code-gen', title: '增删改查' },
        { path: '/java-class', title: 'JAVA类处理' },
      ]
    },
    {
      path: idGen.getStringKey(), title: '系统相关', children: [
        { path: '/lan-port-scan', title: '端口扫描' },
        { path: '/process', title: '查杀进程' },
      ]
    },
    {
      path: idGen.getStringKey(), title: '数据生成', children: [
        { path: '/faker', title: '个人资料' }
      ]
    },
    {
      path: idGen.getStringKey(), title: '格式转换', children: [
        { path: '/format-now-time', title: '格式化当前时间' },
        { path: '/office-convert', title: 'OFFICE转换' }
      ]
    }
    ,
    {
      path: idGen.getStringKey(), title: '文本处理', children: [
        { path: '/replace-each-row', title: '替换每一行' }
      ]
    },
  ],
  routes: [
    { path: '/', component: Home },
    { path: '/format-now-time', component: DateTime },
    { path: '/process', component: Process },
    { path: '/faker', component: Faker },
    { path: '/office-convert', component: OfficeConvert },
    { path: '/crud-code-gen', component: CrudCodeGen },
    { path: '/replace-each-row', component: ReplaceEachRow },
    { path: '/java-class', component: JavaClass },
    { path: '/lan-port-scan', component: LanPortScan },
  ]
}

const defaultOpeneds = config.menus.map(m => m.path);
const currentRouter = ref('');
const currentComponent = shallowRef(Home);

watch(currentRouter, path => {
  window.history.replaceState(null, "", config.contextPath + path);
  currentComponent.value = findRoute(path).component;
})

function menuSelect(path) {
  currentRouter.value = path;
}

function findRoute(path) {
  return config.routes.find(r => r.path === path);
}

onBeforeMount(() => {
  const path = location.pathname.substring(config.contextPath.length - 1);
  const route = findRoute(path);
  menuSelect(route ? path : config.defaultPath);
})

onMounted(() => {
  new Clipboard('.copy-btn', {
    text: function (trigger) {
      return trigger.previousElementSibling;
    }
  });


});
</script>

<template>
  <div class="clboy-kit-container">
    <el-row>
      <el-col :span="3">
        <div class="kit-title" @click="menuSelect('/')">
          <el-avatar :src="config.iconSrc" v-if="config.iconSrc" style="vertical-align:middle;" />
          <a href="javascript:void(0);">{{ config.title }}</a>
        </div>
        <el-menu :default-active="currentRouter" @select="menuSelect" :default-openeds="defaultOpeneds">
          <el-sub-menu v-for="menu in config.menus" :index="menu.path">
            <template #title>{{ menu.title }}</template>
            <el-menu-item v-for="subMenu in menu.children" :index="subMenu.path">{{ subMenu.title }}</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-col>
      <el-col :span="21">
        <div class="route-content">
          <component :is="currentComponent" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style>
.el-menu,
.kit-title {
  user-select: none;
}

.clboy-kit-container {
  width: 100%;
}

.clboy-kit-container .route-content {
  padding: 64px 1rem;
}
</style>