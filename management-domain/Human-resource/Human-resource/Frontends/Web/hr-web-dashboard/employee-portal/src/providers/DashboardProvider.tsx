'use client';

import React, { createContext, useContext, useState, ReactNode } from 'react';

interface DashboardContextType {
  selectedDepartment: string | null;
  setSelectedDepartment: (department: string | null) => void;
  refreshKey: number;
  triggerRefresh: () => void;
}

const DashboardContext = createContext<DashboardContextType | undefined>(undefined);

export const useDashboard = () => {
  const context = useContext(DashboardContext);
  if (!context) {
    throw new Error('useDashboard must be used within DashboardProvider');
  }
  return context;
};

interface DashboardProviderProps {
  children: ReactNode;
}

export const DashboardProvider: React.FC<DashboardProviderProps> = ({ children }) => {
  const [selectedDepartment, setSelectedDepartment] = useState<string | null>(null);
  const [refreshKey, setRefreshKey] = useState(0);

  const triggerRefresh = () => {
    setRefreshKey((prev) => prev + 1);
  };

  return (
    <DashboardContext.Provider
      value={{
        selectedDepartment,
        setSelectedDepartment,
        refreshKey,
        triggerRefresh,
      }}
    >
      {children}
    </DashboardContext.Provider>
  );
};
