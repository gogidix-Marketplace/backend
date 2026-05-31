'use client';

import * as React from 'react';
import Link from 'next/link';
import { useTranslations, useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { ArrowRight, TrendingUp, Clock, Users } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';

const caseStudies = [
  {
    id: 1,
    company: 'TechStart Inc.',
    industry: 'E-commerce',
    challenge: 'Inefficient logistics and high shipping costs',
    solution: 'Logistics Management Platform',
    results: [
      { metric: '40%', label: 'Reduction in shipping costs', icon: TrendingUp },
      { metric: '60%', label: 'Faster delivery times', icon: Clock },
      { metric: '3x', label: 'ROI in first year', icon: TrendingUp },
    ],
  },
  {
    id: 2,
    company: 'RetailCo Global',
    industry: 'Retail',
    challenge: 'Fragmented inventory across 50+ warehouses',
    solution: 'Unified Operations Platform',
    results: [
      { metric: '90%', label: 'Reduction in stockouts', icon: TrendingUp },
      { metric: '50%', label: 'Less manual work', icon: Users },
      { metric: '25%', label: 'Revenue increase', icon: TrendingUp },
    ],
  },
  {
    id: 3,
    company: 'ManufacturingPlus',
    industry: 'Manufacturing',
    challenge: 'Complex supply chain with 200+ suppliers',
    solution: 'Procurement Suite',
    results: [
      { metric: '35%', label: 'Cost savings', icon: TrendingUp },
      { metric: '80%', label: 'Faster procurement cycles', icon: Clock },
      { metric: '100%', label: 'Supplier visibility', icon: Users },
    ],
  },
];

export function CaseStudiesPreview() {
  const t = useTranslations('solutions.caseStudies');
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
          {caseStudies.map((study, index) => (
            <motion.div
              key={study.id}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: index * 0.1 }}
            >
              <Card className="p-6 h-full">
                <div className="mb-6">
                  <div className="text-sm text-primary font-medium mb-2">{study.industry}</div>
                  <h3 className="text-xl font-semibold mb-1">{study.company}</h3>
                </div>

                <div className="space-y-4 mb-6">
                  <div>
                    <div className="text-xs text-muted-foreground uppercase tracking-wide mb-1">
                      Challenge
                    </div>
                    <p className="text-sm">{study.challenge}</p>
                  </div>
                  <div>
                    <div className="text-xs text-muted-foreground uppercase tracking-wide mb-1">
                      Solution
                    </div>
                    <p className="text-sm font-medium">{study.solution}</p>
                  </div>
                </div>

                <div className="border-t pt-4">
                  <div className="text-xs text-muted-foreground uppercase tracking-wide mb-3">
                    {t('results')}
                  </div>
                  <div className="space-y-2">
                    {study.results.map((result, i) => (
                      <div key={i} className="flex items-center gap-2">
                        <TrendingUp className="h-4 w-4 text-green-500" />
                        <span className="font-semibold">{result.metric}</span>
                        <span className="text-xs text-muted-foreground">{result.label}</span>
                      </div>
                    ))}
                  </div>
                </div>

                <Link
                  href={`/${locale}/solutions/case-studies/${study.id}`}
                  className="inline-flex items-center gap-2 text-sm font-medium mt-4 text-primary hover:gap-3 transition-all"
                >
                  Read full case study
                  <ArrowRight className="h-4 w-4" />
                </Link>
              </Card>
            </motion.div>
          ))}
        </div>

        <div className="text-center mt-12">
          <Link
            href={`/${locale}/solutions/case-studies`}
            className="inline-flex items-center gap-2 px-6 py-3 rounded-lg border hover:bg-accent transition-colors"
          >
            {t('viewAll')}
            <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
      </div>
    </section>
  );
}
