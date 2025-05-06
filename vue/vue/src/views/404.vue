<script setup>
import img from "@/assets/404.png";
import router from "@/router/index.js";

const goHome = () => {
  location.href = '/manager/home'
}
</script>

<template>
  <div style="text-align: center">
    <el-image :src="img" alt="404 Not Found"></el-image>
    <div>
      <el-button type="primary" style="width: 100px; height: 50px; font-size: large" @click="goHome" >返回主页</el-button>
    </div>
  </div>
</template>

<style scoped>
/* 你可以在这里添加样式 */
</style>