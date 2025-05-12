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
import { onBeforeUnmount, onMounted, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

// 定义图表实例引用
const chartInstances = {
  supplierBar: null,
  materialBar: null,
  iqcLine: null,
  iqcPie: null
};

// 绘制图表配置（响应式）
const supplierBarOption = ref({});
const materialBarOption = ref({});
const iqcLineOption = ref({});
const iqcPieOption = ref({});

// 查询条件
const queryDateRange = ref([
  new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000),
  new Date()
]);

const loadChartsData = () => {
  const startDate = queryDateRange.value[0] ? queryDateRange.value[0].toISOString().split('T')[0] : null;
  const endDate = queryDateRange.value[1] ? queryDateRange.value[1].toISOString().split('T')[0] : null;

  // 获取各供应商的不良数量对比数据（柱状图：竖向）
  request.get('/iqcIncomingInspection/supplierDefectiveCount', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      const suppliers = data.map(item => item.supplier);
      const defectiveCounts = data.map(item => item.defectiveCount);
      supplierBarOption.value = {
        color: ['#5470C6'],
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '5%',
          right: '5%',
          bottom: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          name: '供应商',
          data: suppliers,
          axisTick: { alignWithLabel: true },
          axisLabel: { interval: 0 } // 如数据较多可适当调整
        },
        yAxis: {
          type: 'value',
          name: '物料不良数量',
          minInterval: 1
        },
        series: [{
          data: defectiveCounts,
          type: 'bar',
          barWidth: '50%',
          itemStyle: {
            barBorderRadius: [5, 5, 0, 0],
            shadowColor: 'rgba(0, 0, 0, 0.3)',
            shadowBlur: 5,
            shadowOffsetX: 2,
            shadowOffsetY: 2
          }
        }]
      };
      chartInstances.supplierBar.setOption(supplierBarOption.value);
    } else {
      ElMessage.error(res.msg);
    }
  });

  // 获取不同物料的不良数量对比数据（柱状图：横向）
  request.get('/iqcIncomingInspection/materialDefectiveCount', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      const materials = data.map(item => item.materialName);
      const defectiveCounts = data.map(item => item.defectiveCount);
      materialBarOption.value = {
        color: ['#91CC75'],
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '10%',
          right: '5%',
          bottom: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          name: '不良数量',
          minInterval: 1
        },
        yAxis: {
          type: 'category',
          name: '物料',
          data: materials,
          axisTick: { alignWithLabel: true },
          axisLabel: {
            formatter: params => params.length > 8 ? params.substring(0, 8) + "..." : params
          }
        },
        series: [{
          data: defectiveCounts,
          type: 'bar',
          barWidth: '50%',
          itemStyle: {
            borderRadius: [0, 5, 5, 0],
            shadowColor: 'rgba(0, 0, 0, 0.3)',
            shadowBlur: 5
          }
        }]
      };
      chartInstances.materialBar.setOption(materialBarOption.value);
    } else {
      ElMessage.error(res.msg);
    }
  });

  // 获取来料数量、不良率趋势数据（折线图）
  request.get('/iqcIncomingInspection/trendData', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      const dates = data.map(item => item.inspectionDate);
      const incomingQuantities = data.map(item => item.inspectionQuantity);
      const defectiveRates = data.map(item => item.defectiveRate);
      iqcLineOption.value = {
        color: ['#EE6666', '#73C0DE'],
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '10%',
          right: '10%',
          bottom: '15%',
          top: '15%',
          containLabel: true
        },
        legend: {
          data: ['来料数量', '不良率'],
          top: '5%'
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '来料数量',
            min: 0,
            axisLine: {
              lineStyle: {
                color: '#EE6666'
              }
            }
          },
          {
            type: 'value',
            name: '不良率(%)',
            min: 0,
            max: 100,
            axisLine: {
              lineStyle: {
                color: '#73C0DE'
              }
            }
          }
        ],
        series: [
          {
            name: '来料数量',
            type: 'line',
            data: incomingQuantities,
            smooth: true,
            lineStyle: { width: 3 },
            symbol: 'circle',
            symbolSize: 8
          },
          {
            name: '不良率',
            type: 'line',
            yAxisIndex: 1,
            data: defectiveRates,
            smooth: true,
            lineStyle: { width: 3 },
            symbol: 'circle',
            symbolSize: 8
          }
        ]
      };
      chartInstances.iqcLine.setOption(iqcLineOption.value);
    } else {
      ElMessage.error(res.msg);
    }
  });

  // 获取物料合格占比数据（饼图）
  request.get('/iqcIncomingInspection/inspectionResultRatio', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      iqcPieOption.value = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: data.map(item => item.inspectionResult)
        },
        series: [
          {
            name: '检查结果',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: { show: false },
            data: data.map(item => ({
              value: item.resultCount,
              name: item.inspectionResult
            })),
            itemStyle: {
              borderColor: '#fff',
              borderWidth: 2
            }
          }
        ]
      };
      chartInstances.iqcPie.setOption(iqcPieOption.value);
    } else {
      ElMessage.error(res.msg);
    }
  });
};

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
});

// 销毁时释放资源
onBeforeUnmount(() => {
  Object.values(chartInstances).forEach(chart => chart?.dispose());
});
</script>


<style scoped>

</style>