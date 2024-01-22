<template>
  <v-form @submit.prevent="submit" v-model="isFormValid">
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
      <v-container class="pt-0">
        <v-alert :type="alertType"
                 :title="alertTitle"
                 :text="alertMessage"
                 :model-value="showAlert"
                 transition="slide-y-transition"
                 class="mb-5"
        />
      </v-container>
      <div class="bg-transparent">

        <v-row>
          <v-col sm="3" offset="2">
            <v-btn :loading="isReloadLoading" @click="reload" prepend-icon="mdi-reload" width="100%"
                   class="ma-2 bg-white"
                   variant="outlined">Reload
            </v-btn>
          </v-col>
          <v-col sm="3">
            <v-btn :disabled="!isFormValid" :loading="isSubmitLoading" append-icon="mdi-arrow-up-thin" width="100%" color="blue"
                   type="submit" class="ma-2">Save
            </v-btn>
          </v-col>
        </v-row>
      </div>
    </div>
  </v-form>
</template>

<script setup lang="ts">
//
import PersonRow from "@/components/PersonRow.vue";
import {ref} from "vue";
import {Person} from "@/model/persons";
import {getAllPersons, savePersons} from "@/clients/PersonApiClient";
import {AxiosError, AxiosResponse} from "axios";

const alertTitle = ref("")
const alertMessage = ref("")
const alertType= ref()
const showAlert = ref(false)

const persons = ref<Array<Person>>([])
const deletedPersonIds = ref<Array<number>>([])
const isFormValid = ref(false)

const isSubmitLoading = ref(false)
const isReloadLoading = ref(false)


getAllPersons().then((personresponse) => {
  persons.value = personresponse
}).catch((error: AxiosError) => {
  showErrorAlert(error.message)
})


function showErrorAlert(error: string) {
  alertTitle.value = "Feil"
  alertMessage.value = error
  alertType.value = "error"
  showAlert.value=  true

  setTimeout(()=> {
    showAlert.value = false
  }, 15000)
}

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
  }).catch((error: AxiosError) => {
    showErrorAlert(error.message)
  })
    .finally(() => {
    isReloadLoading.value = false
  })
}

function removePerson(index: number) {
  const removedPerson = persons.value.splice(index, 1)[0]

  if (removedPerson.id !== undefined) {
    deletedPersonIds.value.push(removedPerson.id)
  }
}

function submit() {
  isSubmitLoading.value = true
  savePersons(persons.value, deletedPersonIds.value)
    .then((response: AxiosResponse<any>) => {
      alertTitle.value = "Vellykket"
      alertMessage.value = "Du har lagret personlisten din"
      alertType.value = "success"
      showAlert.value=  true
      setTimeout(() => {
        showAlert.value = false
      }, 3000)

      return response.data
    })
    .catch((error: AxiosError) => {
      showErrorAlert(error.message)
    })
    .finally(() => {
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
