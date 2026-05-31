'use client';

import { useTranslations } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';

interface ProductFeaturesProps {
  productId: string;
}

export function ProductFeatures({ productId }: ProductFeaturesProps) {
  const t = useTranslations(`products.${productId}`);

  const features = t.raw('features') as Record<string, string>;
  const featureList = Object.entries(features).map(([key, value]) => ({
    key,
    value,
  }));

  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Key Features</h2>
          <p className="text-lg text-muted-foreground">
            Everything you need to succeed
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {featureList.map((feature, index) => (
            <motion.div
              key={feature.key}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: index * 0.1 }}
              className="flex gap-4"
            >
              <div className="h-10 w-10 rounded-lg bg-primary/10 flex items-center justify-center shrink-0">
                <div className="h-2 w-2 rounded-full bg-primary" />
              </div>
              <div>
                <h3 className="font-semibold mb-1">{feature.value}</h3>
                <p className="text-sm text-muted-foreground">
                  {t(`features.${feature.key}`)}
                </p>
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
