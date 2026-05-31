'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Calendar, Newspaper } from 'lucide-react';
import { Card } from '@/components/ui/card';

const pressReleases = [
  {
    title: 'Gogidix Raises $100M Series C to Expand Global Operations',
    date: 'December 15, 2024',
    category: 'Press Release',
  },
  {
    title: 'New AI-Powered Analytics Platform Launches',
    date: 'November 28, 2024',
    category: 'Product News',
  },
  {
    title: 'Gogidix Named a Leader in 2024 Gartner Magic Quadrant',
    date: 'October 10, 2024',
    category: 'Award',
  },
];

const coverage = [
  { name: 'TechCrunch', headline: 'Gogidix is revolutionizing logistics management' },
  { name: 'Forbes', headline: 'How this startup is transforming supply chains' },
  { name: 'Bloomberg', headline: 'Gogidix reaches unicorn status with latest funding' },
];

export function PressPreview() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Press</h2>
          <p className="text-lg text-muted-foreground">
            Latest news and updates from Gogidix
          </p>
        </div>

        <div className="grid lg:grid-cols-2 gap-8">
          {/* Press Releases */}
          <div>
            <h3 className="font-semibold text-lg mb-4 flex items-center gap-2">
              <Newspaper className="h-5 w-5" />
              Press Releases
            </h3>
            <div ref={ref} className="space-y-4">
              {pressReleases.map((release, index) => (
                <motion.div
                  key={release.title}
                  initial={{ opacity: 0, y: 20 }}
                  animate={inView ? { opacity: 1, y: 0 } : {}}
                  transition={{ delay: index * 0.1 }}
                >
                  <Link href={`/${locale}/company/press/${release.title.toLowerCase().replace(/ /g, '-')}`}>
                    <Card className="p-4 transition-all duration-300 hover:shadow-md">
                      <span className="text-xs text-primary font-medium">{release.category}</span>
                      <h4 className="font-semibold mt-1 mb-2">{release.title}</h4>
                      <span className="text-sm text-muted-foreground flex items-center gap-1">
                        <Calendar className="h-3 w-3" />
                        {release.date}
                      </span>
                    </Card>
                  </Link>
                </motion.div>
              ))}
            </div>
          </div>

          {/* Media Coverage */}
          <div>
            <h3 className="font-semibold text-lg mb-4">Media Coverage</h3>
            <div className="space-y-4">
              {coverage.map((item) => (
                <Card key={item.name} className="p-4">
                  <div className="text-sm font-medium text-primary mb-1">{item.name}</div>
                  <p className="text-sm text-muted-foreground">{item.headline}</p>
                </Card>
              ))}
            </div>
          </div>
        </div>

        <div className="text-center mt-12">
          <Link
            href={`/${locale}/company/press`}
            className="inline-flex items-center px-6 py-3 rounded-lg border hover:bg-accent transition-colors"
          >
            View All Press
          </Link>
        </div>
      </div>
    </section>
  );
}
