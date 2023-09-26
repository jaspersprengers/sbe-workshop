package nl.jsprengers.sbeworkshop.model

import com.fasterxml.jackson.annotation.JsonProperty

data class LegalStructure(val companies: List<Relation>? = emptyList())

data class Relation(
    @JsonProperty("relation_id")
    val relationId: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("parent_company_id")
    val parentRelationId: String? = null
)
