package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.RealtorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RealtorRepository extends CrudRepository<RealtorEntity, Long> {

    @Override
    List<RealtorEntity> findAll();

    Optional<RealtorEntity> findByUserId(String userId);
}
