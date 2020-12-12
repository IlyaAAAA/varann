package spbstu.project.varann;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import spbstu.project.varann.controller.VariationController;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VarannControllerTests {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private VariationController variationController;

  @Test
  public void storeVatiationTest() throws Exception {

    String fileContent = "##fileformat=VCFv4.1\n" +
            "##reference=file:///seq/references/1000GenomesPilot-NCBI36.fasta\n" +
            "##contig=<ID=20,length=62435964,assembly=B36,md5=f126cdf8a6e0c7f379d618ff66beb2da,species=\"Homo sapiens\">\n" +
            "##INFO=<ID=NS,Number=1,Type=Integer,Description=\"Number of Samples With Data\">\n" +
            "##INFO=<ID=DP,Number=1,Type=Integer,Description=\"Total Depth\">\n" +
            "##INFO=<ID=AF,Number=A,Type=Float,Description=\"Allele Frequency\">\n" +
            "##INFO=<ID=AA,Number=1,Type=String,Description=\"Ancestral Allele\">\n" +
            "##INFO=<ID=DB,Number=0,Type=Flag,Description=\"dbSNP membership, build 129\">\n" +
            "##INFO=<ID=H2,Number=0,Type=Flag,Description=\"HapMap2 membership\">\n" +
            "##INFO=<ID=HOMSEQ,Number=.,Type=String>\n" +
            "##FILTER=<ID=q10,Description=\"Quality below 10\">\n" +
            "##FILTER=<ID=s50,Description=\"Less than 50% of samples have data\">\n" +
            "##FORMAT=<ID=GT,Number=1,Type=String,Description=\"Genotype\">\n" +
            "##FORMAT=<ID=GQ,Number=1,Type=Integer,Description=\"Genotype Quality\">\n" +
            "##FORMAT=<ID=DP,Number=1,Type=Integer,Description=\"Read Depth\">\n" +
            "##FORMAT=<ID=HQ,Number=2,Type=Integer,Description=\"Haplotype Quality\">\n" +
            "##FORMAT=<ID=CNL,Number=.,Type=Integer>\n" +
            "#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT\tNA00001\tNA00002\tNA00003\n" +
            "20\t14370\trs6054257\tG\tA\t29.1\t.\tNS=3;DP=14;AF=0.5;HOMSEQ;DB\tGT:GQ:DP:HQ:CNL\t0|0:48:1:25,30:10,20\t1|0:48:8:49,51:.\t./.:43:5:.,.:1\n" +
            "20\t17330\t.\tT\tA\t.\tq10;s50\tNS=3;DP=11;AF=0.017;H2\tGT:GQ:DP:HQ\t0|0:49:3:58,50\t0|1:3:5:65,3\t0/0:41:3:4,5\n" +
            "20\t1110696\trs6040355\tA\tG,T\t67\tPASS\tNS=2;DP=10;AF=0.333,0.667;AA=T;DB\tGT:GQ:DP:HQ\t1|2:21:6:23,27\t2|1:2:0:18,2\t2/2:35:4:10,20\n" +
            "20\t1230237\t.\tT\t.\t47\tPASS\tNS=3;DP=13;AA=T\tGT:GQ:DP:HQ\t0|0:54:7:56,60\t0|0:48:4:51,51\t./.\n" +
            "20\t1234567\tmicrosat1\tGTC\tG,GTCT\t50\tPASS\tNS=3;DP=9;AA=G\tGT:GQ:DP\t0/1:35:4\t0/2:17:2";

    MockMultipartFile file = new MockMultipartFile("file", "src/main/resources/vcf/test.vcf", "multipart/form-data", fileContent.getBytes());

    mockMvc.perform(MockMvcRequestBuilders
            .multipart("/variation/store")
            .file(file))
            .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void annotateTest() throws Exception {

    String str = "{\n" +
            "  \"alt\": \"[A]\",\n" +
            "  \"chrom\": \"20\",\n" +
            "  \"pos\": 14370,\n" +
            "  \"ref\": \"G\"\n" +
            "}";

    String res = "{\n" +
            "    \"chrom\": \"20\",\n" +
            "    \"pos\": 14370,\n" +
            "    \"ref\": \"G\",\n" +
            "    \"alt\": \"[A]\",\n" +
            "    \"info\": \"{HOMSEQ=., DP=14, NS=3, AF=0.5, DB=true}\"\n" +
            "}";

    mockMvc.perform(MockMvcRequestBuilders
            .post("/variation/annotate", str)
            .contentType(MediaType.APPLICATION_JSON)
            .content(str))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(res));
  }
}
