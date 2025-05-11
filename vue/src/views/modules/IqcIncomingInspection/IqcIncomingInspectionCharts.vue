<template>
  <!-- 图表区域 -->
  <div>
    <!-- 查询条件区域 -->
    <el-card style="margin-bottom: 10px">
      <el-date-picker
          v-model="queryDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="margin-right: 20px"
      >
      </el-date-picker>
      <el-button type="primary" @click="loadChartsData">查询</el-button>
      <el-button type="warning" @click="resetChartsData">重 置</el-button>
    </el-card>
    <div style="display: flex; grid-gap: 10px; margin-bottom: 10px">
      <!-- 各供应商的（物料）不良数量对比图 -->
      <el-card style="flex: 1; height: 300px" id="supplierBar"></el-card>
      <!-- 不同物料的不良数量对比图 -->
      <el-card style="flex: 1; height: 300px" id="materialBar"></el-card>
    </div>
    <div style="display: flex; grid-gap: 10px">
      <!-- 来料数量、不良率趋势图 -->
      <el-card style="flex: 1; height: 300px" id="iqcLine"></el-card>
      <!-- 物料合格占比饼图 -->
      <el-card style="flex: 1; height: 300px" id="iqcPie"></el-card>
    </div>
  </div>
</template>

<script setup>
import * as echarts from 'echarts';
import {onBeforeUnmount, onMounted, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

// 定义图表实例引用
const chartInstances = {
  supplierBar: null,
  materialBar: null,
  iqcLine: null,
  iqcPie: null
};

// 绘制图表
const supplierBarOption = ref({});
const materialBarOption = ref({});
const iqcLineOption = ref({});
const iqcPieOption = ref({});

// 查询条件
const queryDateRange = ref([new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000), new Date()]);

const loadChartsData = () => {
  const startDate = queryDateRange.value[0] ? queryDateRange.value[0].toISOString().split('T')[0] : null;
  const endDate = queryDateRange.value[1] ? queryDateRange.value[1].toISOString().split('T')[0] : null;

  // 获取各供应商的不良数量对比数据
  request.get('/iqcIncomingInspection/supplierDefectiveCount', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      console.log(data)
      const suppliers = data.map(item => item.supplier);
      const defectiveCounts = data.map(item => item.defectiveCount);
      supplierBarOption.value = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          name: '供应商',
          data: suppliers
        },
        yAxis: {
          type: 'value',
          name: '物料不良数量',
        },
        series: [{
          data: defectiveCounts,
          type: 'bar'
        }]
      };
      chartInstances.supplierBar.setOption(supplierBarOption.value);
    } else {
      ElMessage.error(res.msg)
    }

  });

  // 获取不同物料的不良数量对比数据
  request.get('/iqcIncomingInspection/materialDefectiveCount', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      console.log(data)
      const materials = data.map(item => item.materialName);
      const defectiveCounts = data.map(item => item.defectiveCount);
      materialBarOption.value = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'value',
          name: '不良数量',
        },
        yAxis: {
          type: 'category',
          name: '物料',
          data: materials
        },
        series: [{
          data: defectiveCounts,
          type: 'bar'
        }]
      };
      chartInstances.materialBar.setOption(materialBarOption.value);
    } else {
      ElMessage.error(res.msg)
    }
  });

  // 获取来料数量、不良率趋势数据
  request.get('/iqcIncomingInspection/trendData', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      console.log(data)
      const dates = data.map(item => item.inspectionDate);
      const incomingQuantities = data.map(item => item.inspectionQuantity);
      const defectiveRates = data.map(item => item.defectiveRate);
      iqcLineOption.value = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: [
          {
            type: 'value',
            name: '来料数量',
            min: 0
          },
          {
            type: 'value',
            name: '不良率(%)',
            min: 0,
            max: 100
          }
        ],
        series: [
          {
            name: '来料数量',
            type: 'line',
            data: incomingQuantities
          },
          {
            name: '不良率',
            type: 'line',
            yAxisIndex: 1,
            data: defectiveRates
          }
        ]
      };
      chartInstances.iqcLine.setOption(iqcLineOption.value);
    } else {
      ElMessage.error(res.msg)
    }

  });

  // 获取物料合格占比数据
  request.get('/iqcIncomingInspection/inspectionResultRatio', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      console.log(data)
      iqcPieOption.value = {
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: data.map(item => ({ value: item.resultCount, name: item.inspectionResult }))
          }
        ]
      };
      chartInstances.iqcPie.setOption(iqcPieOption.value);
    } else {
      ElMessage.error(res.msg)
    }
  });
}

// 重置查询
const resetChartsData = () => {
  queryDateRange.value = [
    new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000),
    new Date()
  ];
  loadChartsData(); // 重新加载数据
};

// 初始化时创建实例
onMounted(() => {
  Object.keys(chartInstances).forEach(key => {
    chartInstances[key] = echarts.init(document.getElementById(key));
  });
  loadChartsData();
})

// 销毁时释放资源
onBeforeUnmount(() => {
  Object.values(chartInstances).forEach(chart => chart?.dispose());
});

</script>

<style scoped>

</style>