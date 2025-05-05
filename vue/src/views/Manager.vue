<template>
  <div>

    <!-- 头部开始 -->
    <div style="display: flex; align-items: center; height: 60px; background-color: #3c7fff">
      <div style="display: flex; align-items: center; width: 200px; padding-left: 20px">
        <img src="@/assets/logo.png" alt="" style="width: 40px;" />
        <span style="font-size: 24px; color: white">后台管理系统</span>
      </div>
      <div style="flex: 1"></div>
      <div style="display: flex; width: fit-content; align-items: center; padding-right: 10px">
<!--        <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="" style="width: 40px; height: 40px">-->
        <el-image :src=" data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 40px; height: 40px; border-radius: 50%"></el-image>
        <span style="color: white; margin-left: 5px">{{ data.user.name }}</span>
      </div>
    </div>
    <!-- 头部结束 -->

    <!-- 下面部分结束 -->
    <div style="display: flex">
      <!-- 左侧导航菜单开始 -->
      <div style="width: 200px;">
        <el-menu router :default-active="router.currentRoute.value.path" :default-openeds="['1',]">
          <el-menu-item index="/manager/home">
            <el-icon><house /></el-icon>
            系统首页
          </el-menu-item>

          <el-sub-menu index="1">
            <template #title>
              <el-icon><data-analysis /></el-icon>
              <span>IQC来料检验</span>
            </template>
            <el-menu-item index="/manager/IqcIncomingInspection">来料检验列表</el-menu-item>
            <el-menu-item index="/manager/IqcIncomingInspectionCharts">来料检验数据统计</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="2">
            <template #title>
              <el-icon><user /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/employee">员工信息</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/manager/person">
            <el-icon><user-filled /></el-icon>
            个人信息
          </el-menu-item>

          <el-menu-item index="/manager/password">
            <el-icon><Lock /></el-icon>
            修改密码
          </el-menu-item>

          <el-menu-item @click="logout">
            <el-icon><switch-button /></el-icon>
            退出登录
          </el-menu-item>
        </el-menu>
      </div>
      <!-- 左侧导航菜单结束 -->

      <!-- 右侧主体区域开始 -->
      <div style="flex: 1; width: 0; background-color: #f8f8ff;padding: 10px">
        <RouterView @updateUser="updateUser"></RouterView>
      </div>

    </div>

  </div>
</template>

<script setup>
import {DataAnalysis, House, Lock, SwitchButton, User} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {reactive} from "vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user'))
})

const logout = () => {
  localStorage.removeItem('xm-user') // 清除当前用户缓存的数据
  location.href = '/login' // 退出到登录页面
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user'))
}
</script>

<style scoped>
.el-menu .is-active{
  background-color: #e6ecf7 !important;
}
</style>
