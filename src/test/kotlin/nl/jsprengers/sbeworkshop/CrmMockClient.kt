package nl.jsprengers.sbeworkshop

import nl.jsprengers.api.crm.model.Company
import nl.jsprengers.sbeworkshop.crm.CrmClient
import nl.jsprengers.sbeworkshop.model.crm.CompanyHelper
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class CrmMockClient : CrmClient {

    private val cache = mutableMapOf<String, Company>()

    fun register(company: Company) = register(company, listOf())

    fun register(company: Company, daughters: List<Company>) {
        val composite = CompanyHelper.clone(company)
        composite.daughters = daughters
        cache[composite.kvkNumber] = composite
    }

    fun init() {
        cache.clear()
    }

    override fun findCompanyByKvk(parentKvk: String): Company? {
        return cache[parentKvk]
    }

    override fun createCompany(kvk: String, name: String): String {
        val id = "000$kvk"
        cache[kvk] = Company(id, kvk, name)
        return id
    }

}
