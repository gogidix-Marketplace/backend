package com.gogidix.digitalmarketing.socialmedia.application.service;

import com.gogidix.digitalmarketing.socialmedia.domain.model.SocialPost;
import com.gogidix.digitalmarketing.socialmedia.domain.repository.SocialPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialPostService {
    private final SocialPostRepository repository;

    public SocialPost create(SocialPost post) { return repository.save(post); }
    public SocialPost getById(String id) { return repository.findById(id).orElse(null); }
    public List<SocialPost> getAll() { return repository.findAll(); }
    public SocialPost update(SocialPost post) { return repository.save(post); }
    public void delete(String id) { repository.deleteById(id); }
}
