// Button Component
// Reusable button with multiple variants

import React from 'react';
import './Button.css';

export type ButtonVariant =
  | 'primary'
  | 'secondary'
  | 'success'
  | 'danger'
  | 'warning'
  | 'ghost'
  | 'link';

export type ButtonSize = 'sm' | 'md' | 'lg';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
  size?: ButtonSize;
  loading?: boolean;
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right';
  fullWidth?: boolean;
  children: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  size = 'md',
  loading = false,
  icon,
  iconPosition = 'left',
  fullWidth = false,
  disabled,
  children,
  className = '',
  ...props
}) => {
  const classes = [
    'btn',
    `btn--${variant}`,
    `btn--${size}`,
    fullWidth && 'btn--full-width',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  const isDisabled = disabled || loading;

  return (
    <button
      className={classes}
      disabled={isDisabled}
      {...props}
    >
      {loading && (
        <span className="btn__spinner" aria-hidden="true">
          <svg
            className="btn__spinner-svg"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <circle
              className="btn__spinner-circle"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              strokeWidth="4"
              fill="none"
            />
          </svg>
        </span>
      )}
      {!loading && icon && iconPosition === 'left' && (
        <span className="btn__icon btn__icon--left">{icon}</span>
      )}
      <span className="btn__content">{children}</span>
      {!loading && icon && iconPosition === 'right' && (
        <span className="btn__icon btn__icon--right">{icon}</span>
      )}
    </button>
  );
};

export interface ButtonGroupProps {
  children: React.ReactNode;
  className?: string;
  vertical?: boolean;
}

export const ButtonGroup: React.FC<ButtonGroupProps> = ({
  children,
  className = '',
  vertical = false,
}) => {
  const classes = [
    'btn-group',
    vertical && 'btn-group--vertical',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div className={classes}>
      {React.Children.map(children, (child, index) => {
        if (React.isValidElement(child)) {
          return (
            <React.cloneElement(child, {
              className: `btn-group__btn ${child.props.className || ''}`,
            } as React.HTMLAttributes<HTMLButtonElement>>
            </React.cloneElement>
          );
        }
        return child;
      })}
    </div>
  );
};

export default Button;
