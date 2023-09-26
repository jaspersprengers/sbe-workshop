package nl.jsprengers.sbeworkshop.crm

import nl.jsprengers.api.crm.CompanyApi
import nl.jsprengers.api.crm.model.Company


interface CrmClient : CompanyApi {
    fun findCompanyByKvk(parentKvk: String): Company?

    fun createCompany(kvk: String, name: String): String
}
