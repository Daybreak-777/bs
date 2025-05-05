<template>
  <!-- 为 ECharts 准备一个定义了宽高的 DOM -->
<!--  <div id="main" style="width: 600px;height:400px;"></div>-->
<!--  <div id="line" style="width: 600px;height:400px;"></div>-->
  <div>
    <div style="display: flex; grid-gap: 10px">
      <el-card style="flex: 1; height: 400px" id="bar"></el-card>
      <el-card style="flex: 1; height: 400px" id="line"></el-card>
    </div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts';
import { reactive, onMounted } from "vue";

// 绘制图表
const barOption = {
  title: {
    text: 'ECharts 入门示例'
  },
  tooltip: {},
  legend: {
    data:['销量']
  },
  xAxis: {
    data: []
  },
  yAxis: {},
  series: [
    {
      name: '销量',
      type: 'bar',
      data: []
    }
  ]
};

const option1 = {
  xAxis: {
    type: 'category',
    data: ['A', 'B', 'C']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [120, 200, 150],
      type: 'line'
    }
  ]
}

// onMounted页面所有元素初始化完成
onMounted(() => {
  // 基于准备好的dom，初始化echarts实例
  const myChart = echarts.init(document.getElementById('bar'));
  // 使用刚指定的配置项目和数据显示图表
  myChart.setOption(barOption)

  const myChart1 = echarts.init(document.getElementById('line'));
  myChart1.setOption(option1)

})

</script>

<style scoped>

</style>