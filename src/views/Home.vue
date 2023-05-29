<template>
  <v-card class="mx-auto mt-10" max-width="500">
    <v-system-bar app color="primary" class="pt-3" absolute>
      <v-icon icon="mdi-wifi-strength-4"></v-icon>
      <v-icon icon="mdi-signal" class="ms-2"></v-icon>
      <v-icon icon="mdi-battery" class="ms-2"></v-icon>

      <span class="ms-2 mr-4">{{ currentDateTime }}</span>
    </v-system-bar>
    <v-toolbar color="primary" class="mt-6">
      <v-app-bar-nav-icon></v-app-bar-nav-icon>
      <v-toolbar-title class="noto-sans">프리지아 보카</v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn icon>
        <v-icon>mdi-magnify</v-icon>
      </v-btn>

      <v-btn icon>
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>

      <template v-slot:extension>
        <v-tabs v-model="tab" align-tabs="title">
          <v-tab value="learn"> LEARN </v-tab>
          <v-tab value="admin"> ADMIN </v-tab>
          <v-tab value="analyze"> ANALYZE </v-tab>
        </v-tabs>
      </template>
    </v-toolbar>

    <v-window v-model="tab">
      <v-window-item value="learn">
        <learning-start v-if="isLearningStarted" />
        <learning-settings v-else class="mt-4" />
      </v-window-item>

      <v-window-item value="admin">
        <admin-home />
      </v-window-item>

      <v-window-item value="analyze">
        <v-card flat>
          <v-card-text>Under development.</v-card-text>
          <v-card-actions> </v-card-actions>
        </v-card>
      </v-window-item>
    </v-window>
  </v-card>
</template>

<script setup>
import LearningSettings from "@/components/learning/LearningSettings.vue";
import LearningStart from "@/components/learning/LearningStart.vue";
import AdminHome from "@/components/admin/AdminHome.vue";
import { useLearningStore } from "@/stores/learning";
import { useSpeechSynthesisStore } from "@/stores/speechSynthesis";
import { storeToRefs } from "pinia";
import { onMounted, ref } from "vue";

const tab = ref(null);

// Pinia
const synthStore = useSpeechSynthesisStore();
const learningStore = useLearningStore();

const { isLearningStarted } = storeToRefs(learningStore);

// 한국의 현재 시간을 가지고 오는 함수
// Format: 오후 9:31:25
const getCurrentDateTime = () => {
  return new Intl.DateTimeFormat("ko-KR", {
    hour: "numeric",
    minute: "numeric",
    second: "numeric",
  }).format();
};

// 1초 간격으로 현재 시간을 재설정 하는 Interval
const intervalCurrentDateTime = () => {
  setInterval(() => {
    currentDateTime.value = getCurrentDateTime();
  }, 1000);
};

// 현재 시간
const currentDateTime = ref(getCurrentDateTime());

onMounted(() => {
  synthStore.initializeSpeechSynthesis();
  intervalCurrentDateTime();
});
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");

.noto-sans {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
