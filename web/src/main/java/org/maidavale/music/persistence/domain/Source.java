package org.maidavale.music.persistence.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Source  extends BaseEntity{
    private String path;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
    private Set<AudioFile> files = new HashSet<>();

    public Source(String path) {
        this.path = path;
    }

    public Source() {
    }

    public String getPath() {
        return path;
    }

    public Set<AudioFile> getFiles() {
        return files;
    }

    public void setFiles(Set<AudioFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", path='" + path + '\'' +
                '}';
    }
}
