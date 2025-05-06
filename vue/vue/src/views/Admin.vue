<template>

  <div>
    <div style="margin-bottom: 5px">
      <el-card>
        <el-input v-model="data.username" placeholder="请输入账号查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-input v-model="data.name" placeholder="请输入名称查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-button type="primary" @click="load">查 询</el-button>
        <el-button type="warning" @click="reset">重 置</el-button>
      </el-card>
    </div>

    <div v-if="data.user.role === 'ADMIN'">
      <div style="margin-bottom: 5px">
        <el-card>
          <el-button type="primary" @click="handleAdd">新 增</el-button>
          <el-button type="warning" @click="delBatch">批量删除</el-button>
  <!--        <el-button type="info">导入</el-button>-->
  <!--        <el-button type="success">导出</el-button>-->
        </el-card>
      </div>
    </div>

    <div style="margin-bottom: 5px">
      <el-card>
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection"  width="55" />
          <el-table-column label="账号" prop="username" />
          <el-table-column label="头像" prop="avatar">
            <template #default="scope">
              <img v-if="scope.row.avatar" :src="scope.row.avatar" alt="" style="width: 40px; height: 40px; border-radius: 50%"/>
            </template>
          </el-table-column>
          <el-table-column label="名称" prop="name" />
          <el-table-column v-if="data.user.role === 'ADMIN'" label="操作" width="">
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
              :page-sizes="[4, 3, 2, 1]"
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="data.total"
          />
        </div>
      </el-card>
    </div>

    <el-dialog v-model="data.formVisible" title="管理员信息" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-right: 40px; padding-top: 20px" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/files/upload"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
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
  user: JSON.parse(localStorage.getItem('xm-user')),
  username: null,
  name: null,
  pageNum: 1,
  pageSize: 4,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  ids: [],
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur'}
    ],
    name: [
      { required: true, message: '请输入名称', trigger: 'blur'}
    ]
  }
})

const formRef = ref()

const load = () => {
  request.get('admin/selectPage', { // ?pageNum=1&pageSize=10
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username: data.username,
      name: data.name,
    }
  }).then(res => {
    // console.log(res.data)
    data.tableData = res.data.list
    data.total = res.data.total
    console.log(data.tableData)
  })
}

const reset = () => {
  data.username = null
  data.name = null
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const save = () => { // 在一个保存方法里做2个操作
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const add = () => { // 新增对象没有id
  request.post('admin/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load() // 新增后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => { // 编辑对象有id
  request.put('admin/update', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load() // 更新后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row)) // 深拷贝一个新的对象用于编辑，这样就不会影响到对象了
  console.log(data.form)
  data.formVisible = true
}

const del = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗', "删除确认", { type : 'warning' }).then( () => {
    request.delete('admin/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load() // 删除后一定要重新加载最新的数据
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()

}

const handleSelectionChange = (rows) => { //返回所有选中的行
  console.log(rows)
  // 从选中的行数组里面去除所有行的id
  data.ids = rows.map(row => row.id)
  console.log(data.ids)
}

const delBatch = () => {
  if (data.ids.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗', "删除确认", { type : 'warning' }).then( () => {
    request.delete('/admin/deleteBatch', { data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const handleAvatarSuccess = (res) => {
  console.log(res.data)
  data.form.avatar = res.data
}

load();
</script>

<style scoped>
/* 你可以在这里添加样式 */
</style>