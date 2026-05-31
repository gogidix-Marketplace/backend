import React from 'react';

export const Loading = ({ size = 'md', text }: { size?: 'sm' | 'md' | 'lg'; text?: string }) => {
  return (
    <div className="flex flex-col items-center justify-center gap-4">
      <div
        className={`animate-spin rounded-full border-4 border-muted border-t-primary ${
          size === 'sm' ? 'h-4 w-4 border-2' : size === 'lg' ? 'h-12 w-12' : 'h-8 w-8'
        }`}
      />
      {text && <p className="text-sm text-muted-foreground animate-pulse">{text}</p>}
      </div>
  );
};
