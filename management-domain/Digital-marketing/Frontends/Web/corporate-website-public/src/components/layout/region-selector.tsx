'use client';

import * as React from 'react';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Button } from '@/components/ui/button';
import { REGIONS } from '@/lib/constants';
import { MapPin } from 'lucide-react';
import { useUIStore } from '@/stores/ui-store';

export function RegionSelector() {
  const { region, setRegion } = useUIStore();

  const currentRegion = REGIONS.find((r) => r.code === region) || REGIONS[0];

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="ghost" size="sm" className="gap-2">
          <MapPin className="h-4 w-4" />
          <span className="hidden sm:inline">{currentRegion.name}</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        {REGIONS.map((reg) => (
          <DropdownMenuItem
            key={reg.code}
            onClick={() => setRegion(reg.code)}
            className={region === reg.code ? 'bg-accent' : ''}
          >
            {reg.name}
          </DropdownMenuItem>
        ))}
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
