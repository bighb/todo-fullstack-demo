import axios from 'axios';

// 生产环境使用相对路径（通过 Nginx 反向代理）
// 开发环境使用完整 URL
const API_BASE_URL = import.meta.env.PROD
  ? '/api/todos'
  : 'http://localhost:8080/api/todos';

export const todoApi = {
  getAll: () => axios.get(API_BASE_URL),

  getById: (id) => axios.get(`${API_BASE_URL}/${id}`),

  create: (todo) => axios.post(API_BASE_URL, todo),

  update: (id, todo) => axios.put(`${API_BASE_URL}/${id}`, todo),

  delete: (id) => axios.delete(`${API_BASE_URL}/${id}`),
};
