<script setup>
import { ref } from 'vue'
import { loginApi } from '@/api/login'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'

let loginForm = ref({username:'', password:''})
let router = useRouter()

const login = async () => {
  const result = await loginApi(loginForm.value)
  if (result.code) {
    ElMessage.success('登录成功')
    localStorage.setItem('loginUser', JSON.stringify(result.data))
    router.push('/dept')
  } else {
    ElMessage.error(result.msg)
  }
}

const cancel = () => {
  loginForm.value = {
    username: '',
    password: ''
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-bg">
      <div class="login-bg-shape shape-1"></div>
      <div class="login-bg-shape shape-2"></div>
      <div class="login-bg-shape shape-3"></div>
    </div>

    <div class="login-card">
      <div class="login-brand">
        <span class="brand-icon">E</span>
        <h1 class="brand-title">智学教育管理平台</h1>
        <p class="brand-subtitle">Education Administration Platform</p>
      </div>

      <el-form label-width="72px" class="login-form" @keyup.enter="login">
        <el-form-item label="用户名">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>

        <el-form-item label="密码">
          <el-input
            type="password"
            v-model="loginForm.password"
            placeholder="请输入密码"
            size="large"
            show-password
            :prefix-icon="Lock"
          />
        </el-form-item>

        <el-form-item class="login-actions">
          <el-button type="primary" size="large" @click="login">登 录</el-button>
          <el-button size="large" @click="cancel">重 置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  overflow: hidden;
  background: linear-gradient(135deg, #0c4a6e 0%, #0d9488 50%, #0284c7 100%);
}

.login-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.login-bg-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.06);
}

.shape-1 {
  width: 480px;
  height: 480px;
  top: -120px;
  right: -80px;
}

.shape-2 {
  width: 320px;
  height: 320px;
  bottom: -60px;
  left: -60px;
}

.shape-3 {
  width: 200px;
  height: 200px;
  top: 40%;
  left: 15%;
  background: rgba(255, 255, 255, 0.04);
}

.login-card {
  position: relative;
  width: 100%;
  max-width: 420px;
  margin: var(--app-spacing-lg);
  padding: 40px 36px 32px;
  background: var(--app-surface);
  border-radius: var(--app-radius-lg);
  box-shadow: var(--app-shadow-lg);
}

.login-brand {
  text-align: center;
  margin-bottom: 36px;
}

.brand-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 52px;
  height: 52px;
  margin-bottom: var(--app-spacing);
  font-size: 26px;
  font-weight: 700;
  color: #fff;
  background: linear-gradient(135deg, var(--app-primary), var(--app-accent));
  border-radius: var(--app-radius);
  box-shadow: 0 4px 14px rgba(13, 148, 136, 0.35);
}

.brand-title {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 600;
  color: var(--app-text);
}

.brand-subtitle {
  margin: 0;
  font-size: 12px;
  color: var(--app-text-muted);
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.login-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: var(--app-text-secondary);
}

.login-actions {
  margin-top: 8px;
  margin-bottom: 0;
}

.login-actions :deep(.el-form-item__content) {
  display: flex;
  gap: var(--app-spacing-sm);
}

.login-actions .el-button {
  flex: 1;
}
</style>
