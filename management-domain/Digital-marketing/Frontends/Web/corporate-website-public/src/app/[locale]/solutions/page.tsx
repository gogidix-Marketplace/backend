import { Metadata } from 'next';
import Link from 'next/link';
import { SolutionsHero } from '@/components/solutions/solutions-hero';
import { IndustrySolutions } from '@/components/solutions/industry-solutions';
import { SizeSolutions } from '@/components/solutions/size-solutions';
import { RegionSolutions } from '@/components/solutions/region-solutions';
import { CaseStudiesPreview } from '@/components/solutions/case-studies-preview';
import { Button } from '@/components/ui/button';
import { ArrowRight } from 'lucide-react';

export async function generateMetadata({
  params,
}: {
  params: Promise<{ locale: string }>;
}): Promise<Metadata> {
  return {
    title: 'Solutions',
    description: 'Explore tailored solutions for your industry, company size, and region.',
  };
}

export default function SolutionsPage() {
  return (
    <>
      <SolutionsHero />
      <IndustrySolutions />
      <SizeSolutions />
      <RegionSolutions />
      <CaseStudiesPreview />
      <SolutionCTA />
    </>
  );
}

function SolutionCTA() {
  return (
    <section className="py-24">
      <div className="container mx-auto px-4">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-3xl md:text-4xl font-bold mb-6">
            Find Your Perfect Solution
          </h2>
          <p className="text-lg text-muted-foreground mb-8">
            Our team can help you identify the right solutions for your unique business needs.
          </p>
          <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
            <Button size="lg" asChild>
              <Link href="/contact">
                Talk to an Expert
                <ArrowRight className="ml-2 h-5 w-5" />
              </Link>
            </Button>
            <Button size="lg" variant="outline" asChild>
              <Link href="/solutions/case-studies">View Case Studies</Link>
            </Button>
          </div>
        </div>
      </div>
    </section>
  );
}
