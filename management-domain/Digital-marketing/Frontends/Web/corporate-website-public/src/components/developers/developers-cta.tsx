'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Button } from '@/components/ui/button';
import { ArrowRight, Github, MessageCircle } from 'lucide-react';

export function DevelopersCTA() {
  const locale = useLocale();
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.3 });

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <motion.div
          ref={ref}
          initial={{ opacity: 0, scale: 0.95 }}
          animate={inView ? { opacity: 1, scale: 1 } : {}}
          transition={{ duration: 0.5 }}
          className="max-w-4xl mx-auto"
        >
          <div className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-primary via-primary/90 to-accent p-12 md:p-20 text-center text-primary-foreground">
            {/* Background Pattern */}
            <div className="absolute inset-0 bg-[radial-gradient(circle_at_50%_120%,rgba(255,255,255,0.1),transparent_50%)]" />

            {/* Content */}
            <div className="relative z-10">
              <h2 className="text-3xl md:text-4xl font-bold mb-4">
                Ready to Start Building?
              </h2>
              <p className="text-lg text-primary-foreground/90 mb-8 max-w-xl mx-auto">
                Get your API key and start integrating Gogidix into your application today.
              </p>

              <div className="flex flex-col sm:flex-row items-center justify-center gap-4 mb-8">
                <Button size="lg" variant="secondary" asChild>
                  <Link href={`/${locale}/signup`}>
                    Get API Key
                    <ArrowRight className="ml-2 h-5 w-5" />
                  </Link>
                </Button>
                <Button
                  size="lg"
                  variant="outline"
                  className="bg-transparent border-primary-foreground text-primary-foreground hover:bg-primary-foreground/10"
                  asChild
                >
                  <Link href="https://github.com/gogidix" target="_blank">
                    <Github className="mr-2 h-5 w-5" />
                    View on GitHub
                  </Link>
                </Button>
              </div>

              <div className="flex items-center justify-center gap-6 text-sm">
                <a
                  href="https://discord.gg/gogidix"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center gap-2 hover:underline"
                >
                  <MessageCircle className="h-4 w-4" />
                  Join Discord
                </a>
                <span>•</span>
                <a
                  href="https://stackoverflow.com/questions/tagged/gogidix"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="hover:underline"
                >
                  Stack Overflow
                </a>
                <span>•</span>
                <a
                  href={`/${locale}/support`}
                  className="hover:underline"
                >
                  Support
                </a>
              </div>
            </div>
          </div>
        </motion.div>
      </div>
    </section>
  );
}
