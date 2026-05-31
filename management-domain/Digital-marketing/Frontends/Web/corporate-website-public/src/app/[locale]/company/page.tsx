import { Metadata } from 'next';
import Link from 'next/link';
import { CompanyHero } from '@/components/company/company-hero';
import { AboutPreview } from '@/components/company/about-preview';
import { LeadershipPreview } from '@/components/company/leadership-preview';
import { CareersPreview } from '@/components/company/careers-preview';
import { PressPreview } from '@/components/company/press-preview';
import { CompanyLocations } from '@/components/company/company-locations';
import { Button } from '@/components/ui/button';
import { ArrowRight } from 'lucide-react';

export async function generateMetadata({
  params,
}: {
  params: Promise<{ locale: string }>;
}): Promise<Metadata> {
  return {
    title: 'Company',
    description: 'Learn about Gogidix - our mission, leadership, careers, and latest news.',
  };
}

export default function CompanyPage() {
  return (
    <>
      <CompanyHero />
      <AboutPreview />
      <LeadershipPreview />
      <CareersPreview />
      <PressPreview />
      <CompanyLocations />
      <CompanyCTA />
    </>
  );
}

function CompanyCTA() {
  return (
    <section className="py-24 bg-muted/30">
      <div className="container mx-auto px-4">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-3xl md:text-4xl font-bold mb-6">
            Join Our Team
          </h2>
          <p className="text-lg text-muted-foreground mb-8">
            Be part of a team that's transforming how businesses operate.
          </p>
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
            <Button size="lg" asChild>
              <Link href="/company/careers">
                View Open Positions
                <ArrowRight className="ml-2 h-5 w-5" />
              </Link>
            </Button>
            <Button size="lg" variant="outline" asChild>
              <Link href="/company/about">Learn About Us</Link>
            </Button>
          </div>
        </div>
      </div>
    </section>
  );
}
