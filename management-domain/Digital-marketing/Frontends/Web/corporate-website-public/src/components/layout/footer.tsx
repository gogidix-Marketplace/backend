'use client';

import * as React from 'react';
import Link from 'next/link';
import { useTranslations, useLocale } from 'next-intl';
import { motion } from 'framer-motion';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { FOOTER_LINKS, SOCIAL_LINKS, LOCALES, REGIONS } from '@/lib/constants';
import { cn } from '@/lib/utils';
import {
  Twitter,
  Linkedin,
  Github,
  Youtube,
  Mail,
  Phone,
  MapPin,
  Heart,
} from 'lucide-react';
import { LanguageSelector } from './language-selector';
import { RegionSelector } from './region-selector';
import { useToast } from 'sonner';

export function Footer() {
  const t = useTranslations();
  const locale = useLocale();
  const { toast } = useToast();
  const [email, setEmail] = React.useState('');

  const handleNewsletterSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (email) {
      toast.success(t('common.newsletter.success'));
      setEmail('');
    }
  };

  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-muted/30 border-t">
      {/* Newsletter Section */}
      <div className="bg-primary text-primary-foreground py-16">
        <div className="container mx-auto px-4">
          <div className="max-w-2xl mx-auto text-center">
            <h3 className="text-2xl font-bold mb-2">
              {t('common.newsletter.title')}
            </h3>
            <p className="text-primary-foreground/80 mb-6">
              {t('common.newsletter.description')}
            </p>
            <form onSubmit={handleNewsletterSubmit} className="flex gap-2 max-w-md mx-auto">
              <Input
                type="email"
                placeholder={t('common.newsletter.placeholder')}
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="bg-background/10 border-primary-foreground/20 text-primary-foreground placeholder:text-primary-foreground/50"
              />
              <Button type="submit" variant="secondary">
                {t('common.newsletter.subscribe')}
              </Button>
            </form>
          </div>
        </div>
      </div>

      {/* Main Footer */}
      <div className="container mx-auto px-4 py-12">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-8">
          {/* Brand Column */}
          <div className="lg:col-span-2">
            <Link href={`/${locale}`} className="flex items-center gap-2 mb-4">
              <div className="h-8 w-8 rounded-lg bg-gradient-to-br from-primary to-accent flex items-center justify-center">
                <Heart className="h-5 w-5 text-white" fill="white" />
              </div>
              <span className="text-xl font-bold">Gogidix</span>
            </Link>
            <p className="text-muted-foreground mb-4 max-w-xs">
              {t('common.description')}
            </p>
            <div className="flex gap-3">
              <SocialLink href={SOCIAL_LINKS.twitter} icon={Twitter} label="Twitter" />
              <SocialLink href={SOCIAL_LINKS.linkedin} icon={Linkedin} label="LinkedIn" />
              <SocialLink href={SOCIAL_LINKS.github} icon={Github} label="GitHub" />
              <SocialLink href={SOCIAL_LINKS.youtube} icon={Youtube} label="YouTube" />
            </div>
          </div>

          {/* Products */}
          <FooterColumn
            title={t('footer.products')}
            links={FOOTER_LINKS.products}
            locale={locale}
          />

          {/* Solutions & Developers */}
          <div className="space-y-8">
            <FooterColumn
              title={t('footer.solutions')}
              links={FOOTER_LINKS.solutions}
              locale={locale}
            />
            <FooterColumn
              title={t('footer.developers')}
              links={FOOTER_LINKS.developers}
              locale={locale}
            />
          </div>

          {/* Company & Partners */}
          <div className="space-y-8">
            <FooterColumn
              title={t('footer.company')}
              links={FOOTER_LINKS.company}
              locale={locale}
            />
            <FooterColumn
              title={t('footer.partners')}
              links={FOOTER_LINKS.partners}
              locale={locale}
            />
          </div>

          {/* Resources & Contact */}
          <div className="space-y-8">
            <FooterColumn
              title={t('footer.resources')}
              links={FOOTER_LINKS.resources}
              locale={locale}
            />
            <div>
              <h4 className="font-semibold mb-3">Contact</h4>
              <ul className="space-y-2 text-sm">
                <li className="flex items-center gap-2 text-muted-foreground">
                  <Mail className="h-4 w-4" />
                  <a href="mailto:hello@gogidix.com">hello@gogidix.com</a>
                </li>
                <li className="flex items-center gap-2 text-muted-foreground">
                  <Phone className="h-4 w-4" />
                  <a href="tel:+18001234567">+1 (800) 123-4567</a>
                </li>
                <li className="flex items-center gap-2 text-muted-foreground">
                  <MapPin className="h-4 w-4" />
                  San Francisco, CA
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      {/* Bottom Bar */}
      <div className="border-t">
        <div className="container mx-auto px-4 py-6">
          <div className="flex flex-col md:flex-row items-center justify-between gap-4">
            <p className="text-sm text-muted-foreground">
              {t('footer.copyright').replace('2024', currentYear.toString())}
            </p>

            <div className="flex items-center gap-4">
              <LanguageSelector />
              <RegionSelector />
            </div>

            <div className="flex items-center gap-4 text-sm text-muted-foreground">
              <Link href={`/${locale}/legal/privacy`} className="hover:text-foreground">
                {t('footer.privacy')}
              </Link>
              <Link href={`/${locale}/legal/terms`} className="hover:text-foreground">
                {t('footer.terms')}
              </Link>
              <Link href={`/${locale}/resources/security`} className="hover:text-foreground">
                {t('footer.security')}
              </Link>
              <Link href="/status" className="hover:text-foreground flex items-center gap-1">
                <span className="h-2 w-2 rounded-full bg-green-500" />
                {t('footer.status')}
              </Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}

interface FooterColumnProps {
  title: string;
  links: Array<{ name: string; href: string }>;
  locale: string;
}

function FooterColumn({ title, links, locale }: FooterColumnProps) {
  return (
    <div>
      <h4 className="font-semibold mb-3">{title}</h4>
      <ul className="space-y-2">
        {links.map((link) => (
          <li key={link.href}>
            <Link
              href={`/${locale}${link.href}`}
              className="text-sm text-muted-foreground hover:text-foreground transition-colors"
            >
              {link.name}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

interface SocialLinkProps {
  href: string;
  icon: React.ComponentType<{ className?: string }>;
  label: string;
}

function SocialLink({ href, icon: Icon, label }: SocialLinkProps) {
  return (
    <a
      href={href}
      target="_blank"
      rel="noopener noreferrer"
      aria-label={label}
      className="h-9 w-9 rounded-full bg-muted flex items-center justify-center hover:bg-primary hover:text-primary-foreground transition-colors"
    >
      <Icon className="h-4 w-4" />
    </a>
  );
}
