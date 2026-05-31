'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Package, Users, TrendingUp, Award, HeadphonesIcon as Headphones, CheckCircle } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';

const benefits = [
  { icon: Package, title: 'Full platform customization', description: 'Brand the platform as your own' },
  { icon: Users, title: 'Dedicated account manager', description: 'Personal support for your business' },
  { icon: TrendingUp, title: 'Revenue sharing model', description: 'Earn competitive commissions' },
  { icon: Award, title: 'Training and certification', description: 'Become a certified expert' },
  { icon: Headphones, title: 'Technical support', description: '24/7 access to our team' },
  { icon: CheckCircle, title: 'Marketing resources', description: 'Co-marketing opportunities' },
];

export function WhiteLabelPartners() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="grid lg:grid-cols-2 gap-16 items-center">
          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={inView ? { opacity: 1, x: 0 } : {}}
            transition={{ duration: 0.5 }}
          >
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 text-primary text-sm font-medium mb-4">
              White-Label Partners
            </div>
            <h2 className="text-3xl md:text-4xl font-bold mb-4">
              Resell Gogidix Under Your Brand
            </h2>
            <p className="text-lg text-muted-foreground mb-6">
              Launch your own logistics and operations platform with our white-label solution.
              We provide the technology, you provide the customer relationships.
            </p>
            <Button size="lg" asChild>
              <Link href={`/${locale}/partners/white-label#apply`}>
                Become a Partner
              </Link>
            </Button>
          </motion.div>

          <motion.div
            ref={ref}
            initial={{ opacity: 0, x: 20 }}
            animate={inView ? { opacity: 1, x: 0 } : {}}
            transition={{ duration: 0.5 }}
          >
            <div className="grid sm:grid-cols-2 gap-4">
              {benefits.map((benefit, index) => {
                const Icon = benefit.icon;
                return (
                  <motion.div
                    key={benefit.title}
                    initial={{ opacity: 0, y: 20 }}
                    animate={inView ? { opacity: 1, y: 0 } : {}}
                    transition={{ delay: index * 0.1 }}
                  >
                    <Card className="p-4 h-full">
                      <Icon className="h-8 w-8 text-primary mb-3" />
                      <h3 className="font-semibold mb-1">{benefit.title}</h3>
                      <p className="text-sm text-muted-foreground">{benefit.description}</p>
                    </Card>
                  </motion.div>
                );
              })}
            </div>
          </motion.div>
        </div>
      </div>
    </section>
  );
}
