package org.maidavale.music.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Track extends BaseEntity {
    private String track;

//    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Artist> artist = new HashSet<>();

    private String name;

//    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Release> release = new HashSet<>();

    private Integer year;
    private String genre;
    private String comment;
    private Long length;

    @OneToMany(mappedBy = "track")
    private Set<AudioFile> files = new HashSet<>();

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public Set<Artist> getArtist() {
        return artist;
    }

    public void setArtist(Set<Artist> artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Release> getRelease() {
        return release;
    }

    public void setRelease(Set<Release> release) {
        this.release = release;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<AudioFile> getFiles() {
        return files;
    }

    public void setFiles(Set<AudioFile> files) {
        this.files = files;
    }

    public void addRelease(Release release) {
        this.release.add(release);
    }

    public void addArtist(Artist artist) {
        this.artist.add(artist);
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}
