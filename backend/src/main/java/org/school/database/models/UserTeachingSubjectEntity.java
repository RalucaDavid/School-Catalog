package org.school.database.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "user_teaching_subject", schema = "public", catalog = "school")
public class UserTeachingSubjectEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "userId", nullable = false)
    private long userId;

    @Basic
    @Column(name = "subjectId", nullable = false)
    private long subjectId;

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
