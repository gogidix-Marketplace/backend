import React from 'react';
import { Box, Grid, Card, CardContent, Typography, Stack, Chip, Box as MuiBox, LinearProgress } from '@mui/material';
import { TrendingUp, TrendingDown, Link, Speed } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';

const mockSeoData = {
  organicTraffic: { current: 34567, change: 15.3 },
  organicKeywords: { current: 12453, change: 8.7 },
  avgPosition: { current: 12.4, change: -2.1 },
  backlinks: { current: 5432, change: 22.5 },
  domainAuthority: { current: 54, change: 1 },
  pageSpeedScore: { current: 87, change: 5 },
  topKeywords: [
    { keyword: 'corporate website builder', position: 3, clicks: 1243, impressions: 15432, change: 2 },
    { keyword: 'enterprise cms', position: 5, clicks: 876, impressions: 12321, change: -1 },
    { keyword: 'content management system', position: 7, clicks: 654, impressions: 9876, change: 3 },
    { keyword: 'headless cms', position: 12, clicks: 432, impressions: 8765, change: 5 },
    { keyword: 'webinar platform', position: 8, clicks: 321, impressions: 6543, change: 0 },
  ],
};

const AnalyticsSeo: React.FC = () => {
  return (
    <Box>
      <PageHeader
        title="SEO Performance"
        subtitle="Track search engine optimization metrics"
      />

      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} md={4}>
          <Card>
            <CardContent>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Organic Traffic
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Typography variant="h5">{mockSeoData.organicTraffic.current.toLocaleString()}</Typography>
                <Chip
                  icon={<TrendingUp />}
                  label={`${mockSeoData.organicTraffic.change}%`}
                  size="small"
                  color="success"
                />
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Card>
            <CardContent>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Organic Keywords
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Typography variant="h5">{mockSeoData.organicKeywords.current.toLocaleString()}</Typography>
                <Chip
                  icon={<TrendingUp />}
                  label={`${mockSeoData.organicKeywords.change}%`}
                  size="small"
                  color="success"
                />
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Card>
            <CardContent>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Avg. Position
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Typography variant="h5">{mockSeoData.avgPosition.current}</Typography>
                <Chip
                  icon={<TrendingDown />}
                  label={`${Math.abs(mockSeoData.avgPosition.change)}%`}
                  size="small"
                  color="error"
                />
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Card>
            <CardContent>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Backlinks
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Typography variant="h5">{mockSeoData.backlinks.current.toLocaleString()}</Typography>
                <Chip
                  icon={<Link />}
                  label={`${mockSeoData.backlinks.change}%`}
                  size="small"
                  color="success"
                />
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Card>
            <CardContent>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Domain Authority
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Typography variant="h5">{mockSeoData.domainAuthority.current}/100</Typography>
                <Chip
                  label={`+${mockSeoData.domainAuthority.change}`}
                  size="small"
                  color="success"
                />
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <Card>
            <CardContent>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Page Speed Score
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <Typography variant="h5">{mockSeoData.pageSpeedScore.current}/100</Typography>
                <Chip
                  icon={<Speed />}
                  label={`+${mockSeoData.pageSpeedScore.change}`}
                  size="small"
                  color="success"
                />
              </Box>
              <LinearProgress
                variant="determinate"
                value={mockSeoData.pageSpeedScore.current}
                sx={{ mt: 2, height: 6, borderRadius: 3 }}
                color={mockSeoData.pageSpeedScore.current > 80 ? 'success' : 'warning'}
              />
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Top Keywords</Typography>
              <Stack spacing={1}>
                {mockSeoData.topKeywords.map((keyword, index) => (
                  <Box
                    key={index}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between',
                      py: 1.5,
                      borderBottom: index < mockSeoData.topKeywords.length - 1 ? '1px solid' : 'none',
                      borderColor: 'divider',
                    }}
                  >
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="body2" fontWeight={500}>
                        {keyword.keyword}
                      </Typography>
                    </Box>
                    <Box sx={{ display: 'flex', gap: 3, minWidth: 300, justifyContent: 'flex-end' }}>
                      <Box sx={{ textAlign: 'center', minWidth: 60 }}>
                        <Typography variant="caption" color="text.secondary">Position</Typography>
                        <Typography variant="body2" fontWeight={600}>
                          #{keyword.position}
                        </Typography>
                      </Box>
                      <Box sx={{ textAlign: 'center', minWidth: 60 }}>
                        <Typography variant="caption" color="text.secondary">Clicks</Typography>
                        <Typography variant="body2">{keyword.clicks.toLocaleString()}</Typography>
                      </Box>
                      <Box sx={{ textAlign: 'center', minWidth: 60 }}>
                        <Typography variant="caption" color="text.secondary">Impressions</Typography>
                        <Typography variant="body2">{keyword.impressions.toLocaleString()}</Typography>
                      </Box>
                      <Box sx={{ minWidth: 50 }}>
                        <Chip
                          label={keyword.change > 0 ? `+${keyword.change}` : keyword.change}
                          size="small"
                          color={keyword.change >= 0 ? 'success' : 'error'}
                          icon={keyword.change > 0 ? <TrendingUp /> : <TrendingDown />}
                        />
                      </Box>
                    </Box>
                  </Box>
                ))}
              </Stack>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default AnalyticsSeo;
