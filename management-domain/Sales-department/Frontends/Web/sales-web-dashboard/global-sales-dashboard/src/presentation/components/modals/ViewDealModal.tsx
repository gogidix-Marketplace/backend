// View Deal Modal Component
// Displays detailed information about a major deal

import { useState } from 'react';
import type { MajorDeal } from '@domain/types';
import { formatCurrency, formatDate } from '@shared';
import { Button } from '../common/Button';
import { Card, CardHeader, CardBody } from '../common/Card';
import './ViewDealModal.css';

export interface ViewDealModalProps {
  deal: MajorDeal;
  onClose: () => void;
  onStageUpdate?: (dealId: string, newStage: string) => void;
}

export function ViewDealModal({ deal, onClose, onStageUpdate }: ViewDealModalProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [selectedStage, setSelectedStage] = useState(deal.stage);

  const stages = ['New', 'Qualified', 'Proposal', 'Negotiating', 'Closing', 'Won', 'Lost'];
  const stageColors: Record<string, string> = {
    New: '#9CA3AF',
    Qualified: '#3B82F6',
    Proposal: '#8B5CF6',
    Negotiating: '#F59E0B',
    Closing: '#10B981',
    Won: '#059669',
    Lost: '#EF4444',
  };

  const handleStageChange = () => {
    if (onStageUpdate && selectedStage !== deal.stage) {
      onStageUpdate(deal.id, selectedStage);
    }
    setIsEditing(false);
  };

  const getProbabilityColor = (prob: number) => {
    if (prob >= 75) return 'var(--color-success)';
    if (prob >= 50) return 'var(--color-warning)';
    return 'var(--color-error)';
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content modal-content-large" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div className="modal-title">
            <h2>{deal.name}</h2>
            <span className="deal-number">{deal.dealNumber}</span>
          </div>
          <button className="modal-close" onClick={onClose} aria-label="Close">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
              <path d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" />
            </svg>
          </button>
        </div>

        <div className="modal-body">
          {/* Deal Value & Stage */}
          <div className="deal-hero">
            <div className="deal-value-section">
              <span className="deal-value-label">Deal Value</span>
              <span className="deal-value-amount">
                {formatCurrency(deal.value, deal.currency)}
              </span>
              {deal.isMajor && (
                <span className="deal-major-badge">Major Deal</span>
              )}
            </div>

            <div className="deal-stage-section">
              {isEditing ? (
                <div className="stage-edit">
                  <select
                    value={selectedStage}
                    onChange={(e) => setSelectedStage(e.target.value)}
                    className="stage-select"
                  >
                    {stages.map((stage) => (
                      <option key={stage} value={stage}>
                        {stage}
                      </option>
                    ))}
                  </select>
                  <div className="stage-edit-actions">
                    <Button variant="primary" size="sm" onClick={handleStageChange}>
                      Save
                    </Button>
                    <Button variant="ghost" size="sm" onClick={() => {
                      setSelectedStage(deal.stage);
                      setIsEditing(false);
                    }}>
                      Cancel
                    </Button>
                  </div>
                </div>
              ) : (
                <div className="stage-display" onClick={() => setIsEditing(true)}>
                  <span
                    className="stage-indicator"
                    style={{ backgroundColor: stageColors[deal.stage] }}
                  >
                    {deal.stage}
                  </span>
                  <span className="stage-edit-hint">Click to edit</span>
                </div>
              )}
            </div>

            <div className="deal-probability-section">
              <span className="probability-label">Win Probability</span>
              <div className="probability-value" style={{ color: getProbabilityColor(deal.probability) }}>
                {deal.probability}%
              </div>
              <div className="probability-bar">
                <div
                  className="probability-fill"
                  style={{
                    width: `${deal.probability}%`,
                    backgroundColor: getProbabilityColor(deal.probability),
                  }}
                />
              </div>
            </div>
          </div>

          <div className="deal-details-grid">
            {/* Company Information */}
            <Card className="deal-detail-card">
              <CardHeader>
                <h3>Company Information</h3>
              </CardHeader>
              <CardBody>
                <div className="detail-row">
                  <span className="detail-label">Company</span>
                  <span className="detail-value">{deal.company}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Country</span>
                  <span className="detail-value">
                    {deal.country.flag} {deal.country.name}
                  </span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Currency</span>
                  <span className="detail-value">{deal.currency}</span>
                </div>
              </CardBody>
            </Card>

            {/* Deal Owner */}
            <Card className="deal-detail-card">
              <CardHeader>
                <h3>Deal Owner</h3>
              </CardHeader>
              <CardBody>
                <div className="detail-row">
                  <span className="detail-label">Name</span>
                  <span className="detail-value">{deal.owner.name}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Country</span>
                  <span className="detail-value">{deal.owner.country}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Owner ID</span>
                  <span className="detail-value detail-id">{deal.owner.id}</span>
                </div>
              </CardBody>
            </Card>

            {/* Timeline */}
            <Card className="deal-detail-card">
              <CardHeader>
                <h3>Timeline</h3>
              </CardHeader>
              <CardBody>
                <div className="detail-row">
                  <span className="detail-label">Expected Close</span>
                  <span className="detail-value">
                    {formatDate(deal.expectedCloseDate, 'DISPLAY')}
                  </span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Days Until Close</span>
                  <span className="detail-value">
                    {Math.max(0, Math.ceil((deal.expectedCloseDate.getTime() - Date.now()) / (1000 * 60 * 60 * 24)))} days
                  </span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">Deal ID</span>
                  <span className="detail-value detail-id">{deal.id}</span>
                </div>
              </CardBody>
            </Card>
          </div>

          {/* Stage History */}
          <Card>
            <CardHeader>
              <h3>Stage Progression</h3>
            </CardHeader>
            <CardBody>
              <div className="stage-progress">
                {stages.slice(0, -2).map((stage, index) => {
                  const currentStageIndex = stages.indexOf(deal.stage);
                  const isComplete = index < currentStageIndex;
                  const isCurrent = stage === deal.stage;

                  return (
                    <div key={stage} className="stage-progress-item">
                      <div
                        className={`stage-progress-dot ${isComplete ? 'complete' : ''} ${isCurrent ? 'current' : ''}`}
                        style={{
                          backgroundColor: isComplete || isCurrent ? stageColors[stage] : '#E5E7EB',
                        }}
                      >
                        {isComplete && '\u2713'}
                      </div>
                      <span className={`stage-progress-label ${isCurrent ? 'current' : ''}`}>
                        {stage}
                      </span>
                      {index < stages.slice(0, -2).length - 1 && (
                        <div className={`stage-progress-line ${isComplete ? 'complete' : ''}`} />
                      )}
                    </div>
                  );
                })}
              </div>
            </CardBody>
          </Card>
        </div>

        <div className="modal-footer">
          <Button variant="secondary" onClick={onClose}>
            Close
          </Button>
          <Button variant="primary">
            View Full Deal Details
          </Button>
        </div>
      </div>
    </div>
  );
}

// Quick Deal View Component (compact version)
export interface DealQuickViewProps {
  deal: MajorDeal;
  onClick?: () => void;
}

export function DealQuickView({ deal, onClick }: DealQuickViewProps) {
  return (
    <div className="deal-quick-view" onClick={onClick}>
      <div className="deal-quick-header">
        <span className="deal-quick-company">{deal.company}</span>
        <span className="deal-quick-value">{formatCurrency(deal.value, deal.currency)}</span>
      </div>
      <div className="deal-quick-details">
        <span className="deal-quick-stage">{deal.stage}</span>
        <span className="deal-quick-probability">{deal.probability}%</span>
        <span className="deal-quick-country">{deal.country.flag}</span>
      </div>
      <div className="deal-quick-footer">
        <span className="deal-quick-owner">{deal.owner.name}</span>
        <span className="deal-quick-close">
          {formatDate(deal.expectedCloseDate, 'SHORT')}
        </span>
      </div>
    </div>
  );
}
