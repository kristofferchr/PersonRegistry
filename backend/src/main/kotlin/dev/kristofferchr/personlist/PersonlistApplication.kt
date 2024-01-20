package dev.kristofferchr.personlist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonlistApplication

fun main(args: Array<String>) {
	runApplication<PersonlistApplication>(*args)
}
