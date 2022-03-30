package com.oldaim.fkbackend.Service;

import com.oldaim.fkbackend.DTO.UserDTO;
import com.oldaim.fkbackend.Entity.EnumType.Auth;
import com.oldaim.fkbackend.Entity.User;
import com.oldaim.fkbackend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User dtoToEntity(UserDTO userDTO){
        return User.builder()
                .userId(userDTO.getUserId())
                .userPassword(passwordEncoder.encode(userDTO.getUserPw()))
                .userEmail(userDTO.getEmail())
                .auth(Auth.valueOf(userDTO.getAuth()))
                .build();
    }

    public Optional<User> findByUserId(String userId) {

       return userRepository.findByUserId(userId);

    }

    public String registerUser(UserDTO userDTO) {

        return userRepository.save(dtoToEntity(userDTO)).getUserId();

    }
}
