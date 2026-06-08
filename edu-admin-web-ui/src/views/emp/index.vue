<script setup>
import { ref, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryPageApi, addEmpApi, queryInfoApi, updateEmpApi, deleteEmpApi} from '@/api/emp'
import { queryAllApi as queryAllDeptApi } from '@/api/dept'

//职位列表数据
const jobs = ref([{ name: '班主任', value: 1 },{ name: '讲师', value: 2 },{ name: '学工主管', value: 3 },{ name: '教研主管', value: 4 },{ name: '咨询师', value: 5 },{ name: '其他', value: 6 }])
//性别列表数据
const genders = ref([{ name: '男', value: 1 }, { name: '女', value: 2 }])
//部门列表数据
const deptList = ref([])

const searchEmp = ref({
  name: '',
  gender: '',
  date: [],
  begin: '',
  end: ''
})

//侦听searchEmp中的date属性
watch(
  () => searchEmp.value.date,
  (newValue, oldValue) => {
     if(newValue.length == 2){
      searchEmp.value.begin = newValue[0]
      searchEmp.value.end = newValue[1]
     }else {
      searchEmp.value.begin = ''
      searchEmp.value.end = ''
     }
  }
)

onMounted(async () => {
  handleSearch()

  //加载所有部门数据
  const result = await queryAllDeptApi();
  if(result.code){
    deptList.value = result.data
  }
})

//查询员工
const handleSearch = async () => {
  const result = await queryPageApi(searchEmp.value.name, searchEmp.value.gender, searchEmp.value.begin, searchEmp.value.end, currentPage.value, pageSize.value);
  if(result.code){
    empList.value = result.data.rows
    total.value = result.data.total
  }
}

const handleReset = () => {
  // 清空表单
  searchEmp.value = {
    name: '',
    gender: '',
    date: [],
    begin: '',
    end: ''
  }
  handleSearch()
}

// 示例数据
const empList = ref([])

// 分页配置
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 职位转换函数
const getJobTitle = (job) => {
  switch (job) {
    case 1:
      return '班主任'
    case 2:
      return '讲师'
    case 3:
      return '学工主管'
    case 4:
      return '教研主管'
    case 5:
      return '咨询师'
    default:
      return '其他'
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  handleSearch()
}
const handleCurrentChange = (val) => {
  currentPage.value = val
  handleSearch()
}

// 操作处理
const handleEdit = async (id) => {
  const result = await queryInfoApi(id);
  if(result.code){
    dialogVisible.value = true
    dialogTitle.value = '修改员工'
    employee.value = result.data

    //处理返回的工作经历信息
    let exprList = employee.value.exprList;
    if(exprList && exprList.length > 0){
      exprList.forEach(expr => {
        expr.exprDate = [expr.begin, expr.end]
      })
    }
  }
}

// 删除单个员工
const handleDelete = async (id) => {
  //弹出一个确认框, 如果确认, 就删除;
  ElMessageBox.confirm('确定删除该员工吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 删除员工
    const result = await deleteEmpApi(id);
    if(result.code){
      ElMessage.success('删除员工成功')
      handleSearch()
    }else{
      ElMessage.error(result.msg)
    }
  })
}

//新增员工
const addEmp = () => {
  dialogVisible.value = true
  dialogTitle.value = '新增员工'
  employee.value = {
    username: '',
    name: '',
    gender: '',
    phone: '',
    job: '',
    salary: '',
    deptId: '',
    entryDate: '',
    exprList: []
  }
  employeeFormRef.value.resetFields()
}


//新增/修改表单
const employeeFormRef = ref(null)
const employee = ref({
  username: '',
  name: '',
  gender: '',
  phone: '',
  job: '',
  salary: '',
  deptId: '',
  entryDate: '',
  exprList: []
})

//表单校验规则
// 验证规则
const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度应在2到20个字符之间', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度应在2到10个字符之间', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '请输入有效的手机号', trigger: 'blur' }
  ]
});

// 控制弹窗
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')

//工作经历
//动态添加工作经历 .
const addExprItem = () => {
  employee.value.exprList.push({exprDate: [], begin: '', end: '', company: '', job: ''})
}

//动态删除工作经历 .
const delExprItem = (index) => {
  employee.value.exprList.splice(index, 1)
}

//监听-employee员工对象中的工作经历数据
watch(employee, (newVal, oldVal) => {
  if(employee.value.exprList) {
    employee.value.exprList.forEach(expr => {
      expr.begin = expr.exprDate[0]
      expr.end = expr.exprDate[1]
    })
  }
}, {deep: true})


//保存员工信息
const save = async () => {
  employeeFormRef.value.validate(async valid => {
    if(valid){ // 校验通过
      let result ;
      if(employee.value.id){ //存在ID, 修改
        result = await updateEmpApi(employee.value);
      }else { //不存在ID, 新增
        result = await addEmpApi(employee.value);
      }
      if(result.code){
        ElMessage.success('新增员工成功')
        dialogVisible.value = false
        handleSearch()
      }else {
        ElMessage.error(result.msg)
      }
    }
  })
}

// 存储选中的 ID
const selectedIds = ref([]);

// 处理复选框选择变化的函数
function handleSelectionChange(selection) {
  const ids = selection.map(item => item.id);
  selectedIds.value = ids;
}

//批量删除
const deleteByIds = async () => {
  //弹出一个确认框, 如果确认, 就删除;
  ElMessageBox.confirm('确定删除选中员工吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 删除员工
    const result = await deleteEmpApi(selectedIds.value);
    if(result.code){
      ElMessage.success('删除员工成功')
      handleSearch()
    }else{
      ElMessage.error(result.msg)
    }
  })
}

</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">员工管理</h1>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchEmp">
        <el-form-item label="姓名">
          <el-input v-model="searchEmp.name" placeholder="请输入员工姓名"></el-input>
        </el-form-item>

        <el-form-item label="性别">
          <el-select v-model="searchEmp.gender" placeholder="请选择">
            <el-option label="男" value="1"></el-option>
            <el-option label="女" value="2"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="入职日期">
          <el-date-picker
            v-model="searchEmp.date"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          ></el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">清空</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="addEmp">+ 新增员工</el-button>
      <el-button type="danger" @click="deleteByIds">- 批量删除</el-button>
    </div>

    <div class="table-card">
      <el-table :data="empList" style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" align="center"></el-table-column>
    <el-table-column prop="name" label="姓名" width="120" align="center"></el-table-column>
    <el-table-column label="性别" width="80" align="center">
      <template #default="scope" >
        {{ scope.row.gender == 1 ? '男' : '女' }}
      </template>
    </el-table-column>
    <el-table-column prop="deptName" label="部门名称" width="170" align="center"></el-table-column>
    <el-table-column label="职位" width="120" align="center">
      <template #default="scope">
        {{ getJobTitle(scope.row.job) }}
      </template>
    </el-table-column>
    <el-table-column prop="entryDate" label="入职日期" width="180" align="center"></el-table-column>
    <el-table-column prop="updateTime" label="最后操作时间" width="210" align="center"></el-table-column>
    <el-table-column label="操作" fixed="right" align="center">
      <template #default="scope">
        <el-button size="small" type="primary" @click="handleEdit(scope.row.id)">编辑</el-button>
        <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
      </el-table>
    </div>

    <div class="pagination-bar">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle">
      <el-form ref="employeeFormRef" :model="employee" :rules="rules" label-width="80px">
        <!-- 基本信息 -->
        <!-- 第一行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="employee.username" placeholder="请输入员工用户名，2-20个字"></el-input>
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="employee.name" placeholder="请输入员工姓名，2-10个字"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第二行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="employee.gender" placeholder="请选择性别" style="width: 100%;">
                <el-option v-for="gender in genders" :key="gender.name" :label="gender.name" :value="gender.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="employee.phone" placeholder="请输入员工手机号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第三行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位">
              <el-select v-model="employee.job" placeholder="请选择职位" style="width: 100%;">
                <el-option v-for="job in jobs" :key="job.name" :label="job.name" :value="job.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资" prop="salary">
              <el-input v-model="employee.salary" placeholder="请输入员工薪资"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第四行 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门">
              <el-select v-model="employee.deptId" placeholder="请选择部门" style="width: 100%;">
                <el-option v-for="dept in deptList" :key="dept.id" :label="dept.name" :value="dept.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker v-model="employee.entryDate" type="date" style="width: 100%;" placeholder="选择日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 工作经历 -->
        <!-- 第五行 -->
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item label="工作经历">
              <el-button type="success" size="small" @click="addExprItem">+ 添加工作经历</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第六行 ...  工作经历 -->
        <el-row :gutter="3" v-for="(expr, index) in employee.exprList">
          <el-col :span="10">
            <el-form-item size="small" label="时间" label-width="80px">
              <el-date-picker type="daterange" v-model="expr.exprDate" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" ></el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item size="small" label="公司" label-width="60px">
              <el-input placeholder="请输入公司名称" v-model="expr.company"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item size="small" label="职位" label-width="60px">
              <el-input placeholder="请输入职位" v-model="expr.job"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-form-item size="small" label-width="0px">
              <el-button type="danger" @click="delExprItem(index)">- 删除</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 底部按钮 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
