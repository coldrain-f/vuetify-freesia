<template>
  <v-card flat>
    <v-card-text>
      <v-table class="table-container">
        <thead>
          <tr>
            <th style="width: 75%">Title</th>
            <th style="width: 25%">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="unit in unitPage.content" :key="unit.id">
            <td>{{ unit.subject }}</td>
            <td>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-trash-can-outline"
                @click="() => (unitDialogControl.showDeleteDialog = true)"
              ></v-btn>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="() => (unitDialogControl.showUpdateDialog = true)"
              ></v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>

      <v-row>
        <v-col cols="12">
          <v-pagination
            :length="unitPage.totalPages"
            v-model="currentPage"
            rounded="circle"
          ></v-pagination>
        </v-col>
      </v-row>
      <v-divider></v-divider>
    </v-card-text>
    <v-card-actions class="mt-5">
      <v-btn
        variant="flat"
        size="default"
        class="w-100"
        :color="themeStore.theme"
        @click="() => (unitDialogControl.showAddDialog = true)"
        :disabled="!selectedVocabularyId"
      >
        <v-icon start icon="mdi-plus"></v-icon>
        Add item
      </v-btn>
    </v-card-actions>
  </v-card>

  <!-- 단위 삭제 다이얼로그 -->
  <v-dialog v-model="unitDialogControl.showDeleteDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-error"> ※ 단위 삭제 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (unitDialogControl.showDeleteDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field readonly label="Title" model-value="Unit 01 - 일상1" />
        <v-text-field readonly label="Subword" model-value="40개" />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%"> DELETE </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (unitDialogControl.showDeleteDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단위 수정 다이얼로그 -->
  <v-dialog v-model="unitDialogControl.showUpdateDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-info"> ※ 단위 수정 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (unitDialogControl.showUpdateDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field
          label="Title"
          append-inner-icon="mdi-file-document-edit-outline"
          model-value="Unit 01 - 일상1"
        />
        <v-text-field label="Subword" model-value="40개" readonly />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="info" style="width: 48%"> UPDATE </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (unitDialogControl.showUpdateDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단위 등록 다이얼로그 -->
  <v-dialog v-model="unitDialogControl.showAddDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-primary"> ※ 단위 등록 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (unitDialogControl.showAddDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field label="Subject" v-model="unitAddFormData.subject" />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="primary" style="width: 48%" @click="onClickAddButton">
          ADD
        </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (unitDialogControl.showAddDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { useThemeStore } from "@/stores/theme";
import { unitService } from "@/service/unitService";
import { useAdminHomeStore } from "@/stores/adminHome";
import { storeToRefs } from "pinia";

const themeStore = useThemeStore();
const adminHomeStore = useAdminHomeStore();

const { selectedVocabularyId } = storeToRefs(adminHomeStore);

// Unit Pageable
const unitPage = ref({});

// Pagination Page
const currentPage = ref(1);

// Unit 다이얼로그 컨트롤
const unitDialogControl = reactive({
  showDeleteDialog: false,
  showUpdateDialog: false,
  showAddDialog: false,
});

//  단위 등록 FormData
const unitAddFormData = reactive({
  subject: null,
});

// 단위 등록 버튼 클릭 이벤트
const onClickAddButton = async () => {
  const savedId = await unitService.registerUnit(
    selectedVocabularyId.value,
    unitAddFormData
  );

  // 추가된 상태의 최신 데이터 갱신
  unitPage.value = await unitService.searchUnitResponsePage(
    selectedVocabularyId.value
  );

  Object.assign(unitAddFormData, {
    subject: null,
  });

  unitDialogControl.showAddDialog = false;
  currentPage.value = 1;

  console.log(`saved unit = ${savedId}`);
};

onMounted(async () => {
  if (!selectedVocabularyId.value) {
    unitPage.value.content = [];
    return;
  }
  unitPage.value = await unitService.searchUnitResponsePage(
    selectedVocabularyId.value
  );
});
</script>

<style>
.table-container {
  /* 가로, 세로 스크롤 생성 */
  overflow: auto;
  /* 테이블 데이터의 줄 바꿈 제거 */
  white-space: nowrap;

  height: 215px;
  width: 100%;
}
</style>
