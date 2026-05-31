// Tabs Component
// Tab navigation component

import React, { useRef, useEffect } from 'react';
import './Tabs.css';

export interface Tab {
  id: string;
  label: string;
  icon?: React.ReactNode;
  badge?: number | string;
  disabled?: boolean;
  content?: React.ReactNode;
}

export interface TabsProps {
  tabs: Tab[];
  activeTab: string;
  onChange: (tabId: string) => void;
  variant?: 'default' | 'pills' | 'underlined' | 'segmented';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  fullWidth?: boolean;
}

export const Tabs: React.FC<TabsProps> = ({
  tabs,
  activeTab,
  onChange,
  variant = 'default',
  size = 'md',
  className = '',
  fullWidth = false,
}) => {
  const activeIndicatorRef = useRef<HTMLDivElement>(null);
  const tabsRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (variant === 'underlined' && activeIndicatorRef.current && tabsRef.current) {
      const activeTabElement = tabsRef.current.querySelector(`[data-tab-id="${activeTab}"]`) as HTMLElement;
      if (activeTabElement) {
        activeIndicatorRef.current.style.width = `${activeTabElement.offsetWidth}px`;
        activeIndicatorRef.current.style.left = `${activeTabElement.offsetLeft}px`;
      }
    }
  }, [activeTab, variant, tabs]);

  const handleKeyDown = (e: React.KeyboardEvent, tabId: string) => {
    const currentIndex = tabs.findIndex(t => t.id === activeTab);
    let newIndex = currentIndex;

    switch (e.key) {
      case 'ArrowLeft':
        newIndex = Math.max(0, currentIndex - 1);
        break;
      case 'ArrowRight':
        newIndex = Math.min(tabs.length - 1, currentIndex + 1);
        break;
      case 'Home':
        newIndex = 0;
        break;
      case 'End':
        newIndex = tabs.length - 1;
        break;
      default:
        return;
    }

    if (newIndex !== currentIndex && !tabs[newIndex].disabled) {
      onChange(tabs[newIndex].id);
      tabsRef.current?.querySelector<HTMLButtonElement>(`[data-tab-id="${tabs[newIndex].id}"]`)?.focus();
    }
  };

  return (
    <div className={`tabs-container ${className}`}>
      <div
        role="tablist"
        className={`tabs tabs--${variant} tabs--${size} ${fullWidth ? 'tabs--full-width' : ''}`}
        ref={tabsRef}
      >
        {tabs.map((tab) => (
          <button
            key={tab.id}
            type="button"
            role="tab"
            data-tab-id={tab.id}
            aria-selected={activeTab === tab.id}
            aria-disabled={tab.disabled}
            disabled={tab.disabled}
            className={`tab ${activeTab === tab.id ? 'tab--active' : ''} ${tab.disabled ? 'tab--disabled' : ''}`}
            onClick={() => !tab.disabled && onChange(tab.id)}
            onKeyDown={(e) => handleKeyDown(e, tab.id)}
          >
            {tab.icon && <span className="tab__icon">{tab.icon}</span>}
            <span className="tab__label">{tab.label}</span>
            {tab.badge !== undefined && (
              <span className="tab__badge">
                {typeof tab.badge === 'number' && tab.badge > 99 ? '99+' : tab.badge}
              </span>
            )}
          </button>
        ))}
        {variant === 'underlined' && (
          <div
            ref={activeIndicatorRef}
            className="tabs__indicator"
            aria-hidden="true"
          />
        )}
      </div>
    </div>
  );
};

export interface TabPanelProps {
  id: string;
  activeTab: string;
  children: React.ReactNode;
  className?: string;
}

export const TabPanel: React.FC<TabPanelProps> = ({
  id,
  activeTab,
  children,
  className = '',
}) => {
  if (id !== activeTab) return null;

  return (
    <div
      role="tabpanel"
      id={`panel-${id}`}
      aria-labelledby={`tab-${id}`}
      className={`tab-panel ${className}`}
    >
      {children}
    </div>
  );
};

export default Tabs;
