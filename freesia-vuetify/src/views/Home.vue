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
      <v-toolbar-title class="noto-sans">기묘한 보카</v-toolbar-title>
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
            <v-list-item :value="1" disabled>
              <v-list-item-title> TTS(Text-To-Speech) </v-list-item-title>
            </v-list-item>
            <v-list-item :value="2">
              <v-list-item-title @click="showThemeDialog = true">
                Theme
              </v-list-item-title>
            </v-list-item>
            <v-list-item :value="3">
              <v-list-item-title @click="showPlannerDialog = true">
                Planner
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-btn>

      <template v-slot:extension>
        <v-tabs v-model="currentTabItem" align-tabs="title">
          <v-tab value="LEARN"> LEARN </v-tab>
          <!-- 미사용 업데이트 후 삭제 예정 -->
          <v-tab value="admin" v-if="false"> ADMIN </v-tab>

          <!-- Begin:: Admin Tab Items -->
          <v-tab v-for="item in adminTabitems" :key="item" :value="item">
            {{ item }}
          </v-tab>

          <v-menu>
            <template v-slot:activator="{ props }">
              <v-btn
                variant="plain"
                rounded="0"
                class="align-self-center"
                height="100%"
                v-bind="props"
              >
                ADMIN
                <v-icon end> mdi-menu-down </v-icon>
              </v-btn>
            </template>

            <v-list>
              <v-list-item @click="changeAdminTabItem('VOCABULARY')">
                <v-list-item-title> Vocabulary </v-list-item-title>
              </v-list-item>
              <v-list-item @click="changeAdminTabItem('UNIT')">
                <v-list-item-title> Unit </v-list-item-title>
              </v-list-item>
              <v-list-item @click="changeAdminTabItem('WORD')">
                <v-list-item-title> Word </v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
          <!-- End:: Admin Tab Items -->

          <v-tab value="ANALYZE" disabled> ANALYZE </v-tab>
          <v-tab value="RANK"> RANK </v-tab>
        </v-tabs>
      </template>
    </v-toolbar>

    <v-window v-model="currentTabItem">
      <v-window-item value="LEARN">
        <learning-start v-if="isLearningStarted"></learning-start>
        <learning-home v-else></learning-home>
      </v-window-item>

      <v-window-item value="ADMIN">
        <admin-home></admin-home>
      </v-window-item>

      <v-window-item value="ANALYZE">
        <v-card flat>
          <v-card-text>Under development.</v-card-text>
          <v-card-actions> </v-card-actions>
        </v-card>
      </v-window-item>
      <v-window-item value="RANK">
        <v-card flat>
          <v-card-text>Under development.</v-card-text>
          <v-card-actions> </v-card-actions>
        </v-card>
      </v-window-item>

      <v-window-item value="VOCABULARY">
        <AdminVocabularyGrid class="mt-5" />
      </v-window-item>
      <v-window-item value="UNIT"> UNIT </v-window-item>
      <v-window-item value="WORD"> WORD </v-window-item>
    </v-window>

    <v-footer :color="themeStore.theme" border>
      <v-row>
        <v-col class="text-center" cols="12">
          <p>
            <strong>Email:</strong> sangwoonin@gmail.com
            <v-icon
              class="ms-1"
              size="small"
              icon="mdi-emoticon-kiss-outline"
            ></v-icon>
          </p>

          <p>Copyright ⓒ 2023. coldrain-f. All rights reserved.</p>
        </v-col>
      </v-row>
    </v-footer>
  </v-card>

  <!-- Theme 설정 다이얼로그 -->
  <v-dialog v-model="showThemeDialog" width="500">
    <v-card>
      <template #title>
        <span class="text-primary"> ※ Theme </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showThemeDialog = false">
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-select
          v-model="selectedTheme"
          label="Theme"
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

  <!-- Planner 다이얼로그 -->
  <LearningPlannerDialog v-model="showPlannerDialog" />

  <!-- 공통 메시지 다이얼로그 -->
  <v-dialog v-model="showMessageDialog" width="auto">
    <v-card>
      <template #title>
        <span class="noto-sans text-primary"> ※ Alert </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="showMessageDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text>
        {{ messageDialogText }}
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn
          style="width: 100%"
          color="primary"
          @click="showMessageDialog = false"
        >
          CONFIRM
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import LearningHome from "@/components/learning/LearningHome.vue";
import LearningStart from "@/components/learning/LearningStart.vue";
import LearningPlannerDialog from "@/components/learning/LearningPlannerDialog.vue";
import AdminHome from "@/components/admin/AdminHome.vue";
import AdminVocabularyGrid from "@/components/admin/AdminVocabularyGrid.vue";

import { useLearningStore } from "@/stores/learning";
import { useSpeechSynthesisStore } from "@/stores/speechSynthesis";
import { useThemeStore } from "@/stores/theme";
import { storeToRefs } from "pinia";
import { nextTick, onMounted, ref, watch } from "vue";
import { useCommonStore } from "@/stores/common";

// Pinia
const synthStore = useSpeechSynthesisStore();
const learningStore = useLearningStore();
const themeStore = useThemeStore();
const commonStore = useCommonStore();

const { isLearningStarted } = storeToRefs(learningStore);
const { showMessageDialog, messageDialogText } = storeToRefs(commonStore);

// Theme
const showThemeDialog = ref(false);
const selectedTheme = ref("primary");

const onClickThemeApply = () => {
  themeStore.theme = selectedTheme.value;
  showThemeDialog.value = false;
};

// Tab
const currentTabItem = ref(null);

const adminTabitems = ref([]);

const changeAdminTabItem = (item) => {
  // 기존에 열린 관리자 탭 아이템 제거
  closeAdminTabItem();

  // 클릭한 관리자 탭 아이템 오픈
  openAdminTabItem(item);

  nextTick(() => {
    currentTabItem.value = item;
  });
};

const closeAdminTabItem = () => {
  adminTabitems.value.splice(0);
};

const openAdminTabItem = (item) => {
  adminTabitems.value.push(item);
};

const isAdminTabItem = (item) => {
  item = item.toUpperCase();
  return ["VOCABULARY", "UNIT", "WORD"].includes(item);
};

watch(currentTabItem, () => {
  if (!isAdminTabItem(currentTabItem.value)) {
    closeAdminTabItem();
  }
});

// Planner
const showPlannerDialog = ref(false);

// 한국의 현재 시간을 가지고 오는 함수 ( Format: 오후 9:31:25 )
const getCurrentDateTime = () => {
  return new Intl.DateTimeFormat("ko-KR", {
    hour: "numeric",
    minute: "numeric",
    // second: "numeric",
    hour12: true,
  }).format();
};

// 초 단위 현재 시간 갱신 인터벌
const intervalCurrentDateTime = () => {
  setInterval(() => {
    currentDateTime.value = getCurrentDateTime();
  }, 1000);
};

// 현재 시간
const currentDateTime = ref();

onMounted(() => {
  synthStore.initializeSpeechSynthesis();

  // 현재 시간 초기값 설정
  currentDateTime.value = getCurrentDateTime();

  // 초 단위 현재 시간 갱신 인터벌 시작
  intervalCurrentDateTime();
});
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");

.noto-sans {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
