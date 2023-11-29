<script setup>
import { ref, onMounted, onBeforeMount, h, shallowRef } from 'vue';
import { useAppConfigStore } from "./stores/app-config";
import Clipboard from 'clipboard';
import { checkUpdate, update as updateApp } from "./api/app";
import { ElNotification, ElButton } from 'element-plus'


const appConfigStore = useAppConfigStore();
const menuRef = ref();
const iframeRef = ref();

const defaultOpeneds = [appConfigStore.menuList[0].path];
const currentRouter = shallowRef({ path: '' });
const updateLoading = ref(false);

function menuSelect(path) {
  window.history.replaceState(null, "", appConfigStore.config.contextPath + path);
  currentRouter.value = findRoute(path);
}

function findRoute(path) {
  return appConfigStore.routerList.find(r => r.path === path);
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

function iframeLoaded() {
  try {
    const iframeDocument = iframeRef.value.contentWindow.document;
    const scriptElement = iframeDocument.createElement('script');
    scriptElement.text = currentRouter.value.insertScript;
    scriptElement.id = "clkit-insert-script";
    iframeDocument.body.appendChild(scriptElement);
  } catch (err) {
    //ignore
    console.error(err);
  }
}

onBeforeMount(() => {
  appConfigStore.renderMenu().finally(() => {
    const path = location.pathname.substring(appConfigStore.config.contextPath.length - 1);
    const route = findRoute(path);
    menuSelect(route ? path : appConfigStore.config.defaultPath);
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
          <el-avatar :src="appConfigStore.config.iconSrc" v-if="appConfigStore.config.iconSrc"
            style="vertical-align:middle;" />
          <a href="javascript:void(0);">{{ appConfigStore.config.title }}</a>
        </div>
        <el-menu ref="menuRef" :default-active="currentRouter.path" @select="menuSelect"
          :default-openeds="defaultOpeneds">
          <el-sub-menu v-for="menu in appConfigStore.menuList" :index="menu.path">
            <template #title>{{ menu.title }}</template>
            <el-menu-item v-for="subMenu in menu.children" :index="subMenu.path">{{ subMenu.title }}</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-col>
      <el-col :span="21">
        <div class="route-content">
          <component v-if="currentRouter.isComponent" :is="currentRouter.view" />
          <iframe v-else :src="currentRouter.view" frameborder="0" allowfullscreen="true" @load="iframeLoaded"
            ref="iframeRef" style="width: 100%;height: 800px;"></iframe>
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