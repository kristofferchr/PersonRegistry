<template>
  <v-container class="fill-screen">
    <v-responsive class="align-center text-center">
      <h1 class="text-h2 font-weight-bold">Sheldon Coopers mortal enemies</h1>
      <div class="py-4"/>
      <div v-for="(person,i) in persons" :key="i">
        <PersonRow :person="person" :index=i @update:personName="editPersonName" @update:person-age="editPersonAge"
                   @delete:person="removePerson"/>
      </div>
      <v-row>
        <v-col sm="2">
          <v-btn @click="newPerson()" prepend-icon="mdi-plus" width="100%" class="ma-2" variant="outlined">New</v-btn>
        </v-col>
      </v-row>
    </v-responsive>
  </v-container>

  <div class="bottom-actions">
    <div class = "bg-transparent">

    <v-row>
      <v-col sm="3" offset="2">
        <v-btn :loading="isLoading" @click="reload" prepend-icon="mdi-reload" width="100%" class="ma-2 bg-white"
               variant="outlined">Reload
        </v-btn>
      </v-col>
      <v-col sm="3">
        <v-btn @click="submit" :loading="isLoading" append-icon="mdi-arrow-up-thin" width="100%" color="blue"
               type="submit" class="ma-2">Save
        </v-btn>
      </v-col>
    </v-row>
    </div>
  </div>
</template>

<style>
.fill-screen {
  height: 100%;
  min-height: 100vh;
  background: inherit;
  z-index: -1;
}

.bg-transparent {
  background-color: rgba(82,67,0,0.1) !important;
}
.bottom-actions {
  position: sticky;
  bottom: 0;
  background-color: white;
  width: 100%;
  background-size: cover;
  /* Additional styling for the sticky footer */
}
</style>

<script setup lang="ts">
//
import PersonRow from "@/components/PersonRow.vue";
import {ref} from "vue";
import {getOriginalPersons, Person} from "@/model/Persons";

const persons = ref<Array<Person>>(getOriginalPersons())
const isLoading = ref(false)

function newPerson() {
  persons.value.push({Name: "", Age: ""})
}

function editPersonName(value: any, index: number) {
  persons.value[index].Name = value
}

function editPersonAge(value: string, index: number) {
  persons.value[index].Age = value
}

function reload() {
  persons.value = getOriginalPersons()
}

function removePerson(index: number) {
  persons.value.splice(index, 1)
}

function submit() {
  isLoading.value = true
  setTimeout(() => {
    isLoading.value = false
  }, 2000)
}

</script>
