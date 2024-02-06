package org.school.data.response;

import lombok.Getter;
import org.school.database.models.SubjectEntity;

@Getter
public class SubjectNameAndId {

    private final long id;
    private final String name;

    public SubjectNameAndId(SubjectEntity subject) {
        this.id = subject.getId();
        this.name = subject.getDescription();
    }
}
