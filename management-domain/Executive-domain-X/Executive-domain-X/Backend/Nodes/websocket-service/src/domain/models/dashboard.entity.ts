import { WidgetType } from '../enums/widget-type.enum';

export interface WidgetProps {
  widgetId: string;
  type: WidgetType;
  title: string;
  data: Record<string, unknown>;
  position: { x: number; y: number; width: number; height: number };
  config: Record<string, unknown>;
}

export interface DashboardProps {
  id?: string;
  dashboardId: string;
  name: string;
  description: string;
  executiveId: string;
  widgets: WidgetProps[];
  isPublic: boolean;
  sharedWith: Array<{ userId: string; permissions: 'VIEW' | 'EDIT' }>;
  lastUpdated: Date;
  createdAt: Date;
  updatedAt: Date;
}

export class Dashboard {
  readonly props: DashboardProps;

  constructor(props: DashboardProps) {
    this.props = { ...props, id: props.id || crypto.randomUUID(), widgets: props.widgets || [], isPublic: props.isPublic || false, sharedWith: props.sharedWith || [], description: props.description || '', lastUpdated: props.lastUpdated || new Date(), createdAt: props.createdAt || new Date(), updatedAt: props.updatedAt || new Date() };
  }

  addWidget(widget: WidgetProps) { this.props.widgets.push(widget); this.props.lastUpdated = new Date(); }
  removeWidget(widgetId: string) { this.props.widgets = this.props.widgets.filter(w => w.widgetId !== widgetId); this.props.lastUpdated = new Date(); }
  updateWidget(widgetId: string, updates: Partial<WidgetProps>) { const i = this.props.widgets.findIndex(w => w.widgetId === widgetId); if (i !== -1) { this.props.widgets[i] = { ...this.props.widgets[i], ...updates }; this.props.lastUpdated = new Date(); } }

  toJSON() { return { ...this.props, widgets: this.props.widgets }; }
}
