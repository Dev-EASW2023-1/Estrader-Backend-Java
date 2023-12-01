package kr.easw.estrader.app.domain.repository;

import kr.easw.estrader.app.domain.model.entity.RealtorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RealtorRepository extends CrudRepository<RealtorEntity, Long> {

    @Override
    List<RealtorEntity> findAll();

    Optional<RealtorEntity> findByRealtorId(String realtorId);
}
