package org.school.database.dao;

import org.school.data.response.SubjectListResult;
import org.school.database.Database;
import org.school.database.models.SubjectEntity;
import org.school.data.request.SubjectCreationData;
import org.school.database.models.UserEntity;
import org.school.database.models.UserTeachingSubjectEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SubjectService {

    private final Database database = new Database();
    private final UserService userService = new UserService();

    public SubjectEntity getById(long id) {
        return database.executeQueryTransaction(entityManager ->
                        entityManager.createQuery("select subject from SubjectEntity subject where subject.id = :id", SubjectEntity.class)
                                .setParameter("id", id).getSingleResult(),
                SubjectEntity.class);
    }

    public SubjectEntity getByNameOrDescription(String name, String description) {
        return database.executeQueryTransaction(entityManager ->
                        entityManager.createQuery("select subject from SubjectEntity subject where subject.name = :name or subject.description = :description", SubjectEntity.class)
                                .setParameter("name", name).setParameter("description", description).getSingleResult(),
                SubjectEntity.class);
    }

    public List<SubjectListResult> getAll() {
         List<SubjectEntity> subjects = database.executeQueryTransaction(entityManager ->
                        entityManager.createQuery("SELECT s FROM SubjectEntity s LEFT JOIN s.teachers t", SubjectEntity.class)
                                .getResultList(), List.class);

         return subjects.parallelStream().map(SubjectListResult::new).collect(Collectors.toList());
    }

    public SubjectEntity create(SubjectCreationData data) throws Exception {

        SubjectEntity existing = this.getByNameOrDescription(data.name(), data.description());

        if (existing != null)
            throw new Exception("A subject with this name or description already exists.");

        SubjectEntity subject = new SubjectEntity();

        subject.setName(data.name());
        subject.setDescription(data.description());

        database.executeQueryTransaction(em -> {
            em.persist(subject);
        });

        return subject;
    }

    public void addTeacher(long subjectId, long userId) throws Exception
    {
        SubjectEntity subject = this.getById(subjectId);

        if (subject == null)
            throw new Exception("Invalid subject.");

        UserEntity user = this.userService.getById(userId);

        if (user == null)
            throw new Exception("Invalid user.");

        if (user.getTeachingSubjects().stream().anyMatch(l -> l.getSubjectId() == subjectId))
            throw new Exception(user.getName() + " is already teaching " + subject.getName());

        if (user.getLearningSubjects().stream().anyMatch(l -> l.getSubjectId() == subjectId))
            throw new Exception(user.getName() + " is a student of " + subject.getName());

        UserTeachingSubjectEntity teachingEntity = new UserTeachingSubjectEntity();
        teachingEntity.setSubjectId(subjectId);
        teachingEntity.setUserId(userId);

        database.executeQueryTransaction(em -> {
            em.persist(teachingEntity);
        });
    }
}
