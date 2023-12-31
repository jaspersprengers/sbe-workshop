package nl.jsprengers.sbeworkshop.kvk

import nl.jsprengers.sbeworkshop.model.kvk.OrganisationDto
import nl.jsprengers.sbeworkshop.model.kvk.OrganisationHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KvkService(@Autowired val kvkClient: KvkClient) {

    fun findOrganisation(parentKvk: String): OrganisationDto? {
        return kvkClient.findOrganisation(parentKvk)?.let { OrganisationHelper.map(it) }
    }

}
