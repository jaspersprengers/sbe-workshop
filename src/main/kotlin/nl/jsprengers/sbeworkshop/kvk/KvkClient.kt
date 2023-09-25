package nl.jsprengers.sbeworkshop.kvk

import nl.jsprengers.api.kvk.model.Organisation


interface KvkClient {
    fun findOrganisation(parentKvk: String): Organisation?
}
