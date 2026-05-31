'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Book, Zap, GraduationCap } from 'lucide-react';
import { Card } from '@/components/ui/card';

const steps = [
  {
    number: '01',
    title: 'Get your API key',
    description: 'Sign up and generate your API key from the dashboard',
    icon: Zap,
  },
  {
    number: '02',
    title: 'Install an SDK',
    description: 'Install the SDK for your preferred programming language',
    icon: Book,
  },
  {
    number: '03',
    title: 'Make your first request',
    description: 'Follow our quickstart guide to make your first API call',
    icon: GraduationCap,
  },
];

export function GettingStarted() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Getting Started</h2>
          <p className="text-lg text-muted-foreground">
            Get up and running in minutes with our quick start guide
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-3 gap-8 max-w-5xl mx-auto">
          {steps.map((step, index) => {
            const Icon = step.icon;
            return (
              <motion.div
                key={step.number}
                initial={{ opacity: 0, y: 20 }}
                animate={inView ? { opacity: 1, y: 0 } : {}}
                transition={{ delay: index * 0.1 }}
                className="relative"
              >
                {index < steps.length - 1 && (
                  <div className="hidden md:block absolute top-8 left-full w-full h-0.5 bg-border -translate-x-1/2" />
                )}
                <Card className="p-6 text-center relative">
                  <div className="text-5xl font-bold text-primary/20 absolute -top-4 -right-2">
                    {step.number}
                  </div>
                  <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center mx-auto mb-4">
                    <Icon className="h-6 w-6 text-primary" />
                  </div>
                  <h3 className="text-xl font-semibold mb-2">{step.title}</h3>
                  <p className="text-sm text-muted-foreground">{step.description}</p>
                </Card>
              </motion.div>
            );
          })}
        </div>

        <div className="text-center mt-12">
          <Link
            href={`/${locale}/developers/docs/quickstart`}
            className="inline-flex items-center gap-2 px-6 py-3 rounded-lg border hover:bg-accent transition-colors"
          >
            Read Quick Start Guide
          </Link>
        </div>
      </div>
    </section>
  );
}
