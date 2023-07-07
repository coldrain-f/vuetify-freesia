<template>
  <v-dialog v-model="showDialog" width="1200">
    <v-card>
      <template #title>
        <span class="text-primary"> ※ Planner </span>
      </template>
      <template #append>
        <v-btn variant="text" icon="mdi-close" @click="showDialog = false">
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
              :readonly="isEditMode"
            />
          </v-col>
          <v-col cols="3">
            <v-select
              label="Voca"
              variant="underlined"
              :items="['단어가 읽기다 기본편', '단어가 읽기다 실력편']"
              model-value="단어가 읽기다 기본편"
              :readonly="isEditMode"
            />
          </v-col>
          <v-col cols="2">
            <v-btn
              class="mt-3"
              size="large"
              density="compact"
              icon="mdi-magnify"
              variant="text"
              :disabled="isEditMode"
              @click="showPlannerCreateDialog = true"
            >
            </v-btn>
          </v-col>
          <v-col cols="5" class="text-end">
            <v-btn
              class="mt-4 mr-2"
              size="small"
              variant="flat"
              color="info"
              :disabled="isEditMode"
              v-if="!isEditMode"
              @click="isEditMode = true"
            >
              <v-icon start icon="mdi-content-save-all-outline"></v-icon>
              EDIT
            </v-btn>
            <v-btn
              class="mt-4 mr-2"
              size="small"
              variant="flat"
              color="primary"
              :disabled="!isEditMode"
              v-if="isEditMode"
              @click="isEditMode = false"
            >
              <v-icon start icon="mdi-content-save-all-outline"></v-icon>
              SAVE
            </v-btn>
            <v-btn
              class="mt-4 mr-2"
              size="small"
              variant="flat"
              color="error"
              :disabled="!isEditMode"
              v-if="isEditMode"
            >
              <v-icon start icon="mdi-lock-reset"></v-icon>
              RESET
            </v-btn>
            <v-btn
              class="mt-4 mr-2"
              size="small"
              variant="flat"
              color="success"
              :disabled="!isEditMode"
              v-if="isEditMode"
              @click="isEditMode = false"
            >
              <v-icon start icon="mdi-cancel"></v-icon>
              CANCEL
            </v-btn>
            <v-btn
              class="mt-4 border"
              size="small"
              variant="flat"
              v-if="!isEditMode"
            >
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
          @grid-ready="onGridReady"
          @cellValueChanged="onCellValueChanged"
        >
        </ag-grid-vue>
      </v-card-text>
      <v-card-actions>
        <v-row style="margin: 0">
          <v-col cols="3" style="padding: 0">
            <v-btn class="ml-4 mt-1 border" variant="flat" size="small">
              <v-icon start icon="mdi-cog-outline"></v-icon>
              LEARNING OPTION
            </v-btn>
          </v-col>
          <v-col class="text-end" cols="9" style="padding: 0">
            <v-btn class="mr-5" color="primary" :disabled="isEditMode">
              <v-icon start icon="mdi-school-outline"></v-icon>
              LEARN
            </v-btn>
            <v-btn class="mr-5" color="error" :disabled="isEditMode">
              <v-icon start icon="mdi-school-outline"></v-icon>
              EXAM
            </v-btn>
            <v-btn class="mr-5" @click="showDialog = false">
              <v-icon start icon="mdi-close"></v-icon>
              CLOSE
            </v-btn>
          </v-col>
        </v-row>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 플래너 등록 다이얼로그 -->
  <LearningPlannerCreateDialog v-model="showPlannerCreateDialog" />
</template>

<script setup>
// AG Grid Vue
import { AgGridVue } from "ag-grid-vue3"; // the AG Grid Vue Component
import "ag-grid-community/styles/ag-grid.css"; // Core grid CSS, always needed
import "ag-grid-community/styles/ag-theme-alpine.css"; // Optional theme CSS

// Vue
import { computed, onMounted, ref } from "vue";

// Components
import LearningPlannerCreateDialog from "./LearningPlannerCreateDialog.vue";

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(["update:modelValue"]);

const showPlannerCreateDialog = ref(false);

// 왜 이렇게 해줘야하지만 v-dialog를 사용할 수 있는지 정리 필요.
const showDialog = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const isEditMode = ref(false);

const gridApi = ref(null);

const onGridReady = (params) => {
  gridApi.value = params.api;
};

const gridOptions = {
  singleClickEdit: false,
};

// 그리드 셀 변경 이벤트
const onCellValueChanged = (params) => {
  const { rowIndex, newValue } = params;

  setReviewCycle(rowIndex, newValue);
};

// 복습 주기 설정
const setReviewCycle = (rowIndex, newValue) => {
  const itemsToUpdate = [];

  gridApi.value.forEachNode((node, index) => {
    if (rowIndex + 1 == index) {
      node.data.oneDayPrior = newValue;
    } else if (rowIndex + 3 == index) {
      node.data.threeDaysPrior = newValue;
    } else if (rowIndex + 6 == index) {
      node.data.sixDaysPrior = newValue;
    } else if (rowIndex + 13 == index) {
      node.data.thirteenDaysPrior = newValue;
    }

    itemsToUpdate.push(node.data);
  });

  gridApi.value.applyTransaction({ update: itemsToUpdate });
};

// AG-Grid-Vue 컬럼 기본설정
const defaultColDef = ref({
  editable: false,
  cellDataType: false,
  resizable: true, // 헤더 길이 늘리기 허용
});

// AG-Grid-Vue 컬럼 설정
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
    field: "studyDay",
    cellStyle: {
      color: "gray",
      fontWeight: "bold",
    },
    width: 100,
    pinned: true,
  },
  {
    headerName: "완료 여부",
    field: "learningStatus",
    cellStyle: {
      color: "#B00020", // Priamry: #1867C0
      fontWeight: "bold",
    },
    width: 100,
    pinned: true,
  },

  {
    headerName: "당일",
    field: "today",
    width: 176,
    editable: () => {
      return isEditMode.value;
    },
    cellEditor: "agSelectCellEditor",
    cellEditorParams: {
      values: ["", "일상 생활", "요리", "일상 1", "일상 2"],
    },
  },
  { headerName: "1일전", field: "oneDayPrior", width: 176 },
  { headerName: "3일전", field: "threeDaysPrior", width: 176 },
  { headerName: "6일전", field: "sixDaysPrior", width: 176 },
  { headerName: "13일전", field: "thirteenDaysPrior", width: 176 },
];

// AG-Grid-Vue 데이터
const rowData = ref([]);

// AG-Grid-Vue 임시 데이터 생성 함수
const makeTempRowData = () => {
  const rowData = [];

  for (let i = 1; i <= 30; i++) {
    const data = {
      studyDay: "Day " + i,
      learningStatus: "미학습",
    };

    rowData.push(data);
  }

  return rowData;
};

onMounted(() => {
  rowData.value = makeTempRowData();
});
</script>

<style scoped></style>
