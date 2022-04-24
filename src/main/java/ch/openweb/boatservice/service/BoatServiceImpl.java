package ch.openweb.boatservice.service;

import ch.openweb.boatservice.mapper.BoatEntityToModelMapper;
import ch.openweb.boatservice.model.BoatModel;
import ch.openweb.boatservice.repository.BoatRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BoatServiceImpl implements BoatService {

  private BoatRepository boatRepository;

  private R2dbcEntityTemplate r2dbcEntityTemplate;
  private BoatEntityToModelMapper boatEntityToModelMapper;

  public Mono<Page<BoatModel>> getBoats(PageRequest pageRequest) {

    return boatRepository.findAllBy(pageRequest)
      .map(boatEntityToModelMapper::convert)
      .collectList()
      .zipWith(boatRepository.count())
      .map(boatsEntitiesWithTotalCount ->
        new PageImpl<>(boatsEntitiesWithTotalCount.getT1(), pageRequest, boatsEntitiesWithTotalCount.getT2()));
  }

  @Override
  public Mono<BoatModel> save(BoatModel boatModel) {

    return Mono.justOrEmpty(boatModel)
      .mapNotNull(boatEntityToModelMapper::convert)
      .flatMap(boatEntity -> boatRepository.save(boatEntity))
      .mapNotNull(boatEntityToModelMapper::convert);

  }

  @Override
  @Transactional
  public Mono<BoatModel> update(BoatModel boatModel) {

    return boatRepository.existsById(boatModel.getId())
      .filter(isBoatExists -> isBoatExists) // filter only if exists
      .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("Failed to update boat because " + boatModel.getId() + " not exists")))
      .mapNotNull((el) -> boatEntityToModelMapper.convert(boatModel))
      .flatMap(boatEntity -> r2dbcEntityTemplate.update(boatEntity))
      .mapNotNull(boatEntityToModelMapper::convert);

  }

  @Override
  public Mono<Void> delete(Long id) {

    return boatRepository.existsById(id)
      .filter(isBoatExists -> isBoatExists) // filter only if exists
      .switchIfEmpty(Mono.error(new IllegalArgumentException("Failed to update boat because " + id + " not exists")))
      .flatMap(boatEntity -> boatRepository.deleteById(id));

  }
}
