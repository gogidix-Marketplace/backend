'use client';

import * as React from 'react';
import { useRouter } from 'next/navigation';
import { useTranslations } from 'next-intl';
import { motion, AnimatePresence } from 'framer-motion';
import { Dialog, DialogContent } from '@/components/ui/dialog';
import { Command } from 'cmdk';
import { cn, debounce } from '@/lib/utils';
import {
  Search,
  FileText,
  Users,
  Code,
  Building,
  Book,
  ArrowRight,
} from 'lucide-react';
import { useQuery } from '@tanstack/react-query';

interface SearchResult {
  id: string;
  title: string;
  description: string;
  href: string;
  type: 'page' | 'product' | 'solution' | 'doc' | 'blog';
  icon?: React.ComponentType<{ className?: string }>;
}

interface GlobalSearchProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

const staticResults: SearchResult[] = [
  {
    id: 'home',
    title: 'Home',
    description: 'Navigate to the homepage',
    href: '/',
    type: 'page',
    icon: FileText,
  },
  {
    id: 'products',
    title: 'Products',
    description: 'View all our products',
    href: '/products',
    type: 'page',
    icon: Building,
  },
  {
    id: 'solutions',
    title: 'Solutions',
    description: 'Explore solutions by industry, size, and region',
    href: '/solutions',
    type: 'page',
    icon: Users,
  },
  {
    id: 'developers',
    title: 'Developers',
    description: 'API docs, SDKs, and developer resources',
    href: '/developers',
    type: 'page',
    icon: Code,
  },
  {
    id: 'docs',
    title: 'Documentation',
    description: 'Browse our documentation',
    href: '/resources/docs',
    type: 'page',
    icon: Book,
  },
];

export function GlobalSearch({ open, onOpenChange }: GlobalSearchProps) {
  const router = useRouter();
  const t = useTranslations();
  const [search, setSearch] = React.useState('');

  const { data: searchResults = [] } = useQuery({
    queryKey: ['search', search],
    queryFn: async () => {
      if (!search || search.length < 2) return [];
      // In a real app, this would call an API
      // For now, we'll just filter static results
      return staticResults.filter(
        (result) =>
          result.title.toLowerCase().includes(search.toLowerCase()) ||
          result.description.toLowerCase().includes(search.toLowerCase())
      );
    },
    enabled: search.length >= 2,
  });

  const results = search.length >= 2 ? searchResults : staticResults;

  const handleSelect = (href: string) => {
    onOpenChange(false);
    setSearch('');
    router.push(href);
  };

  // Keyboard shortcut to open search
  React.useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
        e.preventDefault();
        onOpenChange(!open);
      }
    };
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [open, onOpenChange]);

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="p-0 overflow-hidden max-w-2xl">
        <Command className="rounded-lg border shadow-md">
          <div className="flex items-center border-b px-3">
            <Search className="mr-2 h-4 w-4 shrink-0 opacity-50" />
            <Command.Input
              placeholder={t('common.searchPlaceholder')}
              value={search}
              onValueChange={setSearch}
              className="flex h-14 w-full rounded-md bg-transparent py-3 text-sm outline-none placeholder:text-muted-foreground disabled:cursor-not-allowed disabled:opacity-50"
            />
            <kbd className="pointer-events-none ml-auto flex h-5 select-none items-center gap-1 rounded border bg-muted px-1.5 font-mono text-[10px] font-medium opacity-100">
              <span className="text-xs">⌘</span>K
            </kbd>
          </div>

          <Command.List className="max-h-[400px] overflow-y-auto p-2">
            <Command.Empty className="py-6 text-center text-sm text-muted-foreground">
              No results found.
            </Command.Empty>

            {search.length >= 2 && (
              <Command.Group heading="Search Results">
                {results.map((result) => (
                  <Command.Item
                    key={result.id}
                    onSelect={() => handleSelect(result.href)}
                    className="flex items-center gap-3 px-3 py-2 rounded-md hover:bg-accent cursor-pointer"
                  >
                    {result.icon && <result.icon className="h-4 w-4 text-muted-foreground" />}
                    <div className="flex-1">
                      <p className="text-sm font-medium">{result.title}</p>
                      <p className="text-xs text-muted-foreground">{result.description}</p>
                    </div>
                    <ArrowRight className="h-4 w-4 text-muted-foreground" />
                  </Command.Item>
                ))}
              </Command.Group>
            )}

            {search.length < 2 && (
              <>
                <Command.Group heading="Quick Links">
                  {staticResults.map((result) => (
                    <Command.Item
                      key={result.id}
                      onSelect={() => handleSelect(result.href)}
                      className="flex items-center gap-3 px-3 py-2 rounded-md hover:bg-accent cursor-pointer"
                    >
                      {result.icon && <result.icon className="h-4 w-4 text-muted-foreground" />}
                      <div className="flex-1">
                        <p className="text-sm font-medium">{result.title}</p>
                        <p className="text-xs text-muted-foreground">{result.description}</p>
                      </div>
                      <ArrowRight className="h-4 w-4 text-muted-foreground" />
                    </Command.Item>
                  ))}
                </Command.Group>

                <Command.Group heading="Products">
                  <Command.Item
                    onSelect={() => handleSelect('/products/logistics')}
                    className="flex items-center gap-3 px-3 py-2 rounded-md hover:bg-accent cursor-pointer"
                  >
                    <FileText className="h-4 w-4 text-muted-foreground" />
                    <div className="flex-1">
                      <p className="text-sm font-medium">Logistics Management</p>
                      <p className="text-xs text-muted-foreground">Supply chain optimization</p>
                    </div>
                  </Command.Item>
                  <Command.Item
                    onSelect={() => handleSelect('/products/ecommerce')}
                    className="flex items-center gap-3 px-3 py-2 rounded-md hover:bg-accent cursor-pointer"
                  >
                    <FileText className="h-4 w-4 text-muted-foreground" />
                    <div className="flex-1">
                      <p className="text-sm font-medium">E-commerce Platform</p>
                      <p className="text-xs text-muted-foreground">Online store builder</p>
                    </div>
                  </Command.Item>
                </Command.Group>
              </>
            )}
          </Command.List>
        </Command>
      </DialogContent>
    </Dialog>
  );
}
