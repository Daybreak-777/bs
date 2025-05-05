<template>
  <div>
    <!-- 筛选区域 -->
    <el-card style="margin-bottom: 15px">
      <el-row :gutter="10">
        <el-col :span="8">
          <el-date-picker
              v-model="filterParams.inspectionDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
          />
        </el-col>
        <el-col :span="4">
          <el-input
              v-model="filterParams.supplier"
              placeholder="筛选供应商"
              prefix-icon="Search"
          />
        </el-col>
        <el-col :span="4">
          <el-input
              v-model="filterParams.materialName"
              placeholder="筛选产品名称"
              prefix-icon="Search"
          />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="refreshChart">刷新图表</el-button>
          <el-button type="warning" @click="resetFilter">重置筛选</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 图表区域 -->
    <el-card>
      <!-- 不良率趋势图 -->
      <div ref="trendChart" style="height: 400px"></div>
      <!-- 供应商不良数量对比图 -->
      <div ref="supplierChart" style="height: 400px; margin-top: 30px"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import * as echarts from 'echarts';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';

// 筛选条件
const filterParams = reactive({
  inspectionDateRange: null, // 时间范围 [start, end]
  supplier: '', // 供应商
  materialName: '' // 产品名称
});

// ECharts 实例
const trendChartRef = ref(null); // 趋势图
const supplierChartRef = ref(null); // 供应商对比图
let trendChart = null;
let supplierChart = null;

// 初始化图表
const initCharts = () => {
  trendChart = echarts.init(trendChartRef.value);
  supplierChart = echarts.init(supplierChartRef.value);
  updateCharts(); // 首次加载数据
};

// 刷新图表数据
const refreshChart = () => {
  updateCharts();
};

// 重置筛选条件
const resetFilter = () => {
  filterParams.inspectionDateRange = null;
  filterParams.supplier = '';
  filterParams.materialName = '';
  updateCharts();
};

// // 获取图表数据并更新
// const updateCharts = async () => {
//   try {
//     const res = await request.get('iqcIncomingInspection/statistic', {
//       params: {
//         startDate: filterParams.inspectionDateRange?.[0],
//         endDate: filterParams.inspectionDateRange?.[1],
//         supplier: filterParams.supplier,
//         materialName: filterParams.materialName
//       }
//     });
//
//     // 处理趋势图数据（示例：时间-不良率）
//     const trendData = res.trendData.map(item => ({
//       name: item.inspectionDate,
//       value: item.defectiveRate
//     }));
//     trendChart.setOption({
//       xAxis: { type: 'category', data: trendData.map(item => item.name) },
//       yAxis: { type: 'value', name: '不良率(%)' },
//       series: [{ type: 'line', data: trendData.map(item => item.value) }]
//     });
//
//     // 处理供应商对比图数据（示例：供应商-不良数量）
//     const supplierData = res.supplierData.map(item => ({
//       name: item.supplier,
//       value: item.defectiveQuantity
//     }));
//     supplierChart.setOption({
//       xAxis: { type: 'value', name: '不良数量' },
//       yAxis: { type: 'category', data: supplierData.map(item => item.name) },
//       series: [{ type: 'bar', data: supplierData.map(item => item.value) }]
//     });
//   } catch (error) {
//     ElMessage.error('数据加载失败');
//   }
// };
//
// // 页面加载时初始化
// onMounted(() => {
//   initCharts();
// });
</script>

<style scoped>

</style>