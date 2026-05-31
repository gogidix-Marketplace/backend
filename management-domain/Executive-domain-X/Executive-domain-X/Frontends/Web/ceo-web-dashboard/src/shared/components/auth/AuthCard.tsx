import { ReactNode } from 'react'
import { Building2 } from 'lucide-react'

interface AuthCardProps {
  children: ReactNode
  title: ReactNode
  subtitle?: string
  showLogo?: boolean
}

export function AuthCard({
  children,
  title,
  subtitle,
  showLogo = true,
}: AuthCardProps) {
  return (
    <div
      className="w-full max-w-[480px]"
      style={{
        background: 'rgba(255, 255, 255, 0.97)',
        backdropFilter: 'blur(20px)',
        border: '1px solid rgba(255, 255, 255, 0.18)',
        borderRadius: '16px',
        padding: '40px',
        boxShadow: '0 8px 32px rgba(0, 0, 0, 0.12)',
      }}
    >
      {showLogo && (
        <div className="flex items-center justify-center gap-3 mb-8">
          <div
            className="w-10 h-10 rounded-xl flex items-center justify-center"
            style={{ backgroundColor: '#0F172A' }}
          >
            <Building2 className="w-5 h-5 text-white" />
          </div>
          <span
            className="text-xl font-semibold"
            style={{ color: '#0F172A', letterSpacing: '-0.5px' }}
          >
            Gogidix
          </span>
        </div>
      )}

      <div className="text-center mb-8">
        <h1
          className="text-2xl font-semibold mb-2"
          style={{ color: '#1E293B', letterSpacing: '-0.5px' }}
        >
          {title}
        </h1>
        {subtitle && (
          <p className="text-sm" style={{ color: '#64748B' }}>
            {subtitle}
          </p>
        )}
      </div>

      <div>{children}</div>
    </div>
  )
}
