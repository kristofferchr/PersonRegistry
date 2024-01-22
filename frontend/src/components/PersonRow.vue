<script lang="ts">
import {defineComponent, PropType} from "vue";
import {Person} from "@/model/persons";
import {ageRules, nameRules} from "@/utils/validationRule";


export default defineComponent({
  methods: {
    ageRules() {
      return ageRules
    },
    nameRules() {
      return nameRules
    }
  },
  props: {
    index: Number,
    person: {
      type: Object as PropType<Person>,
      default: {Name: "", Age:""}
    },
  },
  emits: ['update:personAge', "update:personName", "delete:person"],
  setup(props, {emit}) {
    function onChangedName(e:Event) {
      const htmlTarget = e.target as HTMLInputElement
      emit('update:personName',htmlTarget.value, props.index)
    }
    function onChangedAge(e:Event) {
      const htmlTarget = e.target as HTMLInputElement
      emit('update:personAge',htmlTarget.value, props.index)
    }

    function onRemovePerson() {
      emit('delete:person', props.index)
    }
    return {onChangedAge, onChangedName, onRemovePerson}
  }
})
</script>

<template>
  <v-row no-gutters align="stretch">
    <v-col sm="6" class="pr-6">
      <v-text-field clearable density="compact" :rules="nameRules()" label="Navn" variant="outlined" @input='onChangedName' v-model='person.name'></v-text-field>
    </v-col>
    <v-col sm="2" class="pr-2">
      <v-text-field clearable density="compact" :rules="ageRules()" label="Alder" variant="outlined" @input='onChangedAge' v-model='person.age'></v-text-field>
    </v-col>
    <v-col sm="1">
      <v-btn variant="text" block height="40" @click="onRemovePerson"> X</v-btn>
    </v-col>
  </v-row>
</template>

<style scoped>

</style>
