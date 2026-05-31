import axios, { AxiosInstance } from "axios";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api/sales/v1";
const COUNTRY_CODE = process.env.NEXT_PUBLIC_COUNTRY_CODE || "US";

class SalesApiService {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_URL,
      headers: {
        "Content-Type": "application/json",
        "X-Country": COUNTRY_CODE,
      },
    });

    // Add request interceptor
    this.client.interceptors.request.use(
      (config) => {
        // Add auth token if available
        const token = localStorage.getItem("auth_token");
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Add response interceptor
    this.client.interceptors.response.use(
      (response) => response,
      (error) => {
        console.error("API Error:", error.response?.data || error.message);
        return Promise.reject(error);
      }
    );
  }

  // Dashboard APIs
  async getDashboardSummary(countryCode: string = COUNTRY_CODE) {
    return this.client.get("/dashboard/summary", {
      headers: { "X-Country": countryCode },
    });
  }

  async getSalesMetrics(countryCode: string = COUNTRY_CODE, asOfDate?: string) {
    const params = asOfDate ? { asOfDate } : {};
    return this.client.get("/dashboard/metrics", {
      params,
      headers: { "X-Country": countryCode },
    });
  }

  async getPipelineSummary(countryCode: string = COUNTRY_CODE) {
    return this.client.get("/dashboard/pipeline/summary", {
      headers: { "X-Country": countryCode },
    });
  }

  async getOverdueDeals(countryCode: string = COUNTRY_CODE) {
    return this.client.get("/deals/overdue", {
      headers: { "X-Country": countryCode },
    });
  }

  async getDealsNeedingFollowUp(countryCode: string = COUNTRY_CODE) {
    return this.client.get("/deals/followup", {
      headers: { "X-Country": countryCode },
    });
  }

  async getCustomersNeedingFollowUp(countryCode: string = COUNTRY_CODE) {
    return this.client.get("/dashboard/customers/needs-followup", {
      headers: { "X-Country": countryCode },
    });
  }

  // Deal APIs
  async getDeals(params?: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: string;
  }) {
    return this.client.get("/deals", { params });
  }

  async getDealById(id: string) {
    return this.client.get(`/deals/${id}`);
  }

  async getDealsByStage(stage: string, page = 0, size = 20) {
    return this.client.get(`/deals/stage/${stage}`, { params: { page, size } });
  }

  async createDeal(data: any) {
    return this.client.post("/deals", data);
  }

  async updateDeal(id: string, data: any) {
    return this.client.put(`/deals/${id}`, data);
  }

  async deleteDeal(id: string) {
    return this.client.delete(`/deals/${id}`);
  }

  // Customer APIs
  async getCustomers(params?: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDirection?: string;
  }) {
    return this.client.get("/customers", { params });
  }

  async getCustomerById(id: string) {
    return this.client.get(`/customers/${id}`);
  }

  async searchCustomers(query: string, page = 0, size = 20) {
    return this.client.get("/customers/search", {
      params: { query, page, size },
    });
  }

  async getCustomersByStatus(status: string, page = 0, size = 20) {
    return this.client.get(`/customers/status/${status}`, {
      params: { page, size },
    });
  }

  async createCustomer(data: any) {
    return this.client.post("/customers", data);
  }

  async updateCustomer(id: string, data: any) {
    return this.client.put(`/customers/${id}`, data);
  }

  async deleteCustomer(id: string) {
    return this.client.delete(`/customers/${id}`);
  }
}

export const salesApi = new SalesApiService();
