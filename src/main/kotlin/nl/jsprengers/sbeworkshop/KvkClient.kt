package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.kvk.model.Organisation
import org.springframework.stereotype.Service

@Service
class KvkClient {

    fun findOrganisation(parentKvk: String): Organisation? {
        return Organisation(parentKvk, "name")
    }

}
