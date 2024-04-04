package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.companyportal.model.Company
import nl.jsprengers.sbeworkshop.companyportal.CompanyPortalClient
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class CompanyPortalMockClient : CompanyPortalClient {

    private val cache = mutableMapOf<String, Company>()

    fun register(org: Company) {
        cache[org.cin] = org
    }

    fun init() {
        cache.clear()
    }

    override fun findOrganisation(cin: String): Company? = cache[cin]

}
