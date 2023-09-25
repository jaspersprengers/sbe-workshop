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
    lateinit var kvkMockBackend: KvkMockClient

    @Autowired
    lateinit var crmMockBackend: CrmMockClient

    private var userType: String = "user"
    private var kvk = "EMPTY"

    @Before
    fun setup() {
        crmMockBackend.init()
        kvkMockBackend.init()
    }


    @When("^a request for kvk (.*?) is made$")
    fun kvkIsPresented(kvk: Int) {
        this.kvk = kvk.toString()
    }

    @And("^kvk has organisation (.*?) \\(kvk (.*?)\\) with no subsidiaries$")
    fun kvkHasOrg(name: String, kvk: String) {
        kvkMockBackend.register(Organisation(kvk, name))
    }

    @Given("^kvk has organisation (.*?) \\(kvk (.*?)\\) with subsidiaries (.*?)$")
    fun kvkHasOrganisationFrietVanPietKvkWithSubsidiaryKnaksKvk(name: String, kvk: String, subsidiaries: String) {
        val childOrganisations = parseDaughters(subsidiaries).map { Organisation(it.id, it.name) }
        kvkMockBackend.register(Organisation(kvk, name), childOrganisations)
    }

    fun parseDaughters(raw: String): List<IdAndName> {
        val pattern = Regex("^(.*) \\(\\w{2,3} (\\d+)\\)$")
        return raw.split("and")
            .map { it.trim() }
            .map { pattern.matchEntire(it)!!.groupValues }
            .map { IdAndName(id = it[2], name = it[1]) }
    }

    @And("^crm has company (.*?) \\(kvk (.*?)\\) with no daughters$")
    fun crmHasCompany(name: String, kvk: String) {
        crmMockBackend.register(Company("000$kvk", kvk, name))
    }

    @And("^crm has company (.*?) \\(kvk (.*?)\\) with daughters (.*?)$")
    fun crmHasCompanyKvkWithChildKvk(name: String, kvk: String, daughters: String) {
        val childOrganisations = parseDaughters(daughters).map { Company("000${it.id}", it.id, it.name) }
        crmMockBackend.register(Company("000$kvk", kvk, name), childOrganisations)
    }

    @Then("^the response contains company (.*?) \\(id (.*?)\\) with daughters (.*?)$")
    fun theLegalStructureContainsCompanyFrietVanPietIdWithDaughters(name: String, id: String, daughters: String) {
        val parent = Relation(relationId = id, name = name)
        val childOrganisations = parseDaughters(daughters)
            .map { Relation(relationId = it.id, name = it.name, parentRelationId = parent.relationId) }
        assertCorrectReply(LegalStructure(listOf(parent) + childOrganisations))
    }

    @Then("^the response contains company (.*?) \\(id (.*?)\\) with no daughters$")
    fun theLegalStructureContainsCompany(name: String, id: String) {
        assertCorrectReply(LegalStructure(listOf(Relation(relationId = id, name = name))))
    }

    private fun assertCorrectReply(legalStructureExpected: LegalStructure) {
        val reply = template.exchange(createRequest(this.kvk), LegalStructure::class.java)
        assertThat(reply.statusCode.is2xxSuccessful).describedAs("status code").isTrue()
        assertThat(reply.body).isEqualTo(legalStructureExpected)
    }

    @Then("^the request is rejected with reason (.*?)$")
    fun theRequestIsRejectedWithReason(reason: String) {
        val reply = template.exchange(createRequest(this.kvk), RestError::class.java)
        assertThat(reply.statusCode.is2xxSuccessful).describedAs("status code").isFalse()
        assertThat(reply.body?.code).isEqualTo(reason)
    }

    private fun createRequest(kvk: String): RequestEntity<String> {
        val url = "http://localhost:${env.port}/legalstructure"
        return RequestEntity
            .post(url)
            .headers { it.add("user_type", this.userType) }
            .accept(MediaType.APPLICATION_JSON)
            .body(kvk)
    }

    @When("^an (.*?) user enter the system$")
    fun aUserEnterTheSystem(type: String) {
        this.userType = if (type == "anonymous") "guest" else "user"
    }
}
