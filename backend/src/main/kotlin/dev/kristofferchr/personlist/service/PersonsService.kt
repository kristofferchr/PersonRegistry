package dev.kristofferchr.personlist.service

import dev.kristofferchr.personlist.controllers.Person
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class PersonsService {
    var currentPersonIdCount = 1
    var persons = ConcurrentHashMap(mutableMapOf(0 to PersonDto(0, "kristoffer", 30)))

    fun getAll(): List<PersonDto> {
        return persons.values.toList()
    }

    fun addPerson(
        name: String,
        age: Int,
    ) {
        val newId = currentPersonIdCount
        val newPerson = PersonDto(newId, name, age)

        persons[newId] = newPerson
        currentPersonIdCount += 1
    }

    fun removePerson(id: Int) {
        persons.remove(id)
    }

    fun editPerson(
        id: Int,
        newName: String,
        newAge: Int,
    ) {
        persons[id] = PersonDto(id, newName, newAge)
    }

    fun saveAll(
        currentPersons: List<Person>,
        deletedPersonIds: List<Int>,
    ) {
        deletedPersonIds.forEach {
            persons.remove(it)
        }

        val (newPersons, newExistingPersons) = currentPersons.partition { it.id == null }

        newPersons.forEach {
            addPerson(it.name, it.age)
        }

        newExistingPersons.forEach {
            editPerson(it.id.assertNonNull(), it.name, it.age)
        }
    }
}

fun <T> T?.assertNonNull(): T {
    if (this == null) {
        throw IllegalStateException("Variable that was marked as non null is null")
    }

    return this
}

data class PersonDto(
    val id: Int,
    val name: String,
    val age: Int,
)
