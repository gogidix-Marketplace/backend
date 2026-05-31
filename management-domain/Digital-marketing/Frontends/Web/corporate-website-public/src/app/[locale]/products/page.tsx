import { useTranslations } from 'next-intl';
import { Metadata } from 'next';
import Link from 'next/link';
import { ProductsGrid } from '@/components/products/products-grid';
import { ProductsHero } from '@/components/products/products-hero';
import { Button } from '@/components/ui/button';
import { ArrowRight } from 'lucide-react';

export async function generateMetadata({
  params,
}: {
  params: Promise<{ locale: string }>;
}): Promise<Metadata> {
  const { locale } = await params;

  return {
    title: 'Products',
    description: 'Explore our comprehensive suite of enterprise-grade solutions for logistics, e-commerce, procurement, and business operations.',
    openGraph: {
      locale,
    },
  };
}

export default function ProductsPage() {
  return (
    <>
      <ProductsHero />
      <ProductsGrid />
      <ProductPricingSection />
      <ProductCTASection />
    </>
  );
}

function ProductPricingSection() {
  const t = useTranslations();

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Simple, Transparent Pricing</h2>
          <p className="text-lg text-muted-foreground">
            Choose the plan that fits your business needs. Scale as you grow.
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-8 max-w-5xl mx-auto">
          {[
            { name: 'Starter', price: '$99', description: 'Perfect for small teams getting started' },
            { name: 'Professional', price: '$299', description: 'For growing businesses with more needs', popular: true },
            { name: 'Enterprise', price: 'Custom', description: 'Tailored solutions for large organizations' },
          ].map((plan, i) => (
            <div
              key={plan.name}
              className={`relative p-8 rounded-2xl border bg-card ${
                plan.popular ? 'border-primary shadow-lg scale-105' : ''
              }`}
            >
              {plan.popular && (
                <div className="absolute -top-4 left-1/2 -translate-x-1/2 px-4 py-1 bg-primary text-primary-foreground text-sm font-medium rounded-full">
                  Most Popular
                </div>
              )}
              <h3 className="text-xl font-semibold mb-2">{plan.name}</h3>
              <div className="text-3xl font-bold mb-2">{plan.price}<span className="text-base font-normal text-muted-foreground">/mo</span></div>
              <p className="text-muted-foreground mb-6">{plan.description}</p>
              <Button className="w-full" variant={plan.popular ? 'default' : 'outline'}>
                Get Started
              </Button>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function ProductCTASection() {
  const t = useTranslations();

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-3xl md:text-4xl font-bold mb-6">Need help choosing?</h2>
          <p className="text-lg text-muted-foreground mb-8">
            Our team can help you find the right solution for your business needs.
          </p>
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
            <Button size="lg" asChild>
              <Link href="/contact">
                Contact Sales
                <ArrowRight className="ml-2 h-5 w-5" />
              </Link>
            </Button>
            <Button size="lg" variant="outline" asChild>
              <Link href="/developers">View Documentation</Link>
            </Button>
          </div>
        </div>
      </div>
    </section>
  );
}
