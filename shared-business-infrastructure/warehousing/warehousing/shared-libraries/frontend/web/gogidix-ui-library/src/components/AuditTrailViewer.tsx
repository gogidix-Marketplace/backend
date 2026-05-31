import React, { useState, useMemo } from 'react'
import {
  Card,
  CardContent,
  CardHeader,
  Timeline,
  TimelineItem,
  TimelineSeparator,
  TimelineConnector,
  TimelineContent,
  TimelineDot,
  Typography,
  Box,
  Chip,
  Avatar,
  IconButton,
  Collapse,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Stack,
  Button,
  Tooltip,
  Alert,
} from '@mui/material'
import {
  ExpandMore as ExpandMoreIcon,
  ExpandLess as ExpandLessIcon,
  Person as PersonIcon,
  Security as SecurityIcon,
  Error as ErrorIcon,
  Warning as WarningIcon,
  Info as InfoIcon,
  CheckCircle as SuccessIcon,
  FilterList as FilterIcon,
  Search as SearchIcon,
  Download as ExportIcon,
} from '@mui/icons-material'

export interface AuditEntry {
  id: string
  timestamp: Date
  userId: string
  userName?: string
  userAvatar?: string
  action: string
  description: string
  category: 'USER' | 'SYSTEM' | 'SECURITY' | 'DATA' | 'ADMIN' | 'COMPLIANCE'
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  result: 'SUCCESS' | 'FAILURE' | 'WARNING' | 'INFO'
  ipAddress?: string
  userAgent?: string
  sessionId?: string
  resourceId?: string
  resourceType?: string
  metadata?: Record<string, any>
  changes?: {
    before?: Record<string, any>
    after?: Record<string, any>
  }
}

export interface AuditTrailViewerProps {
  entries: AuditEntry[]
  loading?: boolean
  error?: string
  title?: string
  maxHeight?: number
  showFilters?: boolean
  showExport?: boolean
  onExport?: (entries: AuditEntry[]) => void
  onEntryClick?: (entry: AuditEntry) => void
  
  // Customization
  compact?: boolean
  groupByDate?: boolean
  showMetadata?: boolean
  allowExpansion?: boolean
}

interface AuditFilters {
  search: string
  category: string
  severity: string
  result: string
  userId: string
  dateFrom: string
  dateTo: string
}

const getCategoryIcon = (category: AuditEntry['category']) => {
  switch (category) {
    case 'SECURITY': return <SecurityIcon />
    case 'USER': return <PersonIcon />
    case 'SYSTEM': return <InfoIcon />
    case 'DATA': return <InfoIcon />
    case 'ADMIN': return <SecurityIcon />
    case 'COMPLIANCE': return <SecurityIcon />
    default: return <InfoIcon />
  }
}

const getCategoryColor = (category: AuditEntry['category']) => {
  switch (category) {
    case 'SECURITY': return 'error'
    case 'USER': return 'primary'
    case 'SYSTEM': return 'info'
    case 'DATA': return 'warning'
    case 'ADMIN': return 'secondary'
    case 'COMPLIANCE': return 'success'
    default: return 'default'
  }
}

const getResultIcon = (result: AuditEntry['result']) => {
  switch (result) {
    case 'SUCCESS': return <SuccessIcon color="success" />
    case 'FAILURE': return <ErrorIcon color="error" />
    case 'WARNING': return <WarningIcon color="warning" />
    case 'INFO': return <InfoIcon color="info" />
    default: return <InfoIcon />
  }
}

const getSeverityColor = (severity: AuditEntry['severity']) => {
  switch (severity) {
    case 'CRITICAL': return 'error'
    case 'HIGH': return 'warning'
    case 'MEDIUM': return 'info'
    case 'LOW': return 'default'
    default: return 'default'
  }
}

const formatDate = (date: Date) => {
  return new Intl.DateTimeFormat('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  }).format(date)
}

const formatDateShort = (date: Date) => {
  return new Intl.DateTimeFormat('en-US', {
    month: 'short',
    day: 'numeric',
  }).format(date)
}

export const AuditTrailViewer: React.FC<AuditTrailViewerProps> = ({
  entries = [],
  loading = false,
  error,
  title = 'Audit Trail',
  maxHeight = 600,
  showFilters = true,
  showExport = true,
  onExport,
  onEntryClick,
  compact = false,
  groupByDate = false,
  showMetadata = true,
  allowExpansion = true,
}) => {
  const [filters, setFilters] = useState<AuditFilters>({
    search: '',
    category: '',
    severity: '',
    result: '',
    userId: '',
    dateFrom: '',
    dateTo: '',
  })
  const [showFilterPanel, setShowFilterPanel] = useState(false)
  const [expandedEntries, setExpandedEntries] = useState<Set<string>>(new Set())

  // Filter entries
  const filteredEntries = useMemo(() => {
    return entries.filter(entry => {
      // Search filter
      if (filters.search) {
        const searchLower = filters.search.toLowerCase()
        const searchFields = [
          entry.action,
          entry.description,
          entry.userName,
          entry.userId,
          entry.resourceType,
          entry.resourceId,
        ]
        if (!searchFields.some(field => field?.toLowerCase().includes(searchLower))) {
          return false
        }
      }

      // Category filter
      if (filters.category && entry.category !== filters.category) {
        return false
      }

      // Severity filter
      if (filters.severity && entry.severity !== filters.severity) {
        return false
      }

      // Result filter
      if (filters.result && entry.result !== filters.result) {
        return false
      }

      // User filter
      if (filters.userId && entry.userId !== filters.userId) {
        return false
      }

      // Date range filter
      if (filters.dateFrom) {
        const fromDate = new Date(filters.dateFrom)
        if (entry.timestamp < fromDate) {
          return false
        }
      }

      if (filters.dateTo) {
        const toDate = new Date(filters.dateTo)
        toDate.setHours(23, 59, 59, 999) // End of day
        if (entry.timestamp > toDate) {
          return false
        }
      }

      return true
    })
  }, [entries, filters])

  // Group by date if required
  const groupedEntries = useMemo(() => {
    if (!groupByDate) {
      return { '': filteredEntries }
    }

    const groups: Record<string, AuditEntry[]> = {}
    filteredEntries.forEach(entry => {
      const dateKey = formatDateShort(entry.timestamp)
      if (!groups[dateKey]) {
        groups[dateKey] = []
      }
      groups[dateKey].push(entry)
    })

    return groups
  }, [filteredEntries, groupByDate])

  const handleFilterChange = (field: keyof AuditFilters, value: string) => {
    setFilters(prev => ({ ...prev, [field]: value }))
  }

  const clearFilters = () => {
    setFilters({
      search: '',
      category: '',
      severity: '',
      result: '',
      userId: '',
      dateFrom: '',
      dateTo: '',
    })
  }

  const toggleExpand = (entryId: string) => {
    setExpandedEntries(prev => {
      const newSet = new Set(prev)
      if (newSet.has(entryId)) {
        newSet.delete(entryId)
      } else {
        newSet.add(entryId)
      }
      return newSet
    })
  }

  const renderMetadata = (entry: AuditEntry) => {
    if (!showMetadata) return null

    const metadata = []
    if (entry.ipAddress) metadata.push(['IP Address', entry.ipAddress])
    if (entry.sessionId) metadata.push(['Session ID', entry.sessionId])
    if (entry.resourceId) metadata.push(['Resource ID', entry.resourceId])
    if (entry.resourceType) metadata.push(['Resource Type', entry.resourceType])
    
    if (entry.metadata) {
      Object.entries(entry.metadata).forEach(([key, value]) => {
        metadata.push([key, String(value)])
      })
    }

    if (metadata.length === 0) return null

    return (
      <Box sx={{ mt: 1, pl: 2, borderLeft: '2px solid', borderColor: 'divider' }}>
        <Typography variant="caption" sx={{ fontWeight: 600, display: 'block', mb: 0.5 }}>
          Details:
        </Typography>
        {metadata.map(([key, value], index) => (
          <Typography key={index} variant="caption" sx={{ display: 'block' }}>
            <strong>{key}:</strong> {value}
          </Typography>
        ))}
      </Box>
    )
  }

  const renderChanges = (entry: AuditEntry) => {
    if (!entry.changes || (!entry.changes.before && !entry.changes.after)) return null

    return (
      <Box sx={{ mt: 1, pl: 2, borderLeft: '2px solid', borderColor: 'divider' }}>
        <Typography variant="caption" sx={{ fontWeight: 600, display: 'block', mb: 0.5 }}>
          Changes:
        </Typography>
        {entry.changes.before && (
          <Box sx={{ mb: 1 }}>
            <Typography variant="caption" sx={{ color: 'error.main', fontWeight: 600 }}>
              Before:
            </Typography>
            <pre style={{ fontSize: '0.75rem', margin: 0, whiteSpace: 'pre-wrap' }}>
              {JSON.stringify(entry.changes.before, null, 2)}
            </pre>
          </Box>
        )}
        {entry.changes.after && (
          <Box>
            <Typography variant="caption" sx={{ color: 'success.main', fontWeight: 600 }}>
              After:
            </Typography>
            <pre style={{ fontSize: '0.75rem', margin: 0, whiteSpace: 'pre-wrap' }}>
              {JSON.stringify(entry.changes.after, null, 2)}
            </pre>
          </Box>
        )}
      </Box>
    )
  }

  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <CardHeader
        title={title}
        action={
          <Box sx={{ display: 'flex', gap: 1 }}>
            {showFilters && (
              <IconButton onClick={() => setShowFilterPanel(!showFilterPanel)}>
                <FilterIcon />
              </IconButton>
            )}
            {showExport && onExport && (
              <IconButton onClick={() => onExport(filteredEntries)}>
                <ExportIcon />
              </IconButton>
            )}
          </Box>
        }
      />

      {/* Filter Panel */}
      {showFilters && (
        <Collapse in={showFilterPanel}>
          <CardContent sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Stack spacing={2}>
              <Stack direction="row" spacing={2} alignItems="center">
                <TextField
                  size="small"
                  label="Search"
                  value={filters.search}
                  onChange={(e) => handleFilterChange('search', e.target.value)}
                  InputProps={{
                    startAdornment: <SearchIcon sx={{ mr: 1, color: 'text.secondary' }} />,
                  }}
                  sx={{ flex: 1 }}
                />
                <FormControl size="small" sx={{ minWidth: 120 }}>
                  <InputLabel>Category</InputLabel>
                  <Select
                    value={filters.category}
                    label="Category"
                    onChange={(e) => handleFilterChange('category', e.target.value)}
                  >
                    <MenuItem value="">All</MenuItem>
                    <MenuItem value="USER">User</MenuItem>
                    <MenuItem value="SYSTEM">System</MenuItem>
                    <MenuItem value="SECURITY">Security</MenuItem>
                    <MenuItem value="DATA">Data</MenuItem>
                    <MenuItem value="ADMIN">Admin</MenuItem>
                    <MenuItem value="COMPLIANCE">Compliance</MenuItem>
                  </Select>
                </FormControl>
                <FormControl size="small" sx={{ minWidth: 120 }}>
                  <InputLabel>Severity</InputLabel>
                  <Select
                    value={filters.severity}
                    label="Severity"
                    onChange={(e) => handleFilterChange('severity', e.target.value)}
                  >
                    <MenuItem value="">All</MenuItem>
                    <MenuItem value="LOW">Low</MenuItem>
                    <MenuItem value="MEDIUM">Medium</MenuItem>
                    <MenuItem value="HIGH">High</MenuItem>
                    <MenuItem value="CRITICAL">Critical</MenuItem>
                  </Select>
                </FormControl>
                <FormControl size="small" sx={{ minWidth: 120 }}>
                  <InputLabel>Result</InputLabel>
                  <Select
                    value={filters.result}
                    label="Result"
                    onChange={(e) => handleFilterChange('result', e.target.value)}
                  >
                    <MenuItem value="">All</MenuItem>
                    <MenuItem value="SUCCESS">Success</MenuItem>
                    <MenuItem value="FAILURE">Failure</MenuItem>
                    <MenuItem value="WARNING">Warning</MenuItem>
                    <MenuItem value="INFO">Info</MenuItem>
                  </Select>
                </FormControl>
              </Stack>
              
              <Stack direction="row" spacing={2} alignItems="center">
                <TextField
                  size="small"
                  label="From Date"
                  type="date"
                  value={filters.dateFrom}
                  onChange={(e) => handleFilterChange('dateFrom', e.target.value)}
                  InputLabelProps={{ shrink: true }}
                />
                <TextField
                  size="small"
                  label="To Date"
                  type="date"
                  value={filters.dateTo}
                  onChange={(e) => handleFilterChange('dateTo', e.target.value)}
                  InputLabelProps={{ shrink: true }}
                />
                <TextField
                  size="small"
                  label="User ID"
                  value={filters.userId}
                  onChange={(e) => handleFilterChange('userId', e.target.value)}
                />
                <Button size="small" onClick={clearFilters}>
                  Clear Filters
                </Button>
              </Stack>
            </Stack>
          </CardContent>
        </Collapse>
      )}

      {/* Error Display */}
      {error && (
        <Alert severity="error" sx={{ m: 2 }}>
          {error}
        </Alert>
      )}

      {/* Audit Entries */}
      <CardContent
        sx={{
          flex: 1,
          overflow: 'auto',
          maxHeight,
          p: compact ? 1 : 2,
        }}
      >
        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
            <Typography>Loading audit trail...</Typography>
          </Box>
        ) : filteredEntries.length === 0 ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
            <Typography color="text.secondary">
              No audit entries found
            </Typography>
          </Box>
        ) : (
          Object.entries(groupedEntries).map(([dateGroup, groupEntries]) => (
            <Box key={dateGroup}>
              {dateGroup && (
                <Typography variant="h6" sx={{ mb: 2, mt: dateGroup !== Object.keys(groupedEntries)[0] ? 3 : 0 }}>
                  {dateGroup}
                </Typography>
              )}
              
              <Timeline>
                {groupEntries.map((entry, index) => {
                  const isExpanded = expandedEntries.has(entry.id)
                  
                  return (
                    <TimelineItem key={entry.id}>
                      <TimelineSeparator>
                        <TimelineDot color={getCategoryColor(entry.category)} variant="outlined">
                          {getCategoryIcon(entry.category)}
                        </TimelineDot>
                        {index < groupEntries.length - 1 && <TimelineConnector />}
                      </TimelineSeparator>
                      
                      <TimelineContent>
                        <Box
                          sx={{
                            cursor: onEntryClick ? 'pointer' : 'default',
                            '&:hover': onEntryClick ? { bgcolor: 'action.hover' } : {},
                            p: 1,
                            borderRadius: 1,
                          }}
                          onClick={() => onEntryClick?.(entry)}
                        >
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                            <Typography variant={compact ? 'body2' : 'body1'} sx={{ fontWeight: 600 }}>
                              {entry.action}
                            </Typography>
                            
                            <Chip
                              size="small"
                              label={entry.severity}
                              color={getSeverityColor(entry.severity) as any}
                              variant="outlined"
                            />
                            
                            {getResultIcon(entry.result)}
                            
                            {allowExpansion && (showMetadata || entry.changes) && (
                              <IconButton
                                size="small"
                                onClick={(e) => {
                                  e.stopPropagation()
                                  toggleExpand(entry.id)
                                }}
                              >
                                {isExpanded ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                              </IconButton>
                            )}
                          </Box>
                          
                          <Typography
                            variant={compact ? 'caption' : 'body2'}
                            color="text.secondary"
                            sx={{ mb: 1 }}
                          >
                            {entry.description}
                          </Typography>
                          
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 1 }}>
                            {entry.userAvatar ? (
                              <Avatar src={entry.userAvatar} sx={{ width: 24, height: 24 }}>
                                {entry.userName?.charAt(0)}
                              </Avatar>
                            ) : (
                              <Avatar sx={{ width: 24, height: 24, bgcolor: 'primary.main' }}>
                                {entry.userName?.charAt(0) || 'U'}
                              </Avatar>
                            )}
                            
                            <Typography variant="caption">
                              {entry.userName || entry.userId}
                            </Typography>
                            
                            <Typography variant="caption" color="text.secondary">
                              {formatDate(entry.timestamp)}
                            </Typography>
                          </Box>
                          
                          {isExpanded && (
                            <Box sx={{ mt: 1 }}>
                              {renderMetadata(entry)}
                              {renderChanges(entry)}
                            </Box>
                          )}
                        </Box>
                      </TimelineContent>
                    </TimelineItem>
                  )
                })}
              </Timeline>
            </Box>
          ))
        )}
      </CardContent>
    </Card>
  )
}

export default AuditTrailViewer