package dev.kristofferchr.personlist.controllers

import dev.kristofferchr.personlist.service.PersonsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

// TODO: input validation
@RestController
@RequestMapping("/api/v1/personlist")
@CrossOrigin(origins = ["*"], maxAge = 3600)
class PersonListController(val service: PersonsService) {
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    fun saveList(
        @RequestBody payload: SaveListPayload,
    ): ResponseEntity<Any> {
        payload.run {
            val invalidPersons =
                currentPersons.filter {
                    it.name.isEmpty() ||
                        !it.name.matches(Regex("^[A-Za-z]+$")) ||
                        it.age > 200 ||
                        it.age < 0
                }

            if (invalidPersons.isNotEmpty()) {
                return ResponseEntity.badRequest().build()
            }

            service.saveAll(
                currentPersons,
                deletedPersonIds,
            )

            return ResponseEntity.noContent().build()
        }
    }
}

data class SaveListPayload(
    val currentPersons: List<Person>,
    val deletedPersonIds: List<Int>,
)

data class Person(
    val id: Int? = null,
    val name: String,
    val age: Int,
)
