/**
 * Facebook Service
 * Integration with Facebook Graph API
 */

const axios = require('axios');
const logger = require('../utils/logger');
const SocialAccount = require('../models/SocialAccount');

const FACEBOOK_API_BASE = 'https://graph.facebook.com/v18.0';

/**
 * Get Facebook access token
 */
const getAccessToken = async (accountId) => {
  const account = await SocialAccount.getByAccountId(accountId);

  if (!account || account.platform !== 'facebook') {
    throw new Error('Facebook account not found');
  }

  if (!account.isActive) {
    throw new Error('Facebook account is not active');
  }

  await account.updateLastUsed();

  return account.credentials.accessToken;
};

/**
 * Get page ID
 */
const getPageId = async (accountId) => {
  const account = await SocialAccount.getByAccountId(accountId);
  return account.config?.pageId || process.env.FACEBOOK_PAGE_ID;
};

/**
 * Publish post to Facebook
 */
const publishPost = async (postId, content, media, accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);
    const pageId = await getPageId(accountId);

    let postData = {
      message: content.text
    };

    let endpoint = `${FACEBOOK_API_BASE}/${pageId}/feed`;

    // Handle media uploads
    if (media && media.length > 0) {
      const fs = require('fs');
      const FormData = require('form-data');

      if (media.length === 1) {
        // Single photo
        endpoint = `${FACEBOOK_API_BASE}/${pageId}/photos`;

        const formData = new FormData();
        formData.append('caption', content.text);
        formData.append('source', fs.createReadStream(media[0].path));
        formData.append('access_token', accessToken);
        formData.append('published', 'true');

        const response = await axios.post(endpoint, formData, {
          headers: formData.getHeaders()
        });

        const platformPostId = response.data.id;

        logger.info('Facebook photo post published successfully', {
          postId,
          platformPostId
        });

        return {
          success: true,
          platformPostId,
          platformPostUrl: `https://www.facebook.com/${platformPostId}`,
          data: response.data
        };
      } else {
        // Multiple photos - need to create album first
        const albumResponse = await axios.post(
          `${FACEBOOK_API_BASE}/${pageId}/albums`,
          {
            name: content.text.substring(0, 255) || 'New Album',
            access_token: accessToken
          }
        );

        const albumId = albumResponse.data.id;

        // Upload photos to album
        for (const item of media) {
          const formData = new FormData();
          formData.append('source', fs.createReadStream(item.path));
          formData.append('access_token', accessToken);
          formData.append('published', 'true');

          await axios.post(
            `${FACEBOOK_API_BASE}/${albumId}/photos`,
            formData,
            { headers: formData.getHeaders() }
          );
        }

        const platformPostId = albumId;

        logger.info('Facebook album post published successfully', {
          postId,
          platformPostId
        });

        return {
          success: true,
          platformPostId,
          platformPostUrl: `https://www.facebook.com/${pageId}/albums/${platformPostId}`,
          data: { id: platformPostId }
        };
      }
    }

    // Text-only post
    const response = await axios.post(endpoint, {
      ...postData,
      access_token: accessToken
    });

    const platformPostId = response.data.id;

    logger.info('Facebook post published successfully', {
      postId,
      platformPostId
    });

    return {
      success: true,
      platformPostId,
      platformPostUrl: `https://www.facebook.com/${platformPostId}`,
      data: response.data
    };

  } catch (error) {
    logger.error('Error publishing Facebook post:', error);

    if (error.response?.data?.error?.code === 190) {
      throw new Error('Facebook access token expired');
    }

    if (error.response?.status === 429) {
      throw new Error('Facebook rate limit exceeded');
    }

    throw error;
  }
};

/**
 * Delete Facebook post
 */
const deletePost = async (platformPostId, accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);

    await axios.delete(`${FACEBOOK_API_BASE}/${platformPostId}`, {
      params: { access_token: accessToken }
    });

    logger.info('Facebook post deleted successfully', { postId: platformPostId });

    return { success: true };

  } catch (error) {
    logger.error('Error deleting Facebook post:', error);
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
      `${FACEBOOK_API_BASE}/${platformPostId}/insights`,
      {
        params: {
          metric: 'post_impressions,post_engaged_users,post_reactions_like_total,post_comments,post_shares',
          access_token: accessToken
        }
      }
    );

    const metrics = {};
    response.data.data.forEach(item => {
      metrics[item.name] = item.values[0]?.value || 0;
    });

    return {
      likes: metrics.post_reactions_like_total || 0,
      comments: metrics.post_comments || 0,
      shares: metrics.post_shares || 0,
      clicks: metrics.post_engaged_users || 0,
      impressions: metrics.post_impressions || 0
    };

  } catch (error) {
    logger.error('Error fetching Facebook post metrics:', error);
    throw error;
  }
};

/**
 * Get account info
 */
const getAccountInfo = async (accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);
    const pageId = await getPageId(accountId);

    const response = await axios.get(
      `${FACEBOOK_API_BASE}/${pageId}`,
      {
        params: {
          fields: 'name,picture,fan_count,followers_count,verified',
          access_token: accessToken
        }
      }
    );

    const data = response.data;

    return {
      username: data.id,
      displayName: data.name,
      avatar: data.picture?.data?.url,
      followers: data.fan_count || data.followers_count || 0,
      verified: data.verified || false
    };

  } catch (error) {
    logger.error('Error fetching Facebook account info:', error);
    throw error;
  }
};

/**
 * Validate post content
 */
const validatePost = (text) => {
  const maxLength = 63206;

  if (text.length > maxLength) {
    return {
      valid: false,
      error: `Post exceeds maximum length of ${maxLength} characters`,
      length: text.length,
      maxLength
    };
  }

  return {
    valid: true,
    length: text.length,
    remaining: maxLength - text.length
  };
};

module.exports = {
  publishPost,
  deletePost,
  getMetrics,
  getAccountInfo,
  validatePost,
  getAccessToken,
  getPageId
};
