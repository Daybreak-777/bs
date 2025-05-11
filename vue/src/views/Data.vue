<template>

  <div>
    <div style="margin-bottom: 5px">
      <el-card>
        <el-input v-model="data.name" placeholder="请输入名称查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-button type="primary">查 询</el-button>
        <el-button type="warning">重 置</el-button>
      </el-card>
    </div>

    <div style="margin-bottom: 5px">
      <el-card>
        <el-button type="primary">新 增</el-button>
        <el-button type="warning">批量删除</el-button>
        <el-button type="info">导入</el-button>
        <el-button type="success">导出</el-button>
      </el-card>
    </div>

    <div style="margin-bottom: 5px">
      <el-card>
        <el-table :data="data.tableData" stripe>
          <el-table-column label="日期" prop="date"></el-table-column>
          <el-table-column label="名称" prop="name"></el-table-column>
          <el-table-column label="地址" prop="address"></el-table-column>
        </el-table>

        <div style="padding: 10px">
          <el-pagination
              v-model:current-page="data.pageNum"
              v-model:page-size="data.pageSize"
              :page-sizes="[1, 2, 3, 4]"
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="data.tableData.length"
          />
        </div>
      </el-card>
    </div>

  </div>

</template>

<script setup>

import {reactive} from "vue";

const data = reactive({
  name: null,
  pageNum: 1,
  pageSize: 1,
  tableData: [
    {
      date: '2016-05-03',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-02',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-04',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-01',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-03',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-02',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-04',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-01',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-03',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-02',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-04',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-01',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-03',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-02',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-04',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
    {
      date: '2016-05-01',
      name: 'Tom',
      address: 'No. 189, Grove St, Los Angeles',
    },
  ],
})

</script>

<style scoped>
/* 你可以在这里添加样式 */
</style>