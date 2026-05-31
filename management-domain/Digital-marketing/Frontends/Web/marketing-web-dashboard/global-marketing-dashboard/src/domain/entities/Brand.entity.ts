// Domain Entity: Brand
// Represents a brand and its associated assets

export enum BrandAssetType {
  LOGO = 'logo',
  COLOR_PALETTE = 'color_palette',
  TYPOGRAPHY = 'typography',
  TEMPLATE = 'template',
  IMAGE = 'image',
  VIDEO = 'video',
  DOCUMENT = 'document',
  GUIDELINES = 'guidelines',
  OTHER = 'other'
}

export interface BrandAsset {
  id: string;
  name: string;
  type: BrandAssetType;
  url: string;
  thumbnailUrl?: string;
  fileSize: number;
  format: string;
  dimensions?: {
    width: number;
    height: number;
  };
  tags: string[];
  uploadedBy: {
    id: string;
    name: string;
  };
  uploadedAt: Date;
  usageCount: number;
  versions: Array<{
    id: string;
    version: number;
    url: string;
    createdAt: Date;
    createdBy: string;
  }>;
}

export interface BrandGuideline {
  id: string;
  category: string;
  title: string;
  content: string;
  assets: string[]; // Asset IDs
  order: number;
}

export interface Brand {
  id: string;
  name: string;
  description: string;
  logo: string;
  website?: string;
  industry: string;
  colors: {
    primary: string[];
    secondary: string[];
    accent: string[];
  };
  typography: {
    primaryFont: string;
    secondaryFont: string;
    headingFont: string;
  };
  tone: string[];
  values: string[];
  assets: BrandAsset[];
  guidelines: BrandGuideline[];
  countries: string[]; // Country codes where this brand is used
  status: 'active' | 'archived';
  createdAt: Date;
  updatedAt: Date;
  createdBy: {
    id: string;
    name: string;
  };
}

export interface BrandTemplate {
  id: string;
  name: string;
  type: string;
  description: string;
  thumbnail: string;
  brandId: string;
  category: string;
  file: {
    url: string;
    format: string;
    size: number;
  };
  variables: Array<{
    name: string;
    type: string;
    defaultValue?: string;
    required: boolean;
  }>;
  usageCount: number;
  lastUsed?: Date;
  createdAt: Date;
}
