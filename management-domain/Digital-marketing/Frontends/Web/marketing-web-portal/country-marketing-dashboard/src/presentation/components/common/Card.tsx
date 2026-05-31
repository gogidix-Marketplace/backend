import { ReactNode } from 'react'
import './Card.css'

interface CardProps {
  children: ReactNode
  className?: string
  padding?: 'none' | 'sm' | 'md' | 'lg'
}

interface CardHeaderProps {
  children: ReactNode
  action?: ReactNode
}

interface CardBodyProps {
  children: ReactNode
}

export function Card({ children, className = '', padding = 'md' }: CardProps) {
  const classes = [`card`, `card-padding-${padding}`, className].filter(Boolean).join(' ')
  return <div className={classes}>{children}</div>
}

export function CardHeader({ children, action }: CardHeaderProps) {
  return (
    <div className="card-header">
      <div className="card-header-content">{children}</div>
      {action && <div className="card-header-action">{action}</div>}
    </div>
  )
}

export function CardBody({ children }: CardBodyProps) {
  return <div className="card-body">{children}</div>
}
