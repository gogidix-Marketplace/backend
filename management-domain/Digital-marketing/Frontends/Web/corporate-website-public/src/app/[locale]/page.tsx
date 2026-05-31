import { useTranslations } from 'next-intl';
import { HeroSection } from '@/components/home/hero-section';
import { ProductsSection } from '@/components/home/products-section';
import { FeaturesSection } from '@/components/home/features-section';
import { CTASection } from '@/components/home/cta-section';
import { SocialProofSection } from '@/components/home/social-proof-section';

export default function HomePage() {
  return (
    <>
      <HeroSection />
      <SocialProofSection />
      <ProductsSection />
      <FeaturesSection />
      <CTASection />
    </>
  );
}

export async function generateMetadata({
  params,
}: {
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;

  return {
    title: 'Home',
    description: 'Transform your business operations with Gogidix intelligent automation platform.',
    openGraph: {
      locale,
    },
  };
}
