package nl.jsprengers.sbeworkshop.model.kvk

import nl.jsprengers.api.kvk.model.Organisation
import java.time.LocalDate


data class OrganisationDto(
    val kvkNumber: String,
    val name: String,
    val subsidiaries: List<OrganisationDto> = emptyList(),
    val foundationDate: LocalDate? = null
)

object OrganisationHelper {

    fun map(organisation: Organisation): OrganisationDto {
        return OrganisationDto(
            kvkNumber = organisation.kvkNumber,
            name = organisation.name,
            foundationDate = organisation.foundationDate,
            subsidiaries = organisation.subsidiaries?.map { map(it) } ?: emptyList()
        )
    }

    fun clone(organisation: Organisation): Organisation {
        val org = Organisation()
        org.kvkNumber = organisation.kvkNumber
        org.name = organisation.name
        org.foundationDate = organisation.foundationDate
        org.subsidiaries = organisation.subsidiaries?.map { clone(it) }
        return org
    }

}
