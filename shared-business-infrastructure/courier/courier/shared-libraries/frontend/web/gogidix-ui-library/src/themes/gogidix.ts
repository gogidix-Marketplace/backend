import { createTheme } from '@mui/material/styles'

export const gogidixTheme = createTheme({
  palette: {
    primary: {
      main: '#1976d2', // Gogidix blue
      light: '#42a5f5',
      dark: '#1565c0',
    },
    secondary: {
      main: '#dc004e', // Gogidix red
      light: '#ff5983',
      dark: '#9a0036',
    },
    success: {
      main: '#2e7d32',
    },
    warning: {
      main: '#ed6c02',
    },
    error: {
      main: '#d32f2f',
    },
    background: {
      default: '#fafafa',
      paper: '#ffffff',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Arial", sans-serif',
    h1: {
      fontWeight: 300,
    },
    h2: {
      fontWeight: 400,
    },
    h3: {
      fontWeight: 400,
    },
    h4: {
      fontWeight: 500,
    },
    h5: {
      fontWeight: 500,
    },
    h6: {
      fontWeight: 500,
    },
  },
  components: {
    MuiCard: {
      styleOverrides: {
        root: {
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
          borderRadius: 12,
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          textTransform: 'none',
          fontWeight: 500,
        },
      },
    },
  },
})

export const managementTheme = createTheme({
  ...gogidixTheme,
  palette: {
    ...gogidixTheme.palette,
    primary: {
      main: '#6a1b9a', // Management purple
      light: '#9c4dcc',
      dark: '#4a148c',
    },
  },
})

export const commandCenterTheme = createTheme({
  ...gogidixTheme,
  palette: {
    mode: 'dark',
    primary: {
      main: '#ff6b35', // Command orange
      light: '#ff8a65',
      dark: '#e64a19',
    },
    secondary: {
      main: '#f39c12', // Alert yellow
      light: '#ffb74d',
      dark: '#f57c00',
    },
    background: {
      default: '#1a1a1a',
      paper: '#2d2d2d',
    },
  },
})