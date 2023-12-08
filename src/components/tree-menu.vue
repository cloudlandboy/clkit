<template>
    <label v-for="menu in menuList" :key="menu._id" v-show="!menu.hide">
        <el-sub-menu v-if="menu.children" :index="menu.path">
            <template #title>
                <el-icon size="small" v-show="menuCollapse">
                    <Fold />
                </el-icon>
                <span v-show="!menuCollapse">{{ menu.title }}</span>
            </template>
            <tree-menu :menuList="menu.children" />
        </el-sub-menu>
        <el-menu-item v-else :index="menu.path">{{ menu.title }}</el-menu-item>
    </label>
</template>

<script setup>
const { menuList, menuCollapse } = defineProps(['menuList', 'menuCollapse'])
</script>