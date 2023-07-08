<template>
  <div>
    <v-img
      class="border"
      style="cursor: pointer"
      height="375"
      width="500"
      :aspect-ratio="1"
      :src="titleBanner"
      alt="image not found."
      cover
      @click="showPlannerDialog = true"
    >
    </v-img>
    <LearningPlannerDialog v-model="showPlannerDialog" />
  </div>
</template>

<script setup>
import { ref } from "vue";

import titleBanner from "@/assets/images/titleBanner.png";
import LearningPlannerDialog from "./LearningPlannerDialog.vue";

const showPlannerDialog = ref(false);
</script>

<style scoped></style>
