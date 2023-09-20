package nl.jsprengers.sbeworkshop.model.crm

import nl.jsprengers.api.crm.model.Company

data class CompanyDto(
    val relationId: String,
    val kvkNumber: String,
    val name: String,
    val director: String? = null,
    val daughters: List<CompanyDto> = emptyList()
)

object CompanyHelper {

    fun map(crmCompany: Company): CompanyDto {
        return CompanyDto(
            relationId = crmCompany.relationId,
            kvkNumber = crmCompany.kvkNumber,
            name = crmCompany.name,
            director = crmCompany.director,
            daughters = crmCompany.daughters?.map { map(it) } ?: emptyList()
        )
    }

    fun clone(crmCompany: Company): Company {
        val com = Company()
        com.relationId = crmCompany.relationId
        com.kvkNumber = crmCompany.kvkNumber
        com.name = crmCompany.name
        com.director = crmCompany.director
        com.daughters = crmCompany.daughters?.map {
            clone(it)
        }
        return com
    }

}

