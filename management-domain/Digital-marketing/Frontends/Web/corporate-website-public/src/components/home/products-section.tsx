'use client';

import * as React from 'react';
import Link from 'next/link';
import { useTranslations, useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import { PRODUCTS } from '@/lib/constants';
import { Truck, ShoppingCart, FileText, Settings, Building, Server, ArrowRight } from 'lucide-react';
import { cn } from '@/lib/utils';

const iconMap = {
  Truck,
  ShoppingCart,
  FileText,
  Settings,
  Building,
  Server,
};

export function ProductsSection() {
  const t = useTranslations('home.products');
  const locale = useLocale();
  const [ref, inView] = useInView({
    triggerOnce: true,
    threshold: 0.1,
  });

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: { duration: 0.5 },
    },
  };

  return (
    <section ref={ref} className="py-24 bg-background">
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

        {/* Products Grid */}
        <motion.div
          variants={containerVariants}
          initial="hidden"
          animate={inView ? 'visible' : 'hidden'}
          className="grid md:grid-cols-2 lg:grid-cols-3 gap-6"
        >
          {PRODUCTS.map((product, index) => {
            const Icon = iconMap[product.icon as keyof typeof iconMap] || FileText;
            return (
              <motion.div key={product.id} variants={itemVariants}>
                <Card
                  className={cn(
                    'group p-6 h-full transition-all duration-300 hover:shadow-lg hover:-translate-y-1',
                    'border-2 hover:border-primary/20'
                  )}
                >
                  <div className="mb-4">
                    <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center group-hover:bg-primary/20 transition-colors">
                      <Icon className="h-6 w-6 text-primary" />
                    </div>
                  </div>
                  <h3 className="text-xl font-semibold mb-2">{t(product.id + '.title')}</h3>
                  <p className="text-muted-foreground mb-4">{t(product.id + '.description')}</p>
                  <Link
                    href={`/${locale}/products/${product.slug}`}
                    className="inline-flex items-center text-primary font-medium group-hover:gap-2 transition-all"
                  >
                    Learn more
                    <ArrowRight className="ml-1 h-4 w-4 transition-transform group-hover:translate-x-1" />
                  </Link>
                </Card>
              </motion.div>
            );
          })}
        </motion.div>

        {/* CTA */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={inView ? { opacity: 1, y: 0 } : {}}
          transition={{ delay: 0.6 }}
          className="text-center mt-12"
        >
          <Button size="lg" variant="outline" asChild>
            <Link href={`/${locale}/products`}>
              View All Products
              <ArrowRight className="ml-2 h-5 w-5" />
            </Link>
          </Button>
        </motion.div>
      </div>
    </section>
  );
}
