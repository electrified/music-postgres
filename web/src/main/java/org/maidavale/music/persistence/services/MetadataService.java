package org.maidavale.music.persistence.services;

import com.google.common.collect.Streams;
import com.mpatric.mp3agic.*;
import org.apache.commons.lang3.StringUtils;
import org.maidavale.music.persistence.domain.Artist;
import org.maidavale.music.persistence.domain.AudioFile;
import org.maidavale.music.persistence.domain.Release;
import org.maidavale.music.persistence.domain.Track;
import org.maidavale.music.persistence.dto.ArtistWithTrackCount;
import org.maidavale.music.persistence.repositories.ArtistRepository;
import org.maidavale.music.persistence.repositories.ReleaseRepository;
import org.maidavale.music.persistence.repositories.TrackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@Service
public class MetadataService {

    private final static Logger LOG = LoggerFactory.getLogger(MetadataService.class);

    private final AudioFileService audioFileService;
    private final ReleaseRepository releaseRepository;
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;

    public MetadataService(final AudioFileService audioFileService, final ReleaseRepository releaseRepository, final TrackRepository trackRepository, final ArtistRepository artistRepository) {
        this.audioFileService = audioFileService;
        this.releaseRepository = releaseRepository;
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
    }

    public void populateMetadata(final UUID sourceId) {
        populateFileMetadataAndCreateTracksForFiles(audioFileService.getAudioFilesBySource(sourceId));
    }

    public Collection<Track> search(final String searchCriteria) {
        return trackRepository.findByNameContainingIgnoreCaseOrArtistNameContainingIgnoreCaseOrReleaseNameContainingIgnoreCase(searchCriteria, searchCriteria, searchCriteria);
    }

    public Collection<ArtistWithTrackCount> getArtists() {
        return artistRepository.findMostPopularArtists();
    }

    public Collection<Track> getTracksByArtist(final UUID id) {
        return trackRepository.findByArtistId(id);
    }

    private static ID3v1 getTagFromFile(final Mp3File mp3file) {
        ID3v1 tag = null;
        if (mp3file.hasId3v1Tag() && mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();

            if (id3v2Tag.getTitle() == null) {
                LOG.info("Using ID3 v1");
                tag = id3v1Tag;
            } else  if (id3v1Tag.getTitle() == null){
                LOG.info("Using ID3 v2");
                tag = id3v2Tag;
            } else {
                tag = id3v2Tag.getTitle().length() >= id3v1Tag.getTitle().length() ? id3v2Tag : id3v1Tag;
            }
        } else if(mp3file.hasId3v1Tag()) {
            tag = mp3file.getId3v1Tag();

        } else if(mp3file.hasId3v2Tag()) {
            tag = mp3file.getId3v2Tag();
        }
        return tag;
    }

    public void deleteAllMetadata() {
        trackRepository.deleteAll();
        releaseRepository.deleteAll();
        artistRepository.deleteAll();
    }

    private AudioFile populateFileMetadata(final AudioFile file) {
        LOG.info("Analysing file {}", file);
        if (file.getRelativePath().endsWith("mp3")) {
            analyseMp3(file);
        }

        return file;
    }

    private void analyseMp3(final AudioFile file) {
        try {
            final String path = file.getSource().getPath() + "/" + file.getRelativePath();

            final Mp3File mp3file = new Mp3File(path);

            LOG.info("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
            LOG.info("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
            LOG.info("Sample rate: " + mp3file.getSampleRate() + " Hz");
            LOG.info("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
            LOG.info("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
            LOG.info("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));

            file.setBitrate(mp3file.getBitrate());
            file.setSamplerate(mp3file.getSampleRate());
            file.setHasId3v1Tag(mp3file.hasId3v1Tag());
            file.setHasId3v2Tag(mp3file.hasId3v2Tag());
            file.setVbr(mp3file.isVbr());

            var t = file.getTrack() != null ? file.getTrack() : new Track();

            var tag = getTagFromFile(mp3file);

            if (tag != null) {
                constructTrackFromId3(tag, mp3file, t);
                file.setTrack(t);
                t.getFiles().add(file);
            }

        } catch (IOException | UnsupportedTagException | InvalidDataException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void constructTrackFromId3(final ID3v1 id3, final Mp3File file, final Track t) {
        Integer year = null;

        if (id3.getYear() != null ) {
            try {
                year = Integer.parseInt(id3.getYear());
            } catch (NumberFormatException e) {

            }
        }

        try {
            LOG.info("Track: {} Artist: {} Title: {} Album: {} Year: {} Genre: {} ({}) Comment: {}",
                    id3.getTrack(),
                    id3.getArtist(),
                    id3.getTitle(),
                    id3.getAlbum(),
                    year,
                    id3.getGenre(),
                    id3.getGenreDescription(),
                    id3.getComment());
        }
        catch (StringIndexOutOfBoundsException e) {
            LOG.error("Error getting text",  e);
        }

        if (id3.getTitle() != null) {
            t.setName(stripAndTruncate(id3.getTitle(), 255));
        }
        //Top 100 One Hit Wonders -2002/83 - The Safety Dance by Men Without Hats 1983.mp3
        if (id3.getGenreDescription() != null) {
            t.setGenre(stripAndTruncate(id3.getGenreDescription(), 255));
        }

        if (id3.getYear() != null) {
            t.setYear(year);
        }

        try {
            if (id3.getComment() != null) {
                t.setComment(stripAndTruncate(id3.getComment(), 255));
            }
        } catch (StringIndexOutOfBoundsException e) {
            LOG.error("Error getting comment text",  e);
        }

        if (id3.getTrack() != null) {
            t.setTrack(stripAndTruncate(id3.getTrack(), 8));
        }

        if (id3.getAlbum() != null && t.getRelease().stream().noneMatch(r -> r.getName().equals(id3.getAlbum().strip()))) {
            t.addRelease(findReleaseOrCreate(id3.getAlbum()));
        }

        if (id3.getArtist() != null && t.getArtist().stream().noneMatch(r -> r.getName().equals(id3.getArtist().strip()))) {
            t.addArtist(findArtistOrCreate(id3.getArtist()));
        }

        t.setLength(file.getLengthInSeconds());
    }

    private Release findReleaseOrCreate(final String albumTitle) {
        String trimmedTitle = stripAndTruncate(albumTitle, 255);
        var releases = releaseRepository.findByName(trimmedTitle);
        if (releases.size() > 0) {
            return releases.stream().findFirst().get();
        }
        return new Release(trimmedTitle);
    }

    private Artist findArtistOrCreate(final String name) {
        String trimmedTitle =stripAndTruncate(name, 255);

        var artists = artistRepository.findByName(trimmedTitle);
        if (artists.size() > 0) {
            return artists.stream().findFirst().get();
        }
        return new Artist(trimmedTitle);
    }

    private void populateFileMetadataAndCreateTracksForFiles(final Iterable<AudioFile> files) {
        Streams.stream(files)
                .parallel()
                .map(this::populateFileMetadata)
                .forEach(audioFileService::updateFile);
    }
    
    private String stripAndTruncate(final String input, final Integer maxLength) {
        return StringUtils.left(input.replace("\0", "").strip(), maxLength);
    }
}
