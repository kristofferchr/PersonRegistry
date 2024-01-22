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

  <div class="bottom-toolbar">
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

<script setup lang="ts">
//
import PersonRow from "@/components/PersonRow.vue";
import {ref} from "vue";
import {Person} from "@/model/persons";
import {getAllPersons, savePersons} from "@/clients/PersonApiClient";

const persons = ref<Array<Person>>([])
const deletedPersonIds = ref<Array<number>>([])
getAllPersons().then((personresponse) => {
  persons.value = personresponse
})

const isSubmitLoading = ref(false)
const isReloadLoading = ref(false)
function newPerson() {
  persons.value.push({id: undefined, name: "", age: ""})
}

function editPersonName(value: any, index: number) {
  persons.value[index].name = value
}

function editPersonAge(value: string, index: number) {
  persons.value[index].age = value
}

async function reload() {
  isReloadLoading.value = true
  getAllPersons().then((personresponse) => {
    persons.value = personresponse
  }).finally(() => {
    isReloadLoading.value = false
  })
  console.log(persons)
}

function removePerson(index: number) {
  const removedPerson = persons.value.splice(index, 1)[0]

  if (removedPerson.id !== undefined) {
    deletedPersonIds.value.push(removedPerson.id)
  }
}

function submit() {
  isSubmitLoading.value = true
  savePersons(persons.value, deletedPersonIds.value).finally(() => {
    isSubmitLoading.value = false
  })
}

</script>

<style>
.fill-screen {
  height: 100%;
  min-height: 100vh;
  background: inherit;
  z-index: -1;
}

.bg-transparent {
  background-color: rgba(82, 67, 0, 0.1) !important;
}

.bottom-toolbar {
  position: sticky;
  bottom: 0;
  background-color: white;
}
</style>
