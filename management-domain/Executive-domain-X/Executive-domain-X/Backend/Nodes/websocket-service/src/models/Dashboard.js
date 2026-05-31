const { ObjectId } = require('mongodb');
const { v4: uuidv4 } = require('uuid');

const DashboardSchema = new mongoose.Schema({
  dashboardId: {
    type: String,
    default: () => uuidv4(),
    unique: true
  },
  name: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  },
  executiveId: {
    type: ObjectId,
    required: true,
    ref: 'Executive'
  },
  widgets: [{
    widgetId: {
      type: String,
      required: true
    },
    type: {
      type: String,
      required: true,
      enum: ['KPI', 'CHART', 'METRIC', 'TREND', 'GUAGE']
    },
    title: {
      type: String,
      required: true
    },
    data: {
      type: Object,
      default: {}
    },
    position: {
      x: Number,
      y: Number,
      width: Number,
      height: Number
    },
    config: {
      type: Object,
      default: {}
    }
  }],
  isPublic: {
    type: Boolean,
    default: false
  },
  sharedWith: [{
    userId: ObjectId,
    permissions: {
      type: String,
      enum: ['VIEW', 'EDIT'],
      default: 'VIEW'
    }
  }],
  lastUpdated: {
    type: Date,
    default: Date.now
  }
}, {
  timestamps: true
});

DashboardSchema.index({ executiveId: 1, lastUpdated: -1 });
DashboardSchema.index({ name: 1, executiveId: 1 });

DashboardSchema.statics.createDashboard = function (data) {
  return this.create({
    name: data.name,
    description: data.description || '',
    executiveId: data.executiveId,
    widgets: data.widgets || [],
    isPublic: data.isPublic || false,
    sharedWith: data.sharedWith || []
  });
};

DashboardSchema.methods.addWidget = function (widget) {
  this.widgets.push(widget);
  this.lastUpdated = new Date();
  return this.save();
};

DashboardSchema.methods.removeWidget = function (widgetId) {
  this.widgets = this.widgets.filter(w => w.widgetId !== widgetId);
  this.lastUpdated = new Date();
  return this.save();
};

DashboardSchema.methods.updateWidget = function (widgetId, updates) {
  const widgetIndex = this.widgets.findIndex(w => w.widgetId === widgetId);
  if (widgetIndex !== -1) {
    this.widgets[widgetIndex] = { ...this.widgets[widgetIndex], ...updates };
    this.lastUpdated = new Date();
    return this.save();
  }
  throw new Error('Widget not found');
};

const Dashboard = mongoose.models.Dashboard || mongoose.model('Dashboard', DashboardSchema);

module.exports = Dashboard;