package com.gogidix.sysadmin.userprovisioning.domain.repository;
import com.gogidix.sysadmin.userprovisioning.domain.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {}
