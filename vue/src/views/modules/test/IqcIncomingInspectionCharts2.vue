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

// 定义全局样式配置
const globalStyle = {
  color: ['#36CBCB', '#36A2EB', '#FF6384', '#FFCE56', '#4BC0C0', '#FF9F40', '#9966FF'],
  textStyle: {
    fontFamily: 'Inter, system-ui, sans-serif',
    color: '#333'
  },
  title: {
    textStyle: {
      fontWeight: '600',
      fontSize: 16
    }
  },
  grid: {
    containLabel: true,
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '10%'
  }
};

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

      // 优化供应商柱状图配置
      supplierBarOption.value = {
        ...globalStyle,
        title: {
          text: '供应商不良数量对比',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          textStyle: {
            color: '#333'
          },
          formatter: (params) => {
            const param = params[0];
            return `
              <div style="font-weight:600;">${param.name}</div>
              <div>物料不良数量: ${param.value}</div>
            `;
          }
        },
        yAxis: {
          type: 'category',
          name: '供应商',
          data: suppliers,
        },
        xAxis: {
          type: 'value',
          name: '物料不良数量',
          axisLabel: {
            formatter: '{value}'
          },
          splitLine: {
            lineStyle: {
              color: '#eee'
            }
          }
        },
        series: [{
          name: '不良数量',
          data: defectiveCounts,
          type: 'bar',
          barWidth: '60%',
          itemStyle: {
            borderRadius: [4, 4, 0, 0],
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.3)'
            }
          },
          label: {
            show: true,
          }
        }]
      };
      chartInstances.supplierBar.setOption(supplierBarOption.value, true);
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

      // 优化物料不良柱状图配置
      materialBarOption.value = {
        ...globalStyle,
        title: {
          text: '物料不良数量对比',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          borderColor: '#eee',
          borderWidth: 1,
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
          type: 'value',
          name: '不良数量',
          axisLabel: {
            formatter: '{value}'
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
            interval: 'auto'
          }
        },
        series: [{
          name: '不良数量',
          data: defectiveCounts,
          type: 'bar',
          barWidth: '60%',
          itemStyle: {
            borderRadius: [0, 4, 4, 0],
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.3)'
            }
          },
          label: {
            show: true,
            position: 'right',
            formatter: '{c}'
          }
        }]
      };
      chartInstances.materialBar.setOption(materialBarOption.value, true);
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

      // 优化趋势线图配置
      iqcLineOption.value = {
        ...globalStyle,
        title: {
          text: '来料数量与不良率趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          borderColor: '#eee',
          borderWidth: 1,
          textStyle: {
            color: '#333'
          },
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          },
          formatter: (params) => {
            let result = `<div style="font-weight:600;">${params[0].name}</div>`;
            params.forEach(param => {
              result += `<div>${param.seriesName}: ${param.value}${param.seriesIndex === 1 ? '%' : ''}</div>`;
            });
            return result;
          }
        },
        legend: {
          data: ['来料数量', '不良率'],
          top: '5%',
          left: 'center'
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: {
            rotate: 45,
            interval: 'auto'
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '来料数量',
            min: 0,
            axisLabel: {
              formatter: '{value}'
            },
            splitLine: {
              lineStyle: {
                color: '#eee'
              }
            }
          },
          {
            type: 'value',
            name: '不良率(%)',
            min: 0,
            max: 100,
            axisLabel: {
              formatter: '{value}%'
            },
            splitLine: {
              lineStyle: {
                color: '#eee'
              }
            }
          }
        ],
        series: [
          {
            name: '来料数量',
            type: 'line',
            data: incomingQuantities,
            yAxisIndex: 0,
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            itemStyle: {
              color: '#36A2EB'
            },
            lineStyle: {
              width: 2,
              color: '#36A2EB'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(54, 162, 235, 0.4)' // 0% 处的颜色
                }, {
                  offset: 1, color: 'rgba(54, 162, 235, 0)' // 100% 处的颜色
                }],
                global: false // 缺省为 false
              }
            },
            emphasis: {
              focus: 'series'
            }
          },
          {
            name: '不良率',
            type: 'line',
            data: defectiveRates,
            yAxisIndex: 1,
            smooth: true,
            symbol: 'circle',
            symbolSize: 6,
            itemStyle: {
              color: '#FF6384'
            },
            lineStyle: {
              width: 2,
              color: '#FF6384'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: 'rgba(255, 99, 132, 0.4)' // 0% 处的颜色
                }, {
                  offset: 1, color: 'rgba(255, 99, 132, 0)' // 100% 处的颜色
                }],
                global: false // 缺省为 false
              }
            },
            emphasis: {
              focus: 'series'
            }
          }
        ]
      };
      chartInstances.iqcLine.setOption(iqcLineOption.value, true);
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

      // 优化饼图配置
      iqcPieOption.value = {
        ...globalStyle,
        title: {
          text: '物料检验结果占比',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          borderColor: '#eee',
          borderWidth: 1,
          textStyle: {
            color: '#333'
          },
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: '10%',
          top: 'middle',
          data: data.map(item => item.inspectionResult)
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['35%', '50%'],
            data: data.map(item => ({
              value: item.resultCount,
              name: item.inspectionResult,
              itemStyle: {
                borderRadius: 4
              }
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            label: {
              show: true,
              formatter: '{b}: {d}%'
            },
            labelLine: {
              show: true
            }
          }
        ]
      };
      chartInstances.iqcPie.setOption(iqcPieOption.value, true);
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

// 响应式处理
const handleResize = () => {
  Object.values(chartInstances).forEach(chart => chart?.resize());
};

// 初始化时创建实例
onMounted(() => {
  Object.keys(chartInstances).forEach(key => {
    chartInstances[key] = echarts.init(document.getElementById(key));
  });
  loadChartsData();

  // 添加窗口大小变化事件监听
  window.addEventListener('resize', handleResize);
})

// 销毁时释放资源
onBeforeUnmount(() => {
  Object.values(chartInstances).forEach(chart => chart?.dispose());
  window.removeEventListener('resize', handleResize);
});

</script>

<style scoped>

</style>