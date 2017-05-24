package TeamF.Hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class PersonOrchestraRoleEntityPK implements Serializable {
    private int person;
    private Enum orchestraRole;

    @Column(name = "person", nullable = false)
    @Id
    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    @Column(name = "orchestraRole", nullable = false)
    @Id
    public Enum getOrchestraRole() {
        return orchestraRole;
    }

    public void setOrchestraRole(Enum orchestraRole) {
        this.orchestraRole = orchestraRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonOrchestraRoleEntityPK that = (PersonOrchestraRoleEntityPK) o;

        if (person != that.person) return false;
        if (orchestraRole != null ? !orchestraRole.equals(that.orchestraRole) : that.orchestraRole != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = person;
        result = 31 * result + (orchestraRole != null ? orchestraRole.hashCode() : 0);
        return result;
    }
}
