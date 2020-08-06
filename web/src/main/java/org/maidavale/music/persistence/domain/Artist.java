package org.maidavale.music.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Artist extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "artist")
    private Set<Track> track;

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
