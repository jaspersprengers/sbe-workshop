package nl.jsprengers.sbeworkshop.crm

import nl.jsprengers.api.crm.model.Company
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("!test")
class CrmHttpClient : CrmClient {

    override fun findCompanyByKvk(parentKvk: String): Company? {
        return Company("id", "kvk", "name")
    }

    override fun createCompany(kvk: String, name: String): String {
        return "000$kvk"
    }

}
