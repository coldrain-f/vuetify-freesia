<template>
  <v-container>
    <v-row>
      <v-col cols="5">
        <v-select
          label="Language"
          variant="underlined"
          density="compact"
          v-model="languageSelectManager.selectedItem"
          :items="languageSelectManager.items"
          item-title="name"
          item-value="name"
          hide-details
          @update:model-value="
            (changedLanguage) => emit('handleLanguageChange', changedLanguage)
          "
        >
        </v-select>
      </v-col>
      <v-col cols="7">
        <v-select
          label="Voca"
          variant="underlined"
          density="compact"
          v-model="vocabularySelectManager.selectedItem"
          :items="vocabularySelectManager.items"
          item-title="title"
          item-value="id"
          return-object
          hide-details
          @update:model-value="
            (changedVocabulary) =>
              emit('handleVocabularyChange', changedVocabulary)
          "
        >
        </v-select>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-select
          label="Unit"
          variant="underlined"
          density="compact"
          v-model="unitSelectManager.selectedItem"
          :items="unitSelectManager.items"
          item-title="subject"
          item-value="id"
          return-object
          hide-details
        >
        </v-select>
      </v-col>
    </v-row>
    <v-row class="mt-5">
      <v-col cols="4">
        <v-btn size="small" class="w-100" @click="performSearch">
          <v-icon start icon="mdi-note-search-outline" class="margin-top-1px">
          </v-icon>
          SEARCH
        </v-btn>
      </v-col>
      <v-col cols="8" class="text-end">
        <v-btn
          size="small"
          color="primary"
          :disabled="!isSearchPerformed"
          @click="showWordAddDialog = true"
        >
          <v-icon start icon="mdi-note-plus-outline"> </v-icon>
          ADD
        </v-btn>
        <v-btn
          size="small"
          color="info"
          class="ml-2"
          :disabled="isEmptyObject(selectedWord) || !isSearchPerformed"
          @click="showWordUpdateDialog = true"
        >
          <v-icon start icon="mdi-note-edit-outline"> </v-icon>
          UPDATE
        </v-btn>
        <v-btn
          size="small"
          color="error"
          class="ml-2"
          :disabled="isEmptyObject(selectedWord) || !isSearchPerformed"
          @click="showWordDeleteDialog = true"
        >
          <v-icon start icon="mdi-note-remove-outline"> </v-icon>
          DELETE
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <div :class="`text-${themeStore.theme}`" v-if="isSearchPerformed">
          <p class="noto-sans">
            <span class="me-2">【 </span>
            <span> {{ searchedLanguage }} </span>
            <span class="ms-2"> 】</span>
            <span class="ms-2 me-2"> 》</span>
            <span class="me-2">【 </span>
            <span class="me-2">{{ searchedVocabulary.title }}</span>
            <span> 】</span>
          </p>
          <p class="noto-sans">
            <span class="ms-1 me-2">▣</span>
            <span> {{ searchedUnit.subject }}</span>
          </p>
        </div>
        <ag-grid-vue
          style="width: 100%; height: 312px"
          class="ag-theme-alpine noto-sans"
          :columnDefs="columnDefs"
          :defaultColDef="defaultColDef"
          :rowData="rowData"
          :pagination="true"
          :paginationPageSize="5"
          rowSelection="single"
          @selectionChanged="onSelectionChanged"
          @grid-ready="onGridReady"
        >
        </ag-grid-vue>
      </v-col>
    </v-row>

    <!-- 단어 등록 다이얼로그 -->
    <AdminWordGridAddDialog v-model="showWordAddDialog" />

    <!-- 단어 수정 다이얼로그 -->
    <AdminWordGridUpdateDialog
      v-model="showWordUpdateDialog"
      :selectedWord="selectedWord"
    />

    <!-- 단어 삭제 다이얼로그 -->
    <AdminWordGridDeleteDialog
      v-model="showWordDeleteDialog"
      :selectedWord="selectedWord"
    />
  </v-container>
</template>

<script setup>
import AdminWordGridAddDialog from "./AdminWordGridAddDialog.vue";
import AdminWordGridUpdateDialog from "./AdminWordGridUpdateDialog.vue";
import AdminWordGridDeleteDialog from "./AdminWordGridDeleteDialog.vue";

// AG Grid Vue
import { commonUtils } from "@/common/commonUtils";
import { useThemeStore } from "@/stores/theme";
import { AgGridVue } from "ag-grid-vue3";
import { inject, ref, toRefs } from "vue";
import { wordService } from "@/service/wordService";

// Utils
const { isEmptyObject } = commonUtils;

const themeStore = useThemeStore();

const emit = defineEmits(["handleLanguageChange", "handleVocabularyChange"]);

const wordGridManager = inject("wordGridManager");

const languageSelectManager = inject("languageSelectManager");
const vocabularySelectManager = inject("vocabularySelectManager");
const unitSelectManager = inject("unitSelectManager");

// readonly
const {
  isSearchPerformed,
  rowData,
  selectedWord,
  searchedLanguage,
  searchedVocabulary,
  searchedUnit,
} = toRefs(wordGridManager);

const showWordAddDialog = ref(false);
const showWordUpdateDialog = ref(false);
const showWordDeleteDialog = ref(false);

const performSearch = () => {
  fetchData(unitSelectManager.selectedItem.id);

  wordGridManager.searchedLanguage = languageSelectManager.selectedItem;

  wordGridManager.searchedVocabulary.id =
    vocabularySelectManager.selectedItem.id;

  wordGridManager.searchedVocabulary.title =
    vocabularySelectManager.selectedItem.title;

  wordGridManager.searchedUnit.id = unitSelectManager.selectedItem.id;
  wordGridManager.searchedUnit.subject = unitSelectManager.selectedItem.subject;

  isSearchPerformed.value = true;
};

const onSelectionChanged = (e) => {
  // 체크했다가 풀었을 경우엔 초기화 처리
  if (e.api.getSelectedNodes().length == 0) {
    wordGridManager.selectedWord = {};
    return;
  }

  const selectedNodes = e.api.getSelectedNodes();

  // 단일 선택 이므로 항상 0번 Index에만 데이터가 있음.
  const selectedData = selectedNodes.map((node) => node.data)[0];

  Object.assign(wordGridManager.selectedWord, {
    studyWord: selectedData.studyWord,
    nativeWord: selectedData.nativeWord,
    partOfSpeech: selectedData.partOfSpeech,
    incorrectCount: selectedData.incorrectCount,
    correctCount: selectedData.correctCount,
  });
};

const gridApi = ref(null);

const onGridReady = (params) => {
  gridApi.value = params.api;
};

const defaultColDef = {
  resizable: true,
  sortable: true,
  filter: true,
  floatingFilter: false,
};

const columnDefs = [
  {
    headerName: "선택",
    width: 70,
    pinned: "right",
    headerCheckboxSelection: false, // 헤더 체크박스 disable
    checkboxSelection: true,
    resizable: false,
    sortable: false,
    filter: false,
  },
  {
    headerName: "#",
    width: 70,
    filter: false,
    resizable: false,
    valueGetter: (params) => {
      return rowData.value.length - params.node.rowIndex;
    },
  },
  {
    headerName: "학습 단어",
    field: "studyWord",
    width: 155,
  },
  {
    headerName: "모국 단어",
    field: "nativeWord",
    width: 155,
  },
  {
    headerName: "품사",
    field: "partOfSpeech",
    width: 130,
  },
  {
    headerName: "등록일",
    field: "createdAt",
    width: 160,
  },
  {
    headerName: "수정일",
    field: "modifiedAt",
    width: 160,
  },
];

const fetchData = async (unitId) => {
  const pageableParams = { page: 0, size: 2000 };
  const unitPageable = await wordService.getPageable(unitId, pageableParams);

  wordGridManager.rowData = unitPageable.content;
  wordGridManager.selectedUnit = {};
};
</script>

<style scoped></style>
