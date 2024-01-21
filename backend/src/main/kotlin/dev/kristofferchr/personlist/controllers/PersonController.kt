package dev.kristofferchr.personlist.controllers

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
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api/v1/persons")
class PersonController {
    // TODO: PersonController should be split into a PersonService. Eventhough it is not that much logic it will make it simpler when
    // one introduces a repository and a separation of concerns. Reuse of person logic when other controllers are formed
    var currentPersonIdCount = 1
    val persons = ConcurrentHashMap(mutableMapOf(0 to PersonResource("kristoffer", 30)))

    @GetMapping
    suspend fun listPersons(): PersonListResponse {
        return PersonListResponse(
            persons = persons.values.toList(),
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    suspend fun addPerson(
        @RequestBody request: NewPersonPayload,
    ): PersonListResponse {
        val newId = currentPersonIdCount
        val newPerson = PersonResource(request.name, request.age)

        persons[newId] = newPerson
        currentPersonIdCount += 1

        return PersonListResponse(
            persons = persons.values.toList(),
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    suspend fun deletePerson(
        @PathVariable id: Int,
    ) {
        persons.remove(id)
    }

    @PutMapping("/{id}")
    suspend fun editPerson(
        @PathVariable id: Int,
        @RequestBody payload: EditPersonPayload,
    ): ResponseEntity<PersonListResponse> {
        if (persons[id] == null) {
            return ResponseEntity.notFound().build()
        }

        persons[id] = PersonResource(payload.name, payload.age)

        return ResponseEntity.ok(
            PersonListResponse(
                persons = persons.values.toList(),
            ),
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
