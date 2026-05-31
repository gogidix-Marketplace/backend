import { Metadata } from 'next';
import { notFound } from 'next/navigation';
import { useTranslations } from 'next-intl';
import Link from 'next/link';
import { PRODUCTS } from '@/lib/constants';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import { ArrowRight, Check, Star } from 'lucide-react';
import { ProductDemo } from '@/components/products/product-demo';
import { ProductFeatures } from '@/components/products/product-features';

interface ProductPageProps {
  params: Promise<{ locale: string; slug: string }>;
}

export async function generateStaticParams() {
  return PRODUCTS.map((product) => ({
    slug: product.slug,
  }));
}

export async function generateMetadata({ params }: ProductPageProps): Promise<Metadata> {
  const { slug, locale } = await params;
  const product = PRODUCTS.find((p) => p.slug === slug);

  if (!product) {
    return {
      title: 'Product Not Found',
    };
  }

  return {
    title: product.name,
    description: product.description,
  };
}

export default async function ProductPage({ params }: ProductPageProps) {
  const { slug, locale } = await params;
  const product = PRODUCTS.find((p) => p.slug === slug);

  if (!product) {
    notFound();
  }

  return (
    <>
      <ProductHero product={product} />
      <ProductFeatures productId={product.id} />
      <ProductBenefits productId={product.id} />
      <ProductDemo productId={product.id} />
      <ProductPricing />
      <ProductCTA product={product} />
    </>
  );
}

function ProductHero({ product }: { product: typeof PRODUCTS[number] }) {
  return (
    <section className="py-20 md:py-32 bg-gradient-to-b from-primary/5 to-background">
      <div className="container mx-auto px-4">
        <div className="max-w-4xl mx-auto text-center">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 border border-primary/20 mb-6">
            <Star className="h-4 w-4 text-primary fill-primary" />
            <span className="text-sm font-medium">Trusted by 10,000+ businesses</span>
          </div>
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold mb-6">
            {product.name}
          </h1>
          <p className="text-xl text-muted-foreground mb-8">
            {product.description}
          </p>
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
            <Button size="lg" asChild>
              <Link href={`/${locale}/signup?product=${product.slug}`}>
                Start Free Trial
                <ArrowRight className="ml-2 h-5 w-5" />
              </Link>
            </Button>
            <Button size="lg" variant="outline" asChild>
              <Link href={`/contact?product=${product.slug}`}>Contact Sales</Link>
            </Button>
          </div>
        </div>
      </div>
    </section>
  );
}

function ProductBenefits({ productId }: { productId: string }) {
  const benefits = [
    {
      title: 'Increase Efficiency',
      description: 'Automate manual processes and reduce errors by up to 90%',
      stat: '90%',
    },
    {
      title: 'Reduce Costs',
      description: 'Cut operational costs through intelligent optimization',
      stat: '40%',
    },
    {
      title: 'Save Time',
      description: 'Streamline workflows and accelerate time-to-value',
      stat: '60%',
    },
  ];

  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Proven Results</h2>
          <p className="text-lg text-muted-foreground">
            See how businesses transform with {productId}
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-8">
          {benefits.map((benefit) => (
            <Card key={benefit.title} className="p-8 text-center">
              <div className="text-5xl font-bold text-primary mb-4">{benefit.stat}</div>
              <h3 className="text-xl font-semibold mb-2">{benefit.title}</h3>
              <p className="text-muted-foreground">{benefit.description}</p>
            </Card>
          ))}
        </div>
      </div>
    </section>
  );
}

function ProductPricing() {
  const plans = [
    { name: 'Starter', price: 99, features: ['Up to 1,000 transactions/mo', 'Basic analytics', 'Email support'] },
    { name: 'Professional', price: 299, features: ['Up to 10,000 transactions/mo', 'Advanced analytics', 'Priority support', 'API access'], popular: true },
    { name: 'Enterprise', price: null, features: ['Unlimited transactions', 'Custom integrations', 'Dedicated support', 'SLA guarantee'] },
  ];

  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="text-center max-w-2xl mx-auto mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Simple Pricing</h2>
          <p className="text-lg text-muted-foreground">
            Choose the plan that fits your needs. No hidden fees.
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-8 max-w-5xl mx-auto">
          {plans.map((plan) => (
            <Card
              key={plan.name}
              className={`p-8 ${plan.popular ? 'border-primary shadow-lg scale-105' : ''}`}
            >
              {plan.popular && (
                <div className="text-center mb-4">
                  <span className="inline-block px-3 py-1 bg-primary text-primary-foreground text-sm font-medium rounded-full">
                    Most Popular
                  </span>
                </div>
              )}
              <h3 className="text-xl font-semibold text-center mb-4">{plan.name}</h3>
              <div className="text-center mb-6">
                {plan.price ? (
                  <>
                    <span className="text-4xl font-bold">${plan.price}</span>
                    <span className="text-muted-foreground">/mo</span>
                  </>
                ) : (
                  <span className="text-2xl font-bold">Custom</span>
                )}
              </div>
              <ul className="space-y-3 mb-8">
                {plan.features.map((feature) => (
                  <li key={feature} className="flex items-center gap-2">
                    <Check className="h-5 w-5 text-primary" />
                    <span>{feature}</span>
                  </li>
                ))}
              </ul>
              <Button className="w-full" variant={plan.popular ? 'default' : 'outline'}>
                {plan.price ? 'Get Started' : 'Contact Sales'}
              </Button>
            </Card>
          ))}
        </div>
      </div>
    </section>
  );
}

function ProductCTA({ product }: { product: typeof PRODUCTS[number] }) {
  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <Card className="max-w-4xl mx-auto p-12 text-center bg-gradient-to-br from-primary to-accent text-primary-foreground">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Ready to get started?</h2>
          <p className="text-xl mb-8 text-primary-foreground/90">
            Join thousands of businesses using {product.name}
          </p>
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
            <Button size="lg" variant="secondary" asChild>
              <Link href={`/${locale}/signup`}>Start Free Trial</Link>
            </Button>
            <Button
              size="lg"
              variant="outline"
              className="bg-transparent border-primary-foreground text-primary-foreground hover:bg-primary-foreground/10"
              asChild
            >
              <Link href={`/contact`}>Talk to Sales</Link>
            </Button>
          </div>
        </Card>
      </div>
    </section>
  );
}
