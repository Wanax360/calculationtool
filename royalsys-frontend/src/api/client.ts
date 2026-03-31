import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  config.auth = { username: 'admin', password: 'admin123' }
  return config
})

export default api
