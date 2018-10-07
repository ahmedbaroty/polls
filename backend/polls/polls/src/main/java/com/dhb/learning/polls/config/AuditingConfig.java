package com.dhb.learning.polls.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
* To enable JPA Auditing,
* we’ll need to add @EnableJpaAuditing annotation
*/

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
    // That's all here for now. We'll add more auditing configurations later.
}