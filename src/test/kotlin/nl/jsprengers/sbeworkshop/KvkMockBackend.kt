package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.kvk.model.Organisation
import nl.jsprengers.sbeworkshop.model.kvk.OrganisationHelper
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.whenever
import org.springframework.stereotype.Service

@Service
class KvkMockBackend(private val mock: KvkClient) {

    private val cache = mutableMapOf<String, Organisation>()

    fun register(org: Organisation) = register(org, listOf())

    fun register(org: Organisation, daughters: List<Organisation>) {
        val composite = OrganisationHelper.clone(org)
        composite.subsidiaries = daughters
        cache[composite.kvkNumber] = composite
    }

    fun init() {
        Mockito.reset<Any>(mock)
        cache.clear()
        Mockito.doAnswer { answer: InvocationOnMock ->
            val kvk: String = answer.getArgument(0)
            cache[kvk]
        }
            .whenever(mock)
            .findOrganisation(ArgumentMatchers.anyString())

    }

}
