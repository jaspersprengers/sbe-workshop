package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.crm.model.Company
import nl.jsprengers.sbeworkshop.model.crm.CompanyHelper
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.whenever
import org.springframework.stereotype.Service

@Service
class CrmMockBackend(private val mock: CrmClient) {

    private val cache = mutableMapOf<String, Company>()

    fun register(company: Company) = register(company, listOf())

    fun register(company: Company, daughters: List<Company>) {
        val composite = CompanyHelper.clone(company)
        composite.daughters = daughters
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
            .findCompanyByKvk(ArgumentMatchers.anyString())

        Mockito.doAnswer { answer: InvocationOnMock ->
            val kvk: String = answer.getArgument(0)
            val name: String = answer.getArgument(1)
            val id = "000$kvk"
            cache[kvk] = Company(id, kvk, name)
            id
        }
            .whenever(mock)
            .createCompany(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())
    }

}
