package org.school.database.dao;

import org.school.PasswordHasher;
import org.school.database.Database;
import org.school.database.models.UserEntity;
import org.school.data.UserRegisterData;

public class UserService {

    private final Database database = new Database();

    public UserEntity getById(long id) {
        return database.executeQueryTransaction(entityManager ->
                entityManager.createQuery("select user from UserEntity user where user.id = :id", UserEntity.class)
                        .setParameter("id", id).getSingleResult(),
                UserEntity.class);
    }

    public UserEntity getByEmail(String email) {
        return database.executeQueryTransaction(entityManager ->
                        entityManager.createQuery("select user from UserEntity user where user.email = :email", UserEntity.class)
                                .setParameter("email", email).getSingleResult(),
                UserEntity.class);
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
