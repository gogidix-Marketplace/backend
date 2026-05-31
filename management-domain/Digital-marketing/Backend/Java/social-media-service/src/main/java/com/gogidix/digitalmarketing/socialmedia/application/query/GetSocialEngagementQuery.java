package com.gogidix.digitalmarketing.socialmedia.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSocialEngagementQuery {

    private String tenantId;
    private String id;
}
