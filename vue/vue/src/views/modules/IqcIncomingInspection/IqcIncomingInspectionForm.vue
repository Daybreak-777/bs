<template>
  <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px" style="width: 70%; padding: 20px 20px">
    <el-card class="form-card">
      <!-- 来料检测信息区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <h3 class="section-title">来料检测信息</h3>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item prop="inspector" label="检验员" required>
            <el-select
                v-model="data.form.inspector"
                placeholder="请选择检验员">
              <el-option label="Daybreak" value="Daybreak"></el-option>
              <!-- 可以添加更多检验员 -->
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="inspectionDate" label="检验日期" required>
            <el-date-picker
                v-model="data.form.inspectionDate"
                type="date"
                placeholder="请选择日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider></el-divider>

      <!-- 物料信息区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <h3 class="section-title">物料信息</h3>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="materialName" label="物料名称" required>
            <el-input
                v-model="data.form.materialName"
                placeholder="请输入物料名称">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="supplier" label="供应商" required>
            <el-input
                v-model="data.form.supplier"
                placeholder="请输入供应商">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider></el-divider>

      <!-- 检测内容区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <h3 class="section-title">检测内容</h3>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8" style="margin-bottom: 20px">
          <el-form-item prop="inspectionQuantity" label="检测数量" required>
            <el-input
                v-model="data.form.inspectionQuantity"
                placeholder="请输入检测数量"
                @input="calculateDefectiveRate">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8" style="margin-right: 10px">
          <el-form-item prop="defectiveQuantity" label="不良数量" required>
            <el-input
                v-model="data.form.defectiveQuantity"
                placeholder="请输入不良数量"
                @input="calculateDefectiveRate">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="不良率">
            <el-input v-model="data.form.defectiveRate" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider></el-divider>

      <!-- 检测结果区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <h3 class="section-title">检测结果</h3>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item prop="inspectionResult">
            <el-radio-group v-model="data.form.inspectionResult" size="large">
              <el-radio label="合格">合格</el-radio>
              <el-radio label="不合格">不合格</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 按钮区域 -->
      <el-row :gutter="20" class="btn-row">
        <el-col :span="24">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="submitForm">提交</el-button>
        </el-col>
      </el-row>
    </el-card>
  </el-form>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import request from "@/utils/request.js";

const router = useRouter();

const formRef = ref(null);

const data = reactive({
  form: {
    inspector: '',
    inspectionDate: '',
    materialName: '',
    supplier: '',
    inspectionQuantity: '',
    defectiveQuantity: '',
    qualifiedQuantity: '',
    defectiveRate: '',
    inspectionResult: ''
  },
  rules: {
    inspector: [
      { required: true, message: '请选择检验员', trigger: 'blur' }
    ],
    inspectionDate: [
      { required: true, message: '请选择检验日期', trigger: 'blur' }
    ],
    materialName: [
      { required: true, message: '请输入物料名称', trigger: 'blur' },
      { min: 1, max: 50, message: '物料名称长度应在 1 到 50 个字符之间', trigger: 'blur' }
    ],
    supplier: [
      { required: true, message: '请输入供应商', trigger: 'blur' },
      { min: 1, max: 50, message: '供应商名称长度应在 1 到 50 个字符之间', trigger: 'blur' }
    ],
    inspectionQuantity: [
      { required: true, message: '请输入检验数量', trigger: 'blur' },
      { pattern: /^\d+$/, message: '检验数量必须为正整数', trigger: 'blur' }
    ],
    defectiveQuantity: [
      { required: true, message: '请输入不良数量', trigger: 'blur' },
      { pattern: /^\d+$/, message: '不良数量必须为正整数', trigger: 'blur' },
      { validator: (rule, value, callback) => {
          const inspectionQuantity = parseInt(data.form.inspectionQuantity);
          if (parseInt(value) > inspectionQuantity) {
            callback(new Error('不良数量不能超过检测数量'));
          } else {
            callback();
          }
        }, trigger: 'blur' }
    ],
    inspectionResult: [
      { required: true, message: '请选择检验结果', trigger: 'change' }
    ]
  },
});

// 初始化表单数据
onMounted(() => {
  const formData = JSON.parse(sessionStorage.getItem('iqc-formData'));
  if (formData) {
    data.form = formData;
    // 读取后清除数据
    sessionStorage.removeItem('iqc-formData');
  }
});

// 计算不良率
const calculateDefectiveRate = () => {
  const inspectionQuantity = parseFloat(data.form.inspectionQuantity);
  const defectiveQuantity = parseFloat(data.form.defectiveQuantity);
  if (!isNaN(inspectionQuantity) && !isNaN(defectiveQuantity) && inspectionQuantity > 0) {
    data.form.defectiveRate = ((defectiveQuantity / inspectionQuantity) * 100).toFixed(2) + "%";
  } else {
    data.form.defectiveRate = '';
  }
};

// 取消处理
const handleCancel = () => {
  ElMessageBox.confirm('确定放弃当前修改吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    router.back();
  }).catch(() => {});
};

const submitForm = () => { // 在一个保存方法里做2个操作
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
      router.back()
    }
  })
}

const add = () => { // 新增对象没有id
  request.post('iqcIncomingInspection/add', data.form).then(res => {
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
  request.put('iqcIncomingInspection/update', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
      load() // 更新后一定要重新加载最新的数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>

<style scoped>
.form-card {
  padding: 20px;
}

/* 每个区域标题的样式 */
.section-title {
  font-size: 18px;
  color: #3c7fff;
  margin-bottom: 10px;
  padding-bottom: 5px;
}

/* 表单项之间的间距 */
.el-form-item {
  margin-bottom: 10px;
}

/* 按钮区域置右 */
.btn-row {
  text-align: right;
  margin-top: 20px;
}
</style>