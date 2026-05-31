'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Calendar, Clock, ArrowRight } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';

const posts = [
  {
    id: 1,
    title: '5 Ways to Optimize Your Supply Chain in 2024',
    excerpt: 'Discover the latest strategies for streamlining your logistics operations and reducing costs.',
    author: 'Sarah Chen',
    date: 'December 15, 2024',
    readTime: '5 min read',
    category: 'Logistics',
  },
  {
    id: 2,
    title: 'The Future of E-commerce: Trends to Watch',
    excerpt: 'From AI-powered recommendations to same-day delivery, learn what\'s next for online retail.',
    author: 'Michael Rodriguez',
    date: 'December 10, 2024',
    readTime: '7 min read',
    category: 'E-commerce',
  },
  {
    id: 3,
    title: 'Understanding API-First Architecture',
    excerpt: 'Learn why building APIs first leads to better integrations and more flexible applications.',
    author: 'Emily Watson',
    date: 'December 5, 2024',
    readTime: '10 min read',
    category: 'Engineering',
  },
];

export function BlogPreview() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Latest from the Blog</h2>
          <p className="text-lg text-muted-foreground">
            Insights, best practices, and industry news
          </p>
        </div>

        <div ref={ref} className="grid md:grid-cols-3 gap-8">
          {posts.map((post, index) => (
            <motion.article
              key={post.id}
              initial={{ opacity: 0, y: 20 }}
              animate={inView ? { opacity: 1, y: 0 } : {}}
              transition={{ delay: index * 0.1 }}
            >
              <Link href={`/${locale}/resources/blog/${post.id}`}>
                <Card className="p-6 h-full transition-all duration-300 hover:shadow-lg">
                  <span className="text-xs px-2 py-1 rounded-full bg-primary/10 text-primary mb-4 inline-block">
                    {post.category}
                  </span>
                  <h3 className="text-xl font-semibold mb-3 line-clamp-2">{post.title}</h3>
                  <p className="text-sm text-muted-foreground mb-4 line-clamp-3">{post.excerpt}</p>
                  <div className="flex items-center justify-between pt-4 border-t">
                    <div className="flex items-center gap-2">
                      <Avatar className="h-8 w-8">
                        <AvatarFallback className="text-xs">
                          {post.author.split(' ').map(n => n[0]).join('')}
                        </AvatarFallback>
                      </Avatar>
                      <span className="text-sm">{post.author}</span>
                    </div>
                    <span className="text-xs text-muted-foreground">{post.readTime}</span>
                  </div>
                </Card>
              </Link>
            </motion.article>
          ))}
        </div>

        <div className="text-center mt-12">
          <Link
            href={`/${locale}/resources/blog`}
            className="inline-flex items-center gap-2 px-6 py-3 rounded-lg border hover:bg-accent transition-colors"
          >
            View All Posts
            <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
      </div>
    </section>
  );
}
