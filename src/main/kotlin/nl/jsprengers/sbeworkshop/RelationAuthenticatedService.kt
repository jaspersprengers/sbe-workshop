package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.companyportal.model.Company
import nl.jsprengers.api.crm.model.Relation
import nl.jsprengers.sbeworkshop.companyportal.CompanyPortalService
import nl.jsprengers.sbeworkshop.crm.CrmService
import nl.jsprengers.sbeworkshop.model.RelationDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class RelationAuthenticatedService(@Autowired val companyPortal: CompanyPortalService, val crmService: CrmService) {

    val log: Logger = LoggerFactory.getLogger(RelationAuthenticatedService::class.java)

    fun findRelation(cin: String): RelationDetails {
        val organisation = companyPortal.findCompany(cin)
            ?: throw IllegalStateException("No match for CIN in company portal: $cin")
        return RelationFactory.merge(organisation, mapCompanyIdentificationNumberToCompany(organisation))
    }


    fun mapCompanyIdentificationNumberToCompany(organisationDto: Company): Relation {
        val companyDto = crmService.findCompany(organisationDto.cin)
            ?: throw IllegalStateException("No match for CIN in CRM: ${organisationDto.cin}")
        log.info("Validating org with CIN ${organisationDto.cin}")
        return companyDto
    }

}
