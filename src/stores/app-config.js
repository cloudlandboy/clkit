import { ref, shallowRef } from 'vue';
import { defineStore } from 'pinia'
import Home from '@/components/home.vue';
import DateTime from '@/components/date-time.vue';
import LanPortScan from '@/components/lan-port-scan.vue';
import Process from '@/components/process.vue';
import Faker from '@/components/faker.vue';
import OfficeConvert from '@/components/office-convert.vue';
import CrudCodeGen from '@/components/code_gen/crud-code-gen.vue';
import ReplaceEachRow from '@/components/replace_each_row/inde.vue';
import JavaClass from '@/components/java-class.vue';
import IntegrationManage from "@/components/integration-manage.vue";
import { AutoIncrementKey } from "@/util/id-utils";
import { getInstalledTree } from "@/api/integration";
import { INTEGRATION_TYPES } from "../constant/dict.constants";

const serverAddressPrefix = import.meta.env.VITE_SERVER_ADDRESS || '';
function serverIntegrationUrl(id, index) {
    if (index.startsWith('/')) {
        index = index.substring(1);
    }
    return `${serverAddressPrefix}/integration/${id}/${index}`;
}

export const useAppConfigStore = defineStore('appConfig', () => {
    const idGen = new AutoIncrementKey();
    const config = {
        title: 'CLKIT',
        iconSrc: '/author-avatar.png',
        contextPath: '',
        defaultPath: '/',
        maxIframeCache: 8,
        staticMenus: [
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
        staticRoutes: [
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
        path: idGen.getStringKey(), title: '集成', children: [{ path: '/integration-manage', title: '集成管理' }]
    }

    const menuList = ref([].concat(config.staticMenus, integrationMenu));
    const routerList = shallowRef([].concat(config.staticRoutes));

    function collectIntegrationMenu(treeNode, menuBucket, routerBucket) {
        const path = `/integration/${treeNode._id}`;
        const menu = { path, title: treeNode.name, hide: !!treeNode.hide };
        menuBucket.push(menu);
        if (INTEGRATION_TYPES.FOLDER.ve(treeNode.type)) {
            menu.children = [];
            treeNode.children.map(c => collectIntegrationMenu(c, menu.children, routerBucket));
        } else {
            routerBucket.push({
                path: path,
                view: INTEGRATION_TYPES.ONLINE_URL.ve(treeNode.type) ? treeNode.url : serverIntegrationUrl(treeNode._id, treeNode.index),
                isComponent: false,
                insertScript: treeNode.insertScript
            });
        }
    }

    async function renderMenu() {
        const integrationMenuChildren = [{ path: '/integration-manage', title: '集成管理' }];
        const integrationRouters = [];
        const res = await getInstalledTree();
        res.data.forEach(item => {
            collectIntegrationMenu(item, integrationMenuChildren, integrationRouters);
        })
        integrationMenu.children = integrationMenuChildren;
        menuList.value = [].concat(config.staticMenus, integrationMenu);
        routerList.value = [].concat(config.staticRoutes, integrationRouters);
    }

    return { config, menuList, routerList, renderMenu }
})