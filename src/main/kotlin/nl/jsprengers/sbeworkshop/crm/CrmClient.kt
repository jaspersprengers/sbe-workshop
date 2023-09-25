package nl.jsprengers.sbeworkshop.crm

import nl.jsprengers.api.crm.model.Company


interface CrmClient {
    fun findCompanyByKvk(parentKvk: String): Company?

    fun createCompany(kvk: String, name: String): String
}
