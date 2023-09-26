package nl.jsprengers.sbeworkshop

import nl.jsprengers.sbeworkshop.model.LegalStructure
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class LegalStructureController(
    @Autowired val legalStructureAnonymousService: LegalStructureAnonymousService,
    val legalStructureAuthenticatedService: LegalStructureAuthenticatedService
) {

    @PostMapping("/legalstructure/anonymous")
    fun createLegalStructureAnonymous(@RequestBody kvk: String): ResponseEntity<LegalStructure> {
        return ResponseEntity.ok(legalStructureAnonymousService.createLegalStructure(kvk))
    }

    @PostMapping("/legalstructure/authenticated")
    fun createLegalStructureAuthenticated(@RequestBody kvk: String): ResponseEntity<LegalStructure> {
        return ResponseEntity.ok(legalStructureAuthenticatedService.createLegalStructure(kvk))
    }

}
