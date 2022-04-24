package ch.openweb.boatservice.repository;

import ch.openweb.boatservice.entity.BoatEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BoatRepository extends R2dbcRepository<BoatEntity, Long> {
  Flux<BoatEntity> findAllBy(Pageable pageable);
}
