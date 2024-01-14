package org.school.data.response;

import lombok.Getter;
import org.school.database.models.UserEntity;

@Getter
public class UserSearchResult {

    private final long id;
    private final String fullName;

    public UserSearchResult(UserEntity user) {
       id = user.getId();
       fullName = user.getName();
    }
}
