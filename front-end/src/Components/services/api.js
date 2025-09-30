import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // ajuste conforme porta do Spring Boot
  timeout: 8000,
});

// interceptador para JWT (se usar login)
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
