package dev.kristofferchr.personlist.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class PersonService {
    var currentPersonIdCount = 1
    val persons = ConcurrentHashMap(mutableMapOf(0 to PersonDto(0, "kristoffer", 30)))

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

    fun doesPersonExist(id: Int): Boolean {
        return persons[id] != null
    }

    fun editPerson(
        id: Int,
        newName: String,
        newAge: Int,
    ) {
        persons[id] = PersonDto(id, newName, newAge)
    }
}

data class PersonDto(
    val id: Int,
    val name: String,
    val age: Int,
)
