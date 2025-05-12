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
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          formatter: '{b}: {c}'
        },
        grid: {
          left: '3%',
          right: '8%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          name: '供应商',
          axisLabel: {
            rotate: 45,
            fontSize: 12
          },
          data: suppliers
        },
        yAxis: {
          type: 'value',
          name: '不良数量',
          axisLine: { show: true },
          splitLine: { show: true }
        },
        series: [{
          data: defectiveCounts,
          type: 'bar',
          barWidth: '60%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ]),
            borderRadius: [4, 4, 0, 0]
          },
          label: {
            show: true,
            position: 'top',
            formatter: '{c}'
          }
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
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '20%',
          right: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          name: '不良数量',
          splitLine: { show: false }
        },
        yAxis: {
          type: 'category',
          axisLabel: {
            fontSize: 12,
            interval: 0
          },
          data: materials
        },
        series: [{
          data: defectiveCounts,
          type: 'bar',
          barWidth: '60%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
              { offset: 0, color: '#f7ba2c' },
              { offset: 1, color: '#ffd870' }
            ]),
            borderRadius: [0, 4, 4, 0]
          },
          label: {
            show: true,
            position: 'right',
            color: '#666'
          }
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
          trigger: 'axis',
          valueFormatter: value => typeof value === 'number' ? value.toFixed(2) : value
        },
        legend: {
          data: ['来料数量', '不良率'],
          top: 'bottom'
        },
        grid: {
          left: '3%',
          right: '4%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          axisTick: { alignWithLabel: true },
          data: dates
        },
        yAxis: [
          {
            type: 'value',
            name: '来料数量',
            min: 0,
            axisLine: { show: true }
          },
          {
            type: 'value',
            name: '不良率(%)',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value}%'
            }
          }
        ],
        series: [
          {
            name: '来料数量',
            type: 'line',
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            itemStyle: { color: '#5470c6' },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#5470c633' },
                { offset: 1, color: '#5470c600' }
              ])
            },
            data: incomingQuantities
          },
          {
            name: '不良率',
            type: 'line',
            smooth: true,
            yAxisIndex: 1,
            symbol: 'none',
            lineStyle: {
              type: 'dashed',
              color: '#ee6666'
            },
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
        tooltip: {
          trigger: 'item',
          valueFormatter: (value) => `${value} (${((value / total) * 100).toFixed(1)}%)`
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'middle'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 4,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{d}%'
          },
          emphasis: {
            label: { show: true }
          },
          data: data.map(item => ({
            value: item.resultCount,
            name: item.inspectionResult,
            itemStyle: {
              color: getColorByResult(item.inspectionResult)
            }
          }))
        }]
      };

// 颜色映射函数
      function getColorByResult(result) {
        const colors = {
          '合格': '#91cc75',
          '不合格': '#ee6666',
        };
        return colors[result] || '#999';
      }
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