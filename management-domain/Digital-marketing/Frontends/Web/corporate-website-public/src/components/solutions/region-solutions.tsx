'use client';

import * as React from 'react';
import Link from 'next/link';
import { useTranslations, useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { MapPin } from 'lucide-react';
import { Card } from '@/components/ui/card';

const regions = [
  { id: 'na', name: 'North America', cities: ['San Francisco', 'New York', 'Toronto', 'Mexico City'] },
  { id: 'eu', name: 'Europe', cities: ['London', 'Paris', 'Berlin', 'Amsterdam'] },
  { id: 'apac', name: 'Asia Pacific', cities: ['Singapore', 'Tokyo', 'Sydney', 'Mumbai'] },
  { id: 'latam', name: 'Latin America', cities: ['São Paulo', 'Buenos Aires', 'Santiago', 'Bogotá'] },
  { id: 'mea', name: 'Middle East & Africa', cities: ['Dubai', 'Riyadh', 'Cairo', 'Johannesburg'] },
];

export function RegionSolutions() {
  const t = useTranslations('solutions.byRegion');
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">{t('title')}</h2>
          <p className="text-lg text-muted-foreground">
            Global presence with local expertise. We operate in 150+ countries.
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {regions.map((region, index) => (
            <motion.div
              key={region.id}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: index * 0.1 }}
            >
              <Link href={`/${locale}/solutions/region/${region.id}`}>
                <Card className="group p-6 h-full transition-all duration-300 hover:shadow-lg">
                  <div className="flex items-start gap-4">
                    <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center group-hover:bg-primary/20 transition-colors">
                      <MapPin className="h-6 w-6 text-primary" />
                    </div>
                    <div className="flex-1">
                      <h3 className="text-xl font-semibold mb-2">{region.name}</h3>
                      <div className="flex flex-wrap gap-2">
                        {region.cities.map((city) => (
                          <span
                            key={city}
                            className="text-xs px-2 py-1 rounded-full bg-muted text-muted-foreground"
                          >
                            {city}
                          </span>
                        ))}
                      </div>
                    </div>
                  </div>
                </Card>
              </Link>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
