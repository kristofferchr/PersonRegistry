package dev.kristofferchr.personlist.controllers

import dev.kristofferchr.personlist.service.PersonService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/persons")
class PersonController(
    val personService: PersonService,
) {
    @GetMapping
    suspend fun listPersons(): PersonListResponse {
        val personResources =
            personService.getAll().map { PersonResource(it.name, it.age) }
        return PersonListResponse(personResources)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    suspend fun addPerson(
        @RequestBody request: NewPersonPayload,
    ): PersonListResponse {
        personService.addPerson(request.name, request.age)

        val personResources =
            personService.getAll().map { PersonResource(it.name, it.age) }
        return PersonListResponse(personResources)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    suspend fun deletePerson(
        @PathVariable id: Int,
    ) {
        personService.removePerson(id)
    }

    @PutMapping("/{id}")
    suspend fun editPerson(
        @PathVariable id: Int,
        @RequestBody payload: EditPersonPayload,
    ): ResponseEntity<PersonListResponse> {
        if (!personService.doesPersonExist(id)) {
            return ResponseEntity.notFound().build()
        }

        personService.editPerson(id, newName = payload.name, newAge = payload.age)

        val personResources =
            personService.getAll().map { PersonResource(it.name, it.age) }

        return ResponseEntity.ok(
            PersonListResponse(personResources),
        )
    }
}

data class EditPersonPayload(
    val name: String,
    val age: Int,
)

data class NewPersonPayload(
    val name: String,
    val age: Int,
)

data class PersonListResponse(
    val persons: List<PersonResource>,
)

data class PersonResource(
    val name: String,
    val age: Int,
)