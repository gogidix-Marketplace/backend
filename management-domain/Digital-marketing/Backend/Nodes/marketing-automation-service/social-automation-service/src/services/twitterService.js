/**
 * Twitter Service
 * Integration with Twitter API v2
 */

const { TwitterApi } = require('twitter-api-v2');
const logger = require('../utils/logger');
const SocialAccount = require('../models/SocialAccount');

/**
 * Get Twitter client for account
 */
const getTwitterClient = async (accountId) => {
  const account = await SocialAccount.getByAccountId(accountId);

  if (!account || account.platform !== 'twitter') {
    throw new Error('Twitter account not found');
  }

  if (!account.isActive) {
    throw new Error('Twitter account is not active');
  }

  // Check if token is expired
  if (account.isTokenExpired()) {
    await account.refreshAccessToken();
  }

  // Update last used
  await account.updateLastUsed();

  return new TwitterApi(account.credentials.accessToken);
};

/**
 * Publish tweet
 */
const publishPost = async (postId, content, media, accountId) => {
  try {
    const client = await getTwitterClient(accountId);

    let tweetData = {
      text: content.text
    };

    // Upload media if present
    if (media && media.length > 0) {
      const mediaIds = [];

      for (const item of media) {
        try {
          const uploadedMedia = await client.v1.uploadMedia(item.path || item.url);
          mediaIds.push(uploadedMedia);
        } catch (uploadError) {
          logger.error('Error uploading media to Twitter:', uploadError);
        }
      }

      if (mediaIds.length > 0) {
        tweetData.media = {
          media_ids: mediaIds
        };
      }
    }

    // Post tweet
    const tweet = await client.v2.tweet(tweetData);

    logger.info('Tweet published successfully', {
      postId,
      tweetId: tweet.data.id
    });

    return {
      success: true,
      platformPostId: tweet.data.id,
      platformPostUrl: `https://twitter.com/i/status/${tweet.data.id}`,
      data: tweet.data
    };

  } catch (error) {
    logger.error('Error publishing tweet:', error);

    // Handle rate limiting
    if (error.code === 429) {
      throw new Error('Twitter rate limit exceeded');
    }

    throw error;
  }
};

/**
 * Delete tweet
 */
const deletePost = async (platformPostId, accountId) => {
  try {
    const client = await getTwitterClient(accountId);

    await client.v2.deleteTweet(platformPostId);

    logger.info('Tweet deleted successfully', { tweetId: platformPostId });

    return { success: true };

  } catch (error) {
    logger.error('Error deleting tweet:', error);
    throw error;
  }
};

/**
 * Get tweet metrics
 */
const getMetrics = async (platformPostId, accountId) => {
  try {
    const client = await getTwitterClient(accountId);

    const tweet = await client.v2.singleTweet(platformPostId, {
      'tweet.fields': ['public_metrics', 'created_at']
    });

    const metrics = tweet.data.public_metrics;

    return {
      likes: metrics.like_count,
      comments: metrics.reply_count,
      shares: metrics.retweet_count,
      clicks: 0, // Twitter doesn't provide click count in public API
      impressions: metrics.impression_count
    };

  } catch (error) {
    logger.error('Error fetching tweet metrics:', error);
    throw error;
  }
};

/**
 * Upload media
 */
const uploadMedia = async (file, accountId) => {
  try {
    const client = await getTwitterClient(accountId);

    const mediaId = await client.v1.uploadMedia(file.path, {
      type: file.mimeType
    });

    return {
      mediaId,
      success: true
    };

  } catch (error) {
    logger.error('Error uploading media to Twitter:', error);
    throw error;
  }
};

/**
 * Get account info
 */
const getAccountInfo = async (accountId) => {
  try {
    const client = await getTwitterClient(accountId);

    const user = await client.v2.me({
      'user.fields': ['username', 'name', 'description', 'verified', 'public_metrics', 'profile_image_url']
    });

    return {
      username: user.data.username,
      displayName: user.data.name,
      bio: user.data.description,
      verified: user.data.verified,
      avatar: user.data.profile_image_url,
      followers: user.data.public_metrics.followers_count,
      following: user.data.public_metrics.following_count
    };

  } catch (error) {
    logger.error('Error fetching Twitter account info:', error);
    throw error;
  }
};

/**
 * Validate tweet length
 */
const validateTweet = (text) => {
  const maxLength = 280;

  if (text.length > maxLength) {
    return {
      valid: false,
      error: `Tweet exceeds maximum length of ${maxLength} characters`,
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

/**
 * Get rate limit status
 */
const getRateLimitStatus = async (accountId) => {
  try {
    const client = await getTwitterClient(accountId);

    const limits = await client.v2.rateLimit();

    return {
      limits: limits.data || {},
      resetAt: new Date(Date.now() + 15 * 60 * 1000) // Twitter resets every 15 min
    };

  } catch (error) {
    logger.error('Error fetching rate limit status:', error);
    throw error;
  }
};

module.exports = {
  publishPost,
  deletePost,
  getMetrics,
  uploadMedia,
  getAccountInfo,
  validateTweet,
  getRateLimitStatus,
  getTwitterClient
};
