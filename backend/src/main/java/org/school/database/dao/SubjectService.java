package org.school.database.dao;

import org.school.database.Database;
import org.school.database.models.SubjectEntity;
import org.school.data.SubjectCreationData;

import java.util.List;

public class SubjectService {
    private final Database database = new Database();

    public SubjectEntity getById(long id) {
        return database.executeQueryTransaction(entityManager ->
                        entityManager.createQuery("select subject from SubjectEntity subject where subject.id = :id", SubjectEntity.class)
                                .setParameter("id", id).getSingleResult(),
                SubjectEntity.class);
    }

    public List<SubjectEntity> getAll(long id) {
        return database.executeQueryTransaction(entityManager ->
                        entityManager.createQuery("select subject from SubjectEntity subject", SubjectEntity.class)
                                .getResultList()
                , List.class);
    }

    public void create(SubjectCreationData data) throws Exception {

        SubjectEntity subject = new SubjectEntity();

        subject.setName(data.name());
        subject.setDescription(data.description());

        database.executeQueryTransaction(em -> {
            em.persist(subject);
        });
    }
}
