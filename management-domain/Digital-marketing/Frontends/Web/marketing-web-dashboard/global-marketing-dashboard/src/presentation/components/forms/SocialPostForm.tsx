// SocialPostForm Component
// Form for creating and editing social media posts

import { useState } from 'react';
import { Button, Card, CardHeader, CardBody } from '../common';
import './SocialPostForm.css';

export interface SocialPostFormData {
  content: string;
  platform: string;
  media: Array<{ type: string; url: string }>;
  scheduledFor?: string;
  country: string;
  campaignIds: string[];
}

interface SocialPostFormProps {
  initialData?: Partial<SocialPostFormData>;
  onSubmit: (data: SocialPostFormData) => void;
  onCancel: () => void;
  isLoading?: boolean;
  campaigns?: Array<{ id: string; name: string }>;
}

const platformOptions = [
  { value: 'facebook', label: 'Facebook', icon: '\u{1F3C6}', color: '#1877f2' },
  { value: 'instagram', label: 'Instagram', icon: '\u{1F3CF}', color: '#e4405f' },
  { value: 'twitter', label: 'Twitter', icon: '\u{1F426}', color: '#1da1f2' },
  { value: 'linkedin', label: 'LinkedIn', icon: '\u{1F4BC}', color: '#0077b5' },
  { value: 'tiktok', label: 'TikTok', icon: '\u{1F3B5}', color: '#000000' },
  { value: 'youtube', label: 'YouTube', icon: '\u{1F3B5}', color: '#ff0000' },
];

export function SocialPostForm({
  initialData,
  onSubmit,
  onCancel,
  isLoading = false,
  campaigns = [],
}: SocialPostFormProps) {
  const [formData, setFormData] = useState<SocialPostFormData>({
    content: initialData?.content || '',
    platform: initialData?.platform || 'facebook',
    media: initialData?.media || [],
    scheduledFor: initialData?.scheduledFor || '',
    country: initialData?.country || 'US',
    campaignIds: initialData?.campaignIds || [],
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const characterLimits = {
    facebook: 2200,
    instagram: 2200,
    twitter: 280,
    linkedin: 3000,
    tiktok: 2200,
    youtube: 5000,
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.content.trim()) {
      newErrors.content = 'Post content is required';
    }
    if (formData.content.length > characterLimits[formData.platform as keyof typeof characterLimits]) {
      newErrors.content = `Content exceeds ${formData.platform} character limit of ${characterLimits[formData.platform as keyof typeof characterLimits]}`;
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validateForm()) {
      onSubmit(formData);
    }
  };

  const addMedia = () => {
    setFormData(prev => ({
      ...prev,
      media: [...prev.media, { type: 'image', url: '' }]
    }));
  };

  const updateMedia = (index: number, field: string, value: string) => {
    setFormData(prev => ({
      ...prev,
      media: prev.media.map((m, i) => i === index ? { ...m, [field]: value } : m)
    }));
  };

  const removeMedia = (index: number) => {
    setFormData(prev => ({
      ...prev,
      media: prev.media.filter((_, i) => i !== index)
    }));
  };

  const characterCount = formData.content.length;
  const characterLimit = characterLimits[formData.platform as keyof typeof characterLimits];
  const remainingCharacters = characterLimit - characterCount;

  return (
    <Card className="social-post-form">
      <CardHeader>
        <h3>{initialData?.content ? 'Edit Post' : 'Create New Post'}</h3>
      </CardHeader>
      <CardBody>
        <form onSubmit={handleSubmit}>
          {/* Platform Selection */}
          <div className="form-section">
            <h4 className="form-section-title">Platform</h4>
            <div className="platform-grid">
              {platformOptions.map(platform => (
                <label
                  key={platform.value}
                  className={`platform-option ${formData.platform === platform.value ? 'platform-option-selected' : ''}`}
                  style={{ borderColor: formData.platform === platform.value ? platform.color : '#e5e7eb' }}
                >
                  <input
                    type="radio"
                    name="platform"
                    value={platform.value}
                    checked={formData.platform === platform.value}
                    onChange={(e) => setFormData(prev => ({ ...prev, platform: e.target.value }))}
                  />
                  <span className="platform-icon" style={{ color: platform.color }}>
                    {platform.icon}
                  </span>
                  <span className="platform-label">{platform.label}</span>
                </label>
              ))}
            </div>
          </div>

          {/* Content */}
          <div className="form-section">
            <h4 className="form-section-title">Post Content *</h4>
            <div className="content-textarea-wrapper">
              <textarea
                value={formData.content}
                onChange={(e) => setFormData(prev => ({ ...prev, content: e.target.value }))}
                className={errors.content ? 'input-error' : ''}
                placeholder="What would you like to share?"
                rows={5}
                maxLength={characterLimit}
              />
              <div className={`character-count ${remainingCharacters < 0 ? 'count-error' : remainingCharacters < 50 ? 'count-warning' : ''}`}>
                {characterCount} / {characterLimit}
              </div>
            </div>
            {errors.content && <span className="error-message">{errors.content}</span>}

            {/* Quick Actions */}
            <div className="quick-actions">
              <button
                type="button"
                className="action-chip"
                onClick={() => setFormData(prev => ({ ...prev, content: prev.content + '\n\n#' }))}
              >
                # Hashtag
              </button>
              <button
                type="button"
                className="action-chip"
                onClick={() => setFormData(prev => ({ ...prev, content: prev.content + '\n\n@' }))}
              >
                @ Mention
              </button>
              <button
                type="button"
                className="action-chip"
                onClick={() => setFormData(prev => ({ ...prev, content: prev.content + '\n\nhttps://' }))}
              >
                \u{1F517} Link
              </button>
              <button
                type="button"
                className="action-chip"
                onClick={() => setFormData(prev => ({ ...prev, content: prev.content + '\n\n\u{1F604}' }))}
              >
                \u{1F604} Emoji
              </button>
            </div>
          </div>

          {/* Media */}
          <div className="form-section">
            <h4 className="form-section-title">Media</h4>
            {formData.media.map((media, index) => (
              <div key={index} className="media-row">
                <select
                  value={media.type}
                  onChange={(e) => updateMedia(index, 'type', e.target.value)}
                  className="media-type-select"
                >
                  <option value="image">Image</option>
                  <option value="video">Video</option>
                  <option value="document">Document</option>
                  <option value="link">Link</option>
                </select>
                <input
                  type="url"
                  value={media.url}
                  onChange={(e) => updateMedia(index, 'url', e.target.value)}
                  placeholder="Media URL"
                  className="media-url-input"
                />
                <button
                  type="button"
                  className="remove-btn"
                  onClick={() => removeMedia(index)}
                >
                  \u{1F5D1}
                </button>
              </div>
            ))}
            <button type="button" className="add-btn" onClick={addMedia}>
              + Add Media
            </button>
          </div>

          {/* Scheduling */}
          <div className="form-section">
            <h4 className="form-section-title">Scheduling</h4>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="scheduledFor">Schedule For (Optional)</label>
                <input
                  type="datetime-local"
                  id="scheduledFor"
                  value={formData.scheduledFor || ''}
                  onChange={(e) => setFormData(prev => ({ ...prev, scheduledFor: e.target.value }))}
                />
              </div>
              <div className="form-group">
                <label htmlFor="country">Country</label>
                <select
                  id="country"
                  value={formData.country}
                  onChange={(e) => setFormData(prev => ({ ...prev, country: e.target.value }))}
                >
                  <option value="US">United States</option>
                  <option value="UK">United Kingdom</option>
                  <option value="DE">Germany</option>
                  <option value="FR">France</option>
                  <option value="CA">Canada</option>
                  <option value="AU">Australia</option>
                  <option value="JP">Japan</option>
                  <option value="BR">Brazil</option>
                </select>
              </div>
            </div>
          </div>

          {/* Campaign Linking */}
          {campaigns.length > 0 && (
            <div className="form-section">
              <h4 className="form-section-title">Link to Campaigns</h4>
              <div className="campaigns-grid">
                {campaigns.map(campaign => (
                  <label key={campaign.id} className="campaign-checkbox">
                    <input
                      type="checkbox"
                      checked={formData.campaignIds.includes(campaign.id)}
                      onChange={(e) => {
                        if (e.target.checked) {
                          setFormData(prev => ({ ...prev, campaignIds: [...prev.campaignIds, campaign.id] }));
                        } else {
                          setFormData(prev => ({ ...prev, campaignIds: prev.campaignIds.filter(id => id !== campaign.id) }));
                        }
                      }}
                    />
                    <span>{campaign.name}</span>
                  </label>
                ))}
              </div>
            </div>
          )}

          {/* Actions */}
          <div className="form-actions">
            <Button type="button" variant="ghost" onClick={onCancel}>
              Cancel
            </Button>
            <Button
              type="submit"
              variant={formData.scheduledFor ? 'secondary' : 'primary'}
              loading={isLoading}
            >
              {formData.scheduledFor ? 'Schedule Post' : 'Publish Now'}
            </Button>
          </div>
        </form>
      </CardBody>
    </Card>
  );
}
