'use client';

import * as React from 'react';
import { useTranslations } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Puzzle, Brain, Shield, Clock, Globe, Code } from 'lucide-react';
import { cn } from '@/lib/utils';

const features = [
  {
    id: 'feature1',
    icon: Puzzle,
    gradient: 'from-blue-500 to-cyan-500',
  },
  {
    id: 'feature2',
    icon: Brain,
    gradient: 'from-purple-500 to-pink-500',
  },
  {
    id: 'feature3',
    icon: Shield,
    gradient: 'from-green-500 to-emerald-500',
  },
  {
    id: 'feature4',
    icon: Clock,
    gradient: 'from-orange-500 to-yellow-500',
  },
  {
    id: 'feature5',
    icon: Globe,
    gradient: 'from-indigo-500 to-violet-500',
  },
  {
    id: 'feature6',
    icon: Code,
    gradient: 'from-rose-500 to-red-500',
  },
];

export function FeaturesSection() {
  const t = useTranslations('home.features');
  const [ref, inView] = useInView({
    triggerOnce: true,
    threshold: 0.1,
  });

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        {/* Section Header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={inView ? { opacity: 1, y: 0 } : {}}
          className="text-center max-w-2xl mx-auto mb-16"
        >
          <h2 className="text-3xl md:text-4xl font-bold mb-4">{t('title')}</h2>
          <p className="text-lg text-muted-foreground">{t('subtitle')}</p>
        </motion.div>

        {/* Features Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((feature, index) => {
            const Icon = feature.icon;
            return (
              <motion.div
                key={feature.id}
                ref={index === 0 ? ref : null}
                initial={{ opacity: 0, y: 20 }}
                animate={inView ? { opacity: 1, y: 0 } : {}}
                transition={{ delay: index * 0.1 }}
                className="group"
              >
                <div className="relative">
                  {/* Background gradient on hover */}
                  <div className={cn(
                    'absolute inset-0 bg-gradient-to-br rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500',
                    feature.gradient
                  )} />

                  {/* Content */}
                  <div className="relative p-8 bg-card border rounded-2xl h-full">
                    <div className={cn(
                      'h-14 w-14 rounded-xl bg-gradient-to-br flex items-center justify-center mb-6',
                      feature.gradient
                    )}>
                      <Icon className="h-7 w-7 text-white" />
                    </div>
                    <h3 className="text-xl font-semibold mb-3">
                      {t(`${feature.id}.title`)}
                    </h3>
                    <p className="text-muted-foreground">
                      {t(`${feature.id}.description`)}
                    </p>
                  </div>
                </div>
              </motion.div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
