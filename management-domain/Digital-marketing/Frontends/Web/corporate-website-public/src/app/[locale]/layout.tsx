import { routing } from '@/i18n/routing';
import { Header } from '@/components/layout/header';
import { Footer } from '@/components/layout/footer';
import { CookieConsent } from '@/components/layout/cookie-consent';
import { ScrollToTop } from '@/components/layout/scroll-to-top';

export default async function LocaleLayout({
  children,
  params,
}: {
  children: React.ReactNode;
  params: Promise<{ locale: string }>;
}) {
  const { locale } = await params;

  return (
    <div className="min-h-screen flex flex-col" dir={locale === 'ar' ? 'rtl' : 'ltr'}>
      <Header />
      <main className="flex-1">{children}</main>
      <Footer />
      <CookieConsent />
      <ScrollToTop />
    </div>
  );
}

export function generateStaticParams() {
  return routing.locales.map((locale) => ({ locale }));
}
