<script setup>

import { ref, onMounted } from "vue";

import { ElMessage, ElMessageBox } from "element-plus";

import { useRouter, useRoute } from "vue-router";

let router = useRouter();
const route = useRoute();



const loginName = ref("");

onMounted(() => {

  let loginUser = JSON.parse(localStorage.getItem("loginUser"));

  if (loginUser) {

    loginName.value = loginUser.name;

  }

});



const logout = () => {

  ElMessageBox.confirm("确认退出登录吗?", "提示", {

    confirmButtonText: "确定",

    cancelButtonText: "取消",

    type: "warning",

  }).then(() => {

    ElMessage.success("退出登录成功");

    localStorage.removeItem("loginUser");

    router.push("/login");

  });

};

</script>



<template>

  <div class="common-layout">

    <el-container class="layout-root">

      <el-header class="header">

        <div class="header-left">

          <span class="logo-icon">E</span>

          <span class="title">智学教育管理平台</span>

        </div>

        <div class="header-right">

          <a href="javascript:void(0)" class="header-link" @click="logout">

            <el-icon><SwitchButton /></el-icon>

            <span>退出登录</span>

            <span class="user-name">{{ loginName }}</span>

          </a>

        </div>

      </el-header>



      <el-container class="layout-body">

        <el-aside class="aside">

          <el-menu

            router

            class="sidebar-menu"

            :default-active="route.path"

            background-color="transparent"

            text-color="#475569"

            active-text-color="#0d9488"

          >

            <el-sub-menu index="/manage">

              <template #title>

                <el-icon><Menu /></el-icon>

                <span>班级学员管理</span>

              </template>

              <el-menu-item index="/clazz">

                <el-icon><HomeFilled /></el-icon>

                <span>班级管理</span>

              </el-menu-item>

              <el-menu-item index="/stu">

                <el-icon><UserFilled /></el-icon>

                <span>学员管理</span>

              </el-menu-item>

            </el-sub-menu>



            <el-sub-menu index="/system">

              <template #title>

                <el-icon><Tools /></el-icon>

                <span>系统信息管理</span>

              </template>

              <el-menu-item index="/dept">

                <el-icon><HelpFilled /></el-icon>

                <span>部门管理</span>

              </el-menu-item>

              <el-menu-item index="/emp">

                <el-icon><Avatar /></el-icon>

                <span>员工管理</span>

              </el-menu-item>

              <el-menu-item index="/notice">

                <el-icon><Bell /></el-icon>

                <span>公告管理</span>

              </el-menu-item>

            </el-sub-menu>

          </el-menu>

        </el-aside>



        <el-main class="main-content">

          <router-view></router-view>

        </el-main>

      </el-container>

    </el-container>

  </div>

</template>



<style scoped>

.common-layout,

.layout-root {

  height: 100vh;

  overflow: hidden;

}



.header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  height: var(--app-header-height);

  padding: 0 var(--app-spacing-lg);

  background: linear-gradient(135deg, var(--app-header-start) 0%, var(--app-header-end) 100%);

  box-shadow: 0 2px 12px rgba(12, 74, 110, 0.25);

  z-index: 10;

}



.header-left {

  display: flex;

  align-items: center;

  gap: var(--app-spacing-sm);

}



.logo-icon {

  display: flex;

  align-items: center;

  justify-content: center;

  width: 36px;

  height: 36px;

  font-size: 18px;

  font-weight: 700;

  color: var(--app-header-start);

  background: rgba(255, 255, 255, 0.95);

  border-radius: var(--app-radius-sm);

}



.title {

  color: #fff;

  font-size: 22px;

  font-weight: 600;

  letter-spacing: 0.04em;

}



.header-right {

  display: flex;

  align-items: center;

  gap: var(--app-spacing-sm);

}



.header-link {

  display: inline-flex;

  align-items: center;

  gap: 6px;

  color: rgba(255, 255, 255, 0.9);

  text-decoration: none;

  font-size: 14px;

  padding: 6px 10px;

  border-radius: var(--app-radius-sm);

  transition: background 0.2s, color 0.2s;

}



.header-link:hover {

  color: #fff;

  background: rgba(255, 255, 255, 0.12);

}



.divider {

  color: rgba(255, 255, 255, 0.35);

  font-size: 14px;

}



.user-name {

  padding: 2px 8px;

  background: rgba(255, 255, 255, 0.15);

  border-radius: 999px;

  font-size: 13px;

}



.layout-body {

  height: calc(100vh - var(--app-header-height));

  overflow: hidden;

}



.aside {

  width: var(--app-sidebar-width) !important;

  background: var(--app-surface);

  border-right: 1px solid var(--app-border);

  overflow-y: auto;

  flex-shrink: 0;

}



.sidebar-menu {

  border-right: none;

  padding: var(--app-spacing-sm) 0;

}



.sidebar-menu :deep(.el-menu-item),

.sidebar-menu :deep(.el-sub-menu__title) {

  height: 46px;

  line-height: 46px;

  margin: 2px var(--app-spacing-xs);

  border-radius: var(--app-radius-sm);

}



.sidebar-menu :deep(.el-menu-item.is-active) {

  background: linear-gradient(90deg, rgba(13, 148, 136, 0.12), rgba(13, 148, 136, 0.04));

  font-weight: 600;

}



.sidebar-menu :deep(.el-menu-item:hover),

.sidebar-menu :deep(.el-sub-menu__title:hover) {

  background: var(--app-bg);

}



.main-content {

  background: var(--app-bg);

  padding: var(--app-spacing-lg);

  overflow-y: auto;

}

</style>

