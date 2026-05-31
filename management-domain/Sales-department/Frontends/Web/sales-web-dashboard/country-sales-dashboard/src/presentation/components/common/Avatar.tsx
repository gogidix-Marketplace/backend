// Avatar Component
// User avatar with initials fallback

import React from 'react';
import { getInitials } from '@shared';

export interface AvatarProps {
  src?: string;
  alt?: string;
  firstName?: string;
  lastName?: string;
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
  onClick?: () => void;
}

export const Avatar: React.FC<AvatarProps> = ({
  src,
  alt,
  firstName,
  lastName,
  size = 'md',
  className = '',
  onClick,
}) => {
  const initials = firstName && lastName ? getInitials(firstName, lastName) : '?';

  const classes = [
    'avatar',
    `avatar-${size}`,
    onClick ? 'avatar-clickable' : '',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  if (src) {
    return (
      <img
        className={classes}
        src={src}
        alt={alt || `${firstName} ${lastName}`}
        onClick={onClick}
      />
    );
  }

  return (
    <div className={classes} onClick={onClick}>
      <span className="avatar-initials">{initials}</span>
    </div>
  );
};

export default Avatar;
