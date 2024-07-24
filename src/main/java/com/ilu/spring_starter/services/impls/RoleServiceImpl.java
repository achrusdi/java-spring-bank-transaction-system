package com.ilu.spring_starter.services.impls;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ilu.spring_starter.entities.Role;
import com.ilu.spring_starter.repositories.RoleRepository;
import com.ilu.spring_starter.services.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public boolean deleteById(String id) {
        Role existingRole = roleRepository.findById(id).orElse(null);
        
        if (existingRole == null) {
            return false;
        }

        roleRepository.delete(existingRole);
        return true;
    }

    @Override
    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }

    @Override
    public Role findById(String id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role update(Role role) {
        Role existingRole = roleRepository.findById(role.getId()).orElse(null);
        
        if (existingRole == null) {
            return null;
        }

        roleRepository.save(role);

        existingRole = roleRepository.findById(role.getId()).orElse(null);

        return existingRole;
    }

}