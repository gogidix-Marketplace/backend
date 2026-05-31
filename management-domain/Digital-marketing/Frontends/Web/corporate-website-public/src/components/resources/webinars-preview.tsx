'use client';

import Link from 'next-link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Video, Calendar as CalendarIcon, Users, Clock } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';

const webinars = [
  {
    id: 1,
    title: 'Supply Chain Optimization Masterclass',
    description: 'Learn proven strategies for reducing costs and improving efficiency',
    date: 'January 15, 2025',
    time: '11:00 AM PST',
    duration: '1 hour',
    speakers: ['Sarah Chen', 'Michael Rodriguez'],
    isUpcoming: true,
  },
  {
    id: 2,
    title: 'E-commerce Success Stories',
    description: 'Hear from successful merchants using Gogidix',
    date: 'December 18, 2024',
    time: '2:00 PM PST',
    duration: '45 min',
    speakers: ['Emily Watson'],
    isUpcoming: true,
  },
  {
    id: 3,
    title: 'Getting Started with APIs',
    description: 'A technical deep-dive into Gogidix APIs',
    date: 'On Demand',
    time: null,
    duration: '1 hour',
    speakers: ['David Kim'],
    isUpcoming: false,
  },
];

export function WebinarsPreview() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Webinars</h2>
          <p className="text-lg text-muted-foreground">
            Live and on-demand educational sessions
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-3 gap-6">
          {webinars.map((webinar, index) => (
            <motion.div
              key={webinar.id}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: index * 0.1 }}
            >
              <Card className="p-6 h-full">
                <div className="flex items-center gap-2 mb-4">
                  <Video className="h-5 w-5 text-primary" />
                  {webinar.isUpcoming ? (
                    <span className="text-xs px-2 py-1 rounded-full bg-green-100 text-green-700">
                      Upcoming
                    </span>
                  ) : (
                    <span className="text-xs px-2 py-1 rounded-full bg-muted text-muted-foreground">
                      On Demand
                    </span>
                  )}
                </div>
                <h3 className="font-semibold mb-2">{webinar.title}</h3>
                <p className="text-sm text-muted-foreground mb-4">{webinar.description}</p>
                <div className="space-y-2 text-sm text-muted-foreground mb-4">
                  {webinar.time && (
                    <div className="flex items-center gap-2">
                      <CalendarIcon className="h-4 w-4" />
                      {webinar.date} at {webinar.time}
                    </div>
                  )}
                  {!webinar.time && (
                    <div className="flex items-center gap-2">
                      <CalendarIcon className="h-4 w-4" />
                      {webinar.date}
                    </div>
                  )}
                  <div className="flex items-center gap-2">
                    <Clock className="h-4 w-4" />
                    {webinar.duration}
                  </div>
                  <div className="flex items-center gap-2">
                    <Users className="h-4 w-4" />
                    {webinar.speakers.join(', ')}
                  </div>
                </div>
                <Button className="w-full" variant={webinar.isUpcoming ? 'default' : 'outline'}>
                  {webinar.isUpcoming ? 'Register Now' : 'Watch Now'}
                </Button>
              </Card>
            </motion.div>
          ))}
        </div>

        <div className="text-center mt-12">
          <Link
            href={`/${locale}/resources/webinars`}
            className="inline-flex items-center gap-2 px-6 py-3 rounded-lg border hover:bg-accent transition-colors"
          >
            View All Webinars
          </Link>
        </div>
      </div>
    </section>
  );
}
