'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { useLocale } from 'next-intl';
import { Code2, Terminal, BookOpen } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { ArrowRight } from 'lucide-react';

export function DevelopersHero() {
  const locale = useLocale();

  return (
    <section className="py-20 md:py-32 bg-gradient-to-b from-slate-900 via-slate-900 to-background">
      <div className="container mx-auto px-4">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="max-w-4xl mx-auto text-center"
        >
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 border border-primary/20 mb-8">
            <Code2 className="h-4 w-4 text-primary" />
            <span className="text-sm font-medium">Developer Portal</span>
          </div>

          <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold mb-6 text-white">
            Build with Gogidix APIs
          </h1>

          <p className="text-xl text-slate-300 mb-10 max-w-2xl mx-auto">
            Everything you need to integrate Gogidix into your workflow. RESTful APIs,
            SDKs for every language, and a sandbox environment for testing.
          </p>

          <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
            <Button size="lg" variant="secondary" asChild>
              <Link href={`/${locale}/developers/api`}>
                <BookOpen className="mr-2 h-5 w-5" />
                View Documentation
              </Link>
            </Button>
            <Button
              size="lg"
              variant="outline"
              className="bg-transparent border-slate-600 text-white hover:bg-slate-800"
              asChild
            >
              <Link href={`/${locale}/developers/sandbox`}>
                <Terminal className="mr-2 h-5 w-5" />
                Open Sandbox
              </Link>
            </Button>
          </div>

          {/* API Key Preview */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
            className="mt-12 p-4 bg-slate-800/50 rounded-lg border border-slate-700 max-w-lg mx-auto"
          >
            <div className="text-xs text-slate-400 mb-2">Quick Start</div>
            <code className="text-sm text-green-400">
              npm install @gogidix/sdk
            </code>
          </motion.div>
        </motion.div>
      </div>
    </section>
  );
}
