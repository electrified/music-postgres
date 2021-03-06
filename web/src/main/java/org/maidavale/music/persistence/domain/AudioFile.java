package org.maidavale.music.persistence.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class AudioFile extends BaseEntity {
    private String relativePath;
    private String md5sum;
    private String filetype;
    private Integer bitrate;
    private Boolean vbr;
    private Integer samplerate;
    private Boolean hasId3v1Tag;
    private Boolean hasId3v2Tag;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Track track;

    @JsonIgnore
    @ManyToOne
    private Source source;

    public AudioFile(Source source, String relativePath) {
        this.source = source;
        this.relativePath = relativePath;
    }

    public AudioFile() {
    }

    public Source getSource() {
        return source;
    }


    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Boolean getVbr() {
        return vbr;
    }

    public void setVbr(Boolean vbr) {
        this.vbr = vbr;
    }

    public Integer getSamplerate() {
        return samplerate;
    }

    public void setSamplerate(Integer samplerate) {
        this.samplerate = samplerate;
    }

    public Boolean getHasId3v1Tag() {
        return hasId3v1Tag;
    }

    public void setHasId3v1Tag(Boolean hasId3v1Tag) {
        this.hasId3v1Tag = hasId3v1Tag;
    }

    public Boolean getHasId3v2Tag() {
        return hasId3v2Tag;
    }

    public void setHasId3v2Tag(Boolean hasId3v2Tag) {
        this.hasId3v2Tag = hasId3v2Tag;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public String toString() {
        return "AudioFile{" +
                "id=" + id +
                ", relativePath='" + relativePath + '\'' +
                ", source=" + source +
                '}';
    }
}
