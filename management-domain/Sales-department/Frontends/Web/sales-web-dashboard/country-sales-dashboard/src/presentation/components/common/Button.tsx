// Button Component
// Reusable button component with variants and sizes

import React from 'react';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'danger' | 'success';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
  fullWidth?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
}

export const Button: React.FC<ButtonProps> = ({
  children,
  variant = 'primary',
  size = 'md',
  isLoading = false,
  fullWidth = false,
  leftIcon,
  rightIcon,
  disabled,
  className = '',
  ...props
}) => {
  const baseClasses = 'button';
  const variantClasses = {
    primary: 'button-primary',
    secondary: 'button-secondary',
    outline: 'button-outline',
    ghost: 'button-ghost',
    danger: 'button-danger',
    success: 'button-success',
  };
  const sizeClasses = {
    sm: 'button-sm',
    md: 'button-md',
    lg: 'button-lg',
  };

  const classes = [
    baseClasses,
    variantClasses[variant],
    sizeClasses[size],
    fullWidth ? 'button-full-width' : '',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <button
      className={classes}
      disabled={disabled || isLoading}
      {...props}
    >
      {isLoading ? (
        <>
          <span className="button-spinner" />
          <span className="button-text">{children}</span>
        </>
      ) : (
        <>
          {leftIcon && <span className="button-icon-left">{leftIcon}</span>}
          <span className="button-text">{children}</span>
          {rightIcon && <span className="button-icon-right">{rightIcon}</span>}
        </>
      )}
    </button>
  );
};

export default Button;
