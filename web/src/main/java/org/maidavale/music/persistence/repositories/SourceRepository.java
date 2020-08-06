package org.maidavale.music.persistence.repositories;

import org.maidavale.music.persistence.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SourceRepository extends JpaRepository<Source, UUID> {

    Source findByPath(final String path);

    void deleteById(UUID id);
}
