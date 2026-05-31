import React, { useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import {
  Box,
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Divider,
  Avatar,
  Menu,
  MenuItem,
  Badge,
  Collapse,
} from '@mui/material';
import {
  Menu as MenuIcon,
  Dashboard,
  Article,
  ShoppingBag,
  Code,
  Work,
  Handshake,
  Inbox,
  Analytics,
  Settings,
  ExpandLess,
  ExpandMore,
  Notifications,
  ChevronLeft,
  Logout,
  Person,
} from '@mui/icons-material';
import { useAuth } from '@/hooks/useAuth';
import { useUiStore } from '@/stores/uiStore';
import { useResponsive } from '@/hooks/useMediaQuery';

const drawerWidth = 280;

const MainLayout: React.FC = () => {
  const { user, logout, hasPermission } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const { isMobile } = useResponsive();
  const { sidebarOpen, mobileSidebarOpen, setMobileSidebarOpen } = useUiStore();

  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [openMenus, setOpenMenus] = useState<Record<string, boolean>>({});

  const handleProfileMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleProfileMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = async () => {
    handleProfileMenuClose();
    await logout();
  };

  const toggleMenu = (menuKey: string) => {
    setOpenMenus((prev) => ({ ...prev, [menuKey]: !prev[menuKey] }));
  };

  const menuItems = [
    {
      key: 'dashboard',
      label: 'Dashboard',
      icon: <Dashboard />,
      path: '/dashboard',
      permission: 'analytics:read' as const,
    },
    {
      key: 'content',
      label: 'Content Management',
      icon: <Article />,
      permission: 'content:read' as const,
      children: [
        { label: 'Pages', path: '/content/pages' },
        { label: 'Blog', path: '/content/blog' },
        { label: 'Press Releases', path: '/content/press' },
        { label: 'Resources', path: '/content/resources' },
      ],
    },
    {
      key: 'products',
      label: 'Product Catalog',
      icon: <ShoppingBag />,
      permission: 'products:read' as const,
      children: [
        { label: 'Products', path: '/products/list' },
        { label: 'Categories', path: '/products/categories' },
        { label: 'Features', path: '/products/features' },
        { label: 'Pricing', path: '/products/pricing' },
        { label: 'Integrations', path: '/products/integrations' },
      ],
    },
    {
      key: 'developer',
      label: 'Developer Resources',
      icon: <Code />,
      permission: 'content:read' as const,
      children: [
        { label: 'API Documentation', path: '/developer/api-docs' },
        { label: 'SDKs', path: '/developer/sdks' },
        { label: 'Code Examples', path: '/developer/examples' },
      ],
    },
    {
      key: 'careers',
      label: 'Careers',
      icon: <Work />,
      permission: 'careers:read' as const,
      children: [
        { label: 'Jobs', path: '/careers/jobs' },
        { label: 'Applications', path: '/careers/applications' },
        { label: 'Pipeline', path: '/careers/pipeline' },
      ],
    },
    {
      key: 'partners',
      label: 'Partners',
      icon: <Handshake />,
      permission: 'partners:read' as const,
      children: [
        { label: 'Programs', path: '/partners/programs' },
        { label: 'Applications', path: '/partners/applications' },
        { label: 'Portal', path: '/partners/portal' },
      ],
    },
    {
      key: 'leads',
      label: 'Leads',
      icon: <Inbox />,
      permission: 'leads:read' as const,
      children: [
        { label: 'Demo Requests', path: '/leads/demo-requests' },
        { label: 'Sales Inquiries', path: '/leads/sales-inquiries' },
        { label: 'Support Tickets', path: '/leads/support-tickets' },
      ],
    },
    {
      key: 'analytics',
      label: 'Analytics',
      icon: <Analytics />,
      permission: 'analytics:read' as const,
      children: [
        { label: 'Site Analytics', path: '/analytics/site' },
        { label: 'User Behavior', path: '/analytics/behavior' },
        { label: 'Conversion Funnels', path: '/analytics/funnels' },
        { label: 'SEO Performance', path: '/analytics/seo' },
      ],
    },
    {
      key: 'settings',
      label: 'Settings',
      icon: <Settings />,
      permission: 'settings:read' as const,
      children: [
        { label: 'General', path: '/settings/general' },
        { label: 'Users & Permissions', path: '/settings/users' },
        { label: 'Workflows', path: '/settings/workflows' },
        { label: 'Integrations', path: '/settings/integrations' },
      ],
    },
  ];

  const isMenuOpen = (menuKey: string) => {
    return !!openMenus[menuKey];
  };

  const isActivePath = (path: string) => {
    return location.pathname === path || location.pathname.startsWith(path + '/');
  };

  const renderMenuItem = (item: any) => {
    if (item.permission && !hasPermission(item.permission)) {
      return null;
    }

    if (item.children) {
      const hasActiveChild = item.children.some((child: any) => isActivePath(child.path));
      return (
        <div key={item.key}>
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => toggleMenu(item.key)}
              selected={hasActiveChild}
            >
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.label} />
              {isMenuOpen(item.key) ? <ExpandLess /> : <ExpandMore />}
            </ListItemButton>
          </ListItem>
          <Collapse in={isMenuOpen(item.key)} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
              {item.children.map((child: any) => (
                <ListItemButton
                  key={child.path}
                  sx={{ pl: 4 }}
                  onClick={() => {
                    navigate(child.path);
                    if (isMobile) setMobileSidebarOpen(false);
                  }}
                  selected={isActivePath(child.path)}
                >
                  <ListItemText primary={child.label} />
                </ListItemButton>
              ))}
            </List>
          </Collapse>
        </div>
      );
    }

    return (
      <ListItem key={item.path} disablePadding>
        <ListItemButton
          onClick={() => {
            navigate(item.path);
            if (isMobile) setMobileSidebarOpen(false);
          }}
          selected={isActivePath(item.path)}
        >
          <ListItemIcon>{item.icon}</ListItemIcon>
          <ListItemText primary={item.label} />
        </ListItemButton>
      </ListItem>
    );
  };

  const drawer = (
    <>
      <Toolbar
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          px: [1],
        }}
      >
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <img
            src="/logo.svg"
            alt="Gogidix"
            style={{ height: 32, marginRight: 8 }}
          />
          <Typography variant="h6" noWrap component="div">
            Admin
          </Typography>
        </Box>
        {isMobile && (
          <IconButton onClick={() => setMobileSidebarOpen(false)}>
            <ChevronLeft />
          </IconButton>
        )}
      </Toolbar>
      <Divider />
      <List component="nav" sx={{ px: 1 }}>
        {menuItems.map(renderMenuItem)}
      </List>
    </>
  );

  return (
    <Box sx={{ display: 'flex' }}>
      <AppBar
        position="fixed"
        elevation={0}
        sx={{
          width: { sm: `calc(100% - ${sidebarOpen ? drawerWidth : 0}px)` },
          ml: { sm: `${sidebarOpen ? drawerWidth : 0}px` },
          bgcolor: 'background.paper',
          borderBottom: '1px solid',
          borderColor: 'divider',
          color: 'text.primary',
        }}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            edge="start"
            onClick={() => (isMobile ? setMobileSidebarOpen(true) : useUiStore.getState().toggleSidebar())}
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
            {menuItems
              .flatMap((item) => [
                item,
                ...(item.children || []),
              ])
              .find((item) => item.path && isActivePath(item.path))?.label || 'Dashboard'}
          </Typography>
          <IconButton color="inherit" sx={{ mr: 1 }}>
            <Badge badgeContent={3} color="error">
              <Notifications />
            </Badge>
          </IconButton>
          <IconButton
            onClick={handleProfileMenuOpen}
            sx={{ p: 0 }}
          >
            <Avatar
              src={user?.avatar}
              alt={`${user?.firstName} ${user?.lastName}`}
            >
              {user?.firstName?.[0]}{user?.lastName?.[0]}
            </Avatar>
          </IconButton>
        </Toolbar>
      </AppBar>

      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleProfileMenuClose}
        onClick={handleProfileMenuClose}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      >
        <MenuItem onClick={() => navigate('/settings/general')}>
          <ListItemIcon>
            <Person fontSize="small" />
          </ListItemIcon>
          Profile
        </MenuItem>
        <MenuItem onClick={handleLogout}>
          <ListItemIcon>
            <Logout fontSize="small" />
          </ListItemIcon>
          Logout
        </MenuItem>
      </Menu>

      <Box
        component="nav"
        sx={{ width: { sm: sidebarOpen ? drawerWidth : 0 }, flexShrink: { sm: 0 } }}
      >
        <Drawer
          variant={isMobile ? 'temporary' : 'persistent'}
          open={isMobile ? mobileSidebarOpen : sidebarOpen}
          onClose={() => setMobileSidebarOpen(false)}
          ModalProps={{ keepMounted: true }}
          sx={{
            '& .MuiDrawer-paper': {
              boxSizing: 'border-box',
              width: drawerWidth,
            },
          }}
        >
          {drawer}
        </Drawer>
      </Box>

      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          width: { sm: `calc(100% - ${sidebarOpen ? drawerWidth : 0}px)` },
          minHeight: '100vh',
          bgcolor: 'background.default',
        }}
      >
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  );
};

export default MainLayout;
