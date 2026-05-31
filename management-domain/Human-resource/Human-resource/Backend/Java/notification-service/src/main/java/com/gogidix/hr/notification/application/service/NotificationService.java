package com.gogidix.hr.notification.application.service;

import com.gogidix.hr.notification.domain.model.Notification;
import com.gogidix.hr.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    public Notification create(Notification notification) { return repository.save(notification); }
    public Notification getById(String id) { return repository.findById(id).orElse(null); }
    public List<Notification> getAll() { return repository.findAll(); }
    public Notification update(Notification notification) { return repository.save(notification); }
    public void delete(String id) { repository.deleteById(id); }
}
