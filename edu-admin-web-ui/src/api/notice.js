import request from '@/utils/request'

export const queryPageApi = (title, status, page, pageSize) =>
  request.get(`/notices?title=${title || ''}&status=${status ?? ''}&page=${page}&pageSize=${pageSize}`)

export const addApi = (notice) => request.post('/notices', notice)

export const queryInfoApi = (id) => request.get(`/notices/${id}`)

export const updateApi = (notice) => request.put('/notices', notice)

export const deleteApi = (id) => request.delete(`/notices/${id}`)
