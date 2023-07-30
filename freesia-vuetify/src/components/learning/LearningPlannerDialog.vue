<template>
  <v-dialog v-model="showDialog" width="1200">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 플래너 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="showDialog = false"
          :disabled="isEditMode"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-row>
          <v-col cols="2">
            <v-select
              label="Language"
              variant="underlined"
              :model-value="props.languageSelect.selectedItem"
              :items="props.languageSelect.items"
              item-title="name"
              item-value="name"
              @update:model-value="
                (changedLanguage) =>
                  emit('handleLanguageChange', changedLanguage)
              "
              :readonly="isEditMode"
            >
            </v-select>
          </v-col>
          <v-col cols="3">
            <v-select
              label="Voca"
              variant="underlined"
              :model-value="props.vocabularySelect.selectedItem"
              :items="props.vocabularySelect.items"
              item-title="title"
              item-value="id"
              return-object
              @update:model-value="
                (changedVocabulary) =>
                  emit('handleVocabularyChange', changedVocabulary)
              "
              :readonly="isEditMode"
            >
            </v-select>
          </v-col>
          <v-col cols="2">
            <v-btn
              class="mt-3"
              size="large"
              density="compact"
              icon="mdi-magnify"
              variant="text"
              :disabled="isEditMode"
              @click="performSearch"
            >
            </v-btn>
          </v-col>
          <v-col cols="5" class="text-end">
            <v-btn
              class="mt-4 mr-2"
              size="small"
              variant="flat"
              color="info"
              :disabled="isEditMode || !isSearchPerformed"
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
              @click="onSave"
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
              class="mt-4 mr-2 border"
              size="small"
              variant="flat"
              :disabled="!isEditMode"
              v-if="isEditMode"
              @click="onCancel"
            >
              <v-icon start icon="mdi-cancel"></v-icon>
              CANCEL
            </v-btn>
            <v-btn
              class="mt-4"
              size="small"
              variant="flat"
              color="success"
              v-if="!isEditMode"
              :disabled="!isSearchPerformed"
            >
              <v-icon start icon="mdi-file-export-outline"></v-icon>
              EXPORT CSV
            </v-btn>
          </v-col>
        </v-row>

        <div
          :class="`text-${themeStore.theme} mb-1`"
          v-if="isSearchPerformed"
          style="cursor: default"
        >
          <p>
            <span class="me-2">【 </span>
            <span class="noto-sans"> {{ searchedResult.language }} </span>
            <span class="ms-2"> 】</span>
            <span class="ms-2 me-2"> 》</span>
            <span class="me-2">【 </span>
            <span class="me-2 noto-sans">
              {{ searchedResult.vocabularyTitle }}
            </span>
            <span> 】</span>
          </p>
        </div>
        <ag-grid-vue
          style="width: 100%; height: 450px"
          class="ag-theme-alpine noto-sans"
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
            <v-btn
              class="ml-4 mt-1 border"
              size="small"
              :disabled="isEditMode"
              @click="showLearningOptionDialog = true"
            >
              <v-icon start icon="mdi-cog-outline"></v-icon>
              LEARNING OPTION
            </v-btn>
          </v-col>
          <v-col class="text-end" cols="9" style="padding: 0">
            <v-btn
              class="mr-5"
              color="primary"
              :disabled="isEditMode"
              @click="onLearnButtonClicked"
            >
              <v-icon start icon="mdi-school-outline"></v-icon>
              LEARN
            </v-btn>
            <v-btn class="mr-5" color="error" :disabled="isEditMode">
              <v-icon start icon="mdi-school-outline"></v-icon>
              EXAM
            </v-btn>
            <v-btn
              class="mr-5"
              @click="showDialog = false"
              :disabled="isEditMode"
            >
              <v-icon start icon="mdi-close"></v-icon>
              CLOSE
            </v-btn>
          </v-col>
        </v-row>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 플래너 등록 다이얼로그 -->
  <v-dialog v-model="showPlannerCreateDialog" width="350">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 플래너 생성 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="showPlannerCreateDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="noto-sans">
        생성된 학습 플래너가 없습니다. 새로 생성하시겠습니까?
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary" @click="onConfirmClick"> CONFIRM </v-btn>
        <v-btn @click="showPlannerCreateDialog = false" class="me-4">
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 학습 옵션 다이얼로그 -->
  <v-dialog v-model="showLearningOptionDialog" width="500">
    <v-card>
      <template #title>
        <span class="text-primary noto-sans"> ※ 학습 옵션 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="showLearningOptionDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="noto-sans">
        <v-radio-group v-model="learningStore.learningStyle" inline>
          <template v-slot:label>
            <span>
              <strong class="font-weight-medium"> 문제 학습 스타일 </strong>
            </span>
          </template>
          <v-radio label="학습 단어 출제" value="studyWord" color="primary">
          </v-radio>
          <v-radio
            label="모국 단어 출제"
            value="nativeWord"
            color="primary"
            class="ms-10"
          >
          </v-radio>
        </v-radio-group>
        <v-radio-group v-model="learningStore.useLearning" inline>
          <template v-slot:label>
            <span>
              <strong class="font-weight-medium">
                1/4/7/14 학습법 적용 여부
              </strong>
            </span>
          </template>
          <v-radio label="적용" :value="true" color="primary"> </v-radio>
          <v-radio label="미적용" :value="false" color="primary" class="ms-10">
          </v-radio>
        </v-radio-group>
        <v-select
          label="TTS"
          variant="underlined"
          v-model="synthStore.synthDefaultVoiceName"
          :items="synthStore.synthVoiceNames"
        >
        </v-select>
        <v-text-field label="TTS Text" variant="underlined" v-model="ttsText">
          <template v-slot:append-inner>
            <v-tooltip text="발음 듣기" location="top">
              <template v-slot:activator="{ props }">
                <v-btn
                  variant="flat"
                  icon="mdi-volume-high"
                  size="small"
                  @click="synthStore.speakText(ttsText)"
                  v-bind="props"
                >
                </v-btn>
              </template>
            </v-tooltip>
          </template>
        </v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary" @click="showLearningOptionDialog = false">
          APPLY
        </v-btn>
        <v-btn @click="showLearningOptionDialog = false" class="me-4">
          CANCEL
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

// Pinia
import { useLearningStore } from "@/stores/learning";

// Vue
import { computed, onMounted, reactive, ref, watch } from "vue";

// Components
import { storeToRefs } from "pinia";
import { languageService } from "@/service/languageService";
import { PlannerService } from "@/service/plannerService";

import { useCommonMessageDialogStore } from "@/stores/commonMessageDialog";
import { useThemeStore } from "@/stores/theme";
import { useSpeechSynthesisStore } from "@/stores/speechSynthesis";

const themeStore = useThemeStore();
const synthStore = useSpeechSynthesisStore();

const commonMessageDialogStore = useCommonMessageDialogStore();
const { showCommonMessageDialog } = commonMessageDialogStore;

const plannerService = new PlannerService();

const learningStore = useLearningStore();
const { isLearningStarted } = storeToRefs(learningStore);

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  languageSelect: {
    type: Object,
  },
  vocabularySelect: {
    type: Object,
  },
});

const emit = defineEmits([
  "update:modelValue",
  "handleLanguageChange",
  "handleVocabularyChange",
  "handleClose",
]);

const ttsText = ref("");

const showPlannerCreateDialog = ref(false);

const showLearningOptionDialog = ref(false);

const searchedResult = reactive({
  language: "",
  vocabularyTitle: "",
});

// 왜 이렇게 해줘야하지만 v-dialog를 사용할 수 있는지 정리 필요.
const showDialog = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const performSearch = async () => {
  const vocabularyId = props.vocabularySelect.selectedItem.id;
  const isDuplicate = await plannerService.checkDuplicate(vocabularyId);

  if (isDuplicate) {
    // Load planner details...
    rowData.value = await plannerService.findAllByVocabularyId(vocabularyId);
    searchedResult.language = props.languageSelect.selectedItem;
    searchedResult.vocabularyTitle = props.vocabularySelect.selectedItem.title;
    isSearchPerformed.value = true;
    return;
  }

  showPlannerCreateDialog.value = true;
};

const onSave = async () => {
  const vocabularyId = props.vocabularySelect.selectedItem.id;
  try {
    await plannerService.bulkUpdate(rowData.value);
    isEditMode.value = false;
    rowData.value = await plannerService.findAllByVocabularyId(vocabularyId);
    showCommonMessageDialog("플래너 저장을 완료했습니다.");
  } catch (err) {
    console.error(err);
    showCommonMessageDialog("플래너 저장을 실패했습니다.");
  }
};

const onCancel = async () => {
  const vocabularyId = props.vocabularySelect.selectedItem.id;
  isEditMode.value = false;
  rowData.value = await plannerService.findAllByVocabularyId(vocabularyId);
};

const onLearnButtonClicked = () => {
  showDialog.value = false;
  isLearningStarted.value = true;
};

const onConfirmClick = async () => {
  const vocabularyId = props.vocabularySelect.selectedItem.id;
  try {
    await plannerService.register(vocabularyId);
    showPlannerCreateDialog.value = false;
    isSearchPerformed.value = true;
    searchedResult.language = props.languageSelect.selectedItem;
    searchedResult.vocabularyTitle = props.vocabularySelect.selectedItem.title;
    showCommonMessageDialog("플래너 생성을 완료했습니다.");

    rowData.value = await plannerService.findAllByVocabularyId(vocabularyId);
  } catch (err) {
    console.error(err);
    showCommonMessageDialog("플래너 생성을 실패했습니다.");
  }
};

const languageItems = ref([]);

const isSearchPerformed = ref(false);

const isEditMode = ref(false);

const gridApi = ref(null);
const gridColumnApi = ref(null);

const clearLearningPlannerDialog = () => {
  emit("handleClose");
  searchedResult.language = "";
  searchedResult.vocabularyTitle = "";
  isSearchPerformed.value = false;
  isEditMode.value = false;
  rowData.value = [];
};

watch(showDialog, () => {
  // 플래너 다이얼로그 close 시 데이터 초기화
  clearLearningPlannerDialog();
});

watch(isEditMode, () => {
  // 편집모드 인 경우 선택 컬럼 hide
  if (isEditMode.value) {
    gridColumnApi.value.applyColumnState({
      state: [
        { colId: "select", hide: true },
        { colId: "today", width: 190 },
        { colId: "oneDayPrior", width: 190 },
        { colId: "threeDaysPrior", width: 190 },
        { colId: "sixDaysPrior", width: 190 },
        { colId: "thirteenDaysPrior", width: 190 },
      ],
    });

    return;
  }
  gridColumnApi.value.applyColumnState({
    state: [
      { colId: "select", hide: false },
      { colId: "today", width: 176 },
      { colId: "oneDayPrior", width: 176 },
      { colId: "threeDaysPrior", width: 176 },
      { colId: "sixDaysPrior", width: 176 },
      { colId: "thirteenDaysPrior", width: 176 },
    ],
  });
});

const onGridReady = (params) => {
  gridApi.value = params.api;
  gridColumnApi.value = params.columnApi;
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
    headerName: "ID",
    field: "id",
    width: 70,
    editable: false,
    resizable: false,
    filter: false,
    hide: true,
  },
  {
    headerName: "선택",
    field: "select",
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
    cellStyle: (params) => {
      if (params.value == "Ongoing") {
        return {
          color: "#B00020",
          fontWeight: "bold",
        };
      } else if (params.value == "Finished") {
        return {
          color: "#1867C0",
          fontWeight: "bold",
        };
      }
      return { fontWeight: "bold" };
    },
    cellRenderer: (params) => {
      if (params.value == "Ongoing") {
        return "미학습";
      } else if (params.value == "Finished") {
        return "학습 완료";
      }
      return params.value;
    },
    width: 100,
    pinned: true,
  },

  {
    headerName: "당일 (Editable)",
    field: "today",
    width: 176,
    editable: (rowNode) => {
      const learningStatus = rowNode.data.learningStatus;
      if (!isEditMode.value) {
        return false;
      }
      if (learningStatus == "Finished") {
        return false;
      } else if (learningStatus == "Ongoing") {
        return true;
      }
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

onMounted(async () => {
  languageItems.value = await languageService.findAll();
});
</script>

<style scoped></style>
