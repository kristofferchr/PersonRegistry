package dev.kristofferchr.personlist.controllers

import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.kristofferchr.personlist.service.PersonDto
import dev.kristofferchr.personlist.service.PersonsService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PersonListControllerTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var service: PersonsService

    @Test
    fun `Should be able to save persons and delete`() {
        service.addPerson("Anniken", 48)
        webTestClient.put().uri("/api/v1/personlist")
            .bodyValue(
                SaveListPayload(
                    currentPersons = listOf(Person(0, "Yoda", 100), Person(name = "Luke", age = 58)),
                    deletedPersonIds = listOf(1),
                ),
            )
            .exchange()
            .expectStatus().isNoContent

        val personList = service.getAll()
        assertThat(personList).isEqualTo(
            listOf(
                PersonDto(0, "Yoda", 100),
                PersonDto(2, "Luke", 58),
            ),
        )
    }

    @Test
    fun `Should do nothing on save when payload is empty`() {
        val existingPersonList = service.getAll()
        webTestClient.put().uri("/api/v1/personlist")
            .bodyValue(
                SaveListPayload(
                    currentPersons = listOf(),
                    deletedPersonIds = listOf(),
                ),
            )
            .exchange()
            .expectStatus().isNoContent

        val personList = service.getAll()
        assertThat(personList).isEqualTo(existingPersonList)
    }

    @Test
    fun `Should return badrequest when name is not letters`() {
        webTestClient.put().uri("/api/v1/personlist")
            .bodyValue(
                SaveListPayload(
                    currentPersons = listOf(Person(0, "234", 30)),
                    deletedPersonIds = listOf(),
                ),
            )
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `Should return badrequest when name is empty`() {
        webTestClient.put().uri("/api/v1/personlist")
            .bodyValue(
                SaveListPayload(
                    currentPersons = listOf(Person(0, "", 30)),
                    deletedPersonIds = listOf(),
                ),
            )
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `Should return badrequest when age is less than 0`() {
        webTestClient.put().uri("/api/v1/personlist")
            .bodyValue(
                SaveListPayload(
                    currentPersons = listOf(Person(0, "Kris", -10)),
                    deletedPersonIds = listOf(),
                ),
            )
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `Should return badrequest when age is above 200`() {
        webTestClient.put().uri("/api/v1/personlist")
            .bodyValue(
                SaveListPayload(
                    currentPersons = listOf(Person(0, "Kris", 300)),
                    deletedPersonIds = listOf(),
                ),
            )
            .exchange()
            .expectStatus().isBadRequest
    }
}
