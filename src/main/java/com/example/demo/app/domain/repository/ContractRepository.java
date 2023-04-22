package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.ContractEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends CrudRepository<ContractEntity, Long> {
    Optional<ContractEntity> findAllByItem_InformationAndRepresentative_UsernameAndUser_Userid(
            String information,
            String username,
            String userid
    );

    @Query("select distinct u from ContractEntity u left join fetch u.item")
    List<ContractEntity> findAllWithItem();

    @Query("select distinct u from ContractEntity u left join fetch u.user")
    List<ContractEntity> findAllWithUser();

    @Query("select distinct u from ContractEntity u left join fetch u.representative")
    List<ContractEntity> findAllWithRepresentative();
}