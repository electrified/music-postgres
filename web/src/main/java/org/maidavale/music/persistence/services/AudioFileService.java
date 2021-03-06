package org.maidavale.music.persistence.services;

import org.maidavale.music.persistence.domain.AudioFile;
import org.maidavale.music.persistence.domain.Source;
import org.maidavale.music.persistence.domain.Track;
import org.maidavale.music.persistence.repositories.AudioFileRepository;
import org.maidavale.music.persistence.repositories.SourceRepository;
import org.maidavale.music.persistence.repositories.TrackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Service
public class AudioFileService {

    private final static Logger LOG = LoggerFactory.getLogger(AudioFileService.class);

    private final AudioFileRepository audioFileRepository;
    private final SourceRepository sourceRepository;
    private final TrackRepository trackRepository;

    public AudioFileService(final AudioFileRepository audioFileRepository, final SourceRepository sourceRepository, final TrackRepository trackRepository) {
        this.audioFileRepository = audioFileRepository;
        this.sourceRepository = sourceRepository;
        this.trackRepository = trackRepository;
    }

    private void addFile(final Source documentRoot, final String audioFile) {
        var existingFiles = audioFileRepository.findBySourceIdAndRelativePath(documentRoot.getId(), audioFile);

        if (existingFiles.size() == 0) {
            final var audioFileNode = new AudioFile(documentRoot, audioFile);
            LOG.info("adding {}", audioFile);
            audioFileRepository.save(audioFileNode);
//            documentRoot.addAudioFile(audioFileNode);
        } else {
            LOG.info("Already in database {}", audioFile);
        }
    }

    void updateFile(final AudioFile audioFile) {
        LOG.info("Saving AudioFile {}", audioFile.getId());
        audioFileRepository.save(audioFile);
//        if (audioFile.getTrack() != null) {
//            Track t = trackRepository.save(audioFile.getTrack());
//            LOG.info("Saved associated track id {}", t.getId());
//        }
    }

    public void deleteAudioFiles() {
        audioFileRepository.deleteAll();
        trackRepository.deleteAll();
    }

    Iterable<AudioFile> getAudioFiles() {
        return audioFileRepository.findAll();
    }

    private String calculateRelativePath(final Path documentRoot, final Path audioFile) {
        return documentRoot.relativize(audioFile).toString();
    }

    public Source addSource(final String path) {
        final var documentRootPath = Paths.get(path);
        Source src = sourceRepository.findByPath(documentRootPath.toString());

        if (src == null) {
            src = sourceRepository.save(new Source(documentRootPath.toString()));
            LOG.info("Saved source {}", src);
        }
        return src;
    }

    public void importAudio(final UUID sourceId) {
        Source src = sourceRepository.findById(sourceId).get();

        final var documentRootPath = Paths.get(src.getPath());

        final PathMatcher filter = documentRootPath.getFileSystem().getPathMatcher("glob:**.{mp3,wav,flac,ogg}");

        try {
            Files.walk(documentRootPath, FileVisitOption.FOLLOW_LINKS)
//                    .parallel()
                    .filter(Files::isReadable)
                    .filter(Files::isRegularFile)
                    .filter(filter::matches)
                    .forEach(f -> addFile(src, calculateRelativePath(documentRootPath, f)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<AudioFile> getFileByTrackId(final UUID id) {
        return audioFileRepository.findByTrackId(id).stream().findFirst();
    }

    public Iterable<Source> getSources() {
        return sourceRepository.findAll();
    }

    public Iterable<AudioFile> getAudioFilesBySource(final UUID sourceId) {
        return audioFileRepository.findBySourceId(sourceId);
    }

    public void deleteSourceAndFiles(final UUID sourceId) {
        sourceRepository.deleteById(sourceId);
//        trackRepository.deleteLooseTracks();
    }
}
