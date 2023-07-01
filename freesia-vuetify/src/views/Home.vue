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

  <!-- Planner 단어장 & 유닛 선택 다이얼로그 -->
  <v-dialog v-model="showPlannerSelectDialog" width="500">
    <v-card>
      <template #title>
        <span class="text-primary"> ※ Search </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="showPlannerSelectDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-select
          model-value="단어가 읽기다 기본편"
          label="Voca"
          :items="['단어가 읽기다 기본편', '단어가 읽기다 실력편']"
        />

        <v-select
          model-value="Unit 01 - 일상"
          label="Unit"
          :items="['Unit 01 - 일상', 'Unit 02 - 학교 생활1']"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="primary" style="width: 48%" @click="onClickThemeApply()">
          APPLY
        </v-btn>
        <v-btn style="width: 48%" @click="showPlannerSelectDialog = false">
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- Planner 다이얼로그 -->
  <v-dialog v-model="showPlannerDialog" width="1070">
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
        <v-row>
          <v-col cols="2">
            <v-select
              label="Language"
              variant="underlined"
              :items="['English', 'Japanese']"
              model-value="English"
            />
          </v-col>
          <v-col cols="3">
            <v-select
              label="Voca"
              variant="underlined"
              :items="['단어가 읽기다 기본편', '단어가 읽기다 실력편']"
              model-value="단어가 읽기다 기본편"
            />
          </v-col>
          <v-col cols="2">
            <v-btn
              class="mt-3"
              size="large"
              density="compact"
              icon="mdi-magnify"
              variant="text"
            >
            </v-btn>
          </v-col>
          <v-col cols="5" class="text-end">
            <v-btn
              class="mt-4 mr-2"
              size="small"
              variant="flat"
              color="primary"
            >
              <v-icon start icon="mdi-content-save-all-outline"></v-icon>
              SAVE
            </v-btn>
            <v-btn class="mt-4 mr-2" size="small" variant="flat" color="error">
              <v-icon start icon="mdi-trash-can-outline"></v-icon>
              CLEAR
            </v-btn>
            <v-btn class="mt-4 border" size="small" variant="flat">
              <v-icon start icon="mdi-file-export-outline"></v-icon>
              EXPORT
            </v-btn>
          </v-col>
        </v-row>

        <ag-grid-vue
          style="width: 100%; height: 450px"
          class="ag-theme-alpine"
          :columnDefs="columnDefs"
          :rowData="rowData"
          :defaultColDef="defaultColDef"
          :gridOptions="gridOptions"
          :suppressMovableColumns="true"
        >
        </ag-grid-vue>
      </v-card-text>
      <v-card-actions>
        <v-row style="margin: 0">
          <v-col cols="3" style="padding: 0">
            <v-switch
              class="ml-4"
              v-model="isLearning"
              hide-details
              color="primary"
              :true-value="true"
              :false-value="false"
              :label="`1/4/7/14 학습법: ${isLearning ? '적용' : '미적용'}`"
            />
          </v-col>
          <v-col class="text-end" cols="9" style="padding: 0">
            <v-btn class="mr-5 mt-2" color="primary">
              <v-icon start icon="mdi-school-outline"></v-icon>
              LEARN
            </v-btn>
            <v-btn class="mr-5 mt-2" color="error">
              <v-icon start icon="mdi-school-outline"></v-icon>
              EXAM
            </v-btn>
            <v-btn class="mr-5 mt-2" @click="showPlannerDialog = false">
              CANCEL
            </v-btn>
          </v-col>
        </v-row>
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
const showPlannerSelectDialog = ref(false);

const isLearning = ref(true);

const gridOptions = {
  singleClickEdit: false,

  // 여기서부터 개발 진행...
  onCellDoubleClicked: (params) => {
    // console.log("셀 더블 클릭 이벤트:", params);
    showPlannerSelectDialog.value = false;
  },
  onCellClicked: (params) => {
    // console.log("셀 클릭 이벤트:", params);
    // defaultColDef.value.editable = true;
  },
};

const defaultColDef = ref({
  editable: false,
  cellDataType: false,
  suppressHorizontalScroll: false, // 가로 스크롤 적용
  resizable: true, // 헤더 길이 늘리기 허용
});

const columnDefs = [
  {
    headerName: "선택",
    headerCheckboxSelection: false, // 헤더 체크박스 disable
    checkboxSelection: true,
    width: 70,
    pinned: true,
  },
  {
    headerName: "공부 일자",
    field: "day",
    cellStyle: { color: "gray", fontWeight: "bold" },
    width: 100,
    pinned: true,
  },
  {
    headerName: "완료 여부",
    field: "complete",
    // Priamry: #1867C0
    cellStyle: { color: "#B00020", fontWeight: "bold" },
    width: 100,
    pinned: true,
  },

  {
    headerName: "당일",
    field: "zero",
    width: 150,
    editable: true,
    cellEditor: "agSelectCellEditor",
    cellEditorParams: {
      values: [
        "",
        "일상 생활",
        "요리",
        "일상 1",
        "일상 2",
        "개인",
        "신체",
        "취미 1",
        "취미 2",
        "학교 생활 1",
        "학교 생활 2",
        "학교 생활 3",
        "대인 관계 1",
        "대인 관계 2",
        "편의 시설",
        "회사 생활",
        "행사",
        "진로",
        "동식물 1",
        "동식물 2",
        "기후 / 지형",
      ],
    },
  },
  { headerName: "1일전", field: "one", width: 150 },
  { headerName: "3일전", field: "three", width: 150 },
  { headerName: "6일전", field: "six", width: 150 },
  { headerName: "13일전", field: "thirteen", width: 150 },
];

const rowData = getTempRowData();

function getTempRowData() {
  const rowData = [];
  for (let i = 1; i <= 30; i++) {
    rowData.push({
      complete: "미학습",
      day: "Day " + i,
      zero: "",
      one: "",
      three: "",
      six: "",
      thirteen: "",
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
