<template>
  <v-dialog v-model="showDialog" width="500">
    <v-card>
      <template #title>
        <span class="text-primary"> ※ Theme </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false"> </v-btn>
      </template>
      <v-card-text>
        <v-select v-model="selectedTheme" label="Theme" :items="themeItems"> </v-select>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary" @click="onClick()"> APPLY </v-btn>
        <v-btn @click="showDialog = false" class="me-4"> CANCEL </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed, ref } from "vue";
import { useThemeStore } from "@/stores/theme";

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

const themeStore = useThemeStore();

const themeItems = [
  "primary",
  "error",
  "info",
  "success",
  "warning",
  "dark",
  "blue-grey",
  "brown",
  "deep-orange",
  "green",
  "teal",
  "indigo",
  "deep-purple",
  "purple",
  "pink",
  "brown",
  "grey",
];

const selectedTheme = ref("primary");

const onClick = () => {
  themeStore.theme = selectedTheme.value;
  showDialog.value = false;
};
</script>

<style scoped></style>
