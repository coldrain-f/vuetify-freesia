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
            <v-list-item :value="1" disabled>
              <v-list-item-title
                @click="() => (textToSpeech.showTextToSpeechDialog = true)"
              >
                TTS(Text-To-Speech)
              </v-list-item-title>
            </v-list-item>
            <v-list-item :value="2">
              <v-list-item-title @click="() => (showThemeDialog = true)">
                Theme
              </v-list-item-title>
            </v-list-item>
            <v-list-item :value="3">
              <v-list-item-title @click="() => (showPlannerDialog = true)">
                Planner
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-btn>

      <template v-slot:extension>
        <v-tabs v-model="tab" align-tabs="title">
          <v-tab value="learn"> LEARN </v-tab>
          <v-tab value="admin"> ADMIN </v-tab>
          <v-tab value="analyze" disabled> ANALYZE </v-tab>
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

  <!-- TTS(Text-to-Speech) 설정 다이얼로그 -->
  <TextToSpeech />

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
  <v-dialog v-model="showPlannerDialog" width="800">
    <v-card>
      <template #title>
        <span class="text-primary"> ※ Planner </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="showPlannerDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <ag-grid-vue
          style="width: 100%; height: 500px"
          class="ag-theme-alpine"
          :columnDefs="columnDefs"
          :rowData="rowData"
          :defaultColDef="defaultColDef"
          :gridOptions="gridOptions"
        >
        </ag-grid-vue>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn class="mr-5" @click="showPlannerDialog = false"> CANCEL </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 
    공통 스낵바
    사용 예시:
      1. useCommonStore를 import한다.
      2. snackbarText.value = "단어장 등록을 성공했습니다.";
      3. showSnackbar.value = true;
  -->
  <v-snackbar v-model="showSnackbar">
    {{ snackbarText }}

    <template v-slot:actions>
      <v-btn color="pink" variant="text" @click="() => (showSnackbar = false)">
        Close
      </v-btn>
    </template>
  </v-snackbar>

  <!-- 
    공통 메시지 다이얼로그
   -->
  <v-dialog v-model="showMessageDialog" width="auto">
    <v-card>
      <template #title>
        <span class="noto-sans text-primary"> ※ Alert </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (showMessageDialog = false)"
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
          @click="() => (showMessageDialog = false)"
        >
          CONFIRM
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
// AG Grid Vue
import { AgGridVue } from "ag-grid-vue3"; // the AG Grid Vue Component
import "ag-grid-community/styles/ag-grid.css"; // Core grid CSS, always needed
import "ag-grid-community/styles/ag-theme-alpine.css"; // Optional theme CSS

import LearningSettings from "@/components/learning/LearningSettings.vue";
import LearningStart from "@/components/learning/LearningStart.vue";
import AdminHome from "@/components/admin/AdminHome.vue";
import TextToSpeech from "@/components/options/TestToSpeech.vue";
import { useLearningStore } from "@/stores/learning";
import { useSpeechSynthesisStore } from "@/stores/speechSynthesis";
import { useThemeStore } from "@/stores/theme";
import { storeToRefs } from "pinia";
import { onMounted, reactive, ref } from "vue";
import { useCommonStore } from "@/stores/common";

const tab = ref(null);

// Pinia
const synthStore = useSpeechSynthesisStore();
const learningStore = useLearningStore();
const themeStore = useThemeStore();
const commonStore = useCommonStore();

const { isLearningStarted } = storeToRefs(learningStore);
const { showSnackbar, snackbarText, showMessageDialog, messageDialogText } =
  storeToRefs(commonStore);

// TTS(Text-to-Speech)
const textToSpeech = reactive({
  showTextToSpeechDialog: ref(false),
});

// Theme
const showThemeDialog = ref(false);
const selectedTheme = ref("primary");

const onClickThemeApply = () => {
  themeStore.theme = selectedTheme.value;
  showThemeDialog.value = false;
};

// Planner
const showPlannerDialog = ref(false);

const gridOptions = {
  // 여기서부터 개발 진행...
  onCellDoubleClicked: (params) => {
    console.log("셀 클릭 이벤트:", params);
    showThemeDialog.value = true;
  },
};

const defaultColDef = {
  editable: false,
  cellDataType: false,
  suppressHorizontalScroll: false, // 가로 스크롤 적용
  cellStyle: {
    color: "gray",
    fontWeight: "bold",
  },
};

const columnDefs = [
  { headerName: "진도", field: "progress", flex: 0.16 },
  { headerName: "1 복습", field: "one", flex: 0.16 },
  { headerName: "4 복습", field: "four", flex: 0.16 },
  { headerName: "7 복습", field: "seven", flex: 0.16 },
  { headerName: "14 복습", field: "fourteen", flex: 0.16 },
  { headerName: "30 복습", field: "thirty", flex: 0.16 },
];

const rowData = getTempRowData();

function getTempRowData() {
  const rowData = [];
  for (let i = 1; i <= 30; i++) {
    rowData.push({
      progress: i + "일",
      one: "",
      four: "",
      seven: "",
      fourteen: "",
      thirty: "",
    });
  }
  return rowData;
}

// 한국의 현재 시간을 가지고 오는 함수
// Format: 오후 9:31:25
const getCurrentDateTime = () => {
  return new Intl.DateTimeFormat("ko-KR", {
    hour: "numeric",
    minute: "numeric",
    // second: "numeric",
    hour12: true,
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
