package nl.jsprengers.sbeworkshop

import nl.jsprengers.sbeworkshop.model.LegalStructure
import nl.jsprengers.sbeworkshop.model.crm.CompanyDto
import nl.jsprengers.sbeworkshop.model.kvk.OrganisationDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class LegalStructureAuthenticatedService(@Autowired val kvkService: KvkService, val crmService: CrmService) {

    val log: Logger = LoggerFactory.getLogger(LegalStructureAuthenticatedService::class.java)

    fun createLegalStructure(kvk: String): LegalStructure {
        val org = kvkService.findOrganisation(kvk)
            ?: throw IllegalStateException("No match for kvk number in kvk portal: $kvk")
        return LegalStructure(MapCrmCompanyToLegalStructure(mapChamberOfCommerceToCRM(org)))
    }


    fun mapChamberOfCommerceToCRM(organisationDto: OrganisationDto): CompanyDto {
        val companyDto = crmService.findCompany(organisationDto.kvkNumber)
            ?: throw IllegalStateException("No match for kvk in CRM: ${organisationDto.kvkNumber}")
        log.info("Validating org with kvk ${organisationDto.kvkNumber}")
        val daughtersInKvK = organisationDto.subsidiaries.map { it.kvkNumber }.toSet()
        val daughtersInCrm = companyDto.daughters.map { it.kvkNumber }.toSet()
        val missing = daughtersInKvK.minus(daughtersInCrm)
        if (missing.isNotEmpty())
            throw IllegalStateException("kvk structure contains entries unknown in CRM: $missing")
        return companyDto
    }

}
