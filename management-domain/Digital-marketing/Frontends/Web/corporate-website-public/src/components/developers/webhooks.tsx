'use client';

import * as React from 'react';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Webhook, Bell, Shield, RefreshCw } from 'lucide-react';
import { Card } from '@/components/ui/card';

const events = [
  { category: 'Shipments', events: ['shipment.created', 'shipment.updated', 'shipment.delivered'] },
  { category: 'Inventory', events: ['inventory.low', 'inventory.out_of_stock', 'inventory.restocked'] },
  { category: 'Orders', events: ['order.created', 'order.paid', 'order.cancelled'] },
  { category: 'Webhooks', events: ['webhook.delivery.failed', 'webhook.delivery.succeeded'] },
];

export function Webhooks() {
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Webhooks</h2>
          <p className="text-lg text-muted-foreground">
            Real-time event notifications to your endpoints
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-6 max-w-5xl mx-auto mb-12">
          <Card className="p-6">
            <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center mb-4">
              <Bell className="h-6 w-6 text-primary" />
            </div>
            <h3 className="text-lg font-semibold mb-2">Event Types</h3>
            <p className="text-sm text-muted-foreground">
              Subscribe to specific events that matter to your application
            </p>
          </Card>

          <Card className="p-6">
            <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center mb-4">
              <Shield className="h-6 w-6 text-primary" />
            </div>
            <h3 className="text-lg font-semibold mb-2">Webhook Security</h3>
            <p className="text-sm text-muted-foreground">
              HMAC signatures to verify webhook authenticity
            </p>
          </Card>

          <Card className="p-6">
            <div className="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center mb-4">
              <RefreshCw className="h-6 w-6 text-primary" />
            </div>
            <h3 className="text-lg font-semibold mb-2">Retry Policy</h3>
            <p className="text-sm text-muted-foreground">
              Automatic retries with exponential backoff
            </p>
          </Card>
        </div>

        <motion.div
          ref={ref}
          initial={{ opacity: 0, y: 20 }}
          animate={inView ? { opacity: 1, y: 0 } : {}}
          className="max-w-3xl mx-auto"
        >
          <Card className="p-6">
            <h3 className="text-xl font-semibold mb-4">Available Events</h3>
            <div className="space-y-4">
              {events.map((category) => (
                <div key={category.category}>
                  <div className="text-sm font-medium text-muted-foreground mb-2">
                    {category.category}
                  </div>
                  <div className="flex flex-wrap gap-2">
                    {category.events.map((event) => (
                      <code
                        key={event}
                        className="px-3 py-1 bg-muted rounded text-sm"
                      >
                        {event}
                      </code>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </motion.div>
      </div>
    </section>
  );
}
