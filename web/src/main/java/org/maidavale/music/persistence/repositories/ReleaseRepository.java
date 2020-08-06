package org.maidavale.music.persistence.repositories;

import org.maidavale.music.persistence.domain.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface ReleaseRepository extends JpaRepository<Release, UUID> {

    Collection<Release> findByNameContainingIgnoreCase(@Param("name") final String name);

    Collection<Release> findByName(@Param("name") final String name);
}
