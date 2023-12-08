<script setup>
import { ref, onMounted, onBeforeMount, h, shallowRef } from 'vue';
import { useAppConfigStore } from "./stores/app-config";
import Clipboard from 'clipboard';
import { checkUpdate, update as updateApp } from "./api/app";
import { ElNotification, ElButton } from 'element-plus'
import TreeMenu from "./components/tree-menu.vue";

const containerLayoutDef = {
  normal: {
    menu: {
      xs: 6,
      sm: 4,
      lg: 2,
    },
    view: {
      xs: 18,
      sm: 20,
      lg: 22,
    }
  },
  min: {
    menu: {
      xs: 2,
      sm: 1,
      lg: 1,
    },
    view: {
      xs: 22,
      sm: 23,
      lg: 23,
    }
  }
}

const containerLayout = ref(containerLayoutDef.normal);

const menuCollapse = ref(false);
const appConfigStore = useAppConfigStore();
const menuRef = ref();

const iframeCache = ref([]);

const defaultOpeneds = [appConfigStore.menuList[0].path];
const currentRouter = shallowRef({ path: '' });
const updateLoading = ref(false);

function menuSelect(path) {
  window.history.replaceState(null, "", appConfigStore.config.contextPath + path);
  currentRouter.value = findRoute(path);
  if (currentRouter.value.isComponent) {
    return
  }

  let toView = iframeCache.value.find(frame => frame.router.path === currentRouter.value.path);
  if (toView) {
    toView.viewCount++;
    return;
  }

  if (iframeCache.value.length < appConfigStore.config.maxIframeCache) {
    toView = { index: iframeCache.value.length, router: currentRouter.value, ref: null, viewCount: 0 };
    iframeCache.value.push(toView);
    return
  }

  //最少使用
  toView = iframeCache.value[0];
  let maxViewCount = 0;
  iframeCache.value.forEach(frame => {
    if (frame.viewCount < toView.viewCount) {
      toView = frame;
    }
    if (frame.viewCount > maxViewCount) {
      maxViewCount = frame.viewCount;
    }
  })
  toView.viewCount = maxViewCount + 1;
  toView.router = currentRouter.value;
}

function findRoute(path) {
  return appConfigStore.routerList.find(r => r.path === path);
}

function toggleMenuCollapse(collapse) {
  menuCollapse.value = collapse;
  if (collapse) {
    containerLayout.value = containerLayoutDef.min;
  } else {
    containerLayout.value = containerLayoutDef.normal;
  }
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
      customClass: 'clkit-update-notification',
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

function iframeLoaded(frame) {
  try {
    const iframeDocument = frame.ref.contentWindow.document;
    const scriptElement = iframeDocument.createElement('script');
    scriptElement.text = route.insertScript;
    scriptElement.id = "clkit-insert-script";
    iframeDocument.body.appendChild(scriptElement);
  } catch (err) {
    //ignore
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
  <div class="clkit-container" v-loading.fullscreen.lock="updateLoading" element-loading-text="更新中...">
    <el-row>
      <el-col :xs="containerLayout.menu.xs" :sm="containerLayout.menu.sm" :lg="containerLayout.menu.lg">
        <div class="kit-title" @click="menuSelect('/')">
          <el-avatar :src="appConfigStore.config.iconSrc" v-if="appConfigStore.config.iconSrc"
            style="vertical-align:middle;" />
          <a v-show="!menuCollapse" href="javascript:void(0);">{{ appConfigStore.config.title }}</a>
        </div>
        <div style="position: relative;height: 24px;text-align: center;">
          <el-icon v-show="menuCollapse" style="cursor: pointer" @click="toggleMenuCollapse(false)">
            <Menu />
          </el-icon>
          <el-icon v-show="!menuCollapse" style="cursor: pointer;position:absolute;right: 5px;"
            @click="toggleMenuCollapse(true)" size="large">
            <Fold />
          </el-icon>
        </div>
        <el-menu ref="menuRef" :default-active="currentRouter.path" @select="menuSelect" :default-openeds="defaultOpeneds"
          :collapse="menuCollapse" :collapse-transition="false">
          <tree-menu :menuList="appConfigStore.menuList" :menuCollapse="menuCollapse" />
        </el-menu>
      </el-col>
      <el-col :xs="containerLayout.view.xs" :sm="containerLayout.view.sm" :lg="containerLayout.view.lg">
        <div class="route-content">
          <div>
            <component v-if="currentRouter.isComponent" :is="currentRouter.view" />
          </div>
          <div v-for="frame in iframeCache" :key="frame.index">
            <iframe v-show="currentRouter.path === frame.router.path" :src="frame.router.view" frameborder="0"
              allowfullscreen="true" @load="iframeLoaded(frame)" :ref="frame.ref"
              style="width: 100%;height: 800px;"></iframe>
          </div>
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

.clkit-container {
  width: 100%;
}

.clkit-container .route-content {
  padding: 64px 1rem;
}

.clkit-update-notification {
  width: 180px
}
</style>