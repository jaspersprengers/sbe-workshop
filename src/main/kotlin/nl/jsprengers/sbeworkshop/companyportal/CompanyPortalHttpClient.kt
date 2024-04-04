package nl.jsprengers.sbeworkshop.companyportal

import nl.jsprengers.api.companyportal.model.Company
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("!test")
class CompanyPortalHttpClient : CompanyPortalClient {

    override fun findOrganisation(cin: String): Company? = TODO("not implemented!")

}
