package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.crm.model.Relation
import nl.jsprengers.api.crm.model.Relation.StatusEnum
import nl.jsprengers.sbeworkshop.crm.CrmClient
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class CrmMockClient : CrmClient {

    private val cache = mutableMapOf<String, Relation>()

    fun register(company: Relation) {
        cache[company.cin] = company
    }

    fun init() {
        cache.clear()
    }

    override fun findCompanyByCIN(cin: String): Relation? {
        return cache[cin]
    }

    override fun createCompany(relationId: String, cin: String, name: String, status: StatusEnum?): String {
        cache[cin] = Relation(relationId, cin, name).status(status)
        return relationId
    }

}
