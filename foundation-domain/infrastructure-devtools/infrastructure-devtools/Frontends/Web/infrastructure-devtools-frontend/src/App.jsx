import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Box, AppBar, Toolbar, Typography, Drawer, List, ListItem, ListItemIcon, ListItemText } from '@mui/material';
import {
  ApiIcon,
  StorageIcon,
  BugReportIcon,
  CloudUploadIcon,
  DescriptionIcon,
  DashboardIcon,
} from '@mui/icons-material';
import Dashboard from './pages/Dashboard';
import ApiTesting from './pages/ApiTesting';
import DatabaseQuery from './pages/DatabaseQuery';
import Logging from './pages/Logging';
import Deployment from './pages/Deployment';
import Documentation from './pages/Documentation';

const drawerWidth = 240;

const menuItems = [
  { text: 'Dashboard', icon: <DashboardIcon />, path: '/' },
  { text: 'API Testing', icon: <ApiIcon />, path: '/api-testing' },
  { text: 'Database Query', icon: <StorageIcon />, path: '/database' },
  { text: 'Logging', icon: <BugReportIcon />, path: '/logging' },
  { text: 'Deployment', icon: <CloudUploadIcon />, path: '/deployment' },
  { text: 'Documentation', icon: <DescriptionIcon />, path: '/documentation' },
];

function App() {
  const [mobileOpen, setMobileOpen] = React.useState(false);

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const drawer = (
    <div>
      <Toolbar>
        <Typography variant="h6" noWrap component="div" sx={{ color: '#00bcd4' }}>
          DevTools
        </Typography>
      </Toolbar>
      <List>
        {menuItems.map((item) => (
          <ListItem
            button
            key={item.text}
            component="a"
            href={item.path}
            onClick={() => setMobileOpen(false)}
          >
            <ListItemIcon sx={{ color: '#00bcd4' }}>{item.icon}</ListItemIcon>
            <ListItemText primary={item.text} />
          </ListItem>
        ))}
      </List>
    </div>
  );

  return (
    <Box sx={{ display: 'flex' }}>
      <AppBar
        position="fixed"
        sx={{
          width: { sm: `calc(100% - ${drawerWidth}px)` },
          ml: { sm: `${drawerWidth}px` },
          bgcolor: '#1e1e1e',
          boxShadow: 'none',
          borderBottom: '1px solid #333'
        }}
      >
        <Toolbar>
          <Typography variant="h6" noWrap component="div">
            Infrastructure DevTools
          </Typography>
        </Toolbar>
      </AppBar>
      <Box
        component="nav"
        sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
      >
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onClose={handleDrawerToggle}
          ModalProps={{ keepMounted: true }}
          sx={{
            display: { xs: 'block', sm: 'none' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth, bgcolor: '#121212' }
          }}
        >
          {drawer}
        </Drawer>
        <Drawer
          variant="permanent"
          sx={{
            display: { xs: 'none', sm: 'block' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth, bgcolor: '#121212', borderRight: '1px solid #333' }
          }}
          open
        >
          {drawer}
        </Drawer>
      </Box>
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          width: { sm: `calc(100% - ${drawerWidth}px)` },
          minHeight: '100vh',
          bgcolor: '#0d0d0d'
        }}
      >
        <Toolbar />
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/api-testing" element={<ApiTesting />} />
          <Route path="/database" element={<DatabaseQuery />} />
          <Route path="/logging" element={<Logging />} />
          <Route path="/deployment" element={<Deployment />} />
          <Route path="/documentation" element={<Documentation />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Box>
    </Box>
  );
}

export default App;
