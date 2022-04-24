package ch.openweb.boatservice.mock;

import ch.openweb.boatservice.model.BoatModel;

import java.util.List;

public class BoatModelMock {

  public static BoatModel createA() {
    return BoatModel.builder().id(1L).name("Boat A").description("Desc A").build();
  }

  public static BoatModel createB() {
    return BoatModel.builder().id(2L).name("Boat B").description("Desc B").build();
  }

  public static List<BoatModel> list() {
    return List.of(createA(), createB());
  }
}
