package org.school.database.dao;

import org.school.PasswordHasher;
import org.school.data.response.UserSearchResult;
import org.school.database.Database;
import org.school.database.models.UserEntity;
import org.school.data.request.UserRegisterData;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final Database database = new Database();

    public UserEntity getById(long id) {
        return database.executeQueryTransaction(entityManager ->
                entityManager.createQuery("SELECT u FROM UserEntity u LEFT JOIN FETCH u.teachingSubjects ts LEFT JOIN FETCH ts.subject LEFT JOIN FETCH u.learningSubjects ls LEFT JOIN FETCH ls.subject WHERE u.id = :userId", UserEntity.class)
                        .setParameter("userId", id).getSingleResult(),
                UserEntity.class);
    }

    public UserEntity getByEmail(String email) {
        return database.executeQueryTransaction(entityManager ->
                        entityManager.createQuery("select user from UserEntity user where user.email = :email", UserEntity.class)
                                .setParameter("email", email).getSingleResult(),
                UserEntity.class);
    }

    public List<UserSearchResult> search(String keyword) {
        List<UserEntity> users = database.executeQueryTransaction(entityManager ->
                        entityManager.createQuery("select user from UserEntity user where CONCAT(user.firstName, ' ', user.lastName) LIKE :keyword", UserEntity.class)
                                .setParameter("keyword", '%' + keyword + '%').getResultList()
                , List.class);
        return users.parallelStream().map(u -> new UserSearchResult(u)).collect(Collectors.toList());
    }

    public void create(UserRegisterData data) throws Exception {

        UserEntity existingUser = this.getByEmail(data.email());

        if (existingUser != null)
            throw new Exception("Email address is already in use.");

        UserEntity user = new UserEntity();

        user.setEmail(data.email());
        user.setFirstName(data.firstName());
        user.setLastName(data.lastName());
        user.setSuperUser(false);
        user.setPassword(PasswordHasher.hashPassword(data.password()));

        database.executeQueryTransaction(em -> {
            em.persist(user);
        });
    }
}
