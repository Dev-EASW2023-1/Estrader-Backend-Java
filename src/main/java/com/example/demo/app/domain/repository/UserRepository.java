package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    // 사용자 이름으로 사용자를 검색하는 메소드

    // 모든 사용자를 조회하는 메소드
    @Override
    List<UserEntity> findAll();

}