<template>

  <div>
    <div style="margin-bottom: 5px">
      <el-card>
        <el-input v-model="data.materialName" placeholder="请输入物料名称查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-input v-model="data.specification" placeholder="请输入物料规格查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
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
          <el-table-column label="物料编号" prop="id" />
          <el-table-column label="物料名称" prop="materialName" />
          <el-table-column label="物料规格" prop="specification" />
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

    <el-dialog v-model="data.formVisible" title="物料信息" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" style="padding-right: 40px; padding-top: 20px" label-width="80px">
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="data.form.materialName" autocomplete="off" placeholder="请输入物料名称"/>
        </el-form-item>
        <el-form-item label="物料规格" prop="specification">
          <el-input v-model="data.form.specification" autocomplete="off" placeholder="请输入物料规格"/>
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
  materialName: null,
  specification: null,
  pageNum: 1,
  pageSize: 4,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  ids: [],
  rules: {
    materialName: [
      { required: true, message: '请输入物料名称', trigger: 'blur'}
    ],
    specification: [
      { required: true, message: '请输入物料规格', trigger: 'blur'}
    ]
  }
})

const formRef = ref()

// 获取数据列表
const load = () => {
  request.get('material/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      materialName: data.materialName,
      specification: data.specification
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}

// 重置查询
const reset = () => {
  data.materialName = null
  data.specification = null
  load()
}

// 新增行对象
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

// 编辑行对象
const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 根据表单是否有id判断更新还是新增数据
const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

// 新增数据
const add = () => {
  request.post('material/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新数据
const update = () => {
  request.put('material/update', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除行对象
const del = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗', "删除确认", { type : 'warning' }).then( () => {
    request.delete('material/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

// 筛选行
const handleSelectionChange = (rows) => {
  data.ids = rows.map(row => row.id)
}

// 批量删除行数据
const delBatch = () => {
  if (data.ids.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗', "删除确认", { type : 'warning' }).then( () => {
    request.delete('material/deleteBatch', { data: data.ids}).then(res => {
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