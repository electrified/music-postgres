package org.maidavale.music.persistence.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maidavale.music.persistence.domain.Artist;
import org.maidavale.music.persistence.domain.AudioFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AudioFileServiceTest {

    @Autowired
    AudioFileService audioFileService;

    @Test
    public void testUpdateFile() {

        var source = audioFileService.addSource("/cheese/");

        var af = new AudioFile();
        af.setBitrate(128);
        af.setFiletype("mp3");
        af.setHasId3v1Tag(false);
        af.setHasId3v2Tag(false);
        af.setRelativePath("/cheese/whatever");
        af.setSource(source);
        audioFileService.updateFile(af);

        var artist = new Artist();
        artist.setName("MRSA");
    }

    public void testAddSource() {
    }
}