import { AlertCircle } from 'lucide-react';
import { cn } from '../../shared/utils';

interface ErrorAlertProps {
  message: string;
  onDismiss?: () => void;
  className?: string;
}

export function ErrorAlert({ message, onDismiss, className }: ErrorAlertProps) {
  return (
    <div className={cn('bg-danger-50 border border-danger-200 rounded-lg p-4', className)}>
      <div className="flex items-start gap-3">
        <AlertCircle className="w-5 h-5 text-danger-600 flex-shrink-0 mt-0.5" />
        <div className="flex-1">
          <h3 className="text-sm font-medium text-danger-900">Error</h3>
          <p className="text-sm text-danger-700 mt-1">{message}</p>
        </div>
        {onDismiss && (
          <button
            onClick={onDismiss}
            className="text-danger-400 hover:text-danger-600 transition-colors"
          >
            <span className="sr-only">Dismiss</span>
            ×
          </button>
        )}
      </div>
    </div>
  );
}
