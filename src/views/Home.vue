<template>
  <v-card class="mx-auto mt-10" max-width="500">
    <v-system-bar app :color="themeStore.theme" class="pt-3" absolute>
      <v-icon icon="mdi-wifi-strength-4"></v-icon>
      <v-icon icon="mdi-signal" class="ms-2"></v-icon>
      <v-icon icon="mdi-battery" class="ms-2"></v-icon>

      <span class="ms-2 mr-4">{{ currentDateTime }}</span>
    </v-system-bar>
    <v-toolbar :color="themeStore.theme" class="mt-6">
      <v-app-bar-nav-icon></v-app-bar-nav-icon>
      <v-toolbar-title class="noto-sans">프리지아 보카</v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn icon>
        <v-icon>mdi-magnify</v-icon>
      </v-btn>

      <v-btn icon>
        <v-icon icon="mdi-dots-vertical"></v-icon>
        <v-menu
          activator="parent"
          location="end"
          transition="slide-x-transition"
        >
          <v-list>
            <v-list-item :value="1">
              <v-list-item-title>TTS(Text-To-Speech)</v-list-item-title>
            </v-list-item>
            <v-list-item :value="2">
              <v-list-item-title @click="() => (showThemeDialog = true)">
                Theme
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
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

  <!-- Theme 설정 다이얼로그 -->
  <v-dialog v-model="showThemeDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-primary"> ※ Theme </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showThemeDialog = false">
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-select
          v-model="selectedTheme"
          :items="['primary', 'error', 'info', 'success', 'warning', 'dark']"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="primary" style="width: 48%" @click="onClickThemeApply()">
          APPLY
        </v-btn>
        <v-btn style="width: 48%" @click="showThemeDialog = false">
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import LearningSettings from "@/components/learning/LearningSettings.vue";
import LearningStart from "@/components/learning/LearningStart.vue";
import AdminHome from "@/components/admin/AdminHome.vue";
import { useLearningStore } from "@/stores/learning";
import { useSpeechSynthesisStore } from "@/stores/speechSynthesis";
import { useThemeStore } from "@/stores/theme";
import { storeToRefs } from "pinia";
import { onMounted, ref } from "vue";

const tab = ref(null);

// Pinia
const synthStore = useSpeechSynthesisStore();
const learningStore = useLearningStore();
const themeStore = useThemeStore();

const { isLearningStarted } = storeToRefs(learningStore);

// Theme
const showThemeDialog = ref(false);
const selectedTheme = ref("primary");

const onClickThemeApply = () => {
  themeStore.theme = selectedTheme.value;
  showThemeDialog.value = false;
};

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
