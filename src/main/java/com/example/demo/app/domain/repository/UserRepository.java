package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.Enum.Role;
import com.example.demo.app.domain.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Override
    List<UserEntity> findAll();

    Optional<UserEntity> findByUserId(String UserId);

    List<UserEntity> findByRole(Role role);
}
