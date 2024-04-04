package nl.jsprengers.sbeworkshop.crm

import nl.jsprengers.api.crm.model.Relation
import nl.jsprengers.api.crm.model.Relation.StatusEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CrmService(@Autowired val crmClient: CrmClient) {

    fun findCompany(cin: String): Relation? {
        return crmClient.findCompanyByCIN(cin)
    }

    fun createRelation(relationId: String, cin: String, name: String, status: StatusEnum?): Relation {
        val id = crmClient.createCompany(relationId, cin, name, status)
        return Relation(id, cin, name)
    }

}
