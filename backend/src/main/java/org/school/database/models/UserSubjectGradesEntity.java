package org.school.database.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Entity
@Table(name = "user_subject_grades", schema = "public", catalog = "school")
@Cacheable(false)
public class UserSubjectGradesEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @JsonIgnore
    @Basic
    @Column(name = "user_subject_id", nullable = false)
    @Setter
    private long userSubjectId;

    @Basic
    @Column(name = "grade", nullable = false, precision = 0)
    @Setter
    private double grade;

    @Basic
    @Column(name = "is_final", nullable = false)
    @Setter
    private boolean isFinal;

    @Basic
    @Column(name = "date", nullable = false)
    @Setter
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_subject_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    private UserLearningSubjectEntity userSubject;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSubjectGradesEntity that = (UserSubjectGradesEntity) o;
        return userSubjectId == that.userSubjectId && Double.compare(grade, that.grade) == 0 && isFinal == that.isFinal && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSubjectId, grade, isFinal, id);
    }
}
