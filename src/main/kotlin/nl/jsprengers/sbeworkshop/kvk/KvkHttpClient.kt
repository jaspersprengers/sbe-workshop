package nl.jsprengers.sbeworkshop.kvk

import nl.jsprengers.api.kvk.model.Organisation
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("!test")
class KvkHttpClient : KvkClient{

    override fun findOrganisation(parentKvk: String): Organisation? {
        return Organisation(parentKvk, "name")
    }

}
