<template>
  <v-dialog v-model="showDialog" width="550">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 단어장 등록 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text>
        <v-text-field label="Title" v-model="formData.title"> </v-text-field>
        <v-select
          label="Language"
          :items="['English', 'Japanese']"
          v-model="formData.language"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary" @click="onClick"> ADD </v-btn>
        <v-btn @click="showDialog = false" class="me-4"> CANCEL </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed, reactive } from "vue";

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(["update:modelValue"]);

const showDialog = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const formData = reactive({
  title: null,
  language: "English",
});

const onClick = () => {
  console.log(`title = ${formData.title}`);
  console.log(`language = ${formData.language}`);
};
</script>

<style scoped></style>
