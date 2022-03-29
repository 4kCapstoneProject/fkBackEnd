package com.oldaim.fkbackend.Service;

import com.oldaim.fkbackend.Entity.User;
import com.oldaim.fkbackend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User userFind(Long userId) {
        User user = new User();

        Optional<User> userCheck = userRepository.findById(userId);

        if(userCheck.isPresent()){
            user = userCheck.get();
        }

        return user;
    }
}
