<template>
  <v-dialog v-model="showDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-primary"> ※ TTS(Text-To-Speech) </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (showDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5"> Under development. </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="primary" style="width: 48%"> APPLY </v-btn>
        <v-btn style="width: 48%" @click="() => (showDialog = false)">
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref } from "vue";

const showDialog = ref(false);
</script>
