import React, { createContext, useContext, useState, useCallback, ReactNode } from 'react';
import { Snackbar, Alert, AlertColor } from '@mui/material';

interface SnackbarMessage {
  message: string;
  severity: AlertColor;
}

interface SnackbarContextType {
  showMessage: (message: string, severity?: AlertColor) => void;
  showSuccess: (message: string) => void;
  showError: (message: string) => void;
  showWarning: (message: string) => void;
  showInfo: (message: string) => void;
}

const SnackbarContext = createContext<SnackbarContextType | undefined>(undefined);

export const useSnackbar = () => {
  const context = useContext(SnackbarContext);
  if (!context) {
    throw new Error('useSnackbar must be used within SnackbarProvider');
  }
  return context;
};

interface SnackbarProviderProps {
  children: ReactNode;
}

export const SnackbarProvider: React.FC<SnackbarProviderProps> = ({ children }) => {
  const [snackbar, setSnackbar] = useState<SnackbarMessage | null>(null);
  const [open, setOpen] = useState(false);

  const showMessage = useCallback((message: string, severity: AlertColor = 'info') => {
    setSnackbar({ message, severity });
    setOpen(true);
  }, []);

  const showSuccess = useCallback((message: string) => showMessage(message, 'success'), [showMessage]);
  const showError = useCallback((message: string) => showMessage(message, 'error'), [showMessage]);
  const showWarning = useCallback((message: string) => showMessage(message, 'warning'), [showMessage]);
  const showInfo = useCallback((message: string) => showMessage(message, 'info'), [showMessage]);

  const handleClose = () => {
    setOpen(false);
  };

  const value: SnackbarContextType = {
    showMessage,
    showSuccess,
    showError,
    showWarning,
    showInfo,
  };

  return (
    <SnackbarContext.Provider value={value}>
      {children}
      <Snackbar
        open={open}
        autoHideDuration={6000}
        onClose={handleClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert onClose={handleClose} severity={snackbar?.severity} sx={{ width: '100%' }}>
          {snackbar?.message}
        </Alert>
      </Snackbar>
    </SnackbarContext.Provider>
  );
};
