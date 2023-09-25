package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.kvk.model.Organisation
import nl.jsprengers.sbeworkshop.kvk.KvkClient
import nl.jsprengers.sbeworkshop.model.kvk.OrganisationHelper
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class KvkMockClient : KvkClient {

    private val cache = mutableMapOf<String, Organisation>()

    fun register(org: Organisation) = register(org, listOf())

    fun register(org: Organisation, daughters: List<Organisation>) {
        val composite = OrganisationHelper.clone(org)
        composite.subsidiaries = daughters
        cache[composite.kvkNumber] = composite
    }

    fun init() {
        cache.clear()
    }

    override fun findOrganisation(parentKvk: String): Organisation? = cache[parentKvk]

}
