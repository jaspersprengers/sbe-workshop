package nl.jsprengers.sbeworkshop.model

import com.fasterxml.jackson.annotation.JsonProperty
import nl.jsprengers.api.crm.model.Relation.StatusEnum

data class RelationDetails(
    @JsonProperty("relation_id")
    val relationId: String,
    @JsonProperty("company_identification_number")
    val cin: String,
    val name: String,
    @JsonProperty("foundation_year")
    val foundationYear: Int? = null,
    @JsonProperty("status")
    val status: StatusEnum? = null

)
