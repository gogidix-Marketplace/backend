import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import { routing } from '@/i18n/routing';
import { notFound } from 'next/navigation';
import { NextIntlClientProvider } from 'next-intl';
import { getMessages } from 'next-intl/server';
import { ThemeProvider } from '@/components/providers/theme-provider';
import { QueryProvider } from '@/components/providers/query-provider';
import { Toaster } from '@/components/ui/sonner';
import '@/app/globals.css';

const inter = Inter({
  subsets: ['latin'],
  display: 'swap',
  variable: '--font-inter',
});

export const metadata: Metadata = {
  title: {
    default: 'Gogidix - Transform Your Business Operations',
    template: '%s | Gogidix',
  },
  description:
    'Enterprise-grade solutions for logistics, e-commerce, procurement, and business operations. Streamline your workflows with intelligent automation.',
  keywords: [
    'logistics',
    'e-commerce',
    'procurement',
    'business operations',
    'automation',
    'enterprise software',
    'supply chain',
    'inventory management',
  ],
  authors: [{ name: 'Gogidix' }],
  creator: 'Gogidix',
  publisher: 'Gogidix',
  formatDetection: {
    email: false,
    address: false,
    telephone: false,
  },
  metadataBase: new URL(
    process.env.NEXT_PUBLIC_SITE_URL || 'https://gogidix.com'
  ),
  openGraph: {
    type: 'website',
    locale: 'en_US',
    url: 'https://gogidix.com',
    siteName: 'Gogidix',
    title: 'Gogidix - Transform Your Business Operations',
    description:
      'Enterprise-grade solutions for logistics, e-commerce, procurement, and business operations.',
    images: [
      {
        url: '/og-image.png',
        width: 1200,
        height: 630,
        alt: 'Gogidix',
      },
    ],
  },
  twitter: {
    card: 'summary_large_image',
    title: 'Gogidix - Transform Your Business Operations',
    description:
      'Enterprise-grade solutions for logistics, e-commerce, procurement, and business operations.',
    images: ['/og-image.png'],
    creator: '@gogidix',
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      'max-video-preview': -1,
      'max-image-preview': 'large',
      'max-snippet': -1,
    },
  },
  verification: {
    google: process.env.GOOGLE_SITE_VERIFICATION,
  },
};

export function generateStaticParams() {
  return routing.locales.map((locale) => ({ locale }));
}

export default async function RootLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;

  // Ensure that the incoming `locale` is valid
  if (!routing.locales.includes(locale as any)) {
    notFound();
  }

  // Providing all messages to the client
  // side is the easiest way to get started
  const messages = await getMessages();

  return (
    <html lang={locale} suppressHydrationWarning>
      <body className={inter.variable}>
        <ThemeProvider attribute="class" defaultTheme="system" enableSystem>
          <QueryProvider>
            <NextIntlClientProvider messages={messages}>
              {children}
            </NextIntlClientProvider>
            <Toaster />
          </QueryProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
