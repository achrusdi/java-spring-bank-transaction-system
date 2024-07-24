package com.ilu.spring_starter.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ilu.spring_starter.entities.Role;
import com.ilu.spring_starter.entities.User;
import com.ilu.spring_starter.repositories.RoleRepository;
import com.ilu.spring_starter.repositories.UserRepository;
import com.ilu.spring_starter.services.UserService;

@Service
// @RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User create(User user, String[] roles) {
        if (roles == null || roles.length == 0) {
            Role role = roleRepository.findByName("general");

            if (role == null) {
                return null;
            }

            user.setRoles(List.of(role));
        } else {
            user.setRoles(roleRepository.findByNameIn(List.of(roles)));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public boolean deleteById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User update(User user, String[] roles) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser == null) {
            return null;
        }

        if (user.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        
        if (roles != null) {
            existingUser.setRoles(roleRepository.findByNameIn(List.of(roles)));
        }
        return userRepository.saveAndFlush(existingUser);
    }
    
}