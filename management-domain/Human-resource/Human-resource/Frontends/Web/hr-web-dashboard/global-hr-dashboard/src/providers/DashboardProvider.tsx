'use client';

import React, { createContext, useContext, useState, ReactNode } from 'react';

interface DashboardContextType {
  selectedRegion: string | null;
  setSelectedRegion: (region: string | null) => void;
  selectedCountries: string[];
  setSelectedCountries: (countries: string[]) => void;
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
  const [selectedRegion, setSelectedRegion] = useState<string | null>(null);
  const [selectedCountries, setSelectedCountries] = useState<string[]>([]);
  const [selectedDateRange, setSelectedDateRange] = useState({
    start: new Date(Date.now() - 90 * 24 * 60 * 60 * 1000),
    end: new Date(),
  });
  const [refreshKey, setRefreshKey] = useState(0);

  const triggerRefresh = () => {
    setRefreshKey((prev) => prev + 1);
  };

  return (
    <DashboardContext.Provider
      value={{
        selectedRegion,
        setSelectedRegion,
        selectedCountries,
        setSelectedCountries,
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
