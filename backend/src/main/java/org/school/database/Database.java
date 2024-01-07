package org.school.database;

import jakarta.persistence.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Database {
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private EntityManager entityManager;

    public Database() {
        this.connect();
    }

    public void connect() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("collegePersistenceUnit");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public void discconect() {
        entityManager.close();
    }

    public <T, R> R executeQueryTransaction(Function<EntityManager, T> action, Class<R> classResult){
        /* Transaction */
        R resultedQuery = null;

        EntityTransaction transaction =this.entityManager.getTransaction();
        try{
            transaction.begin();
            /* Query */
            resultedQuery = (R) action.apply(this.entityManager);

            transaction.commit();
        } catch (RuntimeException e) {
            System.err.println("Query transaction failed" + e.getLocalizedMessage());
            transaction.rollback();
        }

        return resultedQuery;
    }

    public void executeQueryTransaction(Consumer<EntityManager> action){
        EntityTransaction transaction =this.entityManager.getTransaction();
        try{
            transaction.begin();
            action.accept(this.entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            System.err.println("Query transaction failed" + e.getLocalizedMessage());
            transaction.rollback();
        }
    }
}
