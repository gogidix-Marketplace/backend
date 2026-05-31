/**
 * Instagram Service
 * Integration with Instagram Graph API
 */

const axios = require('axios');
const logger = require('../utils/logger');
const SocialAccount = require('../models/SocialAccount');

const INSTAGRAM_API_BASE = 'https://graph.instagram.com';
const FACEBOOK_API_BASE = 'https://graph.facebook.com/v18.0';

/**
 * Get Instagram access token
 */
const getAccessToken = async (accountId) => {
  const account = await SocialAccount.getByAccountId(accountId);

  if (!account || account.platform !== 'instagram') {
    throw new Error('Instagram account not found');
  }

  if (!account.isActive) {
    throw new Error('Instagram account is not active');
  }

  await account.updateLastUsed();

  return account.credentials.accessToken;
};

/**
 * Get Instagram Business Account ID
 */
const getInstagramAccountId = async (accountId) => {
  const account = await SocialAccount.getByAccountId(accountId);
  return account.config?.instagramBusinessAccountId || process.env.INSTAGRAM_BUS_ACCOUNT_ID;
};

/**
 * Publish post to Instagram
 */
const publishPost = async (postId, content, media, accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);
    const igAccountId = await getInstagramAccountId(accountId);

    if (!media || media.length === 0) {
      throw new Error('Instagram posts require at least one image');
    }

    const fs = require('fs');
    const FormData = require('form-data');

    // Step 1: Create container
    const formData = new FormData();
    formData.append('image_url', media[0].url); // Instagram requires publicly accessible URLs
    formData.append('caption', content.text);
    formData.append('access_token', accessToken);

    const containerResponse = await axios.post(
      `${INSTAGRAM_API_BASE}/${igAccountId}/media`,
      formData,
      { headers: formData.getHeaders() }
    );

    const containerId = containerResponse.data.id;

    // Step 2: Publish the container
    const publishResponse = await axios.post(
      `${INSTAGRAM_API_BASE}/${igAccountId}/media_publish`,
      {
        creation_id: containerId,
        access_token: accessToken
      }
    );

    const platformPostId = publishResponse.data.id;

    logger.info('Instagram post published successfully', {
      postId,
      platformPostId
    });

    return {
      success: true,
      platformPostId,
      platformPostUrl: `https://www.instagram.com/p/${platformPostId}/`,
      data: publishResponse.data
    };

  } catch (error) {
    logger.error('Error publishing Instagram post:', error);

    if (error.response?.data?.error?.code === 190) {
      throw new Error('Instagram access token expired');
    }

    if (error.response?.status === 429) {
      throw new Error('Instagram rate limit exceeded');
    }

    throw error;
  }
};

/**
 * Publish Instagram story
 */
const publishStory = async (postId, content, media, accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);
    const igAccountId = await getInstagramAccountId(accountId);

    if (!media || media.length === 0) {
      throw new Error('Instagram stories require at least one image');
    }

    // Create story container
    const response = await axios.post(
      `${INSTAGRAM_API_BASE}/${igAccountId}/media`,
      {
        media_type: media[0].type === 'video' ? 'VIDEO' : 'IMAGE',
        media_url: media[0].url,
        access_token: accessToken
      }
    );

    const containerId = response.data.id;

    // Publish story
    const publishResponse = await axios.post(
      `${INSTAGRAM_API_BASE}/${igAccountId}/media_publish`,
      {
        creation_id: containerId,
        access_token: accessToken
      }
    );

    logger.info('Instagram story published successfully', {
      postId,
      containerId
    });

    return {
      success: true,
      platformPostId: containerId,
      data: publishResponse.data
    };

  } catch (error) {
    logger.error('Error publishing Instagram story:', error);
    throw error;
  }
};

/**
 * Delete Instagram post
 */
const deletePost = async (platformPostId, accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);

    await axios.delete(`${INSTAGRAM_API_BASE}/${platformPostId}`, {
      params: { access_token: accessToken }
    });

    logger.info('Instagram post deleted successfully', { postId: platformPostId });

    return { success: true };

  } catch (error) {
    logger.error('Error deleting Instagram post:', error);
    throw error;
  }
};

/**
 * Get post metrics
 */
const getMetrics = async (platformPostId, accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);

    const response = await axios.get(
      `${INSTAGRAM_API_BASE}/${platformPostId}/insights`,
      {
        params: {
          metric: 'engagement,impressions,reach,likes,comments,shares',
          access_token: accessToken
        }
      }
    );

    const metrics = {};
    response.data.data.forEach(item => {
      metrics[item.name] = item.values[0]?.value || 0;
    });

    return {
      likes: metrics.likes || 0,
      comments: metrics.comments || 0,
      shares: metrics.shares || 0,
      clicks: metrics.engagement || 0,
      impressions: metrics.impressions || 0,
      reach: metrics.reach || 0
    };

  } catch (error) {
    logger.error('Error fetching Instagram post metrics:', error);
    throw error;
  }
};

/**
 * Get account info
 */
const getAccountInfo = async (accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);
    const igAccountId = await getInstagramAccountId(accountId);

    const response = await axios.get(
      `${INSTAGRAM_API_BASE}/${igAccountId}`,
      {
        params: {
          fields: 'username,profile_picture_url,followers_count,media_count,biography',
          access_token: accessToken
        }
      }
    );

    const data = response.data;

    return {
      username: data.username,
      displayName: data.username,
      avatar: data.profile_picture_url,
      bio: data.biography,
      followers: data.followers_count || 0,
      verified: false,
      mediaCount: data.media_count || 0
    };

  } catch (error) {
    logger.error('Error fetching Instagram account info:', error);
    throw error;
  }
};

/**
 * Validate post content
 */
const validatePost = (text) => {
  const maxLength = 2200;
  const hashtagLimit = 30;

  // Extract hashtags
  const hashtags = text.match(/#[\w]+/g) || [];

  if (text.length > maxLength) {
    return {
      valid: false,
      error: `Caption exceeds maximum length of ${maxLength} characters`,
      length: text.length,
      maxLength
    };
  }

  if (hashtags.length > hashtagLimit) {
    return {
      valid: false,
      error: `Instagram allows maximum ${hashtagLimit} hashtags`,
      hashtagCount: hashtags.length,
      hashtagLimit
    };
  }

  return {
    valid: true,
    length: text.length,
    remaining: maxLength - text.length,
    hashtagCount: hashtags.length
  };
};

/**
 * Get Instagram user's media
 */
const getUserMedia = async (accountId, limit = 25) => {
  try {
    const accessToken = await getAccessToken(accountId);
    const igAccountId = await getInstagramAccountId(accountId);

    const response = await axios.get(
      `${INSTAGRAM_API_BASE}/${igAccountId}/media`,
      {
        params: {
          fields: 'id,caption,media_type,media_url,permalink,thumbnail_url,timestamp,like_count,comments_count',
          limit,
          access_token: accessToken
        }
      }
    );

    return response.data.data || [];

  } catch (error) {
    logger.error('Error fetching Instagram user media:', error);
    throw error;
  }
};

module.exports = {
  publishPost,
  publishStory,
  deletePost,
  getMetrics,
  getAccountInfo,
  validatePost,
  getUserMedia,
  getAccessToken,
  getInstagramAccountId
};
