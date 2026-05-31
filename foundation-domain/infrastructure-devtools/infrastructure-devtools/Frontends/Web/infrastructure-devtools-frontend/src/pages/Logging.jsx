import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Chip,
  Paper,
  List,
  ListItem,
  ListItemText,
  Accordion,
  AccordionSummary,
  AccordionDetails,
} from '@mui/material';
import { ExpandMore as ExpandMoreIcon } from '@mui/icons-material';
import { devToolsApi } from '../api/devTools';
import { format } from 'date-fns';

function Logging() {
  const [levelFilter, setLevelFilter] = useState('');
  const [searchTerm, setSearchTerm] = useState('');

  const { data: stats } = useQuery({
    queryKey: ['log-stats'],
    queryFn: () => devToolsApi.getLogStatistics().then((res) => res.data),
  });

  const { data: logs, isLoading } = useQuery({
    queryKey: ['logs', levelFilter, searchTerm],
    queryFn: () =>
      devToolsApi
        .queryLogs(
          {
            levels: levelFilter ? [levelFilter] : ['ERROR', 'WARN', 'INFO', 'DEBUG'],
            search: searchTerm,
          },
          0,
          100
        )
        .then((res) => res.data),
  });

  const getLevelColor = (level) => {
    switch (level) {
      case 'ERROR':
        return '#f44336';
      case 'WARN':
        return '#ff9800';
      case 'INFO':
        return '#2196f3';
      case 'DEBUG':
        return '#9e9e9e';
      default:
        return '#757575';
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Logging & Debugging
      </Typography>

      {/* Statistics Cards */}
      <Box display="flex" gap={2} mb={3} flexWrap="wrap">
        <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333', flex: 1, minWidth: 150 }}>
          <CardContent>
            <Typography variant="body2" color="text.secondary">
              Total
            </Typography>
            <Typography variant="h4" sx={{ color: '#00bcd4' }}>
              {stats?.totalCount || 0}
            </Typography>
          </CardContent>
        </Card>
        <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333', flex: 1, minWidth: 150 }}>
          <CardContent>
            <Typography variant="body2" color="text.secondary">
              Errors
            </Typography>
            <Typography variant="h4" sx={{ color: '#f44336' }}>
              {stats?.errorCount || 0}
            </Typography>
          </CardContent>
        </Card>
        <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333', flex: 1, minWidth: 150 }}>
          <CardContent>
            <Typography variant="body2" color="text.secondary">
              Warnings
            </Typography>
            <Typography variant="h4" sx={{ color: '#ff9800' }}>
              {stats?.warnCount || 0}
            </Typography>
          </CardContent>
        </Card>
        <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333', flex: 1, minWidth: 150 }}>
          <CardContent>
            <Typography variant="body2" color="text.secondary">
              Info
            </Typography>
            <Typography variant="h4" sx={{ color: '#2196f3' }}>
              {stats?.infoCount || 0}
            </Typography>
          </CardContent>
        </Card>
      </Box>

      {/* Filters */}
      <Box display="flex" gap={2} mb={3} flexWrap="wrap">
        <TextField
          label="Search"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          sx={{ minWidth: 300, '& .MuiInputBase-root': { bgcolor: '#2a2a2a' } }}
        />
        <FormControl sx={{ minWidth: 150 }}>
          <InputLabel>Level</InputLabel>
          <Select
            value={levelFilter}
            onChange={(e) => setLevelFilter(e.target.value)}
            label="Level"
          >
            <MenuItem value="">All Levels</MenuItem>
            <MenuItem value="ERROR">ERROR</MenuItem>
            <MenuItem value="WARN">WARN</MenuItem>
            <MenuItem value="INFO">INFO</MenuItem>
            <MenuItem value="DEBUG">DEBUG</MenuItem>
          </Select>
        </FormControl>
      </Box>

      {/* Log Entries */}
      {isLoading ? (
        <Typography>Loading logs...</Typography>
      ) : logs?.content?.length > 0 ? (
        <List>
          {logs.content.map((log) => (
            <Accordion key={log.uuid} sx={{ bgcolor: '#1e1e1e', mb: 1 }}>
              <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                <Box display="flex" alignItems="center" gap={1} width="100%">
                  <Chip
                    label={log.level}
                    size="small"
                    sx={{
                      bgcolor: getLevelColor(log.level),
                      color: 'white',
                      minWidth: 70,
                    }}
                  />
                  <Chip label={log.source} size="small" variant="outlined" />
                  <Typography variant="body2" sx={{ flex: 1, ml: 1 }}>
                    {log.message?.substring(0, 100)}
                    {log.message?.length > 100 ? '...' : ''}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {log.createdAt ? format(new Date(log.createdAt), 'HH:mm:ss') : ''}
                  </Typography>
                </Box>
              </AccordionSummary>
              <AccordionDetails>
                <Typography variant="body1" paragraph>
                  {log.message}
                </Typography>
                {log.stackTrace && (
                  <Paper sx={{ p: 2, bgcolor: '#0d0d0d', mt: 2 }}>
                    <Typography variant="caption" component="pre" sx={{ color: '#f44336' }}>
                      {log.stackTrace}
                    </Typography>
                  </Paper>
                )}
                {log.context && (
                  <Box mt={2}>
                    <Typography variant="subtitle2" gutterBottom>
                      Context:
                    </Typography>
                    <Paper sx={{ p: 2, bgcolor: '#0d0d0d' }}>
                      <Typography variant="caption" component="pre">
                        {JSON.stringify(log.context, null, 2)}
                      </Typography>
                    </Paper>
                  </Box>
                )}
                <Box display="flex" gap={1} mt={2} flexWrap="wrap">
                  {log.sessionId && <Chip label={`Session: ${log.sessionId}`} size="small" />}
                  {log.requestId && <Chip label={`Request: ${log.requestId}`} size="small" />}
                  {log.userId && <Chip label={`User: ${log.userId}`} size="small" />}
                </Box>
              </AccordionDetails>
            </Accordion>
          ))}
        </List>
      ) : (
        <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333', p: 3, textAlign: 'center' }}>
          <Typography variant="body1" color="text.secondary">
            No log entries found matching your filters.
          </Typography>
        </Card>
      )}
    </Box>
  );
}

export default Logging;
