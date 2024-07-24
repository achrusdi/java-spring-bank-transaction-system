package com.ilu.spring_starter.repositories;

// import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ilu.spring_starter.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {

    Role findByName(String name);

    List<Role> findByNameIn(List<String> names);
}