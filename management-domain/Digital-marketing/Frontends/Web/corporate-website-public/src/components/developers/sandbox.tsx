'use client';

import * as React from 'react';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Terminal, Play, RotateCcw } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';

export function Sandbox() {
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });
  const [isRunning, setIsRunning] = React.useState(false);

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Sandbox Environment</h2>
          <p className="text-lg text-muted-foreground">
            Test your integrations in a safe environment with mock data
          </p>
        </div>

        <motion.div
          ref={ref}
          initial={{ opacity: 0, y: 20 }}
          animate={inView ? { opacity: 1, y: 0 } : {}}
          className="max-w-4xl mx-auto"
        >
          <Card className="overflow-hidden">
            {/* Header */}
            <div className="bg-slate-900 text-white p-4 flex items-center justify-between">
              <div className="flex items-center gap-2">
                <Terminal className="h-5 w-5" />
                <span className="text-sm font-medium">API Playground</span>
              </div>
              <div className="flex items-center gap-2">
                <Button size="sm" variant="secondary" onClick={() => setIsRunning(!isRunning)}>
                  {isRunning ? (
                    <>
                      <RotateCcw className="h-4 w-4 mr-2" />
                      Reset
                    </>
                  ) : (
                    <>
                      <Play className="h-4 w-4 mr-2" />
                      Run
                    </>
                  )}
                </Button>
              </div>
            </div>

            {/* Request/Response */}
            <div className="grid md:grid-cols-2 divide-x">
              {/* Request */}
              <div className="p-6">
                <div className="text-xs font-medium text-muted-foreground mb-2">REQUEST</div>
                <pre className="text-sm bg-muted p-4 rounded-lg overflow-x-auto">
                  <code>{`GET /v1/shipments
Authorization: Bearer sk_test_...
X-Gogidix-Sandbox: true

{
  "status": "in_transit",
  "limit": 10
}`}</code>
                </pre>
              </div>

              {/* Response */}
              <div className="p-6">
                <div className="text-xs font-medium text-muted-foreground mb-2">RESPONSE</div>
                <pre className="text-sm bg-muted p-4 rounded-lg overflow-x-auto">
                  <code>{`{
  "data": [
    {
      "id": "ship_test_123",
      "status": "in_transit",
      "origin": "San Francisco, CA",
      "destination": "New York, NY",
      "tracking": "1Z999AA10123456784"
    }
  ],
  "has_more": true,
  "total_count": 42
}`}</code>
                </pre>
              </div>
            </div>
          </Card>

          {/* Features */}
          <div className="grid md:grid-cols-3 gap-4 mt-8">
            <Card className="p-4">
              <div className="text-sm font-medium mb-1">Test Data</div>
              <div className="text-xs text-muted-foreground">
                Pre-populated with realistic test scenarios
              </div>
            </Card>
            <Card className="p-4">
              <div className="text-sm font-medium mb-1">No Production Impact</div>
              <div className="text-xs text-muted-foreground">
                Completely isolated from production systems
              </div>
            </Card>
            <Card className="p-4">
              <div className="text-sm font-medium mb-1">Free to Use</div>
              <div className="text-xs text-muted-foreground">
                Unlimited requests in sandbox mode
              </div>
            </Card>
          </div>
        </motion.div>
      </div>
    </section>
  );
}
