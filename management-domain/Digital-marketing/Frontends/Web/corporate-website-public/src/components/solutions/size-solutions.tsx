'use client';

import * as React from 'react';
import Link from 'next/link';
import { useTranslations, useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Rocket, Building2, Building } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';

const sizes = [
  {
    id: 'startup',
    icon: Rocket,
    color: 'from-blue-500 to-cyan-500',
    employees: '1-50',
    revenue: '$0 - $5M',
  },
  {
    id: 'smb',
    icon: Building2,
    color: 'from-purple-500 to-pink-500',
    employees: '50-500',
    revenue: '$5M - $50M',
  },
  {
    id: 'enterprise',
    icon: Building,
    color: 'from-orange-500 to-yellow-500',
    employees: '500+',
    revenue: '$50M+',
  },
];

export function SizeSolutions() {
  const t = useTranslations('solutions.bySize');
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">{t('title')}</h2>
          <p className="text-lg text-muted-foreground">{t('subtitle')}</p>
        </div>

        <div ref={ref} className="grid md:grid-cols-3 gap-8">
          {sizes.map((size, index) => {
            const Icon = size.icon;
            return (
              <motion.div
                key={size.id}
                initial={{ opacity: 0, y: 20 }}
                animate={inView ? { opacity: 1, y: 0 } : {}}
                transition={{ delay: index * 0.1 }}
              >
                <Card className="p-8 h-full">
                  <div className={`h-16 w-16 rounded-2xl bg-gradient-to-br ${size.color} flex items-center justify-center mb-6`}>
                    <Icon className="h-8 w-8 text-white" />
                  </div>
                  <h3 className="text-2xl font-bold mb-2">{t(`${size.id}.title`)}</h3>
                  <p className="text-muted-foreground mb-6">{t(`${size.id}.description`)}</p>
                  <div className="space-y-3 mb-6">
                    <div className="flex justify-between text-sm">
                      <span className="text-muted-foreground">Employees:</span>
                      <span className="font-medium">{size.employees}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span className="text-muted-foreground">Revenue:</span>
                      <span className="font-medium">{size.revenue}</span>
                    </div>
                  </div>
                  <Button className="w-full" variant="outline" asChild>
                    <Link href={`/${locale}/solutions/size/${size.id}`}>
                      Explore Solutions
                    </Link>
                  </Button>
                </Card>
              </motion.div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
