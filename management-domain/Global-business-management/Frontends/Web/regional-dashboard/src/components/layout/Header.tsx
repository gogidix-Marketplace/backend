import React from 'react';
import { ArrowLeft, Home, BarChart3, Moon, Sun, Settings } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useRegionalStore } from '../../stores/regional-store';
import { Button } from '../common/Button';

export const Header = () => {
  const navigate = useNavigate();
  const { selectedRegion, selectedCountry, darkMode, setDarkMode, drillDownLevel, setSelectedRegion, setSelectedCountry } = useRegionalStore();

  const handleBack = () => {
    if (drillDownLevel === 'country') {
      setSelectedCountry(null);
    } else if (drillDownLevel === 'regional') {
      setSelectedRegion(null);
    }
  };

  return (
    <header className="sticky top-0 z-30 w-full border-b bg-background/95 backdrop-blur">
      <div className="flex h-16 items-center px-4 gap-4">
        {/* Breadcrumb / Back */}
        <div className="flex items-center gap-2">
          <Button variant="ghost" size="icon" onClick={() => navigate('/')}>
            <Home className="h-5 w-5" />
          </Button>
          {drillDownLevel !== 'global' && (
            <>
              <span className="text-muted-foreground">/</span>
              <Button variant="ghost" size="sm" onClick={handleBack} className="gap-1">
                <ArrowLeft className="h-4 w-4" />
                Back
              </Button>
            </>
          )}
          {selectedRegion && (
            <>
              <span className="text-muted-foreground">/</span>
              <span className="font-medium">{selectedRegion}</span>
            </>
          )}
          {selectedCountry && (
            <>
              <span className="text-muted-foreground">/</span>
              <span className="font-medium">{selectedCountry}</span>
            </>
          )}
        </div>

        {/* Title */}
        <div className="flex-1">
          <h1 className="text-lg font-semibold">
            {drillDownLevel === 'global' && 'Regional Dashboard'}
            {drillDownLevel === 'regional' && `${selectedRegion} Overview`}
            {drillDownLevel === 'country' && `${selectedCountry} Details`}
          </h1>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-2">
          <Button variant="ghost" size="icon" onClick={() => setDarkMode(!darkMode)}>
            {darkMode ? <Sun className="h-5 w-5" /> : <Moon className="h-5 w-5" />}
          </Button>
          <Button variant="ghost" size="icon">
            <Settings className="h-5 w-5" />
          </Button>
        </div>
      </div>
    </header>
  );
};
