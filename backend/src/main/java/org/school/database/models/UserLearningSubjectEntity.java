package org.school.database.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "user_learning_subject", schema = "public", catalog = "school")
public class UserLearningSubjectEntity {

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
        UserLearningSubjectEntity that = (UserLearningSubjectEntity) o;
        return id == that.id && userId == that.userId && subjectId == that.subjectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, subjectId);
    }
}
