package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.companyportal.model.Company
import nl.jsprengers.api.crm.model.Relation
import nl.jsprengers.sbeworkshop.model.RelationDetails
import nl.jsprengers.sbeworkshop.util.LevenShteinDistance


object RelationFactory {

    fun merge(company: Company, relation: Relation): RelationDetails {
        if (relation.cin != company.cin) {
            throw IllegalStateException("CIN does not match: ${relation.cin} != ${company.cin}")
        }
        if (relation.name != company.name) {
            val distance = LevenShteinDistance.invoke(relation.name, company.name)
            if (distance > 3)
                throw IllegalStateException("Names do not match: CRM: ${relation.name} / company portal: ${company.name}")
        }
        return RelationDetails(
            relationId = relation.relationId,
            cin = relation.cin,
            name = relation.name,
            status = relation.status,
            foundationYear = company.foundationDate.year
        )
    }

}
