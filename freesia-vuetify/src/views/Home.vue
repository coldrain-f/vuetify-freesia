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
            <v-list-item @click="showThemeDialog = true">
              <v-list-item-title> Theme </v-list-item-title>
            </v-list-item>
            <v-list-item @click="showPlannerDialog = true">
              <v-list-item-title> Planner </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-btn>

      <template v-slot:extension>
        <v-tabs v-model="currentTabItem" align-tabs="title">
          <v-tab value="LEARN"> LEARN </v-tab>

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

          <v-tab value="ANALYZE"> ANALYZE </v-tab>
          <v-tab value="RANK"> RANK </v-tab>
        </v-tabs>
      </template>
    </v-toolbar>

    <v-window v-model="currentTabItem">
      <v-window-item value="LEARN">
        <learning-start v-if="isLearningStarted"></learning-start>
        <learning-home v-else></learning-home>
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

      <!-- 단어장 관리 화면 -->
      <v-window-item value="VOCABULARY">
        <AdminVocabularyGrid class="mt-5" />
      </v-window-item>
      <!-- 유닛 관리 화면 -->
      <v-window-item value="UNIT">
        <AdminUnitGrid class="mt-5" />
      </v-window-item>
      <!-- 단어 관리 화면 -->
      <v-window-item value="WORD">
        <AdminWordGrid class="mt-5" />
      </v-window-item>
    </v-window>

    <v-footer :color="themeStore.theme" border>
      <v-row>
        <v-col class="text-center" cols="12">
          <p>
            <strong>Email:</strong> sangwoonin@gmail.com
            <v-icon class="ms-1" size="small" icon="mdi-emoticon-kiss-outline">
            </v-icon>
          </p>

          <p>Copyright ⓒ 2023. coldrain-f. All rights reserved.</p>
        </v-col>
      </v-row>
    </v-footer>
  </v-card>

  <!-- Planner 다이얼로그 -->
  <LearningPlannerDialog v-model="showPlannerDialog" />

  <!-- Theme 설정 다이얼로그 -->
  <MoreThemeSettingDialog v-model="showThemeDialog" />

  <!-- 공통 메시지 다잉러로그 -->
  <CommonMessageDialog />
</template>

<script setup>
import LearningHome from "@/components/learning/LearningHome.vue";
import LearningStart from "@/components/learning/LearningStart.vue";
import LearningPlannerDialog from "@/components/learning/LearningPlannerDialog.vue";

import AdminVocabularyGrid from "@/components/admin/AdminVocabularyGrid.vue";
import AdminUnitGrid from "@/components/admin/AdminUnitGrid.vue";
import AdminWordGrid from "@/components/admin/AdminWordGrid.vue";

import MoreThemeSettingDialog from "@/components/more/MoreThemeSettingDialog.vue";

import CommonMessageDialog from "@/components/common/CommonMessageDialog.vue";

import { useLearningStore } from "@/stores/learning";
import { useSpeechSynthesisStore } from "@/stores/speechSynthesis";
import { useThemeStore } from "@/stores/theme";
import { storeToRefs } from "pinia";
import { nextTick, onMounted, provide, reactive, ref, watch } from "vue";

// Pinia
const synthStore = useSpeechSynthesisStore();
const learningStore = useLearningStore();
const themeStore = useThemeStore();

const { isLearningStarted } = storeToRefs(learningStore);

/**
 * 부모가 자식의 상태를 변경하는 방법
 * 1. 부모에서 자식에서 사용할 상태 선언하고 Provide
 * 2. 자식에서 부모에서 선언한 상태를 Inject하여 사용
 * 3. 부모에서 자식의 상태를 변경할 때 선언한 상태를 변경하면 된다.
 */
const isUnitisSearchPerformed = ref(false);
const isWordSearchPerformed = ref(false);

const unitRowData = ref([]);
const wordRowData = ref([]);

const vocabularyGrid = reactive({
  isSearchPerformed: false,
  rowData: [],
  selectedVocabulary: { title: null, language: null, unitCount: null },
});

provide("vocabularyGrid", vocabularyGrid);

provide("isUnitisSearchPerformed", isUnitisSearchPerformed);
provide("isWordSearchPerformed", isWordSearchPerformed);

provide("unitRowData", unitRowData);
provide("wordRowData", wordRowData);

// Theme
const showThemeDialog = ref(false);

// Tab
const currentTabItem = ref(null);

const adminTabitems = ref([]);

// 모든 관리자 그리드의 상태를 초기화한다.
const resetAllAdminGridData = () => {
  vocabularyGrid.isSearchPerformed = false;

  isUnitisSearchPerformed.value = false;
  isWordSearchPerformed.value = false;

  vocabularyGrid.rowData = [];

  unitRowData.value = [];
  wordRowData.value = [];

  Object.assign(vocabularyGrid.selectedVocabulary, {
    title: null,
    language: null,
    unitCount: null,
  });
};

const changeAdminTabItem = (item) => {
  // 기존에 열린 관리자 탭 아이템 제거
  closeAdminTabItem();

  // 클릭한 관리자 탭 아이템 오픈
  openAdminTabItem(item);

  // Grid 검색 상태 초기화
  resetAllAdminGridData();

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
