<template>
  <v-dialog v-model="showDialog" width="350">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 플래너 생성 </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
        </v-btn>
      </template>
      <v-card-text class="noto-sans">
        생성된 학습 플래너가 없습니다. 새로 생성하시겠습니까?
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary"> CONFIRM </v-btn>
        <v-btn @click="showDialog = false" class="me-4"> CANCEL </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed } from "vue";

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
</script>

<style scoped></style>
