package nl.jsprengers.sbeworkshop

import nl.jsprengers.sbeworkshop.model.RelationDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class RelationController(
    @Autowired val relationAnonymousService: RelationAnonymousService,
    val relationAuthenticatedService: RelationAuthenticatedService
) {

    @PostMapping("/relationdetails/anonymous")
    fun createLegalStructureAnonymous(@RequestBody cin: String): ResponseEntity<RelationDetails> {
        return ResponseEntity.ok(relationAnonymousService.createRelation(cin))
    }

    @PostMapping("/relationdetails/authenticated")
    fun createLegalStructureAuthenticated(@RequestBody cin: String): ResponseEntity<RelationDetails> {
        return ResponseEntity.ok(relationAuthenticatedService.findRelation(cin))
    }

}
