package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    @Override
    List<UserEntity> findAll();
}
