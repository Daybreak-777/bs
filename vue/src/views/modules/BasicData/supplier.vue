<template>

  <div>
    <div style="margin-bottom: 5px">
      <el-card>
        <el-input v-model="data.supplierName" placeholder="请输入供应商名称查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-input v-model="data.contactPerson" placeholder="请输入联系人查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-button type="primary" @click="load">查 询</el-button>
        <el-button type="warning" @click="reset">重 置</el-button>
      </el-card>
    </div>

    <div style="margin-bottom: 5px">
      <el-card>
        <el-button type="primary" @click="handleAdd">新 增</el-button>
        <el-button type="warning" @click="delBatch">批量删除</el-button>
        <!--        <el-button type="info">导入</el-button>-->
        <!--        <el-button type="success">导出</el-button>-->
      </el-card>
    </div>

    <div style="margin-bottom: 5px">
      <el-card>
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection"  width="55" />
          <el-table-column label="供应商编号" prop="id" />
          <el-table-column label="供应商名称" prop="supplierName" />
          <el-table-column label="联系人" prop="contactPerson" />
          <el-table-column label="联系电话" prop="phoneNumber" />
          <el-table-column label="地址" prop="address" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" :icon="Edit" circle @click="handleUpdate(scope.row)"/>
              <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)"/>
            </template>
          </el-table-column>
        </el-table>

        <div style="padding: 10px">
          <el-pagination
              @current-change="load"
              @size-change="load"
              v-model:current-page="data.pageNum"
              v-model:page-size="data.pageSize"
              :page-sizes="[4, 6, 8, 10]"
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="data.total"
          />
        </div>
      </el-card>
    </div>

    <el-dialog v-model="data.formVisible" title="供应商信息" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-right: 40px; padding-top: 20px" label-width="80px">
        <el-form-item label="名称" prop="supplierName">
          <el-input v-model="data.form.supplierName" autocomplete="off" placeholder="请输入供应商名称"/>
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="data.form.contactPerson" autocomplete="off" placeholder="请输入联系人"/>
        </el-form-item>
        <el-form-item label="联系电话" prop="phoneNumber">
          <el-input v-model="data.form.phoneNumber" autocomplete="off" placeholder="请输入联电话"/>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="data.form.address" autocomplete="off" placeholder="请输入地址"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible=false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>

  </div>

</template>

<script setup>

import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const data = reactive({
  supplierName: null,
  contactPerson: null,
  pageNum: 1,
  pageSize: 4,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  ids: [],
  rules: {
    supplierName: [
      { required: true, message: '请输入供应商名称', trigger: 'blur'}
    ],
    contactPerson: [
      { required: true, message: '请输入联系人', trigger: 'blur'}
    ],
    phoneNumber: [
      { required: true, message: '请输入联系电话', trigger: 'blur'}
    ],
  }
})

const formRef = ref()

// 获取数据列表
const load = () => {
  request.get('supplier/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      supplierName: data.supplierName,
      contactPerson: data.contactPerson,
    }
  }).then(res => {
    // console.log(res.data)
    data.tableData = res.data.list
    data.total = res.data.total
    console.log(data.tableData)
  })
}

// 重置查询
const reset = () => {
  data.supplierName = null
  data.contactPerson = null
  load()
}

// 新增行对象
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

// 编辑行对象
const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) // 深拷贝一个新的对象用于编辑，这样就不会影响到对象了
  console.log(data.form)
  data.formVisible = true
}

// 根据表单是否有id判断更新还是新增数据
const save = () => { // 在一个保存方法里做2个操作
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

// 新增数据
const add = () => { // 新增对象没有id
  request.post('supplier/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load() // 新增后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新数据
const update = () => { // 编辑对象有id
  request.put('supplier/update', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load() // 更新后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除行对象
const del = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗', "删除确认", { type : 'warning' }).then( () => {
    request.delete('supplier/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load() // 删除后一定要重新加载最新的数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()

}

// 筛选行
const handleSelectionChange = (rows) => { //返回所有选中的行
  console.log(rows)
  // 从选中的行数组里面去除所有行的id
  data.ids = rows.map(row => row.id)
  console.log(data.ids)
}

// 批量删除行数据
const delBatch = () => {
  if (data.ids.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗', "删除确认", { type : 'warning' }).then( () => {
    request.delete('/supplier/deleteBatch', { data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

load();
</script>

<style scoped>
/* 你可以在这里添加样式 */
</style>