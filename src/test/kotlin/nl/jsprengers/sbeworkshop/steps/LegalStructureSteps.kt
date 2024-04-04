package nl.jsprengers.sbeworkshop.steps

import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.CucumberContextConfiguration
import nl.jsprengers.api.companyportal.model.Company
import nl.jsprengers.api.crm.model.Relation
import nl.jsprengers.sbeworkshop.CompanyPortalMockClient
import nl.jsprengers.sbeworkshop.CrmMockClient
import nl.jsprengers.sbeworkshop.model.RelationDetails
import nl.jsprengers.sbeworkshop.model.RestError
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class LegalStructureSteps {

    data class IdAndName(val id: String, val name: String)

    @Autowired
    lateinit var env: ServerProperties

    private val template: TestRestTemplate = TestRestTemplate()

    @Autowired
    lateinit var portalMockBackend: CompanyPortalMockClient

    @Autowired
    lateinit var crmMockBackend: CrmMockClient

    private var isAuthenticated: Boolean = false
    private var cin: String? = null

    @Before
    fun setup() {
        crmMockBackend.init()
        portalMockBackend.init()
    }


    @When("^an? (.*?) user applies with CIN (.*?)\$")
    fun userEnterSystem(userType: String, cin: Int) {
        this.isAuthenticated = userType == "authenticated"
        this.cin = cin.toString()
    }


    @Given("^CP has an organisation \"(.*?)\" with CIN (.*?) and foundation date (.*?)$")
    fun portalHasOrg(name: String, cin: String, foundationDate: String) {
        portalMockBackend.register(Company(cin, name).foundationDate(LocalDate.parse(foundationDate)))
    }

    @And("^CRM has a company \"(.*?)\" with relation id (.*?), CIN (.*?) and status (.*?)$")
    fun crmHasCompanies(name: String, relationId: String, cin: String, status: String) {
        crmMockBackend.register(Relation(relationId, cin, name).relationId(relationId).status(Relation.StatusEnum.fromValue(status)))
    }

    @Then("^the response is a relation with id (.*?), name (.*?), CIN (.*?), foundation year (.*?) and status (.*?)$")
    fun theResponseIs(id: String, name: String, cin: String, foundationYear: Int, status: String) {
        val relationExpected =
            RelationDetails(relationId = id, name = name, cin = cin, foundationYear = foundationYear, status = Relation.StatusEnum.fromValue(status))
        assertThat(postRequest()).isEqualTo(relationExpected)
    }

    @Then("^the response is a relation with a valid id, name (.*?), CIN (.*?) and foundation year (.*?)$")
    fun theResponseIs(name: String, cin: String, foundationYear: Int) {
        val reply = postRequest()
        assertThat(reply.relationId).isNotBlank()
        assertThat(reply.cin).isEqualTo(cin)
        assertThat(reply.name).isEqualTo(name)
        assertThat(reply.foundationYear).isEqualTo(foundationYear)
        assertThat(reply.status).isNull()
    }

    private fun postRequest(): RelationDetails {
        val reply = template.exchange(createRequest(this.cin ?: throw IllegalStateException("CIN not set")), RelationDetails::class.java)
        assertThat(reply.statusCode.is2xxSuccessful).describedAs("status code").isTrue()
        return reply.body!!
    }

    @Then("^the request is rejected with reason (.*?)$")
    fun theRequestIsRejectedWithReason(reason: String) {
        val reply = template.exchange(createRequest(this.cin ?: throw IllegalStateException("CIN not set")), RestError::class.java)
        assertThat(reply.statusCode.is2xxSuccessful).describedAs("status code").isFalse()
        assertThat(reply.body?.code).isEqualTo(reason)
    }

    private fun createRequest(cin: String): RequestEntity<String> {
        val path = if (this.isAuthenticated) "authenticated" else "anonymous"
        val url = "http://localhost:${env.port}/relationdetails/$path"
        return RequestEntity
            .post(url)
            .accept(MediaType.APPLICATION_JSON)
            .body(cin)
    }

}
