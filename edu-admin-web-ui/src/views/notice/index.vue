<script setup>
import { onMounted, ref } from 'vue'
import { queryPageApi, addApi, queryInfoApi, updateApi, deleteApi } from '@/api/notice'
import { ElMessage, ElMessageBox } from 'element-plus'

const statuses = ref([
  { name: '发布', value: 1 },
  { name: '草稿', value: 0 }
])

const searchNotice = ref({ title: '', status: '' })
const tableData = ref([])
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 })

const dialogFormVisible = ref(false)
const formTitle = ref('')
const noticeFormRef = ref()
const notice = ref({ title: '', content: '', status: 1 })

const rules = ref({
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度为2-100个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入公告内容', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
})

onMounted(() => {
  queryPage()
})

const clearNotice = () => {
  notice.value = { title: '', content: '', status: 1 }
}

const queryPage = async () => {
  const result = await queryPageApi(
    searchNotice.value.title,
    searchNotice.value.status,
    pagination.value.currentPage,
    pagination.value.pageSize
  )
  if (result.code) {
    tableData.value = result.data.rows
    pagination.value.total = result.data.total
  }
}

const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize
  queryPage()
}

const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  queryPage()
}

const clear = () => {
  searchNotice.value = { title: '', status: '' }
  pagination.value.currentPage = 1
  queryPage()
}

const addNotice = () => {
  dialogFormVisible.value = true
  formTitle.value = '新增公告'
  clearNotice()
}

const updateNotice = async (id) => {
  clearNotice()
  dialogFormVisible.value = true
  formTitle.value = '修改公告'
  const result = await queryInfoApi(id)
  if (result.code) {
    notice.value = result.data
  }
}

const resetForm = (formRef) => {
  if (!formRef) return
  formRef.resetFields()
}

const save = (formRef) => {
  if (!formRef) return
  formRef.validate(async (valid) => {
    if (!valid) return
    const api = notice.value.id ? updateApi(notice.value) : addApi(notice.value)
    const result = await api
    if (result.code) {
      ElMessage.success('操作成功')
      dialogFormVisible.value = false
      queryPage()
    } else {
      ElMessage.error(result.msg)
    }
  })
}

const delById = (id) => {
  ElMessageBox.confirm('您确认删除此公告吗?', '删除公告', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await deleteApi(id)
    if (result.code) {
      ElMessage.success('删除成功')
      queryPage()
    } else {
      ElMessage.error(result.msg)
    }
  })
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">公告管理</h1>
      <el-button type="primary" @click="addNotice()">+ 新增公告</el-button>
    </div>

    <div class="search-card">
      <el-form :inline="true" :model="searchNotice">
        <el-form-item label="标题">
          <el-input v-model="searchNotice.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchNotice.status" placeholder="请选择" clearable style="width: 120px;">
            <el-option v-for="item in statuses" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryPage">查询</el-button>
          <el-button @click="clear">清空</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-card">
      <el-table :data="tableData" style="width: 100%;">
    <el-table-column type="index" label="序号" width="60" align="center" />
    <el-table-column prop="title" label="标题" width="200" align="center" show-overflow-tooltip />
    <el-table-column prop="content" label="内容" align="center" show-overflow-tooltip />
    <el-table-column label="状态" width="100" align="center">
      <template #default="{ row }">
        <el-tag :type="row.status === 1 ? 'success' : 'info'">
          {{ row.status === 1 ? '发布' : '草稿' }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="updateTime" label="最后修改时间" width="180" align="center" />
    <el-table-column fixed="right" label="操作" width="160" align="center">
      <template #default="{ row }">
        <el-button size="small" @click="updateNotice(row.id)">修改</el-button>
        <el-button size="small" type="danger" @click="delById(row.id)">删除</el-button>
      </template>
    </el-table-column>
      </el-table>
    </div>

    <div class="pagination-bar">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog v-model="dialogFormVisible" :title="formTitle" width="40%" @close="resetForm(noticeFormRef)">
    <el-form :model="notice" :rules="rules" ref="noticeFormRef" label-width="80px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="notice.title" placeholder="请输入公告标题" />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input v-model="notice.content" type="textarea" :rows="6" placeholder="请输入公告内容" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="notice.status">
          <el-radio :label="1">发布</el-radio>
          <el-radio :label="0">草稿</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save(noticeFormRef)">确定</el-button>
      </span>
    </template>
    </el-dialog>
  </div>
</template>

<style scoped>
</style>
