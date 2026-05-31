'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Target, Eye, Heart, Lightbulb, Users, Award } from 'lucide-react';
import { Card } from '@/components/ui/card';

const values = [
  { icon: Lightbulb, title: 'Continuous Innovation', description: 'We push boundaries to create better solutions' },
  { icon: Users, title: 'Customer Success', description: 'Our customers success is our success' },
  { icon: Heart, title: 'Integrity & Transparency', description: 'We do the right thing, always' },
  { icon: Users, title: 'Collaboration', description: 'Better together, diverse perspectives' },
  { icon: Award, title: 'Excellence', description: 'We strive for the highest quality' },
  { icon: Target, title: 'Impact', description: 'We measure our success by customer outcomes' },
];

export function AboutPreview() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="grid lg:grid-cols-2 gap-16 items-center">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={inView ? { opacity: 1, x: 0 } : {}}
            transition={{ duration: 0.5 }}
          >
            <h2 className="text-3xl md:text-4xl font-bold mb-4">Our Story</h2>
            <p className="text-lg text-muted-foreground mb-4">
              Founded in 2018, Gogidix started with a simple mission: make enterprise-grade
              technology accessible to businesses of all sizes.
            </p>
            <p className="text-lg text-muted-foreground mb-6">
              Today, we serve over 10,000 companies worldwide, helping them streamline logistics,
              automate operations, and achieve remarkable growth.
            </p>
            <Link
              href={`/${locale}/company/about`}
              className="inline-flex items-center text-primary font-medium hover:gap-2 transition-all"
            >
              Learn more about our story
            </Link>
          </motion.div>

          <motion.div
            ref={ref}
            initial={{ opacity: 0, x: 20 }}
            animate={inView ? { opacity: 1, x: 0 } : {}}
            transition={{ duration: 0.5 }}
          >
            <h3 className="text-xl font-semibold mb-4">Our Values</h3>
            <div className="grid sm:grid-cols-2 gap-3">
              {values.map((value, index) => {
                const Icon = value.icon;
                return (
                  <motion.div
                    key={value.title}
                    initial={{ opacity: 0, y: 20 }}
                    animate={inView ? { opacity: 1, y: 0 } : {}}
                    transition={{ delay: index * 0.1 }}
                  >
                    <Card className="p-4">
                      <Icon className="h-6 w-6 text-primary mb-2" />
                      <h4 className="font-semibold mb-1">{value.title}</h4>
                      <p className="text-sm text-muted-foreground">{value.description}</p>
                    </Card>
                  </motion.div>
                );
              })}
            </div>
          </motion.div>
        </div>
      </div>
    </section>
  );
}
