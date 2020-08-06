package org.maidavale.music.persistence.dto;

import org.maidavale.music.persistence.domain.Artist;

public interface ArtistWithTrackCount {
    Artist getArtist();
    int getTrackCount();
}
