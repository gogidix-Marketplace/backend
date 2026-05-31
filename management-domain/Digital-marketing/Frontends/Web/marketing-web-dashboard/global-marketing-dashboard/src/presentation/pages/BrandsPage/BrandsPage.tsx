// BrandsPage - Global Marketing Dashboard
// Brand asset management and guidelines page

import React, { useState } from 'react';
import { Brand } from '../../../domain/types';
import { MetricCard } from '../../../shared/components/common/MetricCard';
import { Button } from '../../../shared/components/common/Button';
import { SearchBar } from '../../../shared/components/common/SearchBar';
import { Tabs, TabPanel } from '../../../shared/components/common/Tabs';
import { Modal } from '../../../shared/components/common/Modal';
import mockBrands from '../../../shared/mock-data/brands.mock';
import './BrandsPage.css';

const BrandsPage: React.FC = () => {
  const [brands] = useState<Brand[]>(mockBrands);
  const [selectedBrand, setSelectedBrand] = useState<Brand | null>(null);
  const [activeTab, setActiveTab] = useState('brands');
  const [isModalOpen, setIsModalOpen] = useState(false);

  const tabs = [
    { id: 'brands', label: 'Brands' },
    { id: 'assets', label: 'Assets' },
    { id: 'guidelines', label: 'Guidelines' },
    { id: 'templates', label: 'Templates' },
  ];

  const totalAssets = brands.reduce((sum, b) => sum + b.assets.length, 0);
  const totalGuidelines = brands.reduce((sum, b) => sum + b.guidelines.length, 0);

  return (
    <div className="brands-page">
      <div className="page__header">
        <div className="container">
          <div className="page__header-content">
            <div>
              <h1 className="page__title">Brand Management</h1>
              <p className="page__subtitle">Manage brand assets, guidelines, and templates globally</p>
            </div>
            <div className="page__actions">
              <Button variant="secondary">Upload Asset</Button>
              <Button variant="primary">+ Add Brand</Button>
            </div>
          </div>
        </div>
      </div>

      <div className="container page__content">
        <div className="metrics-grid">
          <MetricCard label="Total Brands" value={brands.length} />
          <MetricCard label="Total Assets" value={totalAssets} />
          <MetricCard label="Guidelines" value={totalGuidelines} />
          <MetricCard label="Templates" value={24} />
        </div>

        <Tabs tabs={tabs} activeTab={activeTab} onChange={setActiveTab} variant="underlined" />

        <TabPanel id="brands" activeTab={activeTab}>
          <div className="brands-grid">
            {brands.map((brand) => (
              <div key={brand.id} className="brand-card" onClick={() => setSelectedBrand(brand)}>
                <div className="brand-card__header">
                  <div className="brand-logo" style={{ backgroundColor: brand.colors.primary }}>
                    {brand.logo ? (
                      <img src={brand.logo} alt={brand.name} />
                    ) : (
                      <span className="brand-initial">{brand.name[0]}</span>
                    )}
                  </div>
                  <div className="brand-info">
                    <h3 className="brand-name">{brand.name}</h3>
                    <span className={`brand-status brand-status--${brand.status}`}>
                      {brand.status}
                    </span>
                  </div>
                </div>
                <div className="brand-card__body">
                  <p className="brand-description">{brand.description}</p>
                  <div className="brand-colors">
                    {Object.entries(brand.colors).slice(0, 4).map(([key, value]) => (
                      <div
                        key={key}
                        className="color-swatch"
                        style={{ backgroundColor: value as string }}
                        title={key}
                      />
                    ))}
                  </div>
                  <div className="brand-stats">
                    <span>{brand.assets.length} Assets</span>
                    <span>{brand.guidelines.length} Guidelines</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="assets" activeTab={activeTab}>
          <div className="assets-grid">
            {brands.flatMap(brand => brand.assets).map((asset) => (
              <div key={asset.id} className="asset-card">
                <div className="asset-thumbnail">
                  {asset.type === 'logo' && '🏷️'}
                  {asset.type === 'image' && '🖼️'}
                  {asset.type === 'video' && '🎬'}
                  {asset.type === 'document' && '📄'}
                </div>
                <div className="asset-info">
                  <span className="asset-name">{asset.name}</span>
                  <span className="asset-type">{asset.type}</span>
                </div>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="guidelines" activeTab={activeTab}>
          <div className="guidelines-list">
            {brands.flatMap(brand => brand.guidelines).map((guideline) => (
              <div key={guideline.id} className="guideline-item">
                <div className="guideline-header">
                  <span className="guideline-category">{guideline.category}</span>
                  <span className="guideline-version">v{guideline.version}</span>
                </div>
                <h3 className="guideline-title">{guideline.title}</h3>
                <p className="guideline-updated">Updated: {new Date(guideline.lastUpdated).toLocaleDateString()}</p>
              </div>
            ))}
          </div>
        </TabPanel>

        <TabPanel id="templates" activeTab={activeTab}>
          <div className="templates-grid">
            {['Email', 'Social Media', 'Presentation', 'Document', 'Banner'].map((type) => (
              <div key={type} className="template-card">
                <div className="template-icon">
                  {type === 'Email' && '✉️'}
                  {type === 'Social Media' && '📱'}
                  {type === 'Presentation' && '📊'}
                  {type === 'Document' && '📄'}
                  {type === 'Banner' && '🎨'}
                </div>
                <span className="template-type">{type}</span>
                <span className="template-count">{Math.floor(Math.random() * 20) + 5}</span>
              </div>
            ))}
          </div>
        </TabPanel>
      </div>

      {/* Brand Detail Modal */}
      <Modal
        isOpen={!!selectedBrand}
        onClose={() => setSelectedBrand(null)}
        title={selectedBrand?.name}
        size="lg"
      >
        {selectedBrand && (
          <div className="brand-detail">
            <p>{selectedBrand.description}</p>
            <div className="brand-colors-full">
              <h4>Brand Colors</h4>
              {Object.entries(selectedBrand.colors).map(([name, value]) => (
                <div key={name} className="color-detail">
                  <div className="color-swatch-full" style={{ backgroundColor: value as string }} />
                  <span className="color-name">{name}</span>
                  <span className="color-value">{value as string}</span>
                </div>
              ))}
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};

export default BrandsPage;
