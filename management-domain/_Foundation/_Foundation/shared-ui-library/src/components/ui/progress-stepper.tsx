import * as React from 'react'
import { Check, Loader2 } from 'lucide-react'
import { cn } from '@lib/utils'

export interface Step {
  id: string
  title: string
  description?: string
  status?: 'pending' | 'in_progress' | 'completed' | 'error'
  icon?: React.ReactNode
}

export interface ProgressStepperProps {
  steps: Step[]
  currentStep?: number
  onStepClick?: (stepIndex: number) => void
  orientation?: 'horizontal' | 'vertical'
  size?: 'sm' | 'md' | 'lg'
  className?: string
}

export function ProgressStepper({
  steps,
  currentStep = 0,
  onStepClick,
  orientation = 'horizontal',
  size = 'md',
  className,
}: ProgressStepperProps) {
  const isHorizontal = orientation === 'horizontal'

  const containerClasses = cn(
    'flex',
    isHorizontal ? 'flex-row' : 'flex-col',
    className
  )

  const sizeClasses = {
    sm: 'h-6 w-6 text-xs',
    md: 'h-8 w-8 text-sm',
    lg: 'h-10 w-10 text-base',
  }

  return (
    <div className={containerClasses}>
      {steps.map((step, index) => {
        const isCurrent = index === currentStep
        const isCompleted = index < currentStep
        const isPending = index > currentStep
        const isClickable = onStepClick && (isCompleted || (isPending && index === currentStep + 1))

        return (
          <React.Fragment key={step.id}>
            <div
              className={cn(
                'flex items-center',
                isHorizontal ? 'flex-row' : 'flex-col',
                isHorizontal && index !== 0 && 'flex-1'
              )}
            >
              {/* Step Indicator */}
              <div
                className={cn(
                  'flex items-center justify-center rounded-full border-2 font-medium',
                  sizeClasses[size],
                  step.status === 'error' && 'border-red-500 bg-red-50 text-red-700',
                  step.status === 'completed' && 'border-green-600 bg-green-600 text-white',
                  step.status === 'in_progress' && 'border-primary-600 bg-primary-600 text-white',
                  step.status === 'pending' && !isCompleted && !isCurrent && 'border-gray-300 bg-white text-gray-500',
                  !step.status && isCompleted && 'border-green-600 bg-green-600 text-white',
                  !step.status && isCurrent && 'border-primary-600 bg-primary-600 text-white',
                  !step.status && isPending && !isCurrent && 'border-gray-300 bg-white text-gray-500',
                  isClickable && 'cursor-pointer hover:opacity-80'
                )}
                onClick={() => isClickable && onStepClick?.(index)}
              >
                {step.status === 'completed' || (!step.status && isCompleted) ? (
                  <Check className={size === 'lg' ? 'h-5 w-5' : size === 'md' ? 'h-4 w-4' : 'h-3 w-3'} />
                ) : step.status === 'in_progress' || (!step.status && isCurrent) ? (
                  <Loader2 className={cn('animate-spin', size === 'lg' ? 'h-5 w-5' : size === 'md' ? 'h-4 w-4' : 'h-3 w-3')} />
                ) : step.icon ? (
                  step.icon
                ) : (
                  <span>{index + 1}</span>
                )}
              </div>

              {/* Step Content */}
              <div
                className={cn(
                  'ml-3',
                  !isHorizontal && 'mb-1'
                )}
              >
                <p
                  className={cn(
                    'font-medium',
                    size === 'sm' && 'text-sm',
                    size === 'md' && 'text-base',
                    size === 'lg' && 'text-lg',
                    (step.status === 'completed' || (!step.status && isCompleted)) && 'text-green-700',
                    step.status === 'error' && 'text-red-700',
                    (step.status === 'pending' || (!step.status && isPending)) && !isCurrent && 'text-gray-500'
                  )}
                >
                  {step.title}
                </p>
                {step.description && (
                  <p className="text-sm text-gray-500">{step.description}</p>
                )}
              </div>
            </div>

            {/* Connector Line */}
            {index < steps.length - 1 && (
              <div
                className={cn(
                  'flex-1',
                  isHorizontal ? 'mx-2 h-0.5 mt-5' : 'ml-4 mt-2 h-4 w-0.5',
                  (isCompleted || index < currentStep) ? 'bg-green-600' : 'bg-gray-300'
                )}
              />
            )}
          </React.Fragment>
        )
      })}
    </div>
  )
}

// Vertical Stepper with detailed content
export interface VerticalStepperProps extends Omit<ProgressStepperProps, 'orientation'> {
  children?: React.ReactNode
  stepContent?: (step: Step, index: number) => React.ReactNode
}

export function VerticalStepper({
  steps,
  currentStep = 0,
  onStepClick,
  size = 'md',
  className,
  stepContent,
  children,
}: VerticalStepperProps) {
  return (
    <div className={cn('flex gap-4', className)}>
      {/* Steps Column */}
      <div className="flex flex-col">
        <ProgressStepper
          steps={steps}
          currentStep={currentStep}
          onStepClick={onStepClick}
          orientation="vertical"
          size={size}
        />
      </div>

      {/* Content Column */}
      <div className="flex-1">
        {stepContent
          ? steps.map((step, index) => (
              <div
                key={step.id}
                className={cn(
                  'mb-6',
                  index !== currentStep && 'hidden'
                )}
              >
                {stepContent(step, index)}
              </div>
            ))
          : children}
      </div>
    </div>
  )
}
