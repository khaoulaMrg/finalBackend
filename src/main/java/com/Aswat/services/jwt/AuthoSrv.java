package com.Aswat.services.jwt;

import com.Aswat.Dtos.SignupRequest;
import com.Aswat.Dtos.UserDto;
import com.Aswat.entity.User;

import java.util.List;

public interface AuthoSrv {
    UserDto createUser(SignupRequest signupRequest);

    List<User> getAllUsers();

    boolean deleteUser(Long id);
}
