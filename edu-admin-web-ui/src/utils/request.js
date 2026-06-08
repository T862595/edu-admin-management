import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

//创建axios实例对象
const request = axios.create({
    baseURL: '/api',
    timeout: 600000
})

// axios请求拦截器【修复：处理localStorage为空的情况】
request.interceptors.request.use(
    (config) => {
        // 修复：先判断是否有值，再解析，避免报错！
        const userStr = localStorage.getItem('loginUser')
        if (userStr) {
            try {
                let loginUser = JSON.parse(userStr)
                if (loginUser?.token) {
                    config.headers.token = loginUser.token
                }
            } catch (e) {
                console.error('token解析失败', e)
            }
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// axios响应拦截器
request.interceptors.response.use(
    (response) => { //成功回调
        return response.data
    },
    (error) => { //失败回调
        //如果响应的状态码为401, 则路由到登录页面
        if (error.response?.status === 401) {
            ElMessage.error('登录失效, 请重新登录')
            router.push('/login')
        }
        return Promise.reject(error)
    }
)

export default request