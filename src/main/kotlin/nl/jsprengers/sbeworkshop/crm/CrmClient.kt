package nl.jsprengers.sbeworkshop.crm

import nl.jsprengers.api.crm.RelationApi
import nl.jsprengers.api.crm.model.Relation
import nl.jsprengers.api.crm.model.Relation.StatusEnum


interface CrmClient : RelationApi {
    fun findCompanyByCIN(cin: String): Relation?

    fun createCompany(relationId: String, cin: String, name: String, status: StatusEnum?): String
}
