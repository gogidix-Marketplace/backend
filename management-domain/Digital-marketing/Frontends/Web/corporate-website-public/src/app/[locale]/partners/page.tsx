import { Metadata } from 'next';
import { PartnersHero } from '@/components/partners/partners-hero';
import { WhiteLabelPartners } from '@/components/partners/white-label-partners';
import { TechnologyPartners } from '@/components/partners/technology-partners';
import { SystemIntegrators } from '@/components/partners/system-integrators';
import { PartnersCTA } from '@/components/partners/partners-cta';

export async function generateMetadata({
  params,
}: {
  params: Promise<{ locale: string }>;
}): Promise<Metadata> {
  return {
    title: 'Partners',
    description: 'Build success together with our partner ecosystem. White-label, technology partners, and system integrators.',
  };
}

export default function PartnersPage() {
  return (
    <>
      <PartnersHero />
      <WhiteLabelPartners />
      <TechnologyPartners />
      <SystemIntegrators />
      <PartnersCTA />
    </>
  );
}
