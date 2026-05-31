'use client';

import * as React from 'react';
import Link from 'next/link';
import { useTranslations, useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { INDUSTRIES } from '@/lib/constants';
import { ShoppingBag, Wrench, HeartPulse, Apple, Car, Cpu, ArrowRight } from 'lucide-react';
import { Card } from '@/components/ui/card';

const iconMap = {
  ShoppingBag,
  Wrench,
  HeartPulse,
  Apple,
  Car,
  Cpu,
};

export function IndustrySolutions() {
  const t = useTranslations('solutions.byIndustry');
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">{t('title')}</h2>
          <p className="text-lg text-muted-foreground">{t('subtitle')}</p>
        </div>

        <div ref={ref} className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {INDUSTRIES.map((industry, index) => {
            const Icon = iconMap[industry.icon as keyof typeof iconMap] || Cpu;
            return (
              <motion.div
                key={industry.id}
                initial={{ opacity: 0, y: 20 }}
                animate={inView ? { opacity: 1, y: 0 } : {}}
                transition={{ delay: index * 0.1 }}
              >
                <Link href={`/${locale}/solutions/industry/${industry.id}`}>
                  <Card className="group p-6 h-full transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
                    <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center mb-4 group-hover:bg-primary/20 transition-colors">
                      <Icon className="h-6 w-6 text-primary" />
                    </div>
                    <h3 className="text-xl font-semibold mb-2">{t(`${industry.id}.title`)}</h3>
                    <p className="text-muted-foreground mb-4">{t(`${industry.id}.description`)}</p>
                    <span className="inline-flex items-center text-primary font-medium group-hover:gap-2 transition-all">
                      Learn more
                      <ArrowRight className="ml-1 h-4 w-4 transition-transform group-hover:translate-x-1" />
                    </span>
                  </Card>
                </Link>
              </motion.div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
