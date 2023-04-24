package com.example.demo.app.domain.repository;

import com.example.demo.app.domain.model.entity.ContractEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Query("select distinct m from ContractEntity m " +
            "left join fetch m.item i " +
            "left join fetch m.representative r " +
            "left join fetch m.user u " +
            "where i.location = :itemId and r.username = :representativeId and u.userid = :userId")
    List<ContractEntity> findAllByLocationAndRepresentativeAndUserId(@Param("userId") String userId, @Param("representativeId") String representativeId, @Param("itemId") String itemId);

    @Query("select distinct m from ContractEntity m " +
            "left join fetch m.item i " +
            "left join fetch m.representative r " +
            "left join fetch m.user u " +
            "where i.picture = :itemId and r.username = :representativeId and u.userid = :userId")
    List<ContractEntity> findItemByThreeParams(@Param("userId") String userId, @Param("representativeId") String representativeId, @Param("itemId") String itemId);
}