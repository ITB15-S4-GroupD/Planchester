package TeamF.Hibernate.entities;

import TeamF.Hibernate.enums.OrchestraRole;
import javax.persistence.*;

@Entity
@Table(name = "Person_OrchestraRole", schema = "sem4_team2", catalog = "")
@IdClass(PersonOrchestraRoleEntityPK.class)
public class PersonOrchestraRoleEntity {
    private int person;
    private OrchestraRole orchestraRole;
    private PersonEntity personByPerson;

    @Id
    @Column(name = "person", nullable = false)
    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    @Id
    @Column(name = "orchestraRole", nullable = false)
    @Enumerated(EnumType.STRING)
    public OrchestraRole getOrchestraRole() {
        return orchestraRole;
    }

    public void setOrchestraRole(OrchestraRole orchestraRole) {
        this.orchestraRole = orchestraRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonOrchestraRoleEntity that = (PersonOrchestraRoleEntity) o;

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

    @ManyToOne
    @JoinColumn(name = "person", referencedColumnName = "personId", nullable = false, insertable = false, updatable = false)
    public PersonEntity getPersonByPerson() {
        return personByPerson;
    }

    public void setPersonByPerson(PersonEntity personByPerson) {
        this.personByPerson = personByPerson;
    }
}
