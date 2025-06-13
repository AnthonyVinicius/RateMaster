import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  }
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

const request = async (method, url, data = {}, params = {}) => {
  try {
    const response = await api.request({ method, url, data, params });
    return response.data;
  } catch (error) {
    console.error('Erro na requisição:', error);
    throw error;
  }
};

export default request;
