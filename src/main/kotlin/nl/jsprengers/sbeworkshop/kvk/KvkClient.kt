package nl.jsprengers.sbeworkshop.kvk

import nl.jsprengers.api.kvk.OrganisationApi
import nl.jsprengers.api.kvk.model.Organisation


interface KvkClient : OrganisationApi {
    fun findOrganisation(parentKvk: String): Organisation?
}
