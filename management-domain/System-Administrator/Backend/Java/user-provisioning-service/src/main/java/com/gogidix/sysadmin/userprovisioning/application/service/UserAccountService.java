package com.gogidix.sysadmin.userprovisioning.application.service;
import com.gogidix.sysadmin.userprovisioning.domain.model.UserAccount;
import com.gogidix.sysadmin.userprovisioning.domain.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserAccountRepository repository;
    public UserAccount create(UserAccount entity) { return repository.save(entity); }
    public UserAccount getById(String id) { return repository.findById(id).orElse(null); }
    public List<UserAccount> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}
