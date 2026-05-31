// Tabs Component
// Provides tab navigation interface

import { Children, cloneElement, isValidElement, useState, type React, type ReactElement } from 'react';
import './Tabs.css';

export interface TabProps {
  id: string;
  label: string;
  icon?: React.ReactNode;
  disabled?: boolean;
  badge?: string | number;
  children: React.ReactNode;
}

export interface TabsProps {
  children: React.ReactNode;
  defaultTab?: string;
  activeTab?: string;
  onChange?: (tabId: string) => void;
  variant?: 'default' | 'pills' | 'underlined' | 'enclosed';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  fullWidth?: boolean;
}

export function Tab({ children }: TabProps): React.ReactElement {
  return <>{children}</>;
}

export function Tabs({
  children,
  defaultTab,
  activeTab: controlledActiveTab,
  onChange,
  variant = 'default',
  size = 'md',
  className = '',
  fullWidth = false,
}: TabsProps): React.ReactElement {
  const [internalActiveTab, setInternalActiveTab] = useState(defaultTab);

  const activeTab = controlledActiveTab !== undefined ? controlledActiveTab : internalActiveTab;

  const handleTabClick = (tabId: string) => {
    if (controlledActiveTab === undefined) {
      setInternalActiveTab(tabId);
    }
    onChange?.(tabId);
  };

  // Extract tab props from children
  const tabs: ReactElement<TabProps>[] = [];
  const panels: React.ReactNode[] = [];

  Children.forEach(children, (child) => {
    if (isValidElement<TabProps>(child) && child.type === Tab) {
      tabs.push(child);
      panels.push(child.props.children);
    }
  });

  const activePanel = panels[tabs.findIndex(tab => tab.props.id === activeTab)] || panels[0];

  return (
    <div className={`tabs tabs-${variant} tabs-${size} ${fullWidth ? 'tabs-full-width' : ''} ${className}`}>
      {/* Tab Headers */}
      <div className="tabs-header" role="tablist">
        {tabs.map((tab) => {
          const isActive = tab.props.id === activeTab;
          const isDisabled = tab.props.disabled;

          return (
            <button
              key={tab.props.id}
              type="button"
              className={`tabs-tab ${isActive ? 'tabs-tab-active' : ''} ${isDisabled ? 'tabs-tab-disabled' : ''}`}
              role="tab"
              aria-selected={isActive}
              aria-disabled={isDisabled}
              disabled={isDisabled}
              onClick={() => !isDisabled && handleTabClick(tab.props.id)}
            >
              {tab.props.icon && <span className="tabs-tab-icon">{tab.props.icon}</span>}
              <span className="tabs-tab-label">{tab.props.label}</span>
              {tab.props.badge && (
                <span className="tabs-tab-badge">{tab.props.badge}</span>
              )}
            </button>
          );
        })}
      </div>

      {/* Tab Panels */}
      <div className="tabs-content" role="tabpanel">
        {activePanel}
      </div>
    </div>
  );
}

// Tabs with vertical orientation
export interface VerticalTabsProps extends Omit<TabsProps, 'variant'> {
  position?: 'left' | 'right';
  tabWidth?: number;
}

export function VerticalTabs({
  children,
  position = 'left',
  tabWidth = 200,
  ...rest
}: VerticalTabsProps): React.ReactElement {
  const [internalActiveTab, setInternalActiveTab] = useState(rest.defaultTab);
  const activeTab = rest.activeTab !== undefined ? rest.activeTab : internalActiveTab;

  const handleTabClick = (tabId: string) => {
    if (rest.activeTab === undefined) {
      setInternalActiveTab(tabId);
    }
    rest.onChange?.(tabId);
  };

  const tabs: ReactElement<TabProps>[] = [];
  const panels: React.ReactNode[] = [];

  Children.forEach(children, (child) => {
    if (isValidElement<TabProps>(child) && child.type === Tab) {
      tabs.push(child);
      panels.push(child.props.children);
    }
  });

  const activePanel = panels[tabs.findIndex(tab => tab.props.id === activeTab)] || panels[0];

  return (
    <div className={`tabs tabs-vertical tabs-vertical-${position} ${rest.className || ''}`}>
      <div className="tabs-sidebar" style={{ width: `${tabWidth}px` }}>
        {tabs.map((tab) => {
          const isActive = tab.props.id === activeTab;
          const isDisabled = tab.props.disabled;

          return (
            <button
              key={tab.props.id}
              type="button"
              className={`tabs-vertical-tab ${isActive ? 'tabs-vertical-tab-active' : ''} ${isDisabled ? 'tabs-vertical-tab-disabled' : ''}`}
              disabled={isDisabled}
              onClick={() => !isDisabled && handleTabClick(tab.props.id)}
            >
              {tab.props.icon && <span className="tabs-tab-icon">{tab.props.icon}</span>}
              <span className="tabs-tab-label">{tab.props.label}</span>
              {tab.props.badge && (
                <span className="tabs-tab-badge">{tab.props.badge}</span>
              )}
            </button>
          );
        })}
      </div>
      <div className="tabs-content-vertical">
        {activePanel}
      </div>
    </div>
  );
}

export default Tabs;
