package com.gogidix.corporatecms.domain.repository;

import com.gogidix.corporatecms.domain.enums.UserRole;
import com.gogidix.corporatecms.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsernameAndDeletedFalse(String username);

    Optional<User> findByEmailAndDeletedFalse(String email);

    Optional<User> findByUsernameOrEmailAndDeletedFalse(String username, String email);

    List<User> findByRoleAndDeletedFalse(UserRole role);

    Page<User> findByRoleAndDeletedFalse(UserRole role, Pageable pageable);

    @Query("{'$or': [" +
            "{'username': {$regex: ?0, $options: 'i'}}, " +
            "{'email': {$regex: ?0, $options: 'i'}}, " +
            "{'firstName': {$regex: ?0, $options: 'i'}}, " +
            "{'lastName': {$regex: ?0, $options: 'i'}}" +
            "], 'deleted': false}")
    Page<User> searchByKeyword(String keyword, Pageable pageable);

    List<User> findByDepartmentAndDeletedFalse(String department);

    List<User> findByEnabledTrueAndDeletedFalse();

    Boolean existsByUsernameAndDeletedFalse(String username);

    Boolean existsByEmailAndDeletedFalse(String email);

    Long countByRoleAndDeletedFalse(UserRole role);

    @Query("{'enabled': true, 'deleted': false}")
    Long countActiveUsers();
}
