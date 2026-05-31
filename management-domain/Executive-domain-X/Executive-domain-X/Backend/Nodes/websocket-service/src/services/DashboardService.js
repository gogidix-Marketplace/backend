const Dashboard = require('../models/Dashboard');
const { redis } = require('../config/redis');
const logger = require('../config/logger');

class DashboardService {
  constructor() {
    this.subscribedClients = new Map();
  }

  // Cache dashboard with 1 hour TTL
  async cacheDashboard(dashboardId, dashboard) {
    try {
      await redis.getClient().setex(
        `dashboard:${dashboardId}`,
        3600,
        JSON.stringify(dashboard)
      );
    } catch (error) {
      logger.error('Error caching dashboard:', error);
    }
  }

  // Get cached dashboard
  async getCachedDashboard(dashboardId) {
    try {
      const cached = await redis.getClient().get(`dashboard:${dashboardId}`);
      return cached ? JSON.parse(cached) : null;
    } catch (error) {
      logger.error('Error getting cached dashboard:', error);
      return null;
    }
  }

  // Subscribe to dashboard updates
  subscribeToDashboard(dashboardId, ws) {
    if (!this.subscribedClients.has(dashboardId)) {
      this.subscribedClients.set(dashboardId, new Set());
    }
    this.subscribedClients.get(dashboardId).add(ws);

    // Clean up when connection closes
    ws.on('close', () => {
      this.unsubscribeFromDashboard(dashboardId, ws);
    });
  }

  // Unsubscribe from dashboard updates
  unsubscribeFromDashboard(dashboardId, ws) {
    if (this.subscribedClients.has(dashboardId)) {
      this.subscribedClients.get(dashboardId).delete(ws);
      if (this.subscribedClients.get(dashboardId).size === 0) {
        this.subscribedClients.delete(dashboardId);
      }
    }
  }

  // Broadcast dashboard update to all subscribers
  async broadcastDashboardUpdate(dashboardId, updateData) {
    if (this.subscribedClients.has(dashboardId)) {
      const message = JSON.stringify({
        type: 'dashboard_update',
        dashboardId,
        data: updateData,
        timestamp: new Date().toISOString()
      });

      for (const ws of this.subscribedClients.get(dashboardId)) {
        if (ws.readyState === ws.OPEN) {
          ws.send(message);
        }
      }
    }

    // Also publish to Redis channel for other services
    try {
      await redis.getPubClient().publish(
        `dashboard:${dashboardId}`,
        message
      );
    } catch (error) {
      logger.error('Error publishing dashboard update:', error);
    }
  }

  // Handle dashboard widget updates
  async handleWidgetUpdate(dashboardId, widgetId, widgetData) {
    try {
      const dashboard = await Dashboard.findOne({ dashboardId });
      if (!dashboard) {
        throw new Error('Dashboard not found');
      }

      // Update widget
      const updated = await dashboard.updateWidget(widgetId, {
        data: widgetData,
        lastUpdated: new Date()
      });

      // Broadcast update
      await this.broadcastDashboardUpdate(dashboardId, {
        widgetId,
        data: widgetData,
        timestamp: new Date().toISOString()
      });

      return { success: true, data: updated };
    } catch (error) {
      logger.error('Error handling widget update:', error);
      return { success: false, message: error.message };
    }
  }

  // Handle KPI updates
  async handleKpiUpdate(kpiId, kpiData) {
    try {
      // Find all dashboards that have this KPI widget
      const dashboards = await Dashboard.find({
        'widgets.widgetId': kpiId,
        'widgets.type': 'KPI'
      });

      for (const dashboard of dashboards) {
        const widget = dashboard.widgets.find(w => w.widgetId === kpiId);
        if (widget) {
          await this.broadcastDashboardUpdate(dashboard.dashboardId, {
            widgetId: kpiId,
            data: kpiData,
            timestamp: new Date().toISOString()
          });
        }
      }

      return { success: true, updatedDashboards: dashboards.length };
    } catch (error) {
      logger.error('Error handling KPI update:', error);
      return { success: false, message: error.message };
    }
  }

  // Get dashboard analytics
  async getDashboardAnalytics(dashboardId) {
    try {
      const dashboard = await Dashboard.findOne({ dashboardId });
      if (!dashboard) {
        throw new Error('Dashboard not found');
      }

      const analytics = {
        totalWidgets: dashboard.widgets.length,
        widgetTypes: {},
        lastUpdated: dashboard.lastUpdated,
        updateFrequency: this.calculateUpdateFrequency(dashboard.widgets)
      };

      // Count widget types
      dashboard.widgets.forEach(widget => {
        analytics.widgetTypes[widget.type] = (analytics.widgetTypes[widget.type] || 0) + 1;
      });

      return { success: true, data: analytics };
    } catch (error) {
      logger.error('Error getting dashboard analytics:', error);
      return { success: false, message: error.message };
    }
  }

  calculateUpdateFrequency(widgets) {
    const now = new Date();
    const oneHourAgo = new Date(now.getTime() - 60 * 60 * 1000);
    const recentUpdates = widgets.filter(w =>
      w.lastUpdated && w.lastUpdated > oneHourAgo
    ).length;
    return recentUpdates / widgets.length;
  }
}

module.exports = new DashboardService();