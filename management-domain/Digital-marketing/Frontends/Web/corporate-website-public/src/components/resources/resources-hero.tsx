'use client';

import { motion } from 'framer-motion';
import { BookOpen } from 'lucide-react';

export function ResourcesHero() {
  return (
    <section className="py-20 md:py-32 bg-gradient-to-b from-primary/5 to-background">
      <div className="container mx-auto px-4">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="max-w-4xl mx-auto text-center"
        >
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 border border-primary/20 mb-8">
            <BookOpen className="h-4 w-4 text-primary" />
            <span className="text-sm font-medium">Resources</span>
          </div>

          <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold mb-6">
            Learn, Grow, and Succeed
          </h1>

          <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
            Access comprehensive documentation, guides, blog posts, and webinars to
            make the most of Gogidix.
          </p>
        </motion.div>
      </div>
    </section>
  );
}
