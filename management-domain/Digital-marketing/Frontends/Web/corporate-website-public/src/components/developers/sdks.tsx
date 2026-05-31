'use client';

import * as React from 'react';
import { motion } from 'framer-motion';
import { useInView } from 'react-intersection-observer';
import { Check, Download } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';

const sdks = [
  {
    id: 'javascript',
    name: 'JavaScript / Node.js',
    icon: '⚡',
    install: 'npm install @gogidix/sdk',
    code: `import { Gogidix } from '@gogidix/sdk';

const client = new Gogidix({
  apiKey: process.env.GOGIDIX_API_KEY
});

const shipment = await client.shipments.create({
  origin: 'San Francisco, CA',
  destination: 'New York, NY',
  weight: 10
});`,
  },
  {
    id: 'python',
    name: 'Python',
    icon: '🐍',
    install: 'pip install gogidix',
    code: `import gogidix

client = gogidix.Client(
    api_key=os.environ['GOGIDIX_API_KEY']
)

shipment = client.shipments.create(
    origin='San Francisco, CA',
    destination='New York, NY',
    weight=10
)`,
  },
  {
    id: 'java',
    name: 'Java',
    icon: '☕',
    install: 'implementation com.gogidix:sdk:1.0.0',
    code: `import com.gogidix.Client;
import com.gogidix.models.Shipment;

Client client = new Client(
    System.getenv("GOGIDIX_API_KEY")
);

Shipment shipment = client.shipments()
    .create(Shipment.builder()
        .origin("San Francisco, CA")
        .destination("New York, NY")
        .weight(10)
        .build()
    );`,
  },
  {
    id: 'csharp',
    name: 'C# / .NET',
    icon: '🔷',
    install: 'dotnet add package Gogidix.SDK',
    code: `using Gogidix;

var client = new GogidixClient(
    Environment.GetEnvironmentVariable("GOGIDIX_API_KEY")
);

var shipment = await client.Shipments.CreateAsync(new Shipment {
    Origin = "San Francisco, CA",
    Destination = "New York, NY",
    Weight = 10
});`,
  },
];

export function SDKs() {
  const [ref, inView] = useInView({ triggerOnce: true, threshold: 0.1 });

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">SDKs & Libraries</h2>
          <p className="text-lg text-muted-foreground">
            Official SDKs for popular programming languages
          </p>
        </div>

        <motion.div
          ref={ref}
          initial={{ opacity: 0, y: 20 }}
          animate={inView ? { opacity: 1, y: 0 } : {}}
          className="max-w-4xl mx-auto"
        >
          <Tabs defaultValue="javascript" className="w-full">
            <TabsList className="grid grid-cols-4 w-full mb-8">
              {sdks.map((sdk) => (
                <TabsTrigger key={sdk.id} value={sdk.id}>
                  <span className="mr-2">{sdk.icon}</span>
                  {sdk.name.split('/')[0]}
                </TabsTrigger>
              ))}
            </TabsList>

            {sdks.map((sdk) => (
              <TabsContent key={sdk.id} value={sdk.id}>
                <Card className="p-6">
                  <div className="flex items-center justify-between mb-6">
                    <div>
                      <h3 className="text-2xl font-bold flex items-center gap-2">
                        <span>{sdk.icon}</span>
                        {sdk.name}
                      </h3>
                    </div>
                    <Button variant="outline" size="sm">
                      <Download className="h-4 w-4 mr-2" />
                      Download
                    </Button>
                  </div>

                  <div className="space-y-6">
                    <div>
                      <label className="text-sm font-medium mb-2 block">Install</label>
                      <code className="block bg-muted p-3 rounded-lg text-sm">
                        {sdk.install}
                      </code>
                    </div>

                    <div>
                      <label className="text-sm font-medium mb-2 block">Usage</label>
                      <pre className="bg-muted p-4 rounded-lg overflow-x-auto text-sm">
                        <code>{sdk.code}</code>
                      </pre>
                    </div>
                  </div>

                  <div className="mt-6 pt-6 border-t flex flex-wrap gap-4 text-sm text-muted-foreground">
                    <div className="flex items-center gap-2">
                      <Check className="h-4 w-4 text-green-500" />
                      <span>Type definitions included</span>
                    </div>
                    <div className="flex items-center gap-2">
                      <Check className="h-4 w-4 text-green-500" />
                      <span>Async/await support</span>
                    </div>
                    <div className="flex items-center gap-2">
                      <Check className="h-4 w-4 text-green-500" />
                      <span>Comprehensive error handling</span>
                    </div>
                  </div>
                </Card>
              </TabsContent>
            ))}
          </Tabs>
        </motion.div>
      </div>
    </section>
  );
}
