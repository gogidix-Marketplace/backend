import { Metadata } from 'next';
import { DevelopersHero } from '@/components/developers/developers-hero';
import { APIReference } from '@/components/developers/api-reference';
import { SDKs } from '@/components/developers/sdks';
import { Webhooks } from '@/components/developers/webhooks';
import { Sandbox } from '@/components/developers/sandbox';
import { GettingStarted } from '@/components/developers/getting-started';
import { DevelopersCTA } from '@/components/developers/developers-cta';

export async function generateMetadata({
  params,
}: {
  params: Promise<{ locale: string }>;
}): Promise<Metadata> {
  return {
    title: 'Developers',
    description: 'Build with Gogidix APIs and developer tools. Complete documentation, SDKs, and sandbox environment.',
  };
}

export default function DevelopersPage() {
  return (
    <>
      <DevelopersHero />
      <GettingStarted />
      <APIReference />
      <SDKs />
      <Webhooks />
      <Sandbox />
      <DevelopersCTA />
    </>
  );
}
