package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.crm.model.Company
import nl.jsprengers.sbeworkshop.model.crm.CompanyDto
import nl.jsprengers.sbeworkshop.model.crm.CompanyHelper
import nl.jsprengers.sbeworkshop.model.kvk.OrganisationHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CrmService(@Autowired val crmClient: CrmClient) {

    fun findCompany(kvk: String): CompanyDto? {
        return crmClient.findCompanyByKvk(kvk)?.let { CompanyHelper.map(it) }
    }

    fun createCompany(kvk: String, name: String, parentId: String?): CompanyDto {
        val id = crmClient.createCompany(kvk, name)
        return CompanyDto(id, kvk, name)
    }

}
