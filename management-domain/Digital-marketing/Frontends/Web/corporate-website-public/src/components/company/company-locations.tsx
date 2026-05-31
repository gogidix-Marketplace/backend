'use client';

import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { MapPin, Mail, Phone } from 'lucide-react';
import { Card } from '@/components/ui/card';

const offices = [
  {
    city: 'San Francisco',
    country: 'United States',
    type: 'Headquarters',
    address: '123 Market Street\nSan Francisco, CA 94105',
  },
  {
    city: 'New York',
    country: 'United States',
    type: 'Sales Office',
    address: '456 Broadway\nNew York, NY 10013',
  },
  {
    city: 'London',
    country: 'United Kingdom',
    type: 'Regional HQ',
    address: '78 Kensington High Street\nLondon W8 7HA',
  },
  {
    city: 'Singapore',
    country: 'Singapore',
    type: 'APAC Hub',
    address: '12 Marina Boulevard\nSingapore 018982',
  },
];

export function CompanyLocations() {
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Our Offices</h2>
          <p className="text-lg text-muted-foreground">
            Find us around the world
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
          {offices.map((office, index) => (
            <motion.div
              key={office.city}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: index * 0.1 }}
            >
              <Card className="p-6 h-full">
                <div className="flex items-start gap-3 mb-4">
                  <MapPin className="h-5 w-5 text-primary shrink-0 mt-1" />
                  <div>
                    <h3 className="font-semibold">{office.city}</h3>
                    <p className="text-sm text-muted-foreground">{office.country}</p>
                  </div>
                </div>
                <span className="text-xs px-2 py-1 rounded-full bg-primary/10 text-primary">
                  {office.type}
                </span>
                <p className="text-sm text-muted-foreground mt-4 whitespace-pre-line">
                  {office.address}
                </p>
              </Card>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
