<template>
  <div>
    <v-img
      class="mt-10 border"
      height="375"
      width="500"
      :aspect-ratio="1"
      :src="titleImage"
      cover
      alt="image not found."
    >
    </v-img>
  </div>
  <v-card flat>
    <v-card-text> </v-card-text>
    <v-card-actions v-if="false">
      <v-btn
        class="w-100"
        size="default"
        :color="themeStore.theme"
        variant="flat"
        @click="showPlannerDialog = true"
      >
        <v-icon start icon="mdi-calendar-search-outline"></v-icon>
        PLANNER
      </v-btn>
    </v-card-actions>

    <!-- 학습 플래너 다이얼로그 -->
    <LearningPlannerDialog v-model="showPlannerDialog" />
  </v-card>
</template>

<script setup>
import { ref } from "vue";
import { useThemeStore } from "@/stores/theme";

import titleImage from "@/assets/images/titleBanner.png";
import LearningPlannerDialog from "./LearningPlannerDialog.vue";

// Pinia
const themeStore = useThemeStore();

const showPlannerDialog = ref(false);
</script>

<style scoped></style>