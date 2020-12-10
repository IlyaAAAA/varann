package spbstu.project.varann;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import spbstu.project.varann.domain.Variation;
import spbstu.project.varann.domain.VariationID;
import spbstu.project.varann.service.VariationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VarannServiceTest {

  @Autowired
  private VariationService variationService;

  @Before
  public void store() {
    try {
      variationService.store(Files.newInputStream(Paths.get("src/main/resources/vcf/test.vcf")));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void annotate() {
    try {
      variationService.store(Files.newInputStream(Paths.get("src/main/resources/vcf/test.vcf")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    VariationID variationID = new VariationID("20", 14370, "G", "[A]");

    Variation variation = variationService.annotate(variationID);

    assertThat(variation.getChrom()).isEqualTo("20");
    assertThat(variation.getPos()).isEqualTo(14370);
    assertThat(variation.getRef()).isEqualTo("G");
    assertThat(variation.getAlt()).isEqualTo("[A]");
    assertThat(variation.getInfo()).isEqualTo("{HOMSEQ=., DP=14, NS=3, AF=0.5, DB=true}");
  }
}
