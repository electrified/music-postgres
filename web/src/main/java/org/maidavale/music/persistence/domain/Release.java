package org.maidavale.music.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Release  extends BaseEntity{
    private String name;

    @ManyToMany(mappedBy = "release")
    private Set<Track> track;

    public Release() {
    }

    public Release(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
