package nl.jsprengers.sbeworkshop.steps

import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.CucumberContextConfiguration
import nl.jsprengers.api.crm.model.Company
import nl.jsprengers.api.kvk.model.Organisation
import nl.jsprengers.sbeworkshop.CrmMockClient
import nl.jsprengers.sbeworkshop.KvkMockClient
import nl.jsprengers.sbeworkshop.model.LegalStructure
import nl.jsprengers.sbeworkshop.model.Relation
import nl.jsprengers.sbeworkshop.model.RestError
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class LegalStructureSteps {

    data class IdAndName(val id: String, val name: String)

    @Autowired
    lateinit var env: ServerProperties

    private val template: TestRestTemplate = TestRestTemplate()

    @Autowired
    lateinit var kvkMockClient: KvkMockClient

    @Autowired
    lateinit var crmMockClient: CrmMockClient

    @Before
    fun setup() {
        crmMockClient.init()
        kvkMockClient.init()
    }


    private fun createRequest(kvk: String, isAuthenticated: Boolean): ResponseEntity<LegalStructure>? {
        val path = if (isAuthenticated) "authenticated" else "anonymous"
        val url = "http://localhost:${env.port}/legalstructure/$path"
        val request = RequestEntity
            .post(url)
            .accept(MediaType.APPLICATION_JSON)
            .body(kvk)
        return template.exchange(request, LegalStructure::class.java)
    }

}
