package dev.kristofferchr.personlist.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PersonControllerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `Should be able to list all persons`() {
        webTestClient.get().uri("/api/v1/persons")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.persons.length()").isEqualTo(1)
            .jsonPath("$.persons[0].name").isEqualTo("kristoffer")
            .jsonPath("$.persons[0].age").isEqualTo(30)
    }

    @Test
    fun `Should be able to add new person`() {
        val newPerson =
            NewPersonPayload(
                "Mandalorian",
                40,
            )
        webTestClient.post().uri("/api/v1/persons")
            .bodyValue(newPerson)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.persons.length()").isEqualTo(2)
            .jsonPath("$.persons[1].name").isEqualTo(newPerson.name)
            .jsonPath("$.persons[1].age").isEqualTo(newPerson.age)
    }

    @Test
    fun `Should be able to delete a newly created person`() {
        val newPerson =
            NewPersonPayload(
                "Mandalorian",
                40,
            )

        webTestClient.post().uri("/api/v1/persons")
            .bodyValue(newPerson)
            .exchange()
            .expectStatus().isCreated

        webTestClient.delete().uri("/api/v1/persons/{id}", 1)
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get().uri("/api/v1/persons")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.persons.length()").isEqualTo(1)
    }

    @Test
    fun `Delete person should be idempotent`() {
        webTestClient.delete().uri("/api/v1/persons/{id}", 1)
            .exchange()
            .expectStatus().isNoContent

        webTestClient.get().uri("/api/v1/persons")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.persons.length()").isEqualTo(1)
    }

    @Test
    fun `Should be able to update a person`() {
        val newPersonData =
            EditPersonPayload(
                name = "Kristoffer Chris",
                age = 31,
            )

        webTestClient.put().uri("/api/v1/persons/{id}", 0)
            .bodyValue(newPersonData)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.persons.length()").isEqualTo(1)
            .jsonPath("$.persons[0].name").isEqualTo(newPersonData.name)
            .jsonPath("$.persons[0].age").isEqualTo(newPersonData.age)
    }

    @Test
    fun `Should return 404 when trying to update a non existing person`() {
        val newPersonData =
            EditPersonPayload(
                name = "Kristoffer Chris",
                age = 31,
            )

        webTestClient.put().uri("/api/v1/persons/{id}", 1)
            .bodyValue(newPersonData)
            .exchange()
            .expectStatus().isNotFound
    }
}
