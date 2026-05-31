'use client';

import * as React from 'react';
import Link from 'next/link';
import { useTranslations, useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { PRODUCTS } from '@/lib/constants';
import { Truck, ShoppingCart, FileText, Settings, Building, Server, ArrowRight, Check } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';

const iconMap = {
  Truck,
  ShoppingCart,
  FileText,
  Settings,
  Building,
  Server,
};

export function ProductsGrid() {
  const t = useTranslations('products');
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.1 },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 },
  };

  return (
    <section className="py-24 bg-background">
      <div className="container mx-auto px-4">
        <motion.div
          ref={ref}
          variants={containerVariants}
          initial="hidden"
          animate={inView ? 'visible' : 'hidden'}
          className="grid md:grid-cols-2 lg:grid-cols-3 gap-8"
        >
          {PRODUCTS.map((product) => {
            const Icon = iconMap[product.icon as keyof typeof iconMap] || FileText;
            const features = t(`${product.id}.features`, { returnObjects: true }) as string[];

            return (
              <motion.div key={product.id} variants={itemVariants}>
                <Card className="p-8 h-full flex flex-col group hover:shadow-xl transition-all duration-300">
                  <div className="mb-6">
                    <div className="h-16 w-16 rounded-2xl bg-primary/10 flex items-center justify-center mb-4 group-hover:bg-primary/20 transition-colors">
                      <Icon className="h-8 w-8 text-primary" />
                    </div>
                    <h3 className="text-2xl font-bold mb-2">{t(`${product.id}.title`)}</h3>
                    <p className="text-muted-foreground">{t(`${product.id}.subtitle`)}</p>
                  </div>

                  <p className="text-muted-foreground mb-6 flex-1">
                    {t(`${product.id}.description`)}
                  </p>

                  <ul className="space-y-3 mb-8 flex-1">
                    {features.slice(0, 4).map((feature, i) => (
                      <li key={i} className="flex items-start gap-3">
                        <Check className="h-5 w-5 text-primary shrink-0 mt-0.5" />
                        <span className="text-sm">{feature}</span>
                      </li>
                    ))}
                  </ul>

                  <Button className="w-full group-hover:bg-primary/90" asChild>
                    <Link href={`/${locale}/products/${product.slug}`}>
                      {t(`${product.id}.cta`)}
                      <ArrowRight className="ml-2 h-4 w-4" />
                    </Link>
                  </Button>
                </Card>
              </motion.div>
            );
          })}
        </motion.div>
      </div>
    </section>
  );
}
