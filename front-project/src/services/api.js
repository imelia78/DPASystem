import axios from 'axios';

const API_BASE_URL = '/api/v1';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to inject JWT token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token && !config.url.includes('/auth/')) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

// Authentication
export const authService = {
  login: (credentials) => api.post('/auth/login', credentials),
  registerClient: (clientData) => api.post('/auth/register/client', clientData),
  registerDoctor: (doctorData) => api.post('/auth/register/doctor', doctorData),
};

// Clients (Patients)
export const clientService = {
  getByEmail: (email) => api.get(`/clients?email=${encodeURIComponent(email)}`),
  getMe: () => api.get('/clients/me'),
  updateProfile: (clientData) => api.put('/clients', clientData),
  getById: (id) => api.get(`/clients/${id}`),
  updatePhone: (id, phoneNumber) => api.patch(`/clients/${id}/phone`, { phoneNumber }),
  updateEmail: (id, email) => api.patch(`/clients/${id}/email`, { email })
};

// Doctors
export const doctorService = {
  getByEmail: (email) => api.get(`/doctors?email=${encodeURIComponent(email)}`),
  getMe: () => api.get('/doctors/me'),
  getAll: (pageSize = 100, pageNumber = 0) => api.get(`/doctors?pageSize=${pageSize}&pageNumber=${pageNumber}`),
  getById: (id) => api.get(`/doctors/${id}`)
};

// Appointments
export const appointmentService = {
  create: (appointmentData) => api.post('/appointments', appointmentData),
  getByClient: (clientId, pageSize = 100, pageNumber = 0) => api.get(`/appointments/clients/${clientId}?pageSize=${pageSize}&pageNumber=${pageNumber}`),
  getAll: (pageSize = 100, pageNumber = 0) => api.get(`/appointments?pageSize=${pageSize}&pageNumber=${pageNumber}`),
  getByStatus: (status) => api.get(`/appointments/status?status=${status}`),
  updateStatus: (id, statusData) => api.patch(`/appointments/${id}/status`, statusData),
  updateDateTime: (id, dateTimeData) => api.patch(`/appointments/${id}/datetime`, dateTimeData),
  delete: (id) => api.delete(`/appointments/${id}`)
};

// Reviews
export const reviewService = {
  getByDoctor: (doctorId, pageSize = 100, pageNumber = 0) => api.get(`/reviews/doctor/${doctorId}?pageSize=${pageSize}&pageNumber=${pageNumber}`),
  getByClient: (clientId, pageSize = 100, pageNumber = 0) => api.get(`/reviews/client/${clientId}?pageSize=${pageSize}&pageNumber=${pageNumber}`),
  create: (reviewData) => api.post('/reviews', reviewData),
  update: (id, reviewData) => api.put(`/reviews/${id}`, reviewData),
  updateComment: (id, comment) => api.patch(`/reviews/${id}/comment?newComment=${encodeURIComponent(comment)}`),
  updateRating: (id, rating) => api.patch(`/reviews/${id}/rating?newRating=${rating}`),
  delete: (id) => api.delete(`/reviews/${id}`)
};

// Admin
export const adminService = {
  approveDoctor: (id) => api.patch(`/admin/${id}/approve`),
  rejectDoctor: (id, comment) => api.patch(`/admin/${id}/reject`, { comment }),
};
