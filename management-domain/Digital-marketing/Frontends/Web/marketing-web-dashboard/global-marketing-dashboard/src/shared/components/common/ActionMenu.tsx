// ActionMenu Component
// Dropdown menu for contextual actions

import React, { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';
import './ActionMenu.css';

export interface ActionItem {
  id: string;
  label: string;
  icon?: React.ReactNode;
  danger?: boolean;
  disabled?: boolean;
  divider?: boolean;
  onClick?: () => void;
}

export interface ActionMenuProps {
  trigger: React.ReactNode;
  actions: ActionItem[];
  align?: 'left' | 'right';
  className?: string;
}

export const ActionMenu: React.FC<ActionMenuProps> = ({
  trigger,
  actions,
  align = 'left',
  className = '',
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [position, setPosition] = useState({ top: 0, left: 0 });
  const triggerRef = useRef<HTMLDivElement>(null);
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (isOpen && triggerRef.current) {
      const rect = triggerRef.current.getBoundingClientRect();
      const menuHeight = 200; // Approximate height
      const menuWidth = 180;

      let top = rect.bottom + 4;
      let left = align === 'left' ? rect.left : rect.right - menuWidth;

      // Prevent overflow on bottom edge
      if (top + menuHeight > window.innerHeight) {
        top = rect.top - menuHeight - 4;
      }

      // Prevent overflow on right edge
      if (left + menuWidth > window.innerWidth) {
        left = window.innerWidth - menuWidth - 8;
      }

      setPosition({ top, left });
    }
  }, [isOpen, align]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        menuRef.current &&
        !menuRef.current.contains(event.target as Node) &&
        !triggerRef.current?.contains(event.target as Node)
      ) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside);
      return () => document.removeEventListener('mousedown', handleClickOutside);
    }
  }, [isOpen]);

  const handleActionClick = (action: ActionItem) => {
    if (!action.disabled) {
      action.onClick?.();
      setIsOpen(false);
    }
  };

  return (
    <>
      <div
        ref={triggerRef}
        className={`action-menu-trigger ${className}`}
        onClick={() => setIsOpen(!isOpen)}
      >
        {trigger}
      </div>

      {isOpen &&
        createPortal(
          <div
            ref={menuRef}
            className="action-menu"
            style={{
              position: 'fixed',
              top: `${position.top}px`,
              left: `${position.left}px`,
              zIndex: 1000,
            }}
          >
            <div className="action-menu__list">
              {actions.map((action, index) => (
                <React.Fragment key={action.id}>
                  {action.divider && <div className="action-menu__divider" />}
                  <button
                    type="button"
                    className={`action-menu__item ${action.danger ? 'action-menu__item--danger' : ''} ${
                      action.disabled ? 'action-menu__item--disabled' : ''
                    }`}
                    onClick={() => handleActionClick(action)}
                    disabled={action.disabled}
                  >
                    {action.icon && <span className="action-menu__icon">{action.icon}</span>}
                    <span className="action-menu__label">{action.label}</span>
                  </button>
                </React.Fragment>
              ))}
            </div>
          </div>,
          document.body
        )}
    </>
  );
};

export default ActionMenu;
