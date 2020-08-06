package org.maidavale.music.persistence.repositories;

import org.maidavale.music.persistence.domain.Artist;
import org.maidavale.music.persistence.dto.ArtistWithTrackCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    Collection<Artist> findByNameContainingIgnoreCase(@Param("name") final String name);

    Collection<Artist> findByName(@Param("name") final String name);

    @Query("select a AS artist, count(t.id) AS trackCount from Artist a join a.track t group by a ORDER BY trackCount desc") // ORDER BY count desc
    Collection<ArtistWithTrackCount> findMostPopularArtists();
}
