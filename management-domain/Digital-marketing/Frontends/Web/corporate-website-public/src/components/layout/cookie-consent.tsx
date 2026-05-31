'use client';

import * as React from 'react';
import { useTranslations } from 'next-intl';
import { motion, AnimatePresence } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { useUIStore } from '@/stores/ui-store';
import { X } from 'lucide-react';

export function CookieConsent() {
  const t = useTranslations();
  const { cookieConsent, setCookieConsent } = useUIStore();
  const [showDetails, setShowDetails] = React.useState(false);

  if (cookieConsent) {
    return null;
  }

  return (
    <div className="fixed bottom-0 left-0 right-0 z-50 p-4 pointer-events-none">
      <motion.div
        initial={{ y: 100, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ type: 'spring', damping: 25, stiffness: 300 }}
        className="max-w-2xl mx-auto bg-background border shadow-lg rounded-lg pointer-events-auto"
      >
        <div className="p-6">
          <div className="flex items-start justify-between mb-4">
            <h3 className="text-lg font-semibold">{t('common.cookies.title')}</h3>
            <Button variant="ghost" size="icon" className="h-6 w-6" onClick={() => setCookieConsent('declined')}>
              <X className="h-4 w-4" />
            </Button>
          </div>

          <p className="text-sm text-muted-foreground mb-4">
            {t('common.cookies.description')}
          </p>

          <AnimatePresence>
            {showDetails && (
              <motion.div
                initial={{ height: 0, opacity: 0 }}
                animate={{ height: 'auto', opacity: 1 }}
                exit={{ height: 0, opacity: 0 }}
                className="space-y-2 mb-4 overflow-hidden"
              >
                <div className="flex items-center gap-2">
                  <input type="checkbox" checked disabled id="necessary" className="rounded" />
                  <label htmlFor="necessary" className="text-sm">
                    {t('common.cookies.necessary')} ({t('common.cookies.necessaryDesc')})
                  </label>
                </div>
                <div className="flex items-center gap-2">
                  <input type="checkbox" defaultChecked id="preferences" className="rounded" />
                  <label htmlFor="preferences" className="text-sm">
                    {t('common.cookies.preferences')} ({t('common.cookies.preferencesDesc')})
                  </label>
                </div>
                <div className="flex items-center gap-2">
                  <input type="checkbox" defaultChecked id="analytics" className="rounded" />
                  <label htmlFor="analytics" className="text-sm">
                    {t('common.cookies.analytics')} ({t('common.cookies.analyticsDesc')})
                  </label>
                </div>
              </motion.div>
            )}
          </AnimatePresence>

          <div className="flex flex-wrap gap-2">
            <Button size="sm" onClick={() => setCookieConsent('accepted')}>
              {t('common.cookies.accept')}
            </Button>
            <Button size="sm" variant="outline" onClick={() => setCookieConsent('declined')}>
              {t('common.cookies.decline')}
            </Button>
            <Button
              size="sm"
              variant="ghost"
              onClick={() => setShowDetails(!showDetails)}
              className="ml-auto"
            >
              {t('common.cookies.settings')}
            </Button>
          </div>
        </div>
      </motion.div>
    </div>
  );
}
