import React from 'react'
import {
  Card,
  CardContent,
  Box,
  Typography,
  LinearProgress,
  Chip,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Avatar,
  Alert,
  IconButton,
  Tooltip,
  Divider,
} from '@mui/material'
import {
  Security as SecurityIcon,
  Shield as ShieldIcon,
  Warning as WarningIcon,
  Error as ErrorIcon,
  CheckCircle as CheckIcon,
  Info as InfoIcon,
  Refresh as RefreshIcon,
  Settings as SettingsIcon,
  Visibility as ViewIcon,
} from '@mui/icons-material'

export interface SecurityThreat {
  id: string
  type: 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW' | 'INFO'
  title: string
  description: string
  timestamp: Date
  resolved?: boolean
  action?: string
}

export interface SecurityMetric {
  name: string
  value: number
  maximum: number
  unit?: string
  status: 'GOOD' | 'WARNING' | 'CRITICAL'
}

export interface ComplianceStatus {
  standard: string
  score: number
  status: 'COMPLIANT' | 'PARTIAL' | 'NON_COMPLIANT'
  lastAudit: Date
  nextAudit?: Date
}

export interface SecurityStatusData {
  overallScore: number
  status: 'SECURE' | 'WARNING' | 'CRITICAL' | 'UNKNOWN'
  lastUpdate: Date
  threats: SecurityThreat[]
  metrics: SecurityMetric[]
  compliance: ComplianceStatus[]
  recommendations?: string[]
}

export interface SecurityStatusCardProps {
  data: SecurityStatusData
  loading?: boolean
  error?: string
  onRefresh?: () => void
  onViewDetails?: () => void
  onSettings?: () => void
  compact?: boolean
  showThreats?: boolean
  showMetrics?: boolean
  showCompliance?: boolean
  maxThreats?: number
}

const getThreatIcon = (type: SecurityThreat['type']) => {
  switch (type) {
    case 'CRITICAL': return <ErrorIcon color="error" />
    case 'HIGH': return <WarningIcon color="error" />
    case 'MEDIUM': return <WarningIcon color="warning" />
    case 'LOW': return <InfoIcon color="info" />
    case 'INFO': return <InfoIcon color="info" />
    default: return <InfoIcon />
  }
}

const getThreatColor = (type: SecurityThreat['type']) => {
  switch (type) {
    case 'CRITICAL': return 'error'
    case 'HIGH': return 'error'
    case 'MEDIUM': return 'warning'
    case 'LOW': return 'info'
    case 'INFO': return 'default'
    default: return 'default'
  }
}

const getStatusColor = (status: SecurityStatusData['status']) => {
  switch (status) {
    case 'SECURE': return '#4caf50'
    case 'WARNING': return '#ff9800'
    case 'CRITICAL': return '#f44336'
    case 'UNKNOWN': return '#9e9e9e'
    default: return '#9e9e9e'
  }
}

const getStatusIcon = (status: SecurityStatusData['status']) => {
  switch (status) {
    case 'SECURE': return <ShieldIcon sx={{ color: '#4caf50' }} />
    case 'WARNING': return <WarningIcon sx={{ color: '#ff9800' }} />
    case 'CRITICAL': return <ErrorIcon sx={{ color: '#f44336' }} />
    case 'UNKNOWN': return <SecurityIcon sx={{ color: '#9e9e9e' }} />
    default: return <SecurityIcon />
  }
}

const getMetricColor = (status: SecurityMetric['status']) => {
  switch (status) {
    case 'GOOD': return 'success'
    case 'WARNING': return 'warning'
    case 'CRITICAL': return 'error'
    default: return 'primary'
  }
}

const getComplianceColor = (status: ComplianceStatus['status']) => {
  switch (status) {
    case 'COMPLIANT': return 'success'
    case 'PARTIAL': return 'warning'
    case 'NON_COMPLIANT': return 'error'
    default: return 'default'
  }
}

const formatDate = (date: Date) => {
  return new Intl.DateTimeFormat('en-US', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(date)
}

export const SecurityStatusCard: React.FC<SecurityStatusCardProps> = ({
  data,
  loading = false,
  error,
  onRefresh,
  onViewDetails,
  onSettings,
  compact = false,
  showThreats = true,
  showMetrics = true,
  showCompliance = true,
  maxThreats = 5,
}) => {
  const activeThreats = data.threats.filter(threat => !threat.resolved)
  const displayThreats = activeThreats.slice(0, maxThreats)
  const hasMoreThreats = activeThreats.length > maxThreats

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardContent sx={{ flex: 1 }}>
        {/* Header */}
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Avatar
              sx={{
                bgcolor: getStatusColor(data.status),
                width: compact ? 32 : 48,
                height: compact ? 32 : 48,
              }}
            >
              {getStatusIcon(data.status)}
            </Avatar>
            
            <Box>
              <Typography variant={compact ? 'h6' : 'h5'} sx={{ fontWeight: 600 }}>
                Security Status
              </Typography>
              <Typography variant="caption" color="text.secondary">
                Last updated: {formatDate(data.lastUpdate)}
              </Typography>
            </Box>
          </Box>

          <Box sx={{ display: 'flex', gap: 1 }}>
            {onRefresh && (
              <Tooltip title="Refresh">
                <IconButton size="small" onClick={onRefresh} disabled={loading}>
                  <RefreshIcon />
                </IconButton>
              </Tooltip>
            )}
            {onViewDetails && (
              <Tooltip title="View Details">
                <IconButton size="small" onClick={onViewDetails}>
                  <ViewIcon />
                </IconButton>
              </Tooltip>
            )}
            {onSettings && (
              <Tooltip title="Settings">
                <IconButton size="small" onClick={onSettings}>
                  <SettingsIcon />
                </IconButton>
              </Tooltip>
            )}
          </Box>
        </Box>

        {/* Error Display */}
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        {/* Overall Score */}
        <Box sx={{ mb: 3 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1 }}>
            <Typography variant="body2" sx={{ fontWeight: 600 }}>
              Security Score
            </Typography>
            <Typography variant="h6" sx={{ fontWeight: 600 }}>
              {data.overallScore}/100
            </Typography>
          </Box>
          <LinearProgress
            variant="determinate"
            value={data.overallScore}
            color={data.overallScore >= 80 ? 'success' : data.overallScore >= 60 ? 'warning' : 'error'}
            sx={{ height: 8, borderRadius: 4 }}
          />
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 1 }}>
            <Chip
              label={data.status}
              color={
                data.status === 'SECURE' ? 'success' :
                data.status === 'WARNING' ? 'warning' :
                data.status === 'CRITICAL' ? 'error' : 'default'
              }
              size="small"
            />
          </Box>
        </Box>

        {/* Active Threats */}
        {showThreats && activeThreats.length > 0 && (
          <Box sx={{ mb: 3 }}>
            <Typography variant="body2" sx={{ fontWeight: 600, mb: 1 }}>
              Active Threats ({activeThreats.length})
            </Typography>
            <List dense>
              {displayThreats.map((threat) => (
                <ListItem key={threat.id} sx={{ px: 0, py: 0.5 }}>
                  <ListItemIcon sx={{ minWidth: 32 }}>
                    {getThreatIcon(threat.type)}
                  </ListItemIcon>
                  <ListItemText
                    primary={
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Typography variant="body2" sx={{ flex: 1 }}>
                          {threat.title}
                        </Typography>
                        <Chip
                          label={threat.type}
                          size="small"
                          color={getThreatColor(threat.type) as any}
                          variant="outlined"
                        />
                      </Box>
                    }
                    secondary={
                      compact ? undefined : (
                        <Typography variant="caption" color="text.secondary">
                          {threat.description}
                        </Typography>
                      )
                    }
                  />
                </ListItem>
              ))}
            </List>
            {hasMoreThreats && (
              <Typography
                variant="caption"
                color="primary"
                sx={{ cursor: 'pointer', textDecoration: 'underline' }}
                onClick={onViewDetails}
              >
                View {activeThreats.length - maxThreats} more threats
              </Typography>
            )}
          </Box>
        )}

        {/* Security Metrics */}
        {showMetrics && data.metrics.length > 0 && !compact && (
          <Box sx={{ mb: 3 }}>
            <Typography variant="body2" sx={{ fontWeight: 600, mb: 1 }}>
              Security Metrics
            </Typography>
            {data.metrics.map((metric, index) => (
              <Box key={index} sx={{ mb: 1 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 0.5 }}>
                  <Typography variant="caption">
                    {metric.name}
                  </Typography>
                  <Typography variant="caption" sx={{ fontWeight: 600 }}>
                    {metric.value}{metric.unit && ` ${metric.unit}`} / {metric.maximum}
                  </Typography>
                </Box>
                <LinearProgress
                  variant="determinate"
                  value={(metric.value / metric.maximum) * 100}
                  color={getMetricColor(metric.status)}
                  sx={{ height: 4, borderRadius: 2 }}
                />
              </Box>
            ))}
          </Box>
        )}

        {/* Compliance Status */}
        {showCompliance && data.compliance.length > 0 && !compact && (
          <Box sx={{ mb: 2 }}>
            <Typography variant="body2" sx={{ fontWeight: 600, mb: 1 }}>
              Compliance Status
            </Typography>
            {data.compliance.map((compliance, index) => (
              <Box key={index} sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1 }}>
                <Box sx={{ flex: 1 }}>
                  <Typography variant="body2">
                    {compliance.standard}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    Score: {compliance.score}% • Last audit: {formatDate(compliance.lastAudit)}
                  </Typography>
                </Box>
                <Chip
                  label={compliance.status.replace('_', ' ')}
                  color={getComplianceColor(compliance.status) as any}
                  size="small"
                  variant="outlined"
                />
              </Box>
            ))}
          </Box>
        )}

        {/* Recommendations */}
        {data.recommendations && data.recommendations.length > 0 && !compact && (
          <Box>
            <Divider sx={{ mb: 2 }} />
            <Typography variant="body2" sx={{ fontWeight: 600, mb: 1 }}>
              Recommendations
            </Typography>
            <List dense>
              {data.recommendations.map((recommendation, index) => (
                <ListItem key={index} sx={{ px: 0, py: 0.5 }}>
                  <ListItemIcon sx={{ minWidth: 32 }}>
                    <CheckIcon color="primary" fontSize="small" />
                  </ListItemIcon>
                  <ListItemText>
                    <Typography variant="caption">
                      {recommendation}
                    </Typography>
                  </ListItemText>
                </ListItem>
              ))}
            </List>
          </Box>
        )}

        {/* No Threats Message */}
        {showThreats && activeThreats.length === 0 && (
          <Alert severity="success" sx={{ mt: 2 }}>
            <Typography variant="body2">
              No active security threats detected
            </Typography>
          </Alert>
        )}
      </CardContent>
    </Card>
  )
}

export default SecurityStatusCard