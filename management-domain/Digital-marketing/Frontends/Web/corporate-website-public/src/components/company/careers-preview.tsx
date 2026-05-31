'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { MapPin, DollarSign, Clock, Briefcase } from 'lucide-react';
import { Card } from '@/components/ui/card';

const openings = [
  { title: 'Senior Frontend Engineer', department: 'Engineering', location: 'Remote', type: 'Full-time' },
  { title: 'Product Designer', department: 'Design', location: 'San Francisco, CA', type: 'Full-time' },
  { title: 'Sales Manager', department: 'Sales', location: 'New York, NY', type: 'Full-time' },
  { title: 'DevOps Engineer', department: 'Engineering', location: 'Remote', type: 'Full-time' },
];

const benefits = [
  { icon: MapPin, title: 'Remote-first culture' },
  { icon: DollarSign, title: 'Competitive compensation' },
  { icon: Clock, title: 'Unlimited PTO' },
  { icon: Briefcase, title: 'Learning budget' },
];

export function CareersPreview() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="grid lg:grid-cols-2 gap-16">
          <div>
            <h2 className="text-3xl md:text-4xl font-bold mb-4">Careers</h2>
            <p className="text-lg text-muted-foreground mb-6">
              Build the future of business automation with us. We're always looking for
              talented people to join our team.
            </p>

            <div className="grid grid-cols-2 gap-4 mb-8">
              {benefits.map((benefit) => {
                const Icon = benefit.icon;
                return (
                  <div key={benefit.title} className="flex items-center gap-3">
                    <Icon className="h-5 w-5 text-primary" />
                    <span className="text-sm">{benefit.title}</span>
                  </div>
                );
              })}
            </div>

            <Link
              href={`/${locale}/company/careers`}
              className="inline-flex items-center px-6 py-3 rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 transition-colors"
            >
              View All Openings
            </Link>
          </div>

          <div ref={ref} className="space-y-4">
            <h3 className="font-semibold text-lg">Open Positions</h3>
            {openings.map((job, index) => (
              <motion.div
                key={job.title}
                initial={{ opacity: 0, x: 20 }}
                animate={inView ? { opacity: 1, x: 0 } : {}}
                transition={{ delay: index * 0.1 }}
              >
                <Link href={`/${locale}/company/careers/${job.title.toLowerCase().replace(/ /g, '-')}`}>
                  <Card className="p-4 transition-all duration-300 hover:shadow-md hover:-translate-y-0.5">
                    <div className="flex items-start justify-between">
                      <div>
                        <h4 className="font-semibold mb-1">{job.title}</h4>
                        <p className="text-sm text-muted-foreground">{job.department}</p>
                      </div>
                      <span className="text-xs px-2 py-1 rounded-full bg-primary/10 text-primary">
                        {job.type}
                      </span>
                    </div>
                    <div className="flex items-center gap-4 mt-3 text-sm text-muted-foreground">
                      <span className="flex items-center gap-1">
                        <MapPin className="h-3 w-3" />
                        {job.location}
                      </span>
                    </div>
                  </Card>
                </Link>
              </motion.div>
            ))}
          </div>
        </div>
      </div>
    </section>
  );
}
