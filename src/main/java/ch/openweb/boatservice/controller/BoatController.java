package ch.openweb.boatservice.controller;

import ch.openweb.boatservice.exception.BoatNotFoundException;
import ch.openweb.boatservice.model.BoatModel;
import ch.openweb.boatservice.service.BoatService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/v1/boats")
@Validated
public class BoatController {

  private BoatService boatService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<BoatModel>> getBoats(@RequestParam(value = "page", required = false, defaultValue = "0") @PositiveOrZero(message = "page must be postive or zero") int page,
                                        @RequestParam(value = "size", required = false, defaultValue = "10") @Positive(message = "size must be positive") int size) {
    return boatService.getBoats(PageRequest.of(page, size));
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<BoatModel> create(@Valid @RequestBody BoatModel boatModel) {
    return boatService.save(boatModel);
  }

  @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<BoatModel> update(@PathVariable("id") @PositiveOrZero(message = "Id must be positive") Long id, @Valid @RequestBody BoatModel boatModel) {
    boatModel.setId(id);
    return boatService.update(boatModel);
  }

  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> delete(@PathVariable("id") @PositiveOrZero(message = "Id must be positive") Long id) {
    return boatService.delete(id);
  }

  @ExceptionHandler(BoatNotFoundException.class)
  ResponseEntity<String> boatNotFoundException(BoatNotFoundException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
    return new ResponseEntity<>("ConstraintViolationException: " + e.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
