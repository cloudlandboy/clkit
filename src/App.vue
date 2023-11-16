<script setup>
import { ref, onMounted, onBeforeMount, h, shallowRef } from 'vue';
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
import IntegrationManage from "./components/integration-manage.vue";
import { AutoIncrementKey } from "./util/id-utils";
import { findAllInstalled } from "./api/integration";
import { checkUpdate, update as updateApp } from "./api/app";
import { ElNotification, ElButton } from 'element-plus'

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
    { path: '/', view: Home, isComponent: true },
    { path: '/format-now-time', view: DateTime, isComponent: true },
    { path: '/process', view: Process, isComponent: true },
    { path: '/faker', view: Faker, isComponent: true },
    { path: '/office-convert', view: OfficeConvert, isComponent: true },
    { path: '/crud-code-gen', view: CrudCodeGen, isComponent: true },
    { path: '/replace-each-row', view: ReplaceEachRow, isComponent: true },
    { path: '/java-class', view: JavaClass, isComponent: true },
    { path: '/lan-port-scan', view: LanPortScan, isComponent: true },
    { path: '/integration-manage', view: IntegrationManage, isComponent: true }
  ]
}
const integrationMenu = {
  path: idGen.getStringKey(), title: '集成', children: [
    { path: '/integration-manage', title: '集成管理' },
  ]
}
config.menus.push(integrationMenu);

const menuList = ref(config.menus);
const defaultOpeneds = config.menus.map(m => m.path);
const currentRouter = shallowRef(config.routes[0]);
const updateLoading = ref(false);

function menuSelect(path) {
  window.history.replaceState(null, "", config.contextPath + path);
  currentRouter.value = findRoute(path);
}

function findRoute(path) {
  return config.routes.find(r => r.path === path);
}

function checkAppUpdate() {
  checkUpdate().then(res => {
    if (!res.data.serverUpdatable && !res.data.uiUpdatable) {
      return
    }

    const updateNotification = ElNotification({
      title: '更新提示',
      duration: 0,
      showClose: false,
      customClass: 'clboy-kit-update-notification',
      message: h('div', null, [
        h('div', { style: 'margin-bottom:8px' }, [
          h('p', null, res.data.serverUpdatable ? '有新版本发布' : '前端有新版本发布'),
          h('p', null, '是否更新?'),
        ]),
        h(ElButton, {
          type: 'primary',
          size: 'small',
          onClick: () => {
            updateNotification.close();
            updateLoading.value = true;
            updateApp(res.data.serverUpdatable, res.data.uiUpdatable).then((res) => {
              //只更新前端，刷新页面
              location.reload();
            }).catch(err => {
              console.log(err);
            })
          }
        }, {
          default: () => '是',
        }),
        h(ElButton, {
          size: 'small',
          onClick: () => updateNotification.close()
        }, {
          default: () => '否',
        })
      ]),
    })

  })
}

onBeforeMount(() => {
  findAllInstalled().then(res => {
    res.data.forEach(item => {
      integrationMenu.children.push({ path: `/integration/${item._id}`, title: item.name });
      config.routes.push({ path: `/integration/${item._id}`, view: item.url, isComponent: false });
    })
  }).finally(() => {
    const path = location.pathname.substring(config.contextPath.length - 1);
    const route = findRoute(path);
    menuSelect(route ? path : config.defaultPath);
  })

  checkAppUpdate();
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
  <div class="clboy-kit-container" v-loading.fullscreen.lock="updateLoading" element-loading-text="更新中...">
    <el-row>
      <el-col :span="3">
        <div class="kit-title" @click="menuSelect('/')">
          <el-avatar :src="config.iconSrc" v-if="config.iconSrc" style="vertical-align:middle;" />
          <a href="javascript:void(0);">{{ config.title }}</a>
        </div>
        <el-menu :default-active="currentRouter.path" @select="menuSelect" :default-openeds="defaultOpeneds">
          <el-sub-menu v-for="menu in menuList" :index="menu.path">
            <template #title>{{ menu.title }}</template>
            <el-menu-item v-for="subMenu in menu.children" :index="subMenu.path">{{ subMenu.title }}</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-col>
      <el-col :span="21">
        <div class="route-content">
          <component v-if="currentRouter.isComponent" :is="currentRouter.view" />
          <iframe v-else :src="currentRouter.view" frameborder="0" allowfullscreen="true"
            style="width: 100%;height: 800px;"></iframe>
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

.clboy-kit-update-notification {
  width: 180px
}
</style>