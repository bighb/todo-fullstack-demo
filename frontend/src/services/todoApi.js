import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/todos';

export const todoApi = {
  getAll: () => axios.get(API_BASE_URL),

  getById: (id) => axios.get(`${API_BASE_URL}/${id}`),

  create: (todo) => axios.post(API_BASE_URL, todo),

  update: (id, todo) => axios.put(`${API_BASE_URL}/${id}`, todo),

  delete: (id) => axios.delete(`${API_BASE_URL}/${id}`),
};
