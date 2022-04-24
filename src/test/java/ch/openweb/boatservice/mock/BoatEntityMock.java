package ch.openweb.boatservice.mock;

import ch.openweb.boatservice.entity.BoatEntity;

import java.util.List;

public class BoatEntityMock {

  public static BoatEntity createA() {
    return BoatEntity.builder().id(1L).name("Boat A").description("Desc A").build();
  }

  public static BoatEntity createB() {
    return BoatEntity.builder().id(1L).name("Boat A").description("Desc A").build();
  }

  public static List<BoatEntity> list() {
    return List.of(createA(), createB());
  }
}
