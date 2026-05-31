'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { Card } from '@/components/ui/card';

const leadership = [
  {
    name: 'Sarah Chen',
    role: 'Chief Executive Officer',
    bio: 'Former VP at Stripe, leading Gogidix vision',
  },
  {
    name: 'Michael Rodriguez',
    role: 'Chief Technology Officer',
    bio: 'Ex-Google engineering leader with 15+ years experience',
  },
  {
    name: 'Emily Watson',
    role: 'Chief Product Officer',
    bio: 'Product visionary from Salesforce and HubSpot',
  },
  {
    name: 'David Kim',
    role: 'Chief Operating Officer',
    bio: 'Operations expert from Amazon and Uber',
  },
];

export function LeadershipPreview() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Leadership</h2>
          <p className="text-lg text-muted-foreground">
            Meet the team driving Gogidix forward
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
          {leadership.map((leader, index) => (
            <motion.div
              key={leader.name}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: index * 0.1 }}
            >
              <Card className="p-6 text-center h-full">
                <Avatar className="h-24 w-24 mx-auto mb-4">
                  <AvatarFallback className="text-xl">
                    {leader.name.split(' ').map(n => n[0]).join('')}
                  </AvatarFallback>
                </Avatar>
                <h3 className="font-semibold mb-1">{leader.name}</h3>
                <p className="text-sm text-primary mb-2">{leader.role}</p>
                <p className="text-sm text-muted-foreground">{leader.bio}</p>
              </Card>
            </motion.div>
          ))}
        </div>

        <div className="text-center mt-12">
          <Link
            href={`/${locale}/company/leadership`}
            className="inline-flex items-center px-6 py-3 rounded-lg border hover:bg-accent transition-colors"
          >
            View All Leadership
          </Link>
        </div>
      </div>
    </section>
  );
}
