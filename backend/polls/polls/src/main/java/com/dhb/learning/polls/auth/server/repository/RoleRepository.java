package com.dhb.learning.polls.auth.server.repository;

import com.dhb.learning.polls.auth.server.model.Role;
import com.dhb.learning.polls.auth.server.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(RoleName roleName);

}
