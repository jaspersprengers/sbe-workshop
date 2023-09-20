package nl.jsprengers.sbeworkshop

import nl.jsprengers.sbeworkshop.model.LegalStructure
import nl.jsprengers.sbeworkshop.model.crm.CompanyDto
import nl.jsprengers.sbeworkshop.model.kvk.OrganisationDto
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class LegalStructureAnonymousService(
    @Autowired val kvkService: KvkService,
    val crmService: CrmService
) {

    val log: Logger = org.slf4j.LoggerFactory.getLogger(LegalStructureAnonymousService::class.java)

    fun createLegalStructure(kvk: String): LegalStructure {
        val org = kvkService.findOrganisation(kvk)
            ?: throw IllegalStateException("No match for kvk number in kvk portal: $kvk")
        return LegalStructure(MapCrmCompanyToLegalStructure(mapChamberOfCommerceToCrm(org, null)))
    }


    private fun mapChamberOfCommerceToCrm(org: OrganisationDto, parentId: String?): CompanyDto {
        if (crmService.findCompany(org.kvkNumber) != null)
            throw IllegalStateException("CRM already has a company with kvk: ${org.kvkNumber}")
        log.info("Creating company for kvk ${org.kvkNumber}")
        val crmCompany = crmService.createCompany(org.kvkNumber, org.name, parentId)
        return CompanyDto(
            relationId = crmCompany.relationId,
            kvkNumber = org.kvkNumber,
            name = org.name,
            daughters = org.subsidiaries.map { subsidiary -> mapChamberOfCommerceToCrm(subsidiary, crmCompany.relationId) }
        )
    }
}
