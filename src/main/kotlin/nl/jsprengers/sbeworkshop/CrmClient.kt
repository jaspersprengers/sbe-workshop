package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.crm.model.Company
import org.springframework.stereotype.Service

@Service
class CrmClient {

    fun findCompanyByKvk(parentKvk: String): Company? {
        return Company("id", "kvk", "name")
    }

    fun createCompany(kvk: String, name: String): String {
        return "000$kvk"
    }

}
