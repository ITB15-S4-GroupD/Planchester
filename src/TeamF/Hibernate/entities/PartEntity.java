package TeamF.Hibernate.entities;

import TeamF.Hibernate.enums.SectionType;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "Part", schema = "sem4_team2", catalog = "")
public class PartEntity {
    private int partId;
    private int partType;
    private SectionType sectionType;
    private Collection<MusicianPartEntity> musicianPartsByPartId;
    private PartTypeEntity partTypeByPartType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partID", nullable = false)
    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    @Basic
    @Column(name = "partType", nullable = false)
    public int getPartType() {
        return partType;
    }

    public void setPartType(int partType) {
        this.partType = partType;
    }

    @Basic
    @Column(name = "sectionType", nullable = false, precision = 0)
    @Enumerated(EnumType.STRING)
    public SectionType getSectionType() {
        return sectionType;
    }

    public void setSectionType(SectionType sectionType) {
        this.sectionType = sectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartEntity that = (PartEntity) o;

        if (partId != that.partId) return false;
        if (partType != that.partType) return false;
        if (sectionType != null ? !sectionType.equals(that.sectionType) : that.sectionType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = partId;
        result = 31 * result + partType;
        result = 31 * result + (sectionType != null ? sectionType.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "partByPart")
    public Collection<MusicianPartEntity> getMusicianPartsByPartId() {
        return musicianPartsByPartId;
    }

    public void setMusicianPartsByPartId(Collection<MusicianPartEntity> musicianPartsByPartId) {
        this.musicianPartsByPartId = musicianPartsByPartId;
    }

    @ManyToOne
    @JoinColumn(name = "partType", referencedColumnName = "partTypeID", nullable = false, insertable = false, updatable = false)
    public PartTypeEntity getPartTypeByPartType() {
        return partTypeByPartType;
    }

    public void setPartTypeByPartType(PartTypeEntity partTypeByPartType) {
        this.partTypeByPartType = partTypeByPartType;
    }
}
