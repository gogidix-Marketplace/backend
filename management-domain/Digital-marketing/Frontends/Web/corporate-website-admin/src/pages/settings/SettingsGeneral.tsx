import React, { useState } from 'react';
import { Box, Card, CardContent, TextField, Button, Stack, Typography, Divider, Grid } from '@mui/material';
import { Save } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';
import MediaLibrary from '@/components/media/MediaLibrary';
import { useSnackbar } from '@/contexts/SnackbarContext';

const SettingsGeneral: React.FC = () => {
  const [openMediaLibrary, setOpenMediaLibrary] = useState(false);
  const [mediaType, setMediaType] = useState<'logo' | 'favicon' | 'ogImage'>('logo');
  const { showSuccess, showError } = useSnackbar();

  const [settings, setSettings] = useState({
    siteName: 'Gogidix Corporate Website',
    siteDescription: 'Leading enterprise solutions for modern businesses',
    siteUrl: 'https://gogidix.com',
    logoUrl: '',
    faviconUrl: '',
    ogImageUrl: '',
    twitter: 'https://twitter.com/gogidix',
    linkedin: 'https://linkedin.com/company/gogidix',
    facebook: '',
    email: 'contact@gogidix.com',
    phone: '+1 (555) 123-4567',
    address: '',
  });

  const handleSave = () => {
    try {
      // Simulate API call
      showSuccess('Settings saved successfully');
    } catch {
      showError('Failed to save settings');
    }
  };

  const handleMediaSelect = (urls: string[]) => {
    if (urls.length > 0) {
      if (mediaType === 'logo') {
        setSettings({ ...settings, logoUrl: urls[0] });
      } else if (mediaType === 'favicon') {
        setSettings({ ...settings, faviconUrl: urls[0] });
      } else {
        setSettings({ ...settings, ogImageUrl: urls[0] });
      }
    }
    setOpenMediaLibrary(false);
  };

  return (
    <Box>
      <PageHeader
        title="General Settings"
        subtitle="Manage your website configuration"
      />

      <Grid container spacing={3}>
        <Grid item xs={12} lg={8}>
          <Stack spacing={3}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>Site Information</Typography>
                <Stack spacing={3}>
                  <TextField
                    label="Site Name"
                    fullWidth
                    value={settings.siteName}
                    onChange={(e) => setSettings({ ...settings, siteName: e.target.value })}
                  />
                  <TextField
                    label="Site Description"
                    fullWidth
                    multiline
                    rows={2}
                    value={settings.siteDescription}
                    onChange={(e) => setSettings({ ...settings, siteDescription: e.target.value })}
                  />
                  <TextField
                    label="Site URL"
                    fullWidth
                    value={settings.siteUrl}
                    onChange={(e) => setSettings({ ...settings, siteUrl: e.target.value })}
                  />
                </Stack>
              </CardContent>
            </Card>

            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>Images</Typography>
                <Stack spacing={3}>
                  <Box>
                    <Button
                      variant="outlined"
                      onClick={() => { setMediaType('logo'); setOpenMediaLibrary(true); }}
                      fullWidth
                    >
                      {settings.logoUrl ? 'Change Logo' : 'Select Logo'}
                    </Button>
                    {settings.logoUrl && (
                      <Box sx={{ mt: 2 }}>
                        <img src={settings.logoUrl} alt="Logo" style={{ maxHeight: 60 }} />
                      </Box>
                    )}
                  </Box>
                  <Box>
                    <Button
                      variant="outlined"
                      onClick={() => { setMediaType('favicon'); setOpenMediaLibrary(true); }}
                      fullWidth
                    >
                      {settings.faviconUrl ? 'Change Favicon' : 'Select Favicon'}
                    </Button>
                    {settings.faviconUrl && (
                      <Box sx={{ mt: 2 }}>
                        <img src={settings.faviconUrl} alt="Favicon" style={{ maxHeight: 32 }} />
                      </Box>
                    )}
                  </Box>
                  <Box>
                    <Button
                      variant="outlined"
                      onClick={() => { setMediaType('ogImage'); setOpenMediaLibrary(true); }}
                      fullWidth
                    >
                      {settings.ogImageUrl ? 'Change Default OG Image' : 'Select Default OG Image'}
                    </Button>
                    {settings.ogImageUrl && (
                      <Box sx={{ mt: 2 }}>
                        <img src={settings.ogImageUrl} alt="OG Image" style={{ maxHeight: 60 }} />
                      </Box>
                    )}
                  </Box>
                </Stack>
              </CardContent>
            </Card>
          </Stack>
        </Grid>

        <Grid item xs={12} lg={4}>
          <Stack spacing={3}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>Social Links</Typography>
                <Stack spacing={3}>
                  <TextField
                    label="Twitter"
                    fullWidth
                    value={settings.twitter}
                    onChange={(e) => setSettings({ ...settings, twitter: e.target.value })}
                  />
                  <TextField
                    label="LinkedIn"
                    fullWidth
                    value={settings.linkedin}
                    onChange={(e) => setSettings({ ...settings, linkedin: e.target.value })}
                  />
                  <TextField
                    label="Facebook"
                    fullWidth
                    value={settings.facebook}
                    onChange={(e) => setSettings({ ...settings, facebook: e.target.value })}
                  />
                </Stack>
              </CardContent>
            </Card>

            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>Contact Information</Typography>
                <Stack spacing={3}>
                  <TextField
                    label="Email"
                    fullWidth
                    type="email"
                    value={settings.email}
                    onChange={(e) => setSettings({ ...settings, email: e.target.value })}
                  />
                  <TextField
                    label="Phone"
                    fullWidth
                    value={settings.phone}
                    onChange={(e) => setSettings({ ...settings, phone: e.target.value })}
                  />
                  <TextField
                    label="Address"
                    fullWidth
                    multiline
                    rows={2}
                    value={settings.address}
                    onChange={(e) => setSettings({ ...settings, address: e.target.value })}
                  />
                </Stack>
              </CardContent>
            </Card>

            <Button
              variant="contained"
              startIcon={<Save />}
              onClick={handleSave}
              fullWidth
              size="large"
            >
              Save Settings
            </Button>
          </Stack>
        </Grid>
      </Grid>

      <MediaLibrary
        open={openMediaLibrary}
        onClose={() => setOpenMediaLibrary(false)}
        onSelect={handleMediaSelect}
      />
    </Box>
  );
};

export default SettingsGeneral;
