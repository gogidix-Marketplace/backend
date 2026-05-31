'use client';

import React, { createContext, useContext, useState, ReactNode } from 'react';

interface DashboardContextType {
  selectedCountry: string | null;
  setSelectedCountry: (country: string | null) => void;
  selectedDateRange: { start: Date; end: Date };
  setSelectedDateRange: (range: { start: Date; end: Date }) => void;
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
  const [selectedCountry, setSelectedCountry] = useState<string | null>(null);
  const [selectedDateRange, setSelectedDateRange] = useState({
    start: new Date(Date.now() - 90 * 24 * 60 * 60 * 1000), // 90 days ago
    end: new Date(),
  });
  const [refreshKey, setRefreshKey] = useState(0);

  const triggerRefresh = () => {
    setRefreshKey((prev) => prev + 1);
  };

  return (
    <DashboardContext.Provider
      value={{
        selectedCountry,
        setSelectedCountry,
        selectedDateRange,
        setSelectedDateRange,
        refreshKey,
        triggerRefresh,
      }}
    >
      {children}
    </DashboardContext.Provider>
  );
};
