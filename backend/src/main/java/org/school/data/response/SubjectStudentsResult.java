package org.school.data.response;

import lombok.Getter;
import org.school.database.models.UserLearningSubjectEntity;
import org.school.database.models.UserSubjectGradesEntity;

import java.util.List;

@Getter
public class SubjectStudentsResult {

    private long userLearningSubjectId;
    private String userName;
    private List<UserSubjectGradesEntity> grades;

    public SubjectStudentsResult(UserLearningSubjectEntity data) {
        this.userLearningSubjectId = data.getId();
        this.userName = data.getUser().getName();
        this.grades = data.getUserSubjectGrades();
    }
}
