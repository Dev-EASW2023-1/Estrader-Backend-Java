package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.ItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {

    // 사용자 이름으로 사용자를 검색하는 메소드

    // 모든 사용자를 조회하는 메소드
    @Override
    List<ItemEntity> findAll();

}