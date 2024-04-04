package nl.jsprengers.sbeworkshop.companyportal

import nl.jsprengers.api.companyportal.CompanyApi
import nl.jsprengers.api.companyportal.model.Company

interface CompanyPortalClient : CompanyApi {
    fun findOrganisation(cin: String): Company?
}
