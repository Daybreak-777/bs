<template>
  <el-form ref="formRef" :model="data.form" :rules="data.rules" style="width: 70%; margin: 10px auto;">
    <el-card class="form-card">
      <!-- 产品检测信息区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="section-title">产品检验单信息</div>
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

      <!-- 产品信息区域 -->
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="section-title">产品信息</div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="product" label="产品名称" label-position="top" required>
            <!-- 使用带远程搜索的下拉选择器 -->
            <el-select
                v-model="data.form.product"
                placeholder="请选择产品"
                filterable
                remote
                :remote-method="searchProduct"
                remote-show-suffix
                :loading="productLoading"
                clearable
                @change="handleProductChange"
            >
              <el-option
                  v-for="option in productOptions"
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
          <el-form-item prop="workshop" label="生产车间" label-position="top" required>
            <!-- 使用带远程搜索的下拉选择器 -->
            <el-select
                v-model="data.form.workshop"
                placeholder="请选择生产车间"
                filterable
                remote
                :remote-method="searchWorkshop"
                remote-show-suffix
                :loading="workshopLoading"
                clearable
                @change="handleWorkshopChange"
            >
              <el-option
                  v-for="option in workshopOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="3">
          <el-form-item prop="team" label="车间班组" label-position="top">
            <el-input v-model="data.form.team" disabled placeholder="车间班组" />
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
    product: '',
    specification: '',
    workshop: '',
    team: '',
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
    product: [
      { required: true, message: '请输入产品名称', trigger: 'blur' }
    ],
    workshop: [
      { required: true, message: '请输入生产车间', trigger: 'blur' }
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
  const formData = JSON.parse(sessionStorage.getItem('fqc-formData'));
  if (formData) {
    data.form = formData;
    // 读取后清除数据
    sessionStorage.removeItem('fqc-formData');
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
  request.post('fqcProductInspection/add', data.form).then(res => {
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
  request.put('fqcProductInspection/update', data.form).then(res => {
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

// 产品搜索相关
const productOptions = ref([]);
const productLoading = ref(false);

// 远程搜索产品
const searchProduct = (query) => {
  productLoading.value = true;
  request.get('product/selectAll', {
    params: {
      productName: query
    }
  }).then(res => {
    if (res.code === '200') {
      productOptions.value = res.data.map(item => ({
        label: item.productName, // 显示的文本
        value: item.productName,  // 实际的值
        specification: item.specification
      }));
      productLoading.value = false;
    }
    else {
      ElMessage.error(res.msg)
      productLoading.value = false;
      productOptions.value = [];
    }
  }).catch();
};

// 根据选择的产品名称获取规格型号
const handleProductChange = (value) => {
  console.log(value)
  // 找到选中的产品对象
  const selectedProduct = productOptions.value.find(
      (item) => item.value === value
  );

  // 更新规格型号到表单
  if (selectedProduct) {
    data.form.specification = selectedProduct.specification || '';
  } else {
    data.form.specification = '';
  }
};


// 生产车间搜索相关
const workshopOptions = ref([]);
const workshopLoading = ref(false);

// 远程搜索生产车间
const searchWorkshop = (query) => {
  workshopLoading.value = true;
  request.get('workshop/selectAll', {
    params: {
      workshopName: query
    }
  }).then(res => {
    if (res.code === '200') {
      workshopOptions.value = res.data.map(item => ({
        label: item.workshopName, // 显示的文本
        value: item.workshopName,  // 实际的值
        team: item.workshopTeam
      }));
      workshopLoading.value = false;
    }
    else {
      ElMessage.error(res.msg)
      workshopLoading.value = false;
      workshopOptions.value = [];
    }
  }).catch();
};

// 根据选择的生产车间名称选择车间班组
const handleWorkshopChange = (value) => {
  console.log(value)
  // 找到选中的生产车间对象
  const selectedWorkshop = workshopOptions.value.find(
      (item) => item.value === value
  );

  // 更新规格型号到表单
  if (selectedWorkshop) {
    data.form.team = selectedWorkshop.team || '';
  } else {
    data.form.team = '';
  }
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