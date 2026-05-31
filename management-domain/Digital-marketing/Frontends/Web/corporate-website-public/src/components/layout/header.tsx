'use client';

import * as React from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useTranslations, useLocale } from 'next-intl';
import { motion, AnimatePresence } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { useUIStore } from '@/stores/ui-store';
import { LOCALES, FOOTER_LINKS } from '@/lib/constants';
import { cn } from '@/lib/utils';
import {
  Menu,
  X,
  Search,
  Globe,
  ChevronDown,
  ArrowRight,
  Sparkles,
} from 'lucide-react';
import { GlobalSearch } from './global-search';
import { MobileMenu } from './mobile-menu';
import { LanguageSelector } from './language-selector';
import { RegionSelector } from './region-selector';

export function Header() {
  const t = useTranslations();
  const locale = useLocale();
  const pathname = usePathname();
  const {
    mobileMenuOpen,
    setMobileMenuOpen,
    searchOpen,
    setSearchOpen,
    toggleSearch,
  } = useUIStore();

  const [scrolled, setScrolled] = React.useState(false);
  const [activeDropdown, setActiveDropdown] = React.useState<string | null>(null);

  React.useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 20);
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const navItems = [
    { key: 'products', label: t('nav.products'), hasDropdown: true },
    { key: 'solutions', label: t('nav.solutions'), hasDropdown: true },
    { key: 'developers', label: t('nav.developers'), hasDropdown: false },
    { key: 'partners', label: t('nav.partners'), hasDropdown: true },
    { key: 'company', label: t('nav.company'), hasDropdown: true },
    { key: 'resources', label: t('nav.resources'), hasDropdown: true },
  ];

  const getLink = (key: string) => {
    const base = `/${locale}`;
    switch (key) {
      case 'products':
        return `${base}/products`;
      case 'solutions':
        return `${base}/solutions`;
      case 'developers':
        return `${base}/developers`;
      case 'partners':
        return `${base}/partners`;
      case 'company':
        return `${base}/company`;
      case 'resources':
        return `${base}/resources`;
      default:
        return base;
    }
  };

  return (
    <>
      <header
        className={cn(
          'fixed top-0 left-0 right-0 z-50 transition-all duration-300',
          scrolled
            ? 'bg-background/80 backdrop-blur-lg border-b shadow-sm'
            : 'bg-transparent'
        )}
      >
        <div className="container mx-auto px-4">
          <div className="flex h-16 items-center justify-between">
            {/* Logo */}
            <Link href={`/${locale}`} className="flex items-center gap-2 z-50">
              <div className="h-8 w-8 rounded-lg bg-gradient-to-br from-primary to-accent flex items-center justify-center">
                <Sparkles className="h-5 w-5 text-white" />
              </div>
              <span className="text-xl font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
                {t('common.appName')}
              </span>
            </Link>

            {/* Desktop Navigation */}
            <nav className="hidden lg:flex items-center gap-1">
              {navItems.map((item) => (
                <div
                  key={item.key}
                  className="relative"
                  onMouseEnter={() => item.hasDropdown && setActiveDropdown(item.key)}
                  onMouseLeave={() => setActiveDropdown(null)}
                >
                  <Button
                    variant="ghost"
                    asChild
                    className={cn(
                      'gap-1',
                      pathname.includes(item.key) && 'bg-accent/50'
                    )}
                  >
                    <Link href={getLink(item.key)}>
                      {item.label}
                      {item.hasDropdown && (
                        <ChevronDown className="h-4 w-4 transition-transform" />
                      )}
                    </Link>
                  </Button>

                  {/* Dropdown Menu */}
                  <AnimatePresence>
                    {activeDropdown === item.key && item.hasDropdown && (
                      <motion.div
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: 10 }}
                        transition={{ duration: 0.2 }}
                        className="absolute top-full left-0 pt-2 w-64"
                      >
                        <div className="bg-background border rounded-lg shadow-lg p-2">
                          {getDropdownItems(item.key).map((subItem) => (
                            <Link
                              key={subItem.href}
                              href={`/${locale}${subItem.href}`}
                              className="flex items-center justify-between px-3 py-2 rounded-md hover:bg-accent text-sm transition-colors"
                            >
                              <span>{subItem.name}</span>
                              <ArrowRight className="h-4 w-4 opacity-50" />
                            </Link>
                          ))}
                        </div>
                      </motion.div>
                    )}
                  </AnimatePresence>
                </div>
              ))}
            </nav>

            {/* Right Side Actions */}
            <div className="flex items-center gap-2">
              {/* Search */}
              <Button
                variant="ghost"
                size="icon"
                onClick={toggleSearch}
                className="hidden sm:flex"
              >
                <Search className="h-5 w-5" />
              </Button>

              {/* Language Selector */}
              <div className="hidden md:block">
                <LanguageSelector />
              </div>

              {/* Region Selector */}
              <div className="hidden lg:block">
                <RegionSelector />
              </div>

              {/* CTA Buttons */}
              <div className="hidden md:flex items-center gap-2">
                <Button variant="ghost" asChild>
                  <Link href={`/${locale}/login`}>{t('nav.login')}</Link>
                </Button>
                <Button asChild>
                  <Link href={`/${locale}/signup`}>{t('nav.signup')}</Link>
                </Button>
              </div>

              {/* Mobile Menu Toggle */}
              <Button
                variant="ghost"
                size="icon"
                className="lg:hidden"
                onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              >
                {mobileMenuOpen ? (
                  <X className="h-6 w-6" />
                ) : (
                  <Menu className="h-6 w-6" />
                )}
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Mobile Menu */}
      <AnimatePresence>
        {mobileMenuOpen && <MobileMenu onClose={() => setMobileMenuOpen(false)} />}
      </AnimatePresence>

      {/* Global Search */}
      <GlobalSearch open={searchOpen} onOpenChange={setSearchOpen} />
    </>
  );
}

function getDropdownItems(key: string): Array<{ name: string; href: string }> {
  const dropdownMap: Record<string, Array<{ name: string; href: string }>> = {
    products: [
      { name: 'Logistics', href: '/products/logistics' },
      { name: 'E-commerce', href: '/products/ecommerce' },
      { name: 'Procurement', href: '/products/procurement' },
      { name: 'Business Operations', href: '/products/operations' },
      { name: 'Enterprise', href: '/products/enterprise' },
      { name: 'Infrastructure', href: '/products/infrastructure' },
    ],
    solutions: [
      { name: 'By Industry', href: '/solutions/industry' },
      { name: 'By Company Size', href: '/solutions/size' },
      { name: 'By Region', href: '/solutions/region' },
      { name: 'Case Studies', href: '/solutions/case-studies' },
    ],
    partners: [
      { name: 'White-Label Partners', href: '/partners/white-label' },
      { name: 'Technology Partners', href: '/partners/technology' },
      { name: 'System Integrators', href: '/partners/integrators' },
      { name: 'Partner Directory', href: '/partners/directory' },
    ],
    company: [
      { name: 'About Us', href: '/company/about' },
      { name: 'Leadership', href: '/company/leadership' },
      { name: 'Careers', href: '/company/careers' },
      { name: 'Press', href: '/company/press' },
      { name: 'Contact', href: '/company/contact' },
    ],
    resources: [
      { name: 'Documentation', href: '/resources/docs' },
      { name: 'Blog', href: '/resources/blog' },
      { name: 'Webinars', href: '/resources/webinars' },
      { name: 'Security', href: '/resources/security' },
    ],
  };

  return dropdownMap[key] || [];
}
