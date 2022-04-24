package ch.openweb.boatservice.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Data
@Builder
@Table("BOAT")
public class BoatEntity {

  @Id
  @Column("ID")
  private Long id;

  @Column("NAME")
  private String name;

  @Column("DESCRIPTION")
  private String description;

  @Column("CREATED_AT")
  @CreatedDate
  private ZonedDateTime createdAt;

  @Column("UPDATED_AT")
  @LastModifiedDate
  private ZonedDateTime updatedAt;

}
