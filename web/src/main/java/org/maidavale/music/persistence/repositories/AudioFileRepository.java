package org.maidavale.music.persistence.repositories;

import org.maidavale.music.persistence.domain.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface AudioFileRepository extends JpaRepository<AudioFile, UUID> {

    Collection<AudioFile> findBySourceIdAndRelativePath(@Param("sourceId") final UUID sourceId, @Param("relativePath") final String relativePath);

    Collection<AudioFile> findBySourceId(@Param("sourceId") final UUID sourceId);

    Collection<AudioFile> findByTrackId(@Param("trackId") final UUID trackId);
}
