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

// 图表通用配置
const commonOptions = {
  color: ['#36cbcb', '#91cc75', '#fac858', '#ee6666', '#73a6ff', '#c560ff', '#f5994e', '#ea7ccc'],
  backgroundColor: 'transparent',
  animation: true,
  animationDuration: 1000,
  animationEasing: 'elasticOut',
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    textStyle: {
      color: '#333'
    },
    borderColor: '#eee',
    borderWidth: 1,
    padding: 10,
    axisPointer: {
      type: 'shadow',
      shadowStyle: {
        color: 'rgba(0, 0, 0, 0.05)'
      }
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '5%',
    containLabel: true
  },
  toolbox: {
    show: true,
    feature: {
      dataZoom: {
        yAxisIndex: 'none'
      },
      restore: {},
      saveAsImage: {}
    }
  }
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
      const suppliers = data.map(item => item.supplier);
      const defectiveCounts = data.map(item => item.defectiveCount);
      supplierBarOption.value = {
        ...commonOptions,
        xAxis: {
          type: 'category',
          name: '供应商',
          data: suppliers,
          axisLabel: {
            rotate: 45,
            interval: 0,
            color: '#666'
          },
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          }
        },
        yAxis: {
          type: 'value',
          name: '物料不良数量',
          axisLabel: {
            color: '#666'
          },
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          },
          splitLine: {
            lineStyle: {
              color: '#eee'
            }
          }
        },
        series: [{
          data: defectiveCounts,
          type: 'bar',
          itemStyle: {
            borderRadius: [4, 4, 0, 0]
          },
          label: {
            show: true,
            position: 'top',
            color: '#333'
          },
          emphasis: {
            itemStyle: {
              color: '#36cbcb',
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowOffsetY: 0,
              shadowColor: 'rgba(0, 0, 0, 0.3)'
            }
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
      const materials = data.map(item => item.materialName);
      const defectiveCounts = data.map(item => item.defectiveCount);

      materialBarOption.value = {
        ...commonOptions,
        xAxis: {
          type: 'value',
          name: '不良数量',
          axisLabel: {
            color: '#666'
          },
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          },
          splitLine: {
            lineStyle: {
              color: '#eee'
            }
          }
        },
        yAxis: {
          type: 'category',
          name: '物料',
          data: materials,
          axisLabel: {
            color: '#666'
          },
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          }
        },
        series: [{
          data: defectiveCounts,
          type: 'bar',
          itemStyle: {
            borderRadius: [0, 4, 4, 0]
          },
          label: {
            show: true,
            position: 'right',
            color: '#333'
          },
          emphasis: {
            itemStyle: {
              color: '#91cc75',
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowOffsetY: 0,
              shadowColor: 'rgba(0, 0, 0, 0.3)'
            }
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
      const dates = data.map(item => item.inspectionDate);
      const incomingQuantities = data.map(item => item.inspectionQuantity);
      const defectiveRates = data.map(item => item.defectiveRate);

      iqcLineOption.value = {
        ...commonOptions,
        tooltip: {
          ...commonOptions.tooltip,
          trigger: 'axis',
          formatter: function(params) {
            let result = `<div style="font-weight:bold;">${params[0].name}</div>`;
            params.forEach(param => {
              result += `<div>${param.seriesName}: ${param.value}</div>`;
            });
            return result;
          }
        },
        xAxis: {
          type: 'category',
          data: dates,
        },
        yAxis: [
          {
            type: 'value',
            name: '来料数量',
            min: 0,
          },
          {
            type: 'value',
            name: '不良率(%)',
            min: 0,
            max: 100,
          }
        ],
        series: [
          {
            name: '来料数量',
            type: 'line',
            data: incomingQuantities,
            symbol: 'circle',
            symbolSize: 8,
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
            data: defectiveRates,
            yAxisIndex: 1,
            symbol: 'circle',
            symbolSize: 8,
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
            emphasis: {
              symbolSize: 12,
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowColor: 'rgba(0, 0, 0, 0.3)'
              }
            }
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

      iqcPieOption.value = {
        ...commonOptions,
        tooltip: {
          ...commonOptions.tooltip,
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: 10,
          top: 20,
          bottom: 20,
          data: data.map(item => item.inspectionResult),
          textStyle: {
            color: '#666'
          }
        },
        series: [
          {
            name: '检验结果',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['35%', '50%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              formatter: '{b}: {d}%',
              position: 'outside',
              color: '#333'
            },
            labelLine: {
              show: true,
              length: 10,
              length2: 20
            },
            data: data.map(item => ({ value: item.resultCount, name: item.inspectionResult })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
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

    // 添加响应式监听
    window.addEventListener('resize', () => {
      chartInstances[key].resize();
    });
  });

  loadChartsData();
});

// 销毁时释放资源
onBeforeUnmount(() => {
  Object.values(chartInstances).forEach(chart => {
    window.removeEventListener('resize', () => chart.resize());
    chart?.dispose();
  });
});
</script>


<style scoped>

</style>