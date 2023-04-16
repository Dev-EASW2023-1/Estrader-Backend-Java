package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.ItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    @Override
    List<ItemEntity> findAll();

}