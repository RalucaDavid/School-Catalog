package org.school.data.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.school.database.models.SubjectEntity;

import java.util.List;
import java.util.stream.Collectors;

record Teacher(@JsonSerialize long id, @JsonSerialize String fullName) { }

@Getter
public class SubjectListResult {

    private final long id;
    private final String name;
    private final String description;
    private final List<Teacher> teachers;

    public SubjectListResult(SubjectEntity subject) {
        id = subject.getId();
        name = subject.getName();
        description = subject.getDescription();
        teachers = subject.getTeachers().stream().map(t -> new Teacher(t.getId(), t.getName())).collect(Collectors.toList());
    }
}
