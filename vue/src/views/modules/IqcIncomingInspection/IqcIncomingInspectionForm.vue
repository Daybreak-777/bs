<template>
  <el-form ref="formRef" :model="data.form" :rules="data.rules" style="width: 70%; margin: 10px auto;">
    <el-card class="form-card">
      <!-- 来料检测信息区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="section-title">来料检验单信息</div>
        </el-col>
      </el-row>

      <el-row gutter="20">
        <el-col :span="8">
          <el-form-item prop="inspector" label="检验员" label-position="top" required>
            <!-- 使用带远程搜索的下拉选择器 -->
            <el-select
                v-model="data.form.inspector"
                placeholder="请选择检验人员"
                filterable
                remote
                :remote-method="searchInspector"
                remote-show-suffix
                :loading="inspectorLoading"
                clearable
            >
              <el-option
                  v-for="option in inspectorOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="10" :offset="3">
          <el-form-item prop="inspectionDate" label="检验日期" label-position="top" required>
            <el-date-picker
                v-model="data.form.inspectionDate"
                type="date"
                placeholder="请选择日期">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 物料信息区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="section-title">物料信息</div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="material" label="物料名称" label-position="top" required>
            <!-- 使用带远程搜索的下拉选择器 -->
            <el-select
                v-model="data.form.material"
                placeholder="请选择物料"
                filterable
                remote
                :remote-method="searchMaterial"
                remote-show-suffix
                :loading="materialLoading"
                clearable
                @change="handleMaterialChange"
            >
              <el-option
                  v-for="option in materialOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="3">
          <el-form-item prop="specification" label="规格型号" label-position="top">
            <el-input v-model="data.form.specification" disabled placeholder="规格型号" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="supplier" label="供应商" label-position="top" required>
            <!-- 使用带远程搜索的下拉选择器 -->
            <el-select
                v-model="data.form.supplier"
                placeholder="请选择供应商"
                filterable
                remote
                :remote-method="searchSupplier"
                remote-show-suffix
                :loading="supplierLoading"
                clearable
            >
              <el-option
                  v-for="option in supplierOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>


      <!-- 检测内容区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="section-title">检测内容</div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="inspectionQuantity" label="检测数量" label-position="top" required>
            <el-input
                v-model="data.form.inspectionQuantity"
                placeholder="请输入检测数量"
                @input="calculateDefectiveRate">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="3">
          <el-form-item prop="defectiveQuantity" label="不良数量" label-position="top" required>
            <el-input
                v-model="data.form.defectiveQuantity"
                placeholder="请输入不良数量"
                @input="calculateDefectiveRate">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="不良率" label-position="top">
            <el-input v-model="data.form.defectiveRate" disabled></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 检测结果区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="section-title">检测结果</div>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="8">
          <el-form-item label="检验结果" prop="inspectionResult" label-position="top">
            <el-radio-group v-model="data.form.inspectionResult" size="large">
              <el-radio label="合格" style="color: green;">合格</el-radio>
              <el-radio label="不合格" style="color: red;">不合格</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>

        <el-col :span="12" :offset="3">
          <el-form-item label="备注" label-position="top">
            <el-input v-model="data.form.remarks" type="textarea" rows="5"></el-input>
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
import { useRouter } from 'vue-router';
import request from "@/utils/request.js";

const router = useRouter();

const formRef = ref(null);

const data = reactive({
  form: {
    inspector: '',
    inspectionDate: '',
    material: '',
    specification: '',
    supplier: '',
    inspectionQuantity: '',
    defectiveQuantity: '',
    qualifiedQuantity: '',
    defectiveRate: '',
    inspectionResult: '',
    remarks: ''
  },
  rules: {
    inspector: [
      { required: true, message: '请选择检验员', trigger: 'blur' }
    ],
    inspectionDate: [
      { required: true, message: '请选择检验日期', trigger: 'blur' }
    ],
    material: [
      { required: true, message: '请输入物料名称', trigger: 'blur' }
    ],
    supplier: [
      { required: true, message: '请输入供应商', trigger: 'blur' }
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

// 初始化表单数据
onMounted(() => {
  const formData = JSON.parse(sessionStorage.getItem('iqc-formData'));
  if (formData) {
    data.form = formData;
    // 读取后清除数据
    sessionStorage.removeItem('iqc-formData');
  }
});

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

// 根据表单是否有id判断更新还是新增数据
const submitForm = () => { // 在一个保存方法里做2个操作
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add()
      router.back()
    }
  })
}

// 新增数据
const add = () => { // 新增对象没有id
  request.post('iqcIncomingInspection/add', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新数据
const update = () => { // 编辑对象有id
  request.put('iqcIncomingInspection/update', data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false
      ElMessage.success('操作成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 检验员搜索相关
const inspectorOptions = ref([]);
const inspectorLoading = ref(false);

// 远程搜索检验员
const searchInspector = (query) => {
  inspectorLoading.value = true;
  request.get('employee/selectAll', {
    params: {
      name: query
    }
  }).then(res => {
    if (res.code === '200') {
      inspectorOptions.value = res.data.map(item => ({
        label: item.name, // 显示的文本
        value: item.name  // 实际的值
      }));
      inspectorLoading.value = false;
    }
    else {
      ElMessage.error(res.msg)
      inspectorLoading.value = false;
      inspectorOptions.value = [];
    }
  }).catch();
};

// 物料搜索相关
const materialOptions = ref([]);
const materialLoading = ref(false);

// 远程搜索物料
const searchMaterial = (query) => {
  materialLoading.value = true;
  request.get('material/selectAll', {
    params: {
      materialName: query
    }
  }).then(res => {
    if (res.code === '200') {
      materialOptions.value = res.data.map(item => ({
        label: item.materialName, // 显示的文本
        value: item.materialName,  // 实际的值
        specification: item.specification
      }));
      materialLoading.value = false;
    }
    else {
      ElMessage.error(res.msg)
      materialLoading.value = false;
      materialOptions.value = [];
    }
  }).catch();
};

// 根据选择的物料名称获取规格型号
const handleMaterialChange = (value) => {
  console.log(value)
  // 找到选中的物料对象
  const selectedMaterial = materialOptions.value.find(
      (item) => item.value === value
  );

  // 更新规格型号到表单
  if (selectedMaterial) {
    data.form.specification = selectedMaterial.specification || '';
  } else {
    data.form.specification = '';
  }
};


// 供应商搜索相关
const supplierOptions = ref([]);
const supplierLoading = ref(false);

// 远程搜索供应商
const searchSupplier = (query) => {
  supplierLoading.value = true;
  request.get('supplier/selectAll', {
    params: {
      supplierName: query
    }
  }).then(res => {
    if (res.code === '200') {
      supplierOptions.value = res.data.map(item => ({
        label: item.supplierName, // 显示的文本
        value: item.supplierName  // 实际的值
      }));
      supplierLoading.value = false;
    }
    else {
      ElMessage.error(res.msg)
      supplierLoading.value = false;
      supplierOptions.value = [];
    }
  }).catch();
};
</script>

<style scoped>

/* 每个区域标题的样式 */
.section-title {
  font-size: 18px;
  color: #3c7fff;
  font-weight: bold;
  border-bottom: 2px solid #409eff;
  padding: 5px;
  margin-bottom: 5px;
}

/* 表单项之间的间距 */
.el-form-item {
  font-weight: bolder;
}

/* 按钮区域置右 */
.btn-row {
  text-align: right;

}
</style>