package Domain.Model;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "Part", schema = "sem4_team2", catalog = "")
public class PartEntity {
    private int partId;
    private Enum sectionType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partID")
    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartEntity that = (PartEntity) o;

        if (partId != that.partId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return partId;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "sectionType")
    public Enum getSectionType() {
        return sectionType;
    }

    public void setSectionType(Enum sectionType) {
        this.sectionType = sectionType;
    }
}
