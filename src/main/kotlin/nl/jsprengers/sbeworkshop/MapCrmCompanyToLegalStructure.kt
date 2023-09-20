package nl.jsprengers.sbeworkshop

import nl.jsprengers.sbeworkshop.model.Relation
import nl.jsprengers.sbeworkshop.model.crm.CompanyDto


object MapCrmCompanyToLegalStructure {

    operator fun invoke(sc: CompanyDto) = map(sc, null, mutableListOf<Relation>())

    private fun map(company: CompanyDto, parent: String?, relations: MutableList<Relation>): List<Relation> {
        relations.add(Relation(company.relationId, company.name, parent))
        company.daughters.forEach { map(it, company.relationId, relations) }
        return relations
    }
}
