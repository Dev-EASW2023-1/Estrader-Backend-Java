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

    Optional<ContractEntity> findAllByItem_CaseNumberAndRealtor_UserIdAndUser_UserId(
            String caseNumber,
            String realtorId,
            String userId
    );

    @Query("select distinct u from ContractEntity u left join fetch u.item")
    List<ContractEntity> findAllWithItem();

    @Query("select distinct u from ContractEntity u left join fetch u.user")
    List<ContractEntity> findAllWithUser();

    @Query("select distinct u from ContractEntity u left join fetch u.realtor")
    List<ContractEntity> findAllWithRealtor();

    @Query("select distinct m from ContractEntity m " +
            "left join fetch m.item i " +
            "left join fetch m.realtor r " +
            "left join fetch m.user u " +
            "where u.userId = :userId and r.userId = :realtorId and i.caseNumber = :caseNumber")
    List<ContractEntity> findAllByUserIdAndRealtorIdAndCaseNumber(@Param("userId") String userId, @Param("realtorId") String realtorId, @Param("caseNumber") String caseNumber);

    @Query("select distinct m from ContractEntity m " +
            "left join fetch m.item i " +
            "left join fetch m.realtor r " +
            "left join fetch m.user u " +
            "where u.userId = :userId and r.userId = :realtorId and i.photo = :photo")
    Optional<ContractEntity> findItemByThreeParams(@Param("userId") String userId, @Param("realtorId") String realtorId, @Param("photo") String photo);
}