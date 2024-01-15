package org.school.database.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Data
@Entity
@Table(name = "user_teaching_subject", schema = "public", catalog = "school")
@Cacheable(false)
public class UserTeachingSubjectEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Basic
    @Column(name = "subject_id", nullable = false)
    private long subjectId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    @JsonBackReference
    private SubjectEntity subject;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTeachingSubjectEntity that = (UserTeachingSubjectEntity) o;
        return userId == that.userId && subjectId == that.subjectId && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, subjectId, id);
    }
}
