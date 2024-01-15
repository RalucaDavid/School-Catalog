package org.school.data.response;

import lombok.Getter;
import org.school.database.models.SubjectEntity;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SubjectListResult {

    private final long id;
    private final String name;
    private final String description;
    private final List<UserSearchResult> teachers;

    public SubjectListResult(SubjectEntity subject) {
        id = subject.getId();
        name = subject.getName();
        description = subject.getDescription();
        teachers = subject.getTeachers().stream().map(UserSearchResult::new).collect(Collectors.toList());
    }
}
