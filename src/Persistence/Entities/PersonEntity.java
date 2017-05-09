package Persistence.Entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Bernd on 06.04.2017.
 */
@Entity
@Table(name = "Person", schema = "sem4_team2", catalog = "")
public class PersonEntity {
    private int personId;
    private String initials;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String address;
    private String phoneNumber;
    private String personRole;
    private Integer account;
    private Collection<DutyDispositionEntity> dutyDispositionsByPersonId;
    private Collection<InstrumentEntity> instrumentsByPersonId;
    private Collection<MusicianPartEntity> musicianPartsByPersonId;
    private AccountEntity accountByAccount;
    private Collection<PersonOrchestraRoleEntity> personOrchestraRolesByPersonId;
    private Collection<RequestEntity> requestsByPersonId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personId", nullable = false)
    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "initials", nullable = true, length = 10)
    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Basic
    @Column(name = "firstname", nullable = false, length = 255)
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "lastname", nullable = false, length = 255)
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "gender", nullable = false, length = 1)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "address", nullable = false, length = 255)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "phoneNumber", nullable = false, length = 255)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "personRole", nullable = false, columnDefinition = "enum('Musician', 'Substitute', 'External_musician', 'Orchestral_facility_manager', 'Music_librarian', 'Manager')")
    public String getPersonRole() {
        return personRole;
    }

    public void setPersonRole(String personRole) {
        this.personRole = personRole;
    }

    @Basic
    @Column(name = "account", nullable = true)
    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonEntity that = (PersonEntity) o;

        if (personId != that.personId) return false;
        if (initials != null ? !initials.equals(that.initials) : that.initials != null) return false;
        if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
        if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (personRole != null ? !personRole.equals(that.personRole) : that.personRole != null) return false;
        if (account != null ? !account.equals(that.account) : that.account != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = personId;
        result = 31 * result + (initials != null ? initials.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (personRole != null ? personRole.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "personByMusician")
    public Collection<DutyDispositionEntity> getDutyDispositionsByPersonId() {
        return dutyDispositionsByPersonId;
    }

    public void setDutyDispositionsByPersonId(Collection<DutyDispositionEntity> dutyDispositionsByPersonId) {
        this.dutyDispositionsByPersonId = dutyDispositionsByPersonId;
    }

    @OneToMany(mappedBy = "personByMusician")
    public Collection<InstrumentEntity> getInstrumentsByPersonId() {
        return instrumentsByPersonId;
    }

    public void setInstrumentsByPersonId(Collection<InstrumentEntity> instrumentsByPersonId) {
        this.instrumentsByPersonId = instrumentsByPersonId;
    }

    @OneToMany(mappedBy = "personByMusician")
    public Collection<MusicianPartEntity> getMusicianPartsByPersonId() {
        return musicianPartsByPersonId;
    }

    public void setMusicianPartsByPersonId(Collection<MusicianPartEntity> musicianPartsByPersonId) {
        this.musicianPartsByPersonId = musicianPartsByPersonId;
    }

    @OneToOne
    @JoinColumn(name = "account", referencedColumnName = "accountID", insertable = false, updatable = false)
    public AccountEntity getAccountByAccount() {
        return accountByAccount;
    }

    public void setAccountByAccount(AccountEntity accountByAccount) {
        this.accountByAccount = accountByAccount;
    }

    @OneToMany(mappedBy = "personByPerson")
    public Collection<PersonOrchestraRoleEntity> getPersonOrchestraRolesByPersonId() {
        return personOrchestraRolesByPersonId;
    }

    public void setPersonOrchestraRolesByPersonId(Collection<PersonOrchestraRoleEntity> personOrchestraRolesByPersonId) {
        this.personOrchestraRolesByPersonId = personOrchestraRolesByPersonId;
    }

    @OneToMany(mappedBy = "personByMusician")
    public Collection<RequestEntity> getRequestsByPersonId() {
        return requestsByPersonId;
    }

    public void setRequestsByPersonId(Collection<RequestEntity> requestsByPersonId) {
        this.requestsByPersonId = requestsByPersonId;
    }
}
