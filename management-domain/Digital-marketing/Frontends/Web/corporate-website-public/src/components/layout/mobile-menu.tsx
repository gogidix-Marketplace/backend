'use client';

import * as React from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useTranslations, useLocale } from 'next-intl';
import { motion, AnimatePresence } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';
import { X, ChevronRight, Sparkles } from 'lucide-react';
import { FOOTER_LINKS } from '@/lib/constants';

interface MobileMenuProps {
  onClose: () => void;
}

export function MobileMenu({ onClose }: MobileMenuProps) {
  const t = useTranslations();
  const locale = useLocale();
  const pathname = usePathname();
  const [openSection, setOpenSection] = React.useState<string | null>(null);

  const navItems = [
    { key: 'products', label: t('nav.products'), hasSubmenu: true },
    { key: 'solutions', label: t('nav.solutions'), hasSubmenu: true },
    { key: 'developers', label: t('nav.developers'), hasSubmenu: false },
    { key: 'partners', label: t('nav.partners'), hasSubmenu: true },
    { key: 'company', label: t('nav.company'), hasSubmenu: true },
    { key: 'resources', label: t('nav.resources'), hasSubmenu: true },
  ];

  const getSubmenuItems = (key: string) => {
    const menuMap: Record<string, Array<{ name: string; href: string }>> = {
      products: FOOTER_LINKS.products,
      solutions: FOOTER_LINKS.solutions,
      partners: FOOTER_LINKS.partners,
      company: FOOTER_LINKS.company,
      resources: FOOTER_LINKS.resources,
    };
    return menuMap[key] || [];
  };

  return (
    <AnimatePresence>
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        className="fixed inset-0 z-50 lg:hidden"
        onClick={onClose}
      >
        {/* Backdrop */}
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="absolute inset-0 bg-black/50 backdrop-blur-sm"
        />

        {/* Menu Panel */}
        <motion.div
          initial={{ x: '100%' }}
          animate={{ x: 0 }}
          exit={{ x: '100%' }}
          transition={{ type: 'spring', damping: 25, stiffness: 200 }}
          className="absolute right-0 top-0 bottom-0 w-full max-w-md bg-background shadow-2xl overflow-y-auto"
          onClick={(e) => e.stopPropagation()}
        >
          {/* Header */}
          <div className="flex items-center justify-between p-4 border-b">
            <Link href={`/${locale}`} className="flex items-center gap-2" onClick={onClose}>
              <div className="h-8 w-8 rounded-lg bg-gradient-to-br from-primary to-accent flex items-center justify-center">
                <Sparkles className="h-5 w-5 text-white" />
              </div>
              <span className="text-xl font-bold">Gogidix</span>
            </Link>
            <Button variant="ghost" size="icon" onClick={onClose}>
              <X className="h-6 w-6" />
            </Button>
          </div>

          {/* Navigation */}
          <nav className="p-4">
            <ul className="space-y-1">
              {navItems.map((item) => (
                <li key={item.key}>
                  {item.hasSubmenu ? (
                    <div>
                      <button
                        onClick={() =>
                          setOpenSection(openSection === item.key ? null : item.key)
                        }
                        className={cn(
                          'w-full flex items-center justify-between px-4 py-3 rounded-lg transition-colors',
                          pathname.includes(item.key)
                            ? 'bg-accent text-accent-foreground'
                            : 'hover:bg-accent'
                        )}
                      >
                        <span className="font-medium">{item.label}</span>
                        <ChevronRight
                          className={cn(
                            'h-4 w-4 transition-transform',
                            openSection === item.key && 'rotate-90'
                          )}
                        />
                      </button>

                      <AnimatePresence>
                        {openSection === item.key && (
                          <motion.div
                            initial={{ height: 0, opacity: 0 }}
                            animate={{ height: 'auto', opacity: 1 }}
                            exit={{ height: 0, opacity: 0 }}
                            className="overflow-hidden"
                          >
                            <ul className="mt-2 ml-4 space-y-1 border-l-2 border-border pl-4">
                              {getSubmenuItems(item.key).map((subItem) => (
                                <li key={subItem.href}>
                                  <Link
                                    href={`/${locale}${subItem.href}`}
                                    onClick={onClose}
                                    className="block px-4 py-2 rounded-lg text-sm text-muted-foreground hover:text-foreground hover:bg-accent transition-colors"
                                  >
                                    {subItem.name}
                                  </Link>
                                </li>
                              ))}
                            </ul>
                          </motion.div>
                        )}
                      </AnimatePresence>
                    </div>
                  ) : (
                    <Link
                      href={`/${locale}/${item.key}`}
                      onClick={onClose}
                      className={cn(
                        'block px-4 py-3 rounded-lg transition-colors',
                        pathname.includes(item.key)
                          ? 'bg-accent text-accent-foreground'
                          : 'hover:bg-accent'
                      )}
                    >
                      <span className="font-medium">{item.label}</span>
                    </Link>
                  )}
                </li>
              ))}
            </ul>

            {/* CTA Buttons */}
            <div className="mt-8 space-y-2">
              <Button variant="outline" className="w-full" asChild onClick={onClose}>
                <Link href={`/${locale}/login`}>{t('nav.login')}</Link>
              </Button>
              <Button className="w-full" asChild onClick={onClose}>
                <Link href={`/${locale}/signup`}>{t('nav.signup')}</Link>
              </Button>
            </div>
          </nav>
        </motion.div>
      </motion.div>
    </AnimatePresence>
  );
}
