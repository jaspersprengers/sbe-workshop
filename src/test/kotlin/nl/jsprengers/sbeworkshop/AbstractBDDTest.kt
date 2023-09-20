package nl.jsprengers.sbeworkshop

import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@CucumberContextConfiguration
@SpringBootTest(classes = [SbeWorkshopApplication::class])
abstract class AbstractBDDTest(@Autowired val legalStructureController: LegalStructureController) {



}
