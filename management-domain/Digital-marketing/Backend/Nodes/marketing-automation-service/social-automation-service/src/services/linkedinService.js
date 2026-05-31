/**
 * LinkedIn Service
 * Integration with LinkedIn API
 */

const axios = require('axios');
const logger = require('../utils/logger');
const SocialAccount = require('../models/SocialAccount');

const LINKEDIN_API_BASE = 'https://api.linkedin.com/v2';

/**
 * Get LinkedIn access token
 */
const getAccessToken = async (accountId) => {
  const account = await SocialAccount.getByAccountId(accountId);

  if (!account || account.platform !== 'linkedin') {
    throw new Error('LinkedIn account not found');
  }

  if (!account.isActive) {
    throw new Error('LinkedIn account is not active');
  }

  // Update last used
  await account.updateLastUsed();

  return account.credentials.accessToken;
};

/**
 * Publish post to LinkedIn
 */
const publishPost = async (postId, content, media, accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);

    // Get person URN
    const profileResponse = await axios.get(
      `${LINKEDIN_API_BASE}/me`,
      {
        headers: { Authorization: `Bearer ${accessToken}` }
      }
    );

    const personUrn = profileResponse.data.id;

    let postPayload = {
      author: personUrn,
      lifecycleState: 'PUBLISHED',
      specificContent: {
        'com.linkedin.ugc.ShareContent': {
          shareCommentary: {
            text: content.text
          },
          shareMediaCategory: media && media.length > 0 ? 'IMAGE' : 'NONE'
        }
      },
      visibility: {
        'com.linkedin.ugc.MemberNetworkVisibility': 'PUBLIC'
      }
    };

    // Add media if present
    if (media && media.length > 0) {
      const mediaAssets = [];

      for (const item of media) {
        try {
          // Register media upload
          const registerResponse = await axios.post(
            `${LINKEDIN_API_BASE}/assets?action=registerUpload`,
            {
              registerUploadRequest: {
                owner: personUrn,
                recipes: ['urn:li:digitalmediaAsset:urn:li:digitalmediaAsset:image:png'],
                serviceRelationships: [
                  {
                    relationshipType: 'OWNER',
                    asset: 'urn:li:digitalmediaAsset:image:png'
                  }
                ],
                supportedUploadMechanism: ['SYNCHRONOUS_UPLOAD']
              }
            },
            {
              headers: {
                Authorization: `Bearer ${accessToken}`,
                'X-Restli-Protocol-Version': '2.0.0'
              }
            }
          );

          const uploadUrl = registerResponse.data.value.uploadMechanism['com.linkedin.digitalmedia.uploading.MediaUploadWebSocket'].uploadUrl;
          const assetUrn = registerResponse.data.value.asset;

          // Upload binary
          const fs = require('fs');
          const imageData = fs.readFileSync(item.path);

          await axios.put(uploadUrl, imageData, {
            headers: {
              'Content-Type': item.mimeType || 'image/png',
              'X-Restli-Protocol-Version': '2.0.0'
            }
          });

          mediaAssets.push({
            status: 'READY',
            description: {
              text: item.altText || ''
            },
            media: assetUrn,
            title: {
              text: item.filename || 'image'
            }
          });

        } catch (uploadError) {
          logger.error('Error uploading media to LinkedIn:', uploadError);
        }
      }

      if (mediaAssets.length > 0) {
        postPayload.specificContent['com.linkedin.ugc.ShareContent'].media = mediaAssets;
      }
    }

    // Create post
    const response = await axios.post(
      `${LINKEDIN_API_BASE}/ugcPosts`,
      postPayload,
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'X-Restli-Protocol-Version': '2.0.0',
          'Content-Type': 'application/json'
        }
      }
    );

    const platformPostId = response.data.id;

    logger.info('LinkedIn post published successfully', {
      postId,
      platformPostId
    });

    return {
      success: true,
      platformPostId,
      platformPostUrl: `https://www.linkedin.com/feed/update/${platformPostId}`,
      data: response.data
    };

  } catch (error) {
    logger.error('Error publishing LinkedIn post:', error);

    if (error.response?.status === 429) {
      throw new Error('LinkedIn rate limit exceeded');
    }

    throw error;
  }
};

/**
 * Delete LinkedIn post
 */
const deletePost = async (platformPostId, accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);

    await axios.delete(
      `${LINKEDIN_API_BASE}/ugcPosts/${platformPostId}`,
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'X-Restli-Protocol-Version': '2.0.0'
        }
      }
    );

    logger.info('LinkedIn post deleted successfully', { postId: platformPostId });

    return { success: true };

  } catch (error) {
    logger.error('Error deleting LinkedIn post:', error);
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
      `${LINKEDN_API_BASE}/socialActions/${platformPostId}`,
      {
        headers: {
          Authorization: `Bearer ${accessToken}`
        },
        params: {
          'projection': '(*,likes*(*,actor~),comments*(*,actor~),shares*(*,actor~))'
        }
      }
    );

    const data = response.data;

    return {
      likes: data.likes?.total || 0,
      comments: data.comments?.total || 0,
      shares: data.shares?.total || 0,
      clicks: 0,
      impressions: 0
    };

  } catch (error) {
    logger.error('Error fetching LinkedIn post metrics:', error);
    throw error;
  }
};

/**
 * Get account info
 */
const getAccountInfo = async (accountId) => {
  try {
    const accessToken = await getAccessToken(accountId);

    const response = await axios.get(
      `${LINKEDIN_API_BASE}/me`,
      {
        headers: {
          Authorization: `Bearer ${accessToken}`
        },
        params: {
          projection: '(id,firstName,lastName,profilePicture(displayImage~:playableStreams))'
        }
      }
    );

    const data = response.data;

    return {
      username: data.id,
      displayName: `${data.firstName.localized.en_US} ${data.lastName.localized.en_US}`,
      avatar: data.profilePicture?.['displayImage~']?.elements?.[0]?.identifiers?.[0]?.identifier,
      followers: 0,
      verified: false
    };

  } catch (error) {
    logger.error('Error fetching LinkedIn account info:', error);
    throw error;
  }
};

/**
 * Validate post content
 */
const validatePost = (text) => {
  const maxLength = 3000;

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
  getAccessToken
};
