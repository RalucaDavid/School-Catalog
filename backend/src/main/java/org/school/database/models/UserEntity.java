package org.school.database.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.security.Principal;
import java.util.Objects;

@Data
@Entity
@Table(name = "users", schema = "public", catalog = "school")
public class UserEntity implements Principal {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "email", nullable = false, length = -1)
    private String email;

    @Basic
    @Column(name = "password", nullable = false, length = -1)
    @JsonIgnore
    private String password;

    @Basic
    @Column(name = "first_name", nullable = false, length = -1)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = -1)
    private String lastName;

    @Basic
    @Column(name = "is_super_user", nullable = false)
    private boolean isSuperUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id && isSuperUser == that.isSuperUser && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName, isSuperUser);
    }

    @Override
    @JsonIgnore
    public String getName()
    {
        return this.firstName + " " + this.lastName;
    }
}
