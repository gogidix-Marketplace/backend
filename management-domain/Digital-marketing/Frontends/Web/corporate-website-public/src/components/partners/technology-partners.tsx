'use client';

import * as React from 'react';
import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Code, Database, CreditCard, LineChart } from 'lucide-react';
import { Card } from '@/components/ui/card';

const categories = [
  {
    id: 'erp',
    icon: Database,
    title: 'ERP Systems',
    description: 'Integrate with leading ERP platforms',
    partners: ['SAP', 'Oracle', 'Microsoft Dynamics', 'NetSuite'],
  },
  {
    id: 'crm',
    icon: Users,
    title: 'CRM Platforms',
    description: 'Connect customer data seamlessly',
    partners: ['Salesforce', 'HubSpot', 'Microsoft Dynamics', 'Zendesk'],
  },
  {
    id: 'payment',
    icon: CreditCard,
    title: 'Payment Gateways',
    description: '100+ payment processor integrations',
    partners: ['Stripe', 'PayPal', 'Adyen', 'Braintree'],
  },
  {
    id: 'analytics',
    icon: LineChart,
    title: 'Analytics Tools',
    description: 'Push data to your favorite tools',
    partners: ['Tableau', 'Power BI', 'Looker', 'Google Analytics'],
  },
];

// Mock Users icon since it's not imported
function Users(props: React.ComponentProps<'svg'>) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
      <circle cx="9" cy="7" r="4" />
      <path d="M22 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" />
    </svg>
  );
}

export function TechnologyPartners() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Technology Partners</h2>
          <p className="text-lg text-muted-foreground">
            Integrate with leading technology platforms to extend functionality
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
          {categories.map((category, index) => {
            const Icon = category.icon;
            return (
              <motion.div
                key={category.id}
                initial={{ opacity: 0, y: 20 }}
                animate={inView ? { opacity: 1, y: 0 } : {}}
                transition={{ delay: index * 0.1 }}
              >
                <Link href={`/${locale}/partners/technology/${category.id}`}>
                  <Card className="p-6 h-full transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
                    <Icon className="h-8 w-8 text-primary mb-4" />
                    <h3 className="text-lg font-semibold mb-1">{category.title}</h3>
                    <p className="text-sm text-muted-foreground mb-4">{category.description}</p>
                    <div className="flex flex-wrap gap-1">
                      {category.partners.map((partner) => (
                        <span
                          key={partner}
                          className="text-xs px-2 py-0.5 rounded-full bg-muted"
                        >
                          {partner}
                        </span>
                      ))}
                    </div>
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
