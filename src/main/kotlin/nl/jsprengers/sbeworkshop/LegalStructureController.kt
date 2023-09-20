package nl.jsprengers.sbeworkshop

import nl.jsprengers.sbeworkshop.model.LegalStructure
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController


@RestController
class LegalStructureController(
    @Autowired val legalStructureAnonymousService: LegalStructureAnonymousService,
    @Autowired val legalStructureAuthenticatedService: LegalStructureAuthenticatedService
) {

    @PostMapping("/legalstructure")
    fun createLegalStructure(
        @RequestHeader(name = "user_type") userType: String,
        @RequestBody kvk: String
    ): ResponseEntity<LegalStructure> {
        val resp = if (userType == "user") {
            legalStructureAuthenticatedService.createLegalStructure(kvk)
        } else legalStructureAnonymousService.createLegalStructure(kvk)
        return ResponseEntity.ok(resp)
    }

}
