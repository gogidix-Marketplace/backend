import { Metadata } from 'next';
import { ResourcesHero } from '@/components/resources/resources-hero';
import { DocumentationCards } from '@/components/resources/documentation-cards';
import { BlogPreview } from '@/components/resources/blog-preview';
import { WebinarsPreview } from '@/components/resources/webinars-preview';
import { SecurityHighlight } from '@/components/resources/security-highlight';

export async function generateMetadata({
  params,
}: {
  params: Promise<{ locale: string }>;
}): Promise<Metadata> {
  return {
    title: 'Resources',
    description: 'Learn, grow, and succeed with Gogidix. Documentation, blog, webinars, and security resources.',
  };
}

export default function ResourcesPage() {
  return (
    <>
      <ResourcesHero />
      <DocumentationCards />
      <BlogPreview />
      <WebinarsPreview />
      <SecurityHighlight />
    </>
  );
}
