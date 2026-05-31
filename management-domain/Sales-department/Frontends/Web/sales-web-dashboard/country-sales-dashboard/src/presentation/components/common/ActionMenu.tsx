// ActionMenu Component
// Context menu for actions on list items

import React, { useState, useRef, useEffect } from 'react';

export interface ActionItem {
  id: string;
  label: string;
  icon?: string;
  onClick: () => void;
  disabled?: boolean;
  destructive?: boolean;
  divider?: boolean;
}

export interface ActionMenuProps {
  trigger?: React.ReactNode;
  actions: ActionItem[];
  placement?: 'bottom-start' | 'bottom-end' | 'top-start' | 'top-end';
  disabled?: boolean;
  className?: string;
}

export const ActionMenu: React.FC<ActionMenuProps> = ({
  trigger,
  actions,
  placement = 'bottom-end',
  disabled = false,
  className = '',
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target as Node)
      ) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleToggle = () => {
    if (!disabled) {
      setIsOpen(!isOpen);
    }
  };

  const handleActionClick = (action: ActionItem) => {
    if (!action.disabled) {
      action.onClick();
      setIsOpen(false);
    }
  };

  const defaultTrigger = (
    <button
      type="button"
      className="action-menu-trigger"
      onClick={handleToggle}
      disabled={disabled}
      aria-label="Actions"
    >
      \u22EE
    </button>
  );

  return (
    <div
      ref={containerRef}
      className={`action-menu ${isOpen ? 'action-menu-open' : ''} ${className}`}
    >
      <div onClick={handleToggle}>
        {trigger || defaultTrigger}
      </div>

      {isOpen && (
        <div
          ref={menuRef}
          className={`action-menu-dropdown action-menu-dropdown-${placement}`}
        >
          {actions.map((action, index) => (
            <React.Fragment key={action.id}>
              {action.divider && index > 0 && (
                <div className="action-menu-divider" />
              )}
              <button
                type="button"
                className={`action-menu-item ${
                  action.destructive ? 'action-menu-item-destructive' : ''
                } ${action.disabled ? 'action-menu-item-disabled' : ''}`}
                onClick={() => handleActionClick(action)}
                disabled={action.disabled}
              >
                {action.icon && (
                  <span className="action-menu-item-icon">{action.icon}</span>
                )}
                <span className="action-menu-item-label">{action.label}</span>
              </button>
            </React.Fragment>
          ))}
        </div>
      )}
    </div>
  );
};

// Dropdown button variant
export interface DropdownButtonProps {
  label: string;
  actions: ActionItem[];
  variant?: 'primary' | 'secondary' | 'outline';
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  icon?: string;
  className?: string;
}

export const DropdownButton: React.FC<DropdownButtonProps> = ({
  label,
  actions,
  variant = 'secondary',
  size = 'md',
  disabled = false,
  icon,
  className = '',
}) => {
  return (
    <ActionMenu
      actions={actions}
      disabled={disabled}
      className={`dropdown-button dropdown-button-${variant} dropdown-button-${size} ${className}`}
      trigger={
        <button
          type="button"
          className={`dropdown-button-trigger btn btn-${variant} btn-${size}`}
          disabled={disabled}
        >
          {icon && <span className="dropdown-button-icon">{icon}</span>}
          <span className="dropdown-button-label">{label}</span>
          <span className="dropdown-button-chevron">\u25BC</span>
        </button>
      }
    />
  );
};

export default ActionMenu;
