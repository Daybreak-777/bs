<template>
  <div>
    <!-- 查询区域 -->
    <div style="margin-bottom: 5px">
      <el-card>
        <el-input v-model="data.id" placeholder="请输入检验编号查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-input v-model="data.inspector" placeholder="请输入检验员查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-input v-model="data.materialName" placeholder="请输入物料查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-input v-model="data.supplier" placeholder="请输入供应商查询" style="width: 240px; margin-right: 10px" prefix-icon="Search"></el-input>
        <el-button type="primary" @click="load">查 询</el-button>
        <el-button type="warning" @click="reset">重 置</el-button>
      </el-card>
    </div>

    <!-- 操作按钮区域 -->
    <div style="margin-bottom: 5px">
      <el-card>
        <el-button type="primary" @click="handleAdd">新 增</el-button>
        <el-button type="warning" @click="delBatch">批量删除</el-button>
      </el-card>
    </div>

    <!-- 表格展示区域 -->
    <div style="margin-bottom: 5px">
      <el-card>
        <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column label="检验编号" prop="id" />
          <el-table-column label="检验员" prop="inspector" />
          <el-table-column label="检验日期" prop="inspectionDate" />
          <el-table-column label="物料名称" prop="materialName" />
          <el-table-column label="供应商" prop="supplier" />
          <el-table-column label="来料数量" prop="inspectionQuantity" />
          <el-table-column label="不良数量" prop="defectiveQuantity" />
          <el-table-column label="不良率" prop="defectiveRate" />
          <el-table-column label="检验结果" prop="inspectionResult" />
          <el-table-column label="备注" prop="remarks" show-overflow-tooltip/>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" :icon="Edit" circle @click="handleUpdate(scope.row)"/>
              <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)"/>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <div style="padding: 10px">
          <el-pagination
              @current-change="load"
              @size-change="load"
              v-model:current-page="data.pageNum"
              v-model:page-size="data.pageSize"
              :page-sizes="[10, 15, 20, 25]"
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="data.total"
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from 'vue-router';
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit } from "@element-plus/icons-vue";

const router = useRouter();

const data = reactive({
  id: null,
  inspector: null,
  materialName: null,
  supplier: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  ids: [],
});

const formRef = ref();

// 加载数据
const load = () => {
  request.get('iqcIncomingInspection/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      id: data.id,
      inspector: data.inspector,
      materialName: data.materialName,
      supplier: data.supplier,
    }
  }).then(res => {
    data.tableData = res.data.list;
    data.total = res.data.total;
    console.log(data.tableData);
  });
};

// 重置查询条件
const reset = () => {
  data.id = null
  data.inspector = null
  data.materialName = null
  data.supplier = null
  load();
};

// 处理新增操作
const handleAdd = () => {
  router.push({ name: 'IqcIncomingInspectionForm' });
};

// 处理编辑操作
const handleUpdate = (row) => {
  // 使用本地存储临时缓存
  sessionStorage.setItem('iqc-formData', JSON.stringify(row))
  router.push({ name: 'IqcIncomingInspectionForm' });
};

// 删除单条数据
const del = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗', "删除确认", { type: 'warning' }).then(() => {
    request.delete('iqcIncomingInspection/deleteById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch();
};

// 处理表格选中行变化
const handleSelectionChange = (rows) => {
  data.ids = rows.map(row => row.id);
  console.log(data.ids);
};

// 批量删除数据
const delBatch = () => {
  if (data.ids.length === 0) {
    ElMessage.warning('请选择数据');
    return;
  }
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗', "删除确认", { type: 'warning' }).then(() => {
    request.delete('/iqcIncomingInspection/deleteBatch', { data: data.ids }).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功');
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch();
};

// 页面加载时初始化数据
load();
</script>

<style scoped>
/* 你可以在这里添加样式 */
</style>