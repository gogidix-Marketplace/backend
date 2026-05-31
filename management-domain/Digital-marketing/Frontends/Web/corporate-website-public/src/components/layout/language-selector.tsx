'use client';

import * as React from 'react';
import { useRouter, usePathname } from 'next/navigation';
import { useLocale } from 'next-intl';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Button } from '@/components/ui/button';
import { LOCALES } from '@/lib/constants';
import { Globe } from 'lucide-react';

export function LanguageSelector() {
  const locale = useLocale();
  const router = useRouter();
  const pathname = usePathname();

  const currentLocale = LOCALES.find((l) => l.code === locale) || LOCALES[0];

  const changeLocale = (newLocale: string) => {
    // Replace the current locale in the pathname
    const newPathname = pathname.replace(`/${locale}`, `/${newLocale}`);
    router.push(newPathname);
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="ghost" size="sm" className="gap-2">
          <span>{currentLocale.flag}</span>
          <span className="hidden sm:inline">{currentLocale.code.toUpperCase()}</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        {LOCALES.map((loc) => (
          <DropdownMenuItem
            key={loc.code}
            onClick={() => changeLocale(loc.code)}
            className={locale === loc.code ? 'bg-accent' : ''}
          >
            <span className="mr-2">{loc.flag}</span>
            {loc.name}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
