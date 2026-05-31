'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { BadgeCheck, Users2, Briefcase, TrendingUp } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';

const benefits = [
  { icon: BadgeCheck, title: 'Implementation Certifications', description: 'Become a certified expert' },
  { icon: Users2, title: 'Lead Generation', description: 'We send qualified leads your way' },
  { icon: Briefcase, title: 'Co-selling Opportunities', description: 'Sell alongside our team' },
  { icon: TrendingUp, title: 'Partner Portal Access', description: 'Manage deals and track commissions' },
];

const integrators = [
  { name: 'Deloitte', region: 'Global', specialties: ['Enterprise', 'Supply Chain'] },
  { name: 'Accenture', region: 'Global', specialties: ['Digital Transformation', 'Cloud'] },
  { name: 'TechConsult', region: 'North America', specialties: ['SMB', 'Quick Implementation'] },
];

export function SystemIntegrators() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="grid lg:grid-cols-2 gap-16 items-center mb-16">
          <div>
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 text-primary text-sm font-medium mb-4">
              System Integrators
            </div>
            <h2 className="text-3xl md:text-4xl font-bold mb-4">
              Expert Implementation Partners
            </h2>
            <p className="text-lg text-muted-foreground mb-6">
              Partner with us to deliver complex deployments for enterprise customers.
              Our SI program provides training, support, and co-selling opportunities.
            </p>
          </div>

          <div ref={ref} className="grid sm:grid-cols-2 gap-4">
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
                    <Icon className="h-6 w-6 text-primary mb-2" />
                    <h3 className="font-semibold mb-1">{benefit.title}</h3>
                    <p className="text-sm text-muted-foreground">{benefit.description}</p>
                  </Card>
                </motion.div>
              );
            })}
          </div>
        </div>

        {/* Featured Integrators */}
        <div className="bg-muted/30 rounded-2xl p-8">
          <div className="text-center mb-8">
            <h3 className="text-2xl font-bold mb-2">Featured System Integrators</h3>
            <p className="text-muted-foreground">Our certified partners ready to help</p>
          </div>

          <div className="grid md:grid-cols-3 gap-6 max-w-4xl mx-auto">
            {integrators.map((integrator) => (
              <Card key={integrator.name} className="p-6 text-center">
                <Avatar className="h-16 w-16 mx-auto mb-4">
                  <AvatarFallback className="text-xl">{integrator.name.slice(0, 2)}</AvatarFallback>
                </Avatar>
                <h4 className="font-semibold mb-1">{integrator.name}</h4>
                <p className="text-sm text-muted-foreground mb-3">{integrator.region}</p>
                <div className="flex justify-center gap-1 flex-wrap">
                  {integrator.specialties.map((specialty) => (
                    <span
                      key={specialty}
                      className="text-xs px-2 py-0.5 rounded-full bg-primary/10 text-primary"
                    >
                      {specialty}
                    </span>
                  ))}
                </div>
              </Card>
            ))}
          </div>

          <div className="text-center mt-8">
            <Button variant="outline" asChild>
              <Link href={`/${locale}/partners/directory`}>
                View Partner Directory
              </Link>
            </Button>
          </div>
        </div>
      </div>
    </section>
  );
}
