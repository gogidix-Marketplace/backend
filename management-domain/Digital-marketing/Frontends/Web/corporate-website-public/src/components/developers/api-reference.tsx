'use client';

import * as React from 'react';
import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { FileText, Lock, Globe, AlertCircle } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';

const apiSections = [
  {
    id: 'authentication',
    title: 'Authentication',
    description: 'Learn how to authenticate your API requests using API keys or OAuth',
    icon: Lock,
    endpoints: ['POST /auth/token', 'POST /auth/refresh', 'POST /auth/revoke'],
  },
  {
    id: 'shipments',
    title: 'Shipments API',
    description: 'Create, track, and manage shipments across all carriers',
    icon: Globe,
    endpoints: ['GET /shipments', 'POST /shipments', 'GET /shipments/:id'],
  },
  {
    id: 'inventory',
    title: 'Inventory API',
    description: 'Manage inventory across multiple warehouses in real-time',
    icon: FileText,
    endpoints: ['GET /inventory', 'PATCH /inventory/:id', 'POST /inventory/adjust'],
  },
  {
    id: 'webhooks',
    title: 'Webhooks',
    description: 'Configure webhooks to receive real-time event notifications',
    icon: AlertCircle,
    endpoints: ['GET /webhooks', 'POST /webhooks', 'DELETE /webhooks/:id'],
  },
];

export function APIReference() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">API Reference</h2>
          <p className="text-lg text-muted-foreground">
            Complete documentation for all Gogidix APIs
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-2 gap-6 max-w-4xl mx-auto">
          {apiSections.map((section, index) => {
            const Icon = section.icon;
            return (
              <motion.div
                key={section.id}
                initial={{ opacity: 0, y: 20 }}
                animate={inView ? { opacity: 1, y: 0 } : {}}
                transition={{ delay: index * 0.1 }}
              >
                <Link href={`/${locale}/developers/api/${section.id}`}>
                  <Card className="group p-6 h-full transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
                    <div className="flex items-start gap-4 mb-4">
                      <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center group-hover:bg-primary/20 transition-colors">
                        <Icon className="h-6 w-6 text-primary" />
                      </div>
                      <div className="flex-1">
                        <h3 className="text-xl font-semibold mb-1">{section.title}</h3>
                        <p className="text-sm text-muted-foreground">{section.description}</p>
                      </div>
                    </div>
                    <div className="space-y-1">
                      {section.endpoints.map((endpoint) => (
                        <code key={endpoint} className="block text-xs bg-muted px-2 py-1 rounded font-mono">
                          {endpoint}
                        </code>
                      ))}
                    </div>
                  </Card>
                </Link>
              </motion.div>
            );
          })}
        </div>

        <div className="text-center mt-12">
          <Button size="lg" variant="outline" asChild>
            <Link href={`/${locale}/developers/api`}>
              View Full API Reference
            </Link>
          </Button>
        </div>
      </div>
    </section>
  );
}
