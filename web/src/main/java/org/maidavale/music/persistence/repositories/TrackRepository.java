package org.maidavale.music.persistence.repositories;

import org.maidavale.music.persistence.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface TrackRepository extends JpaRepository<Track, UUID> {
    Collection<Track> findByNameContainingIgnoreCaseOrArtistNameContainingIgnoreCaseOrReleaseNameContainingIgnoreCase(@Param("name") final String name, @Param("artistname") final String artistname, @Param("releasename") final String releasename);

    Collection<Track> findByArtistId(@Param("id") final UUID id);
}
