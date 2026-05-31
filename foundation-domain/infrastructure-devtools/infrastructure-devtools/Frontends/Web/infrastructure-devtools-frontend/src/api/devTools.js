import apiClient from './client';

export const devToolsApi = {
  // Health & Info
  getHealth: () => apiClient.get('/health'),
  getInfo: () => apiClient.get('/info'),
  getTools: () => apiClient.get('/tools'),

  // API Testing
  getTestCases: (projectId, page = 0, size = 20) =>
    apiClient.get(`/api-testing/test-cases`, { params: { projectId, page, size } }),
  getTestCase: (uuid) => apiClient.get(`/api-testing/test-cases/${uuid}`),
  createTestCase: (data) => apiClient.post('/api-testing/test-cases', data),
  updateTestCase: (uuid, data) => apiClient.put(`/api-testing/test-cases/${uuid}`, data),
  deleteTestCase: (uuid) => apiClient.delete(`/api-testing/test-cases/${uuid}`),
  executeTestCase: (id, executedBy = 'user') =>
    apiClient.post(`/api-testing/test-cases/${id}/execute`, {}, { params: { executedBy } }),
  executeAdHocTest: (data, executedBy = 'user') =>
    apiClient.post('/api-testing/execute', data, { params: { executedBy } }),
  getTestExecutions: (id, page = 0, size = 10) =>
    apiClient.get(`/api-testing/test-cases/${id}/executions`, { params: { page, size } }),
  getTestStatistics: (projectId) => apiClient.get('/api-testing/statistics', { params: { projectId } }),

  // Database Query
  getQueries: (projectId, page = 0, size = 20) =>
    apiClient.get(`/database/queries`, { params: { projectId, page, size } }),
  getQuery: (uuid) => apiClient.get(`/database/queries/${uuid}`),
  createQuery: (data) => apiClient.post('/database/queries', data),
  updateQuery: (uuid, data) => apiClient.put(`/database/queries/${uuid}`, data),
  deleteQuery: (uuid) => apiClient.delete(`/database/queries/${uuid}`),
  executeQuery: (id, parameters, executedBy = 'user') =>
    apiClient.post(`/database/queries/${id}/execute`, parameters, { params: { executedBy } }),
  executeAdHocQuery: (sql, databaseName, parameters, executedBy = 'user') =>
    apiClient.post('/database/execute', parameters, { params: { sql, databaseName, executedBy } }),
  validateQuery: (sql, databaseName) =>
    apiClient.post('/database/validate', null, { params: { sql, databaseName } }),
  getQueryStatistics: (projectId) => apiClient.get('/database/statistics', { params: { projectId } }),

  // Logging
  queryLogs: (query, page = 0, size = 50) =>
    apiClient.post('/logging/entries/query', query, { params: { page, size } }),
  getLogsBySession: (sessionId) => apiClient.get(`/logging/entries/session/${sessionId}`),
  getLogsByRequest: (requestId) => apiClient.get(`/logging/entries/request/${requestId}`),
  getLogStatistics: (startDate, endDate) =>
    apiClient.get('/logging/statistics', { params: { startDate, endDate } }),
  createLogEntry: (data) => apiClient.post('/logging/entries', data),
  exportLogs: (query) => apiClient.post('/logging/export', query),

  // Deployment
  getJobs: (projectId, page = 0, size = 20) =>
    apiClient.get(`/deployment/jobs`, { params: { projectId, page, size } }),
  getJob: (uuid) => apiClient.get(`/deployment/jobs/${uuid}`),
  createJob: (data) => apiClient.post('/deployment/jobs', data),
  updateJob: (uuid, data) => apiClient.put(`/deployment/jobs/${uuid}`, data),
  deleteJob: (uuid) => apiClient.delete(`/deployment/jobs/${uuid}`),
  executeJob: (id, request) => apiClient.post(`/deployment/jobs/${id}/execute`, request),
  getDeploymentExecutions: (id, page = 0, size = 10) =>
    apiClient.get(`/deployment/jobs/${id}/executions`, { params: { page, size } }),
  rollbackDeployment: (id, executedBy = 'user') =>
    apiClient.post(`/deployment/executions/${id}/rollback`, {}, { params: { executedBy } }),
  getDeploymentStatistics: (projectId) => apiClient.get('/deployment/statistics', { params: { projectId } }),

  // Documentation
  getProjects: (projectId, page = 0, size = 20) =>
    apiClient.get(`/documentation/projects`, { params: { projectId, page, size } }),
  getProject: (uuid) => apiClient.get(`/documentation/projects/${uuid}`),
  createProject: (data) => apiClient.post('/documentation/projects', data),
  updateProject: (uuid, data) => apiClient.put(`/documentation/projects/${uuid}`, data),
  deleteProject: (uuid) => apiClient.delete(`/documentation/projects/${uuid}`),
  generateDocumentation: (id, request) => apiClient.post(`/documentation/projects/${id}/generate`, request),
  getGenerations: (id, page = 0, size = 10) =>
    apiClient.get(`/documentation/projects/${id}/generations`, { params: { page, size } }),
  previewDocumentation: (markdown) => apiClient.post('/documentation/preview', markdown),
};

export default devToolsApi;
