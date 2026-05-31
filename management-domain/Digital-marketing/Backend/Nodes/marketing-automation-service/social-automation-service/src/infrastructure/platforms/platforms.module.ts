import { Module } from '@nestjs/common';
import { SOCIAL_PLATFORM_PORT } from '../../../domain/ports/services/social-platform.port';
import { TwitterAdapter } from './twitter.adapter';
import { LinkedinAdapter } from './linkedin.adapter';
import { FacebookAdapter } from './facebook.adapter';
import { InstagramAdapter } from './instagram.adapter';

const platformFactories = {
  twitter: TwitterAdapter,
  linkedin: LinkedinAdapter,
  facebook: FacebookAdapter,
  instagram: InstagramAdapter,
};

@Module({
  providers: [
    TwitterAdapter, LinkedinAdapter, FacebookAdapter, InstagramAdapter,
    { provide: 'SOCIAL_PLATFORM_PORT' useFactory: (twitter, linkedin, facebook, instagram) => {
      const platform = process.env.DEFAULT_SOCIAL_PLATFORM ?? 'twitter';
      const adapters = { twitter, linkedin, facebook, instagram };
      return adapters[platform] ?? twitter;
    }, inject: [TwitterAdapter, LinkedinAdapter, FacebookAdapter, InstagramAdapter] },
  ],
  exports: [SOCIAL_PLATFORM_PORT, TwitterAdapter, LinkedinAdapter, FacebookAdapter, InstagramAdapter],
})
export class PlatformsModule {}
