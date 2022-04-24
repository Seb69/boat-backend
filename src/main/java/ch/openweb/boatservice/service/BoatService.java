package ch.openweb.boatservice.service;

import ch.openweb.boatservice.model.BoatModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

public interface BoatService {
  Mono<Page<BoatModel>> getBoats(PageRequest pageRequest);

  Mono<BoatModel> save(BoatModel boatModel);

  Mono<BoatModel> update(BoatModel boatModel);

  Mono<Void> delete(Long id);
}
