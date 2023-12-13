/**
 * 
 * @author: clboy
 * @date: 2023-12-13 12:32:30
 * @Copyright (c) 2023 by syl@clboy.cn, All Rights Reserved. 
 */
import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import { basicSetup } from 'codemirror'
import VueCodemirror from 'vue-codemirror'
import VueDragResize from 'vue-drag-resize/src/components/vue-drag-resize.vue';

const app = createApp(App)
app.use(createPinia());
app.use(ElementPlus)
app.component('vue-drag-resize', VueDragResize)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(VueCodemirror, {
  autofocus: true,
  disabled: false,
  indentWithTab: true,
  tabSize: 2,
  placeholder: 'Code goes here...',
  extensions: [basicSetup]
})

app.mount('#app');

