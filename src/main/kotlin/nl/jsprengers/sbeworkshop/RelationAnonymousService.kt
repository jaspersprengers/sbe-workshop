package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.companyportal.model.Company
import nl.jsprengers.api.crm.model.Relation
import nl.jsprengers.sbeworkshop.companyportal.CompanyPortalService
import nl.jsprengers.sbeworkshop.crm.CrmService
import nl.jsprengers.sbeworkshop.model.RelationDetails
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class RelationAnonymousService(
    @Autowired val companyPortalService: CompanyPortalService,
    val crmService: CrmService
) {

    val log: Logger = org.slf4j.LoggerFactory.getLogger(RelationAnonymousService::class.java)

    fun createRelation(cin: String): RelationDetails {
        val company = companyPortalService.findCompany(cin)
            ?: throw IllegalStateException("No match for CIN in company portal: $cin")
        return RelationFactory.merge(company, mapCompanyIdToCRM(company))
    }


    private fun mapCompanyIdToCRM(company: Company): Relation {
        if (crmService.findCompany(company.cin) != null)
            throw IllegalStateException("CRM already has a company with CIN: ${company.cin}")
        log.info("Creating company for CIN ${company.cin}")
        return crmService.createRelation("000${company.cin}", company.cin, company.name, null)
    }
}
