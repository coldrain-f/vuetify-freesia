<template>
  <v-container>
    <v-row>
      <v-col cols="7">
        <v-select
          label="Voca"
          variant="underlined"
          density="compact"
          :modelValue="{ title: '단어가 읽기다 기본편', vocabularyId: 1 }"
          :items="[
            { title: '단어가 읽기다 기본편', vocabularyId: 1 },
            { title: '단어가 읽기다 실력편', vocabularyId: 2 },
          ]"
          item-title="title"
          item-value="vocabularyId"
          hide-details
          return-object
          persistent-hint
        >
        </v-select>
      </v-col>
      <v-col cols="5">
        <v-select
          label="Unit"
          variant="underlined"
          density="compact"
          :modelValue="{ subject: '요리', unitId: 1 }"
          :items="[
            { subject: '요리', unitId: 1 },
            { subject: '일상 1', unitId: 2 },
          ]"
          item-title="subject"
          item-value="unitId"
          hide-details
          return-object
          persistent-hint
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
        <v-btn size="small" color="primary" :disabled="!isSearchPerformed">
          <v-icon start icon="mdi-note-plus-outline"> </v-icon>
          ADD
        </v-btn>
        <v-btn
          size="small"
          color="info"
          class="ml-2"
          :disabled="isEmptyObject(selectedWord) || !isSearchPerformed"
        >
          <v-icon start icon="mdi-note-edit-outline"> </v-icon>
          UPDATE
        </v-btn>
        <v-btn
          size="small"
          color="error"
          class="ml-2"
          :disabled="isEmptyObject(selectedWord) || !isSearchPerformed"
        >
          <v-icon start icon="mdi-note-remove-outline"> </v-icon>
          DELETE
        </v-btn>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <h4 :class="`text-${themeStore.theme}`">
          【 단어가 읽기다 기본편 】<span class="ms-2 me-2">》</span>【 요리 】
          <span>- 3회독 √</span>
        </h4>
        <ag-grid-vue
          style="width: 100%; height: 310px"
          class="ag-theme-alpine"
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
  </v-container>
</template>

<script setup>
// AG Grid Vue
import { commonUtils } from "@/common/commonUtils";
import { useThemeStore } from "@/stores/theme";
import { AgGridVue } from "ag-grid-vue3";
import { inject, ref, toRefs } from "vue";

// Utils
const { isEmptyObject } = commonUtils;

const themeStore = useThemeStore();

const wordGridManager = inject("wordGridManager");

const { isSearchPerformed, rowData, selectedWord } = toRefs(wordGridManager);

const performSearch = () => {
  rowData.value = fetchData();
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
    field: "#",
    width: 70,
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
    headerName: "맞은 횟수",
    field: "correctCount",
    width: 116,
    cellRenderer: (params) => {
      return params.value + "회";
    },
  },
  {
    headerName: "틀린 횟수",
    field: "incorrectCount",
    width: 116,
    cellRenderer: (params) => {
      return params.value + "회";
    },
  },
  {
    headerName: "등록일",
    field: "createdAt",
    width: 130,
  },
  {
    headerName: "수정일",
    field: "modifiedAt",
    width: 130,
  },
];

const fetchData = () => {
  return [
    {
      studyWord: "spice",
      nativeWord: "양념",
      partOfSpeech: "명사",
      correctCount: 0,
      incorrectCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      studyWord: "hot",
      nativeWord: "뜨거운",
      partOfSpeech: "형용사",
      correctCount: 0,
      incorrectCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      studyWord: "add",
      nativeWord: "추가하다",
      partOfSpeech: "동사",
      correctCount: 0,
      incorrectCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      studyWord: "tea",
      nativeWord: "(마시는) 차",
      partOfSpeech: "명사",
      correctCount: 0,
      incorrectCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      studyWord: "pot",
      nativeWord: "주전자",
      partOfSpeech: "명사",
      correctCount: 0,
      incorrectCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
    {
      studyWord: "tea pot",
      nativeWord: "찻 주전자",
      partOfSpeech: "명사",
      correctCount: 0,
      incorrectCount: 0,
      createdAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
      modifiedAt: new Intl.DateTimeFormat("ko-KR").format(new Date()),
    },
  ];
};
</script>

<style scoped></style>
