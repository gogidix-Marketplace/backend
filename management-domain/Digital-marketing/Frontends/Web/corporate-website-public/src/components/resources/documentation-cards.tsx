'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Book, FileCode, GraduationCap, Video } from 'lucide-react';
import { Card } from '@/components/ui/card';

const docs = [
  {
    icon: Book,
    title: 'Getting Started',
    description: 'Quick start guide to get you up and running',
    href: '/resources/docs/quickstart',
  },
  {
    icon: FileCode,
    title: 'API Reference',
    description: 'Complete API documentation and examples',
    href: '/developers/api',
  },
  {
    icon: GraduationCap,
    title: 'Guides & Tutorials',
    description: 'Step-by-step guides for common tasks',
    href: '/resources/docs/guides',
  },
  {
    icon: Video,
    title: 'Video Tutorials',
    description: 'Watch and learn from our experts',
    href: '/resources/videos',
  },
];

export function DocumentationCards() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Documentation</h2>
          <p className="text-lg text-muted-foreground">
            Comprehensive guides and documentation
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
          {docs.map((doc, index) => {
            const Icon = doc.icon;
            return (
              <motion.div
                key={doc.title}
                initial={{ opacity: 0, y: 20 }}
                animate={inView ? { opacity: 1, y: 0 } : {}}
                transition={{ delay: index * 0.1 }}
              >
                <Link href={`/${locale}${doc.href}`}>
                  <Card className="p-6 h-full transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
                    <Icon className="h-8 w-8 text-primary mb-4" />
                    <h3 className="font-semibold mb-2">{doc.title}</h3>
                    <p className="text-sm text-muted-foreground">{doc.description}</p>
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
