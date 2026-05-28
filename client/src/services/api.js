import axios from 'axios';
import { API_BASE_URL } from '../config/apiConfig';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, 
});


api.interceptors.request.use((config) => {
  return config;
});

export default api;