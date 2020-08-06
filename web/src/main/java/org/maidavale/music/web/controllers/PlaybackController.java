package org.maidavale.music.web.controllers;

import org.maidavale.music.persistence.domain.Track;
import org.maidavale.music.persistence.dto.ArtistWithTrackCount;
import org.maidavale.music.persistence.services.MetadataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@PreAuthorize("principal.claims['groups'].contains('listeners')")
@CrossOrigin(origins = "http://localhost:3020")
@RestController
@RequestMapping("/playback")
public class PlaybackController {

    private final MetadataService metadataService;

    public PlaybackController(final MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @RequestMapping("/search")
    public Collection<Track> search(@RequestParam("query") final String searchCriteria) {
        return metadataService.search(searchCriteria);
    }

    @RequestMapping("/artists")
    public Collection<ArtistWithTrackCount> getArtists() {
        return metadataService.getArtists();
    }

    @RequestMapping("/artists/{artistId}/tracks")
    public Collection<Track> getTracksByArtist(@PathVariable("artistId") final UUID artistId) {
        return metadataService.getTracksByArtist(artistId);
    }
}
