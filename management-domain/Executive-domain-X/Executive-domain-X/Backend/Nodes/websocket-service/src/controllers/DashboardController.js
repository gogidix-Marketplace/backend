const Dashboard = require('../models/Dashboard');
const logger = require('../config/logger');

class DashboardController {
  async createDashboard(req, res) {
    try {
      const dashboard = await Dashboard.createDashboard({
        name: req.body.name,
        description: req.body.description,
        executiveId: req.user.executiveId,
        widgets: req.body.widgets,
        isPublic: req.body.isPublic,
        sharedWith: req.body.sharedWith
      });

      res.status(201).json({
        success: true,
        data: dashboard
      });
    } catch (error) {
      logger.error('Error creating dashboard:', error);
      res.status(500).json({
        success: false,
        message: error.message
      });
    }
  }

  async getDashboards(req, res) {
    try {
      const executiveId = req.user.executiveId;

      const dashboards = await Dashboard.find({
        $or: [
          { executiveId },
          { isPublic: true },
          { 'sharedWith.userId': executiveId }
        ]
      })
      .sort({ lastUpdated: -1 })
      .exec();

      res.json({
        success: true,
        data: dashboards
      });
    } catch (error) {
      logger.error('Error fetching dashboards:', error);
      res.status(500).json({
        success: false,
        message: error.message
      });
    }
  }

  async getDashboard(req, res) {
    try {
      const { dashboardId } = req.params;
      const dashboard = await Dashboard.findOne({ dashboardId });

      if (!dashboard) {
        return res.status(404).json({
          success: false,
          message: 'Dashboard not found'
        });
      }

      // Check permissions
      const hasAccess = dashboard.executiveId.toString() === req.user.executiveId ||
                       dashboard.isPublic ||
                       dashboard.sharedWith.some(share =>
                         share.userId.toString() === req.user.executiveId
                       );

      if (!hasAccess) {
        return res.status(403).json({
          success: false,
          message: 'Access denied'
        });
      }

      res.json({
        success: true,
        data: dashboard
      });
    } catch (error) {
      logger.error('Error fetching dashboard:', error);
      res.status(500).json({
        success: false,
        message: error.message
      });
    }
  }

  async updateDashboard(req, res) {
    try {
      const { dashboardId } = req.params;
      const dashboard = await Dashboard.findOne({ dashboardId });

      if (!dashboard) {
        return res.status(404).json({
          success: false,
          message: 'Dashboard not found'
        });
      }

      // Check ownership
      if (dashboard.executiveId.toString() !== req.user.executiveId) {
        return res.status(403).json({
          success: false,
          message: 'Access denied'
        });
      }

      Object.assign(dashboard, req.body);
      dashboard.lastUpdated = new Date();
      await dashboard.save();

      res.json({
        success: true,
        data: dashboard
      });
    } catch (error) {
      logger.error('Error updating dashboard:', error);
      res.status(500).json({
        success: false,
        message: error.message
      });
    }
  }

  async deleteDashboard(req, res) {
    try {
      const { dashboardId } = req.params;
      const dashboard = await Dashboard.findOne({ dashboardId });

      if (!dashboard) {
        return res.status(404).json({
          success: false,
          message: 'Dashboard not found'
        });
      }

      // Check ownership
      if (dashboard.executiveId.toString() !== req.user.executiveId) {
        return res.status(403).json({
          success: false,
          message: 'Access denied'
        });
      }

      await dashboard.remove();

      res.json({
        success: true,
        message: 'Dashboard deleted successfully'
      });
    } catch (error) {
      logger.error('Error deleting dashboard:', error);
      res.status(500).json({
        success: false,
        message: error.message
      });
    }
  }
}

module.exports = new DashboardController();