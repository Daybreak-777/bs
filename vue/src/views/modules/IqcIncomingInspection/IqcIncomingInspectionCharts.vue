<template>
  <!-- 图表区域 -->
  <div>
    <div style="display: flex; grid-gap: 10px; margin-bottom: 10px">
      <!-- 各供应商的（产品）不良数量对比图 -->
      <el-card style="flex: 1; height: 300px" id="supplierBar"></el-card>
      <!-- 不同产品的不良数量对比图 -->
      <el-card style="flex: 1; height: 300px" id="materialBar"></el-card>
    </div>
    <div style="display: flex; grid-gap: 10px">
      <!-- 来料数量、不良率趋势图 -->
      <el-card style="flex: 1; height: 300px" id="iqcLine"></el-card>
      <!-- 不良数量占比饼图 -->
      <el-card style="flex: 1; height: 300px" id="iqcPie"></el-card>
    </div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts';
import { reactive, onMounted } from "vue";

// 绘制图表
const supplierBarOption = {
};

const materialBarOption = {
};

const iqcLineOption = {
};

const iqcPieOption = {
};

// onMounted页面所有元素初始化完成
onMounted(() => {
  // 基于准备好的dom，初始化echarts实例
  const supplierBarChart = echarts.init(document.getElementById('supplierBar'));
  // 使用刚指定的配置项目和数据显示图表
  supplierBarChart.setOption(supplierBarOption)

  const materialBarChart = echarts.init(document.getElementById('materialBar'));
  materialBarChart.setOption(materialBarOption)

  const iqcLineChart = echarts.init(document.getElementById('iqcLineBar'));
  iqcLineChart.setOption(iqcLineOption)

  const iqcPieChart = echarts.init(document.getElementById('iqcPie'));
  iqcPieChart.setOption(iqcPieOption)

})

</script>

<style scoped>

</style>
