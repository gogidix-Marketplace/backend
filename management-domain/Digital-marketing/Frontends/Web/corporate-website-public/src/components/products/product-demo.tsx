'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Play, X } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent } from '@/components/ui/dialog';

interface ProductDemoProps {
  productId: string;
}

export function ProductDemo({ productId }: ProductDemoProps) {
  const [isPlaying, setIsPlaying] = useState(false);
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.3 });

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <motion.div
          ref={ref}
          initial={{ opacity: 0, y: 20 }}
          animate={inView ? { opacity: 1, y: 0 } : {}}
          className="max-w-5xl mx-auto"
        >
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold mb-4">See It in Action</h2>
            <p className="text-lg text-muted-foreground">
              Watch a quick demo to see how it works
            </p>
          </div>

          <div className="relative aspect-video rounded-2xl overflow-hidden bg-card shadow-2xl">
            <div className="absolute inset-0 bg-gradient-to-br from-primary/20 to-accent/20" />
            <div className="absolute inset-0 flex items-center justify-center">
              <Button
                size="lg"
                variant="secondary"
                className="h-20 w-20 rounded-full"
                onClick={() => setIsPlaying(true)}
              >
                <Play className="h-8 w-8 ml-1" />
              </Button>
            </div>
            {/* Screenshot placeholder */}
            <div className="absolute inset-0 flex items-center justify-center opacity-20">
              <div className="w-3/4 h-3/4 bg-muted rounded-lg" />
            </div>
          </div>
        </motion.div>
      </div>

      <Dialog open={isPlaying} onOpenChange={setIsPlaying}>
        <DialogContent className="max-w-4xl p-0">
          <div className="aspect-video bg-black rounded-lg overflow-hidden">
            <video
              className="w-full h-full"
              controls
              autoPlay
              src={`/demos/${productId}.mp4`}
            />
          </div>
        </DialogContent>
      </Dialog>
    </section>
  );
}
