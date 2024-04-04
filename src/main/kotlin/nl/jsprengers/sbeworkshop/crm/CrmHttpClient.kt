package nl.jsprengers.sbeworkshop.crm

import nl.jsprengers.api.crm.model.Relation
import nl.jsprengers.api.crm.model.Relation.StatusEnum
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("!test")
class CrmHttpClient : CrmClient {

    override fun findCompanyByCIN(cin: String): Relation? = TODO("not implemented!")

    override fun createCompany(relationId: String, cin: String, name: String, status: StatusEnum?): String = TODO("not implemented!")

}
