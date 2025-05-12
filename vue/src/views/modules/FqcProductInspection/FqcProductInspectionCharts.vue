<template>
  <!-- 图表区域 -->
  <div>
    <!-- 查询条件区域 -->
    <el-card style="margin-bottom: 5px">
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
    <div style="display: flex; grid-gap: 10px; margin-bottom: 5px">
      <!-- 各生产车间的（产品）不良数量对比图 -->
      <el-card style="flex: 1; height: 300px" id="workshopBar"></el-card>
      <!-- 不同产品的不良数量对比图 -->
      <el-card style="flex: 1; height: 300px" id="productBar"></el-card>
    </div>
    <div style="display: flex; grid-gap: 10px">
      <!-- 检验数量、不良率趋势图 -->
      <el-card style="flex: 1; height: 300px" id="fqcLine"></el-card>
      <!-- 检验合格占比饼图 -->
      <el-card style="flex: 1; height: 300px" id="fqcPie"></el-card>
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
  workshopBar: null,
  productBar: null,
  fqcLine: null,
  fqcPie: null
};

// 绘制图表
// 生产车间不良数量对比柱状图
const workshopBarOption = {
  grid: {
    containLabel: true,
    left: '3%',
    right: '15%',
    bottom: '3%',
    top: '15%'
  },
  title: {
    text: '生产车间不良数量对比',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    textStyle: {
      color: '#333'
    },
    formatter: (params) => {
      const param = params[0];
      return `
              <div style="font-weight:600;">${param.name}</div>
              <div>产品不良数量: ${param.value}</div>
            `;
    }
  },
  xAxis: {
    type: 'value',
    name: '产品不良数量',
  },
  yAxis: {
    type: 'category',
    name: '生产车间',
    data: []
  },
  series: [
    {
      name: '产品不良数量',
      data: [],
      type: 'bar',
      itemStyle: {
        color: '#36cbcb',
        borderRadius: [4, 4, 0, 0]
      },
      label: {
        show: true,
        position: 'right',
      }
    }
  ]
};
// 产品不良数量对比柱状图
const productBarOption = {
  grid: {
    containLabel: true,
    left: '5%',
    bottom: '3%',
  },
  title: {
    text: '产品不良数量对比',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    textStyle: {
      color: '#333'
    },
    formatter: (params) => {
      const param = params[0];
      return `
              <div style="font-weight:600;">${param.name}</div>
              <div>不良数量: ${param.value}</div>
            `;
    }
  },
  xAxis: {
    type: 'category',
    name: '产品',
    data: []
  },
  yAxis: {
    type: 'value',
    name: '不良数量',
  },
  series: [{
    data: [],
    type: 'bar',
    itemStyle: {
      color: '#36cbcb',
      borderRadius: [4, 4, 0, 0]
    },
    label: {
      show: true,
      position: 'top',
    }
  }]
};
// fqc检验数量与不良率趋势图
const fqcLineOption = {
  grid: {
    containLabel: true,
    left: '3%',
    right: '3%',
    bottom: '3%',
  },
  title: {
    text: 'FQC检验数量与不良率趋势',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    top: '10%',
    orient: 'horizontal',
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: [
    {
      type: 'value',
      name: '检验数量',
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
      name: '检验数量',
      type: 'line',
      data: [],
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      itemStyle: {
        color: '#36cbcb'
      },
      lineStyle: {
        width: 2,
        color: '#36cbcb'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: 'rgba(54, 203, 203, 0.4)' // 0% 处的颜色
          }, {
            offset: 1, color: 'rgba(54, 203, 203, 0.05)' // 100% 处的颜色
          }],
          global: false // 缺省为 false
        }
      },
    },
    {
      name: '不良率',
      type: 'line',
      data: [],
      yAxisIndex: 1,
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      itemStyle: {
        color: '#ee6666'
      },
      lineStyle: {
        width: 2,
        color: '#ee6666'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: 'rgba(238, 102, 102, 0.4)' // 0% 处的颜色
          }, {
            offset: 1, color: 'rgba(238, 102, 102, 0.05)' // 100% 处的颜色
          }],
          global: false // 缺省为 false
        }
      },
    }
  ]
};
// fqc检验结果统计
const fqcPieOption = {
  title: {
    text: 'FQC工序检验结果占比',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    right: '10%',
    top: 'middle',
  },
  series: [
    {
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '50%'],
      emphasis: {
        label: {
          show: true,
          fontSize: '18',
          fontWeight: 'bold'
        }
      },
      data: [
        {
          value: null,
          name: '合格',
          itemStyle: { color: '#52c41a', borderRadius: 4} // 绿色表示合格
        },
        {
          value: null,
          name: '不合格',
          itemStyle: { color: '#ff4d4f', borderRadius: 4} // 红色表示不合格
        }
      ]
    }
  ]
};

// 查询条件
const queryDateRange = ref([new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000), new Date()]);

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

const loadChartsData = () => {
  const startDate = queryDateRange.value[0] ? queryDateRange.value[0].toISOString().split('T')[0] : null;
  const endDate = queryDateRange.value[1] ? queryDateRange.value[1].toISOString().split('T')[0] : null;

  // 获取各生产车间的不良数量对比数据
  request.get('fqcProductInspection/workshopDefectiveCount', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      console.log(data)
      const workshops = data.map(item => item.workshop);
      const defectiveCounts = data.map(item => item.defectiveCount);
      workshopBarOption.yAxis.data = workshops;
      workshopBarOption.series[0].data = defectiveCounts;
      chartInstances.workshopBar.setOption(workshopBarOption);
    } else {
      ElMessage.error(res.msg)
    }

  });

  // 获取不同产品的不良数量对比数据
  request.get('fqcProductInspection/productDefectiveCount', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      console.log(data)
      const products = data.map(item => item.product);
      const defectiveCounts = data.map(item => item.defectiveCount);
      productBarOption.xAxis.data = products;
      productBarOption.series[0].data = defectiveCounts;
      chartInstances.productBar.setOption(productBarOption);
    } else {
      ElMessage.error(res.msg)
    }
  });

  // 获取检验数量、不良率趋势数据
  request.get('fqcProductInspection/trendData', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      console.log(data)
      const dates = data.map(item => item.inspectionDate);
      const incomingQuantities = data.map(item => item.inspectionQuantity);
      const defectiveRates = data.map(item => item.defectiveRate);
      fqcLineOption.xAxis.data = dates;
      fqcLineOption.series[0].data = incomingQuantities;
      fqcLineOption.series[1].data = defectiveRates;
      chartInstances.fqcLine.setOption(fqcLineOption);
    } else {
      ElMessage.error(res.msg)
    }
  });
  // 获取检验结果数据
  request.get('fqcProductInspection/inspectionResultRatio', {
    params: { startDate, endDate }
  }).then(res => {
    if (res.code === '200') {
      const data = res.data;
      console.log(data)
      const qualifiedData = data.find(item => item.inspectionResult === '合格');
      const unqualifiedData = data.find(item => item.inspectionResult === '不合格');
      fqcPieOption.series[0].data[0].value = qualifiedData.resultCount;
      fqcPieOption.series[0].data[1].value = unqualifiedData.resultCount;
      chartInstances.fqcPie.setOption(fqcPieOption);
    } else {
      ElMessage.error(res.msg)
    }
  });
}

</script>

<style scoped>

</style>