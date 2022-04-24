package ch.openweb.boatservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BoatModel {
  private Long id;
  @NotBlank(message = "field name must not be null")
  private String name;
  @NotBlank(message = "field description must not be null")
  private String description;
}
