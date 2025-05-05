<template>
  <div>

    <div style="margin-bottom: 20px">
      <RouterLink to="test">通过RouterLink跳转到Test页面</RouterLink> |
      <a href="test">通过a标签跳转Test页面</a>
    </div>

    <div style="margin-bottom: 20px">
      <el-button type="primary" @click="router.push('test')">push按钮跳转到Test页面</el-button>
      <el-button type="primary" @click="router.replace('test')">replace按钮替换到到Test页面</el-button>
    </div>

    <div style="margin-bottom: 20px">
      <el-button type="primary" @click="router.push('test?id=1&username=Daybreak7')">路由传参id=1&username=Daybreak7</el-button>
      <el-button type="primary" @click="router.push({ path: 'test', query: { id: 2, username: 'Daybreak7' } })">路由传参id=1&username=Daybreak7(更加完整)</el-button>
    </div>

    <div style="margin-bottom: 20px">
      <el-input clearable v-model="data.input" style="width: 240px" placeholder="请输入内容" :prefix-icon="Search"/>
      <el-input v-model="data.date" style="width: 200px" :suffix-icon="Calendar" disabled/>
      <el-input type="textarea" v-model="data.descr" style="width: 300px" placeholder="请输入一段描述"/>
    </div>

    <div style="margin: 20px 0">
      <el-select
          clearable
          v-model="data.value"
          value-key="id"
          placeholder="请选择单个水果"
          size="large"
          style="width: 240px"
      >
        <el-option
            v-for="item in data.options"
            :key="item.id"
            :label="item.label"
            :value="item"
        />
      </el-select>
      {{ data.value ? data.value.id : 'no select' }}
      <el-select
          multiple
          clearable
          collapse-tags
          collapse-tags-tooltip
          v-model="data.value"
          value-key="id"
          placeholder="请选择水果"
          size="large"
          style="width: 240px; margin-left: 20px"
      >
        <el-option
            v-for="item in data.options"
            :key="item.id"
            :label="item.label"
            :value="item"
        />
      </el-select>
    </div>

    <div style="margin: 20px 0">
      <el-radio-group v-model="data.sex">
        <el-radio value="男" label="男"></el-radio>
        <el-radio value="女">女</el-radio>
      </el-radio-group>

      <el-radio-group v-model="data.tag" style="margin-left: 20px">
        <el-radio-button value="1" label="我发布的内容"></el-radio-button>
        <el-radio-button value="2" label="我点赞的内容"></el-radio-button>
        <el-radio-button value="3" label="我收藏的内容"></el-radio-button>
      </el-radio-group>
    </div>

    <div style="margin: 20px 0">
      <el-checkbox-group v-model="data.checklist">
        <el-checkbox
            v-for="item in data.options"
            :key="item.id"
            :label="item.label"
            :value="item.label"
        />
      </el-checkbox-group>
      <span style="margin-left: 20px">{{ data.checklist }}</span>
    </div>

    <div style="margin: 20px 0">
      <img src="@/assets/logo.svg" alt="" width="100px">
      <el-image :src="img" alt="" style="width: 100px; margin-left: 100px" :preview-src-list="[img,'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg']"></el-image>
    </div>

    <div style="margin: 20px 0">
      <el-carousel trigger="click" height="200px" style="width: 400px">
        <el-carousel-item v-for="item in data.imgs" :key="item">
<!--          <img :src="item" width="100%" height="200px">-->
          <el-image :src="item" ></el-image>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin: 20px 0">
      <el-date-picker
          v-model="data.date"
          type="date"
          placeholder="请选择日期"
          format="YYYY/MM/DD"
          value-format="YYYY-MM-DD"
      />
      {{ data.date }}

      <el-date-picker
          style="margin-left: 20px"
          v-model="data.datetime"
          type="datetime"
          placeholder="请选择日期时间"
          format="YYYY/MM/DD hh:mm:ss"
          value-format="YYYY-MM-DD h:m:s a"
      />
      {{ data.datetime }}

      <el-date-picker
          style="margin-left: 20px"
          v-model="data.daterange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY/MM/DD"
          value-format="YYYY-MM-DD"
      />
      {{ data.daterange[0]?.length? data.daterange[0] + " : " : '' }}{{ data.daterange[1]?.length? data.daterange[1] : '' }}
    </div>

    <div style="margin: 20px 0">
      <el-table :data="data.tableData" border stripe height="200" style="width: 600px">
        <el-table-column prop="date" label="日期" width="180" />
        <el-table-column prop="name" label="姓名" width="180" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="delete" label="操作">
          <template #default="scope">
            <el-button
                type="primary"
                size="small"
                @click.prevent="edit(scope.row)"
            >
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button
                type="danger"
                size="small"
                @click.prevent="del(scope.$index)"
            >
              <el-icon><Delete /></el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="padding: 10px">
        <el-pagination
            v-model:current-page="data.currentPage"
            v-model:page-size="data.pageSize"
            :page-sizes="[1, 2, 3, 4]"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.tableData.length"
        />
      </div>
    </div>

    <el-dialog
        v-model="data.dialogVisible"
        title="编辑行对象"
        width="500"
    >
      <div style="padding: 20px">
        <div style="margin-bottom: 10px">日期：{{ data.row.date }}</div>
        <div style="margin-bottom: 10px">名称：{{ data.row.name }}</div>
        <div>地址：{{ data.row.address }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">

import {reactive, ref} from "vue";
import {Calendar, Search} from "@element-plus/icons-vue";
import img from "@/assets/logo.svg";
import cl1 from "@/assets/1.jpg";
import cl2 from "@/assets/2.jpg";
import cl3 from "@/assets/3.jpg";
import router from "@/router/index.js";
import request from "@/utils/request";

type Option = {
  id: number
  label: string
}
const data = reactive({
  input: null,
  date: "2025/3/3",
  datetime: "",
  daterange: "",
  descr: "用于输入多行文本信息可缩放的输入框 111111111111111111111111 2222222222222222222 3333333333333333333  44444444444444444444444",
  value: ref<Option>(),
  options: [{id: 1, label: '苹果'}, {id: 2, label: '香蕉'}, {id: 3, label: '桃子'}, {id: 4, label: '苹果'}],
  sex: '',
  tag:'1',
  checklist: [],
  imgs: [cl1, cl2, cl3],
  currentPage: 1,
  pageSize: 5,
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
  dialogVisible: false,
  row: null,
  employeeList: [],
});

const edit = (row) =>{
  data.row = row
  data.dialogVisible = true
}
const del = (index) =>{
  alert("删除第" + (index + 1) + "行")
}

request.get('/employee/selectAll').then( res => {
  // console.log(res)
  data.employeeList = res.data
})

</script>