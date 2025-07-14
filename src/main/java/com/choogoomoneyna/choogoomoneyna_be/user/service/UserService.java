package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;

public interface UserService {

    /**
     * Registers a new user in the system.
     *
     * @param dto the user registration request containing the user's email, password,
     *            nickname, profile image, and preferred type (choogooMi).
     */
    void registerUser(UserJoinRequestDTO dto);

    /**
     * Checks whether the given user login ID is already duplicated in the system.
     *
     * @param userLoginId the login ID of the user to check for duplication
     * @return true if the login ID is duplicated, false otherwise
     */
    public boolean isUserLoginIdDuplicated(String userLoginId);


    /**
     * Checks if a user exists with the given email and login type.
     *
     * @param email the email address of the user to be searched
     * @param loginType the type of login for the user (e.g., LOCAL, OAUTH2)
     * @return true if a user with the given email and login type exists, false otherwise
     */
    public boolean findByEmailAndLoginType(String email, LoginType loginType);
}
