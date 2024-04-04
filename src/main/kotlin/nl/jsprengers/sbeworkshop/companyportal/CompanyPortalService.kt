package nl.jsprengers.sbeworkshop.companyportal

import nl.jsprengers.api.companyportal.model.Company
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CompanyPortalService(@Autowired val companyPortalClient: CompanyPortalClient) {

    fun findCompany(cin: String): Company? {
        return companyPortalClient.findOrganisation(cin)
    }

}
