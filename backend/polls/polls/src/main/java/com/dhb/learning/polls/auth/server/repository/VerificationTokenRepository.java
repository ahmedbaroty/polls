package com.dhb.learning.polls.auth.server.repository;

import com.dhb.learning.polls.auth.server.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByToken(String token);

}
