package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    @Override
    List<ItemEntity> findAll();
    Page<ItemEntity> findByLocationContaining(String district, Pageable pageable);

    Optional<ItemEntity> findByCaseNumber(String caseNumber);

    Optional<ItemEntity> findByPhoto(String photo);


}

