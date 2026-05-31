import { Schema } from 'mongoose';

const sourceConfigDataSchema = new Schema({
  file: { path: String, pattern: String, recursive: Boolean, encoding: { type: String, default: 'utf8' } },
  syslog: { port: Number, protocol: { type: String, enum: ['tcp', 'udp'] }, format: { type: String, enum: ['rfc3164', 'rfc5424'] } },
  http: { endpoint: String, authentication: { type: { type: String, enum: ['basic', 'bearer', 'api_key'] }, credentials: Schema.Types.Mixed } },
  mongodb: { connectionString: String, database: String, collection: String, query: Schema.Types.Mixed, timeField: { type: String, default: 'timestamp' } },
}, { _id: false });

const parsingConfigSchema = new Schema({
  enabled: { type: Boolean, default: true },
  format: { type: String, enum: ['json', 'text', 'common', 'combined', 'syslog'], default: 'json' },
  timestampFormat: String,
  fieldMapping: { type: Map, of: String },
  extractFields: [String],
}, { _id: false });

const retentionConfigSchema = new Schema({
  enabled: { type: Boolean, default: true },
  days: { type: Number, default: 30, min: 1 },
  archive: { type: Boolean, default: false },
  archiveLocation: String,
}, { _id: false });

export const LogSourceSchema = new Schema({
  name: { type: String, required: true, trim: true, unique: true },
  description: { type: String, required: true, trim: true },
  enabled: { type: Boolean, default: true },
  type: { type: String, enum: ['file', 'syslog', 'http', 'kafka', 'mongodb'], required: true },
  sourceType: { type: String, enum: ['application', 'system', 'database', 'network', 'security', 'custom'], required: true },
  config: { type: sourceConfigDataSchema, required: true },
  parsing: { type: parsingConfigSchema, required: true },
  retention: { type: retentionConfigSchema, required: true },
  lastCollectedAt: Date,
  status: { type: String, enum: ['active', 'inactive', 'error'], default: 'active' },
}, { timestamps: true, collection: 'log_sources' });

LogSourceSchema.index({ enabled: 1, status: 1 });
LogSourceSchema.index({ type: 1 });
