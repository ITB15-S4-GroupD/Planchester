package Domain.Entities;

import javax.persistence.*;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "PartType", schema = "sem4_team2", catalog = "")
public class PartTypeEntity {
    private int partTypeId;
    private String partType;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partTypeID")
    public int getPartTypeId() {
        return partTypeId;
    }

    public void setPartTypeId(int partTypeId) {
        this.partTypeId = partTypeId;
    }

    @Basic
    @Column(name = "partType")
    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartTypeEntity that = (PartTypeEntity) o;

        if (partTypeId != that.partTypeId) return false;
        if (partType != null ? !partType.equals(that.partType) : that.partType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = partTypeId;
        result = 31 * result + (partType != null ? partType.hashCode() : 0);
        return result;
    }
}
