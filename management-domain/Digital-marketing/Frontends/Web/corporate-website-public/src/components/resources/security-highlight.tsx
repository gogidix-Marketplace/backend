'use client';

import Link from 'next/link';
import { useLocale } from 'next-intl';
import { Shield, Check, AlertCircle } from 'lucide-react';
import { Card } from '@/components/ui/card';

const compliance = [
  { name: 'SOC 2 Type II', status: 'certified' },
  { name: 'GDPR', status: 'compliant' },
  { name: 'HIPAA', status: 'ready' },
  { name: 'ISO 27001', status: 'certified' },
];

const features = [
  { icon: Shield, title: 'End-to-end Encryption', description: 'All data encrypted in transit and at rest' },
  { icon: Check, title: 'SSO & SAML', description: 'Enterprise single sign-on support' },
  { icon: Check, title: 'Audit Logs', description: 'Complete activity tracking' },
  { icon: AlertCircle, title: 'Bug Bounty', description: 'Responsible disclosure program' },
];

export function SecurityHighlight() {
  const locale = useLocale();

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="grid lg:grid-cols-2 gap-16 items-center">
          <div>
            <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 text-primary text-sm font-medium mb-4">
              <Shield className="h-4 w-4" />
              Security
            </div>
            <h2 className="text-3xl md:text-4xl font-bold mb-4">
              Enterprise-Grade Security
            </h2>
            <p className="text-lg text-muted-foreground mb-6">
              We take security seriously. Our platform is built with industry-leading
              security practices and certifications to protect your data.
            </p>

            <div className="grid grid-cols-2 gap-4 mb-6">
              {compliance.map((cert) => (
                <div key={cert.name} className="flex items-center gap-2">
                  <Check className="h-5 w-5 text-green-500" />
                  <span className="font-medium">{cert.name}</span>
                </div>
              ))}
            </div>

            <Link
              href={`/${locale}/resources/security`}
              className="inline-flex items-center text-primary font-medium hover:gap-2 transition-all"
            >
              Learn more about our security practices
            </Link>
          </div>

          <div className="grid sm:grid-cols-2 gap-4">
            {features.map((feature) => {
              const Icon = feature.icon;
              return (
                <Card key={feature.title} className="p-6">
                  <Icon className="h-8 w-8 text-primary mb-4" />
                  <h3 className="font-semibold mb-2">{feature.title}</h3>
                  <p className="text-sm text-muted-foreground">{feature.description}</p>
                </Card>
              );
            })}
          </div>
        </div>
      </div>
    </section>
  );
}
