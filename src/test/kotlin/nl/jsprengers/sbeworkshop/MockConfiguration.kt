package nl.jsprengers.sbeworkshop

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MockConfiguration {

    @MockBean
    lateinit var crmClient: CrmClient

    @MockBean
    lateinit var kvkClient: KvkClient

    @Bean
    fun crmMockBean(crmClient: CrmClient) = CrmMockBackend(crmClient)

    @Bean
    fun kvkMockBean(kvkClient: KvkClient) = KvkMockBackend(kvkClient)

}
