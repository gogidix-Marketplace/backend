'use client';

import * as React from 'react';
import { useTranslations } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Star, Quote } from 'lucide-react';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Card } from '@/components/ui/card';

const testimonials = [
  {
    id: 1,
    name: 'Sarah Johnson',
    role: 'CEO, TechStart Inc.',
    content: 'Gogidix transformed our logistics operations. We reduced delivery times by 40% and cut costs significantly.',
    rating: 5,
    avatar: '/avatars/sarah.jpg',
  },
  {
    id: 2,
    name: 'Michael Chen',
    role: 'Operations Director, RetailCo',
    content: 'The unified platform approach means we no longer have siloed systems. Everything works together seamlessly.',
    rating: 5,
    avatar: '/avatars/michael.jpg',
  },
  {
    id: 3,
    name: 'Emily Rodriguez',
    role: 'CTO, LogisticsPlus',
    content: 'The API-first architecture made integration with our existing tools a breeze. Developer experience is top-notch.',
    rating: 5,
    avatar: '/avatars/emily.jpg',
  },
];

const stats = [
  { value: '10,000+', label: 'Active Companies' },
  { value: '99.99%', label: 'Uptime SLA' },
  { value: '150+', label: 'Countries Served' },
  { value: '500M+', label: 'Transactions/Month' },
];

export function SocialProofSection() {
  const t = useTranslations('home.socialProof');
  const [ref, inView] = useInView({
    triggerOnce: true,
    threshold: 0.1,
  });

  return (
    <section className="py-20 border-y bg-muted/30">
      <div className="container mx-auto px-4">
        {/* Stats */}
        <motion.div
          ref={ref}
          initial={{ opacity: 0, y: 20 }}
          animate={inView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.5 }}
          className="grid grid-cols-2 md:grid-cols-4 gap-8 mb-20"
        >
          {stats.map((stat, index) => (
            <motion.div
              key={stat.label}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: index * 0.1, duration: 0.5 }}
              className="text-center"
            >
              <div className="text-3xl md:text-4xl font-bold text-primary mb-1">{stat.value}</div>
              <div className="text-sm text-muted-foreground">{stat.label}</div>
            </motion.div>
          ))}
        </motion.div>

        {/* Testimonials */}
        <motion.div
          initial={{ opacity: 0 }}
          animate={inView ? { opacity: 1 } : {}}
          transition={{ delay: 0.3 }}
          className="text-center mb-12"
        >
          <h2 className="text-3xl font-bold mb-4">{t('title')}</h2>
          <p className="text-muted-foreground max-w-2xl mx-auto">
            See what our customers have to say about transforming their business with Gogidix
          </p>
        </motion.div>

        <div className="grid md:grid-cols-3 gap-6">
          {testimonials.map((testimonial, index) => (
            <motion.div
              key={testimonial.id}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: 0.4 + index * 0.1 }}
            >
              <Card className="p-6 h-full">
                <div className="flex items-center gap-1 mb-4">
                  {[...Array(testimonial.rating)].map((_, i) => (
                    <Star key={i} className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                  ))}
                </div>
                <p className="text-muted-foreground mb-6 relative">
                  <Quote className="absolute -top-2 -left-2 h-8 w-8 text-primary/10" />
                  {testimonial.content}
                </p>
                <div className="flex items-center gap-3">
                  <Avatar>
                    <AvatarImage src={testimonial.avatar} alt={testimonial.name} />
                    <AvatarFallback>
                      {testimonial.name
                        .split(' ')
                        .map((n) => n[0])
                        .join('')}
                    </AvatarFallback>
                  </Avatar>
                  <div>
                    <div className="font-medium">{testimonial.name}</div>
                    <div className="text-sm text-muted-foreground">{testimonial.role}</div>
                  </div>
                </div>
              </Card>
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
