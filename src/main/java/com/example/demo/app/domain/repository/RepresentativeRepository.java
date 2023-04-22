package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.RepresentativeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepresentativeRepository extends CrudRepository<RepresentativeEntity, Long> {

    @Override
    List<RepresentativeEntity> findAll();

    Optional<RepresentativeEntity> findByUsername(String username);
}
