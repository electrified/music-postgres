package org.maidavale.music.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("org.maidavale.music.persistence.repositories")
@EnableJpaAuditing
public class JpaConfiguration {

}
