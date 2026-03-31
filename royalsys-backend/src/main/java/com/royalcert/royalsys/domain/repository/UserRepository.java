package com.royalcert.royalsys.domain.repository;

import com.royalcert.royalsys.domain.entity.User;
import com.royalcert.royalsys.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByOrganizationIdAndRoleType(UUID orgId, RoleType roleType);
    List<User> findByOrganizationIdAndActiveTrue(UUID orgId);
}
