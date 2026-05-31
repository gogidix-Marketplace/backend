// Forecast Modal Component
// Allows users to generate and configure sales forecasts

import { useState } from 'react';
import type { GlobalSalesForecast, ForecastPeriod, ForecastScenario } from '@domain/types';
import { formatCurrency } from '@shared';
import { Button } from '../common/Button';
import { Card, CardHeader, CardBody } from '../common/Card';
import './ForecastModal.css';

export interface ForecastModalProps {
  isOpen: boolean;
  onClose: () => void;
  onGenerate?: (config: ForecastConfig) => Promise<GlobalSalesForecast>;
  existingForecast?: GlobalSalesForecast;
}

export interface ForecastConfig {
  period: ForecastPeriod;
  scenario: ForecastScenario;
  includeBestCase: boolean;
  includeWorstCase: boolean;
  countries?: string[];
  useHistoricalTrends: boolean;
}

const PERIOD_OPTIONS = [
  { label: 'Next Month', value: 'monthly' },
  { label: 'Next Quarter', value: 'quarterly' },
  { label: 'Next Year', value: 'yearly' },
] as const;

const SCENARIO_OPTIONS = [
  { label: 'Likely', value: 'likely', description: 'Most probable outcome' },
  { label: 'Best Case', value: 'best', description: 'Optimistic scenario' },
  { label: 'Worst Case', value: 'worst', description: 'Conservative scenario' },
] as const;

export function ForecastModal({
  isOpen,
  onClose,
  onGenerate,
  existingForecast,
}: ForecastModalProps) {
  const [step, setStep] = useState<'period' | 'scenario' | 'options' | 'results'>('period');
  const [isGenerating, setIsGenerating] = useState(false);
  const [generatedForecast, setGeneratedForecast] = useState<GlobalSalesForecast | null>(existingForecast || null);

  const [config, setConfig] = useState<ForecastConfig>({
    period: {
      type: 'quarterly',
      start: new Date(),
      end: new Date(new Date().setMonth(new Date().getMonth() + 3)),
      label: 'Q1 2025',
    },
    scenario: 'likely',
    includeBestCase: true,
    includeWorstCase: true,
    countries: [],
    useHistoricalTrends: true,
  });

  const handlePeriodSelect = (type: 'monthly' | 'quarterly' | 'yearly') => {
    const now = new Date();
    let period: ForecastPeriod;

    switch (type) {
      case 'monthly':
        period = {
          type: 'monthly',
          start: new Date(now.getFullYear(), now.getMonth() + 1, 1),
          end: new Date(now.getFullYear(), now.getMonth() + 2, 0),
          label: now.toLocaleDateString('en-US', { month: 'long', year: 'numeric' }),
        };
        break;
      case 'quarterly':
        const quarter = Math.floor(now.getMonth() / 3) + 1;
        period = {
          type: 'quarterly',
          start: new Date(now.getFullYear(), quarter * 3, 1),
          end: new Date(now.getFullYear(), quarter * 3 + 3, 0),
          label: `Q${quarter > 4 ? 1 : quarter} ${now.getFullYear() + (quarter > 4 ? 1 : 0)}`,
        };
        break;
      case 'yearly':
        period = {
          type: 'yearly',
          start: new Date(now.getFullYear() + 1, 0, 1),
          end: new Date(now.getFullYear() + 1, 11, 31),
          label: `${now.getFullYear() + 1}`,
        };
        break;
    }

    setConfig({ ...config, period });
    setStep('scenario');
  };

  const handleScenarioSelect = (scenario: ForecastScenario) => {
    setConfig({ ...config, scenario });
    setStep('options');
  };

  const handleGenerate = async () => {
    if (!onGenerate) {
      // Mock generation for demo
      setStep('results');
      return;
    }

    setIsGenerating(true);
    try {
      const forecast = await onGenerate(config);
      setGeneratedForecast(forecast);
      setStep('results');
    } catch (error) {
      console.error('Failed to generate forecast:', error);
    } finally {
      setIsGenerating(false);
    }
  };

  const handleBack = () => {
    if (step === 'scenario') setStep('period');
    else if (step === 'options') setStep('scenario');
    else if (step === 'results') setStep('options');
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content modal-content-large" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>
            {step === 'results' ? 'Forecast Results' : 'Generate Sales Forecast'}
          </h2>
          <button className="modal-close" onClick={onClose} aria-label="Close">
            <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
              <path d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" />
            </svg>
          </button>
        </div>

        {/* Progress Steps */}
        {step !== 'results' && (
          <div className="forecast-progress">
            <div className={`progress-step ${step === 'period' ? 'active' : ''} ${['scenario', 'options'].includes(step) ? 'completed' : ''}`}>
              <span className="step-number">1</span>
              <span className="step-label">Period</span>
            </div>
            <div className="progress-connector" />
            <div className={`progress-step ${step === 'scenario' ? 'active' : ''} ${step === 'options' ? 'completed' : ''}`}>
              <span className="step-number">2</span>
              <span className="step-label">Scenario</span>
            </div>
            <div className="progress-connector" />
            <div className={`progress-step ${step === 'options' ? 'active' : ''}`}>
              <span className="step-number">3</span>
              <span className="step-label">Options</span>
            </div>
          </div>
        )}

        <div className="modal-body">
          {step === 'period' && (
            <div className="forecast-step">
              <h3>Select Forecast Period</h3>
              <p className="step-description">Choose the time period for which you want to generate a forecast.</p>
              <div className="period-options">
                {PERIOD_OPTIONS.map((option) => (
                  <button
                    key={option.value}
                    className={`period-option-card ${config.period.type === option.value ? 'selected' : ''}`}
                    onClick={() => handlePeriodSelect(option.value)}
                  >
                    <div className="period-icon">
                      {option.value === 'monthly' && '\uD83D\uDCC5'}
                      {option.value === 'quarterly' && '\uD83D\uDCC8'}
                      {option.value === 'yearly' && '\uD83D\uDCC6'}
                    </div>
                    <span className="period-label">{option.label}</span>
                  </button>
                ))}
              </div>
            </div>
          )}

          {step === 'scenario' && (
            <div className="forecast-step">
              <h3>Select Forecast Scenario</h3>
              <p className="step-description">Choose the scenario model for the forecast calculation.</p>
              <div className="scenario-options">
                {SCENARIO_OPTIONS.map((option) => (
                  <button
                    key={option.value}
                    className={`scenario-option-card ${config.scenario === option.value ? 'selected' : ''}`}
                    onClick={() => handleScenarioSelect(option.value)}
                  >
                    <div className="scenario-header">
                      <span className="scenario-label">{option.label}</span>
                      <div className={`scenario-radio ${config.scenario === option.value ? 'checked' : ''}`}>
                        {config.scenario === option.value && '\u2713'}
                      </div>
                    </div>
                    <span className="scenario-description">{option.description}</span>
                  </button>
                ))}
              </div>
            </div>
          )}

          {step === 'options' && (
            <div className="forecast-step">
              <h3>Forecast Options</h3>
              <p className="step-description">Configure additional options for the forecast.</p>

              <Card>
                <CardHeader>
                  <h4>Forecast Configuration</h4>
                </CardHeader>
                <CardBody>
                  <div className="forecast-options-list">
                    <label className="forecast-option">
                      <input
                        type="checkbox"
                        checked={config.includeBestCase}
                        onChange={(e) => setConfig({ ...config, includeBestCase: e.target.checked })}
                      />
                      <div className="option-content">
                        <span className="option-label">Include Best Case Scenario</span>
                        <span className="option-description">Calculate optimistic forecast based on best possible outcomes</span>
                      </div>
                    </label>

                    <label className="forecast-option">
                      <input
                        type="checkbox"
                        checked={config.includeWorstCase}
                        onChange={(e) => setConfig({ ...config, includeWorstCase: e.target.checked })}
                      />
                      <div className="option-content">
                        <span className="option-label">Include Worst Case Scenario</span>
                        <span className="option-description">Calculate conservative forecast based on worst possible outcomes</span>
                      </div>
                    </label>

                    <label className="forecast-option">
                      <input
                        type="checkbox"
                        checked={config.useHistoricalTrends}
                        onChange={(e) => setConfig({ ...config, useHistoricalTrends: e.target.checked })}
                      />
                      <div className="option-content">
                        <span className="option-label">Use Historical Trends</span>
                        <span className="option-description">Apply historical data patterns to improve accuracy</span>
                      </div>
                    </label>
                  </div>

                  <div className="forecast-summary">
                    <h5>Forecast Summary</h5>
                    <div className="summary-item">
                      <span>Period:</span>
                      <strong>{config.period.label}</strong>
                    </div>
                    <div className="summary-item">
                      <span>Scenario:</span>
                      <strong>{config.scenario}</strong>
                    </div>
                    <div className="summary-item">
                      <span>Scenarios:</span>
                      <strong>
                        {[config.scenario, config.includeBestCase && 'Best', config.includeWorstCase && 'Worst']
                          .filter(Boolean)
                          .join(', ')}
                      </strong>
                    </div>
                  </div>
                </CardBody>
              </Card>
            </div>
          )}

          {step === 'results' && generatedForecast && (
            <div className="forecast-step">
              <div className="forecast-success-banner">
                <span className="success-icon">\u2705</span>
                <span>Forecast generated successfully!</span>
              </div>

              <div className="forecast-results">
                <Card className="forecast-result-card">
                  <CardBody className="result-main">
                    <span className="result-label">Forecast</span>
                    <span className="result-value">
                      {formatCurrency(generatedForecast.global.forecast, generatedForecast.global.currency)}
                    </span>
                    <span className="result-period">{generatedForecast.period.label}</span>
                  </CardBody>
                </Card>

                {generatedForecast.global.bestCase && (
                  <Card className="forecast-result-card">
                    <CardBody className="result-scenario best">
                      <span className="result-label">Best Case</span>
                      <span className="result-value">
                        {formatCurrency(generatedForecast.global.bestCase, generatedForecast.global.currency)}
                      </span>
                    </CardBody>
                  </Card>
                )}

                {generatedForecast.global.worstCase && (
                  <Card className="forecast-result-card">
                    <CardBody className="result-scenario worst">
                      <span className="result-label">Worst Case</span>
                      <span className="result-value">
                        {formatCurrency(generatedForecast.global.worstCase, generatedForecast.global.currency)}
                      </span>
                    </CardBody>
                  </Card>
                )}
              </div>

              <Card>
                <CardHeader>
                  <h4>Confidence Level</h4>
                </CardHeader>
                <CardBody>
                  <div className="confidence-display">
                    <div className="confidence-bar">
                      <div
                        className="confidence-fill"
                        style={{ width: `${generatedForecast.global.confidence}%` }}
                      />
                    </div>
                    <span className="confidence-value">{generatedForecast.global.confidence}%</span>
                  </div>
                </CardBody>
              </Card>
            </div>
          )}
        </div>

        <div className="modal-footer">
          {step !== 'period' && (
            <Button variant="ghost" onClick={handleBack} disabled={isGenerating}>
              Back
            </Button>
          )}
          <div style={{ flex: 1 }} />
          <Button variant="secondary" onClick={onClose} disabled={isGenerating}>
            {step === 'results' ? 'Close' : 'Cancel'}
          </Button>
          {step !== 'results' && (
            <Button
              variant="primary"
              onClick={step === 'options' ? handleGenerate : () => {
                if (step === 'period') setStep('scenario');
                else setStep('options');
              }}
              disabled={isGenerating}
            >
              {isGenerating ? 'Generating...' : step === 'options' ? 'Generate Forecast' : 'Continue'}
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
