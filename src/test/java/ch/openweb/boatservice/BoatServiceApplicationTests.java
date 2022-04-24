package ch.openweb.boatservice;

import ch.openweb.boatservice.config.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({SecurityConfiguration.class})
class BoatServiceApplicationTests {

  @Test
  void contextLoads() {
  }

}


