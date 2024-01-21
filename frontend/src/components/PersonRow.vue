<script lang="ts">
import {Person} from "@/components/interfaces";
import {defineComponent, PropType} from "vue";


export default defineComponent({
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

    function onRemovePerson(e: Event) {
      emit('delete:person', props.index)
    }
    return {onChangedAge, onChangedName, onRemovePerson}
  }
})
</script>

<template>
  <v-row no-gutters align="stretch">
    <v-col sm="6" class="pr-6">
      <v-text-field clearable density="compact" label="Navn" variant="outlined" @input='onChangedName' v-model='person.Name'></v-text-field>
    </v-col>
    <v-col sm="2" class="pr-2">
      <v-text-field clearable density="compact" label="Alder" variant="outlined" @input='onChangedAge' v-model='person.Age'></v-text-field>
    </v-col>
    <v-col sm="1">
      <v-btn variant="text" block height="40" @click="onRemovePerson"> X</v-btn>
    </v-col>
  </v-row>
</template>

<style scoped>

</style>
