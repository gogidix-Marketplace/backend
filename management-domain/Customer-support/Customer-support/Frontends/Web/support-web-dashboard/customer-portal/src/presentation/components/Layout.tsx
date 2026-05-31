import { Link, useLocation } from 'react-router-dom';
import { HeadphonesIcon, MessageSquare, FileText, User, LogOut } from 'lucide-react';
import { cn } from '../../shared/utils';

interface LayoutProps {
  children: React.ReactNode;
}

export function Layout({ children }: LayoutProps) {
  const location = useLocation();
  const isLoginPage = location.pathname === '/login';

  if (isLoginPage) {
    return <>{children}</>;
  }

  const navigation = [
    { name: 'My Tickets', href: '/', icon: MessageSquare },
    { name: 'New Ticket', href: '/new-ticket', icon: FileText },
    { name: 'Knowledge Base', href: '/knowledge', icon: MessageSquare },
    { name: 'Profile', href: '/profile', icon: User },
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 shadow-sm sticky top-0 z-50">
        <div className="px-4 md:px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 bg-primary-600 rounded-lg flex items-center justify-center">
                  <HeadphonesIcon className="w-5 h-5 text-white" />
                </div>
                <div>
                  <h1 className="text-xl font-bold text-gray-900">Customer Portal</h1>
                  <p className="text-xs text-gray-500">Gogidix Support</p>
                </div>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <button className="text-sm text-gray-600 hover:text-gray-900">Help</button>
              <Link to="/login" className="flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900">
                <LogOut className="w-4 h-4" />
                <span>Logout</span>
              </Link>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <nav className="px-4 md:px-6 border-t border-gray-100">
          <div className="flex gap-1 overflow-x-auto">
            {navigation.map((item) => (
              <Link
                key={item.name}
                to={item.href}
                className={cn(
                  'flex items-center gap-2 px-4 py-2 text-sm font-medium rounded-t-lg transition-colors whitespace-nowrap',
                  location.pathname === item.href
                    ? 'text-primary-600 bg-primary-50'
                    : 'text-gray-600 hover:text-gray-900 hover:bg-gray-50'
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
      <main className="p-4 md:p-6">{children}</main>
    </div>
  );
}
