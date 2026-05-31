import { Link } from 'react-router-dom';
import { Activity, BarChart3, Users, TrendingUp, AlertCircle } from 'lucide-react';
import { cn } from '../../shared/utils';

interface LayoutProps {
  children: React.ReactNode;
}

export function Layout({ children }: LayoutProps) {
  const navigation = [
    { name: 'Dashboard', href: '/', icon: BarChart3 },
    { name: 'Countries', href: '/countries', icon: Activity },
    { name: 'Teams', href: '/teams', icon: Users },
    { name: 'Trends', href: '/trends', icon: TrendingUp },
    { name: 'Alerts', href: '/alerts', icon: AlertCircle },
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 shadow-sm">
        <div className="px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 bg-primary-600 rounded-lg flex items-center justify-center">
                  <Activity className="w-5 h-5 text-white" />
                </div>
                <div>
                  <h1 className="text-xl font-bold text-gray-900">Global Support Dashboard</h1>
                  <p className="text-xs text-gray-500">Gogidix Customer Support</p>
                </div>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <div className="text-sm text-gray-500">
                <span className="font-medium text-gray-900">Last updated:</span>{' '}
                {new Date().toLocaleTimeString()}
              </div>
              <div className="w-8 h-8 bg-primary-100 rounded-full flex items-center justify-center">
                <span className="text-sm font-medium text-primary-700">JD</span>
              </div>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <nav className="px-6">
          <div className="flex gap-1">
            {navigation.map((item) => (
              <Link
                key={item.name}
                to={item.href}
                className={cn(
                  'flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-t-lg transition-colors',
                  'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
                )}
              >
                <item.icon className="w-4 h-4" />
                {item.name}
              </Link>
            ))}
          </div>
        </nav>
      </header>

      {/* Main Content */}
      <main className="p-6">{children}</main>
    </div>
  );
}
