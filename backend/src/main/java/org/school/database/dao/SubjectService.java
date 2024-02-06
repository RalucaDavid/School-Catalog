package org.school.database.dao;

import org.school.data.request.SubjectAddGradeData;
import org.school.data.request.TeacherAddRemoveData;
import org.school.data.response.SubjectListResult;
import org.school.data.response.SubjectNameAndId;
import org.school.data.response.SubjectStudentsResult;
import org.school.data.response.UserSearchResult;
import org.school.database.Database;
import org.school.database.models.*;
import org.school.data.request.SubjectCreationData;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

         return subjects.stream().map(SubjectListResult::new).collect(Collectors.toList());
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

    public UserSearchResult addTeacher(TeacherAddRemoveData data) throws Exception
    {
        SubjectEntity subject = this.getById(data.subjectId());

        if (subject == null)
            throw new Exception("Invalid subject.");

        UserEntity user = this.userService.getById(data.userId());

        if (user == null)
            throw new Exception("Invalid user.");

        if (user.getTeachingSubjects().stream().anyMatch(l -> l.getSubjectId() == data.subjectId()))
            throw new Exception(user.getName() + " is already teaching " + subject.getName());

        if (user.getLearningSubjects().stream().anyMatch(l -> l.getSubjectId() == data.subjectId()))
            throw new Exception(user.getName() + " is a student of " + subject.getName());

        UserTeachingSubjectEntity teachingEntity = new UserTeachingSubjectEntity();
        teachingEntity.setSubjectId(data.subjectId());
        teachingEntity.setUserId(data.userId());

        database.executeQueryTransaction(em -> {
            em.persist(teachingEntity);
        });

        return new UserSearchResult(user);
    }

    public void removeTeacher(TeacherAddRemoveData data) throws Exception
    {
        SubjectEntity subject = this.getById(data.subjectId());

        if (subject == null)
            throw new Exception("Invalid subject.");

        UserEntity user = this.userService.getById(data.userId());

        if (user == null)
            throw new Exception("Invalid user.");

        Optional<UserTeachingSubjectEntity> teaching = user.getTeachingSubjects().stream().filter(t -> t.getSubjectId() == data.subjectId()).findFirst();

        if (teaching.isEmpty())
            throw new Exception(user.getName() + " is not teaching " + subject.getName());

        // em.remove will give us an error because UserTeachingSubjectEntity is not persisted (i think).
        // deleting using a query works
        database.executeQueryTransaction(em -> {
            em.createQuery("delete from UserTeachingSubjectEntity where id = :id")
                    .setParameter("id", teaching.get().getId())
                    .executeUpdate();
        });
    }

    public List<SubjectNameAndId> getTeachingSubjects(long userId)
    {
        List <SubjectEntity> subjects = database.executeQueryTransaction(entityManager ->
                entityManager.createQuery("SELECT ts.subject FROM UserTeachingSubjectEntity ts where ts.userId = :userId", SubjectEntity.class)
                        .setParameter("userId", userId)
                        .getResultList(), List.class);

        return subjects.stream().map(SubjectNameAndId::new).toList();
    }

    public List<Object> getLearningSubjects(long userId)
    {
        return database.executeQueryTransaction(entityManager ->
                entityManager.createNativeQuery("select description, grade, date, is_final from user_subject_grades LEFT JOIN user_learning_subject on user_subject_grades.user_subject_id = user_learning_subject.id LEFT JOIN subjects on user_learning_subject.subject_id = subjects.id WHERE user_id = #userId")
                        .setParameter("userId", userId)
                        .getResultList(), List.class);
    }

    public List<SubjectStudentsResult> getSubjectStudents(long subjectId)
    {
        List<UserLearningSubjectEntity> results = database.executeQueryTransaction(entityManager ->
                entityManager.createQuery("SELECT DISTINCT ul FROM UserLearningSubjectEntity ul \n" +
                                "LEFT JOIN UserEntity u ON u.id = ul.userId \n" +
                                "LEFT JOIN FETCH ul.userSubjectGrades ug \n" +
                                "WHERE ul.subjectId = :subjectId")
                        .setParameter("subjectId", subjectId)
                        .getResultList(), List.class);

        return results.stream().map(SubjectStudentsResult::new).toList();
    }

    public UserSearchResult addStudent(TeacherAddRemoveData data) throws Exception
    {
        SubjectEntity subject = this.getById(data.subjectId());

        if (subject == null)
            throw new Exception("Invalid subject.");

        UserEntity user = this.userService.getById(data.userId());

        if (user == null)
            throw new Exception("Invalid user.");

        if (user.getTeachingSubjects().stream().anyMatch(l -> l.getSubjectId() == data.subjectId()))
            throw new Exception(user.getName() + " is teaching " + subject.getName());

        if (user.getLearningSubjects().stream().anyMatch(l -> l.getSubjectId() == data.subjectId()))
            throw new Exception(user.getName() + " is already studying " + subject.getName());

        UserLearningSubjectEntity learningEntity = new UserLearningSubjectEntity();
        learningEntity.setSubjectId(data.subjectId());
        learningEntity.setUserId(data.userId());

        database.executeQueryTransaction(em -> {
            em.persist(learningEntity);
        });

        return new UserSearchResult(user);
    }

    public boolean addGrade(UserEntity user, SubjectAddGradeData gradeData)
    {
        if (gradeData.grade() < 1 || gradeData.grade() > 10)
            throw new RuntimeException("Invalid grade (1-10)");

        UserSubjectGradesEntity grade = new UserSubjectGradesEntity();

        grade.setUserSubjectId(gradeData.userLearningSubjectId());
        grade.setGrade(gradeData.grade());
        grade.setDate(new Date());

        database.executeQueryTransaction(em -> {
            em.persist(grade);
        });

        return true;
    }

    public boolean setFinalGrade(long userLearningSubjectId)
    {

        UserSubjectGradesEntity finalGrade = database.executeQueryTransaction(entityManager ->
                entityManager.createQuery("SELECT g FROM UserSubjectGradesEntity g where g.userSubjectId = :userSubjectId AND g.isFinal = true", SubjectEntity.class)
                        .setParameter("userSubjectId", userLearningSubjectId)
                        .getSingleResult(), UserSubjectGradesEntity.class);

        if (finalGrade != null)
            throw new RuntimeException("Final grade already computed for this user.");

        List<UserSubjectGradesEntity> grades = database.executeQueryTransaction(entityManager ->
            entityManager.createQuery("SELECT g FROM UserSubjectGradesEntity g where g.userSubjectId = :userSubjectId AND g.isFinal = false", SubjectEntity.class)
                    .setParameter("userSubjectId", userLearningSubjectId)
                    .getResultList(), List.class);

        if (grades == null)
            throw new RuntimeException("Student has no grades. Can't compute final grade.");

        double average = grades.stream().mapToDouble(UserSubjectGradesEntity::getGrade).average().orElse(0.0);

        if (average == 0.0)
            throw new RuntimeException("Student has no grades. Can't compute final grade.");

        UserSubjectGradesEntity grade = new UserSubjectGradesEntity();

        grade.setUserSubjectId(userLearningSubjectId);
        grade.setGrade(average);
        grade.setDate(new Date());
        grade.setFinal(true);

        database.executeQueryTransaction(em -> {
            em.persist(grade);
        });

        return true;
    }
}
