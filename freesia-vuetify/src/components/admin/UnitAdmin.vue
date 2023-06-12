<template>
  <v-card flat>
    <v-card-text>
      <v-table class="table-container">
        <thead>
          <tr>
            <th style="width: 75%">Subject</th>
            <th style="width: 25%">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="unit in unitPage.content" :key="unit.id">
            <td>
              <v-badge :content="unit.wordCount" floating color="white">
                <a href="#" class="text-info text-decoration-none">
                  {{ unit.subject }}
                </a>
              </v-badge>
            </td>

            <td>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-trash-can-outline"
                @click="() => openDeleteDialog(unit.id)"
              />
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="() => openUpdateDialog(unit.id)"
              />
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
            @update:model-value="handlePageChange"
          />
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
        :disabled="selectedVocabulary.title === 'No data available'"
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
        />
      </template>
      <v-card-text class="mt-5">
        <v-text-field
          readonly
          label="Subject"
          v-model="unitDeleteFormData.subject"
        />
        <v-text-field
          readonly
          label="Word Count"
          v-model="unitDeleteFormData.wordCount"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%" @click="onClickDeleteButton">
          DELETE
        </v-btn>
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
          label="Subject"
          append-inner-icon="mdi-file-document-edit-outline"
          v-model="unitUpdateFormData.subject"
        />
        <v-text-field
          label="Word Count"
          v-model="unitUpdateFormData.wordCount"
          readonly
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="info" style="width: 48%" @click="onClickUpdateButton">
          UPDATE
        </v-btn>
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
import { reactive, ref, onMounted, watch } from "vue";
import { useThemeStore } from "@/stores/theme";
import { unitService } from "@/service/unitService";
import { useAdminHomeStore } from "@/stores/adminHome";
import { useCommonStore } from "@/stores/common";
import { storeToRefs } from "pinia";

const themeStore = useThemeStore();
const adminHomeStore = useAdminHomeStore();
const commonStore = useCommonStore();

const { VDialogMessage } = commonStore;

const { selectedVocabulary, unitPage } = storeToRefs(adminHomeStore);

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

// 단위 삭제 FormData
const unitDeleteFormData = reactive({
  id: null,
  subject: null,
  wordCount: 0,
});

// 단위 수정 FormData
const unitUpdateFormData = reactive({
  id: null,
  subject: null,
  wordCount: 0,
});

// Pagination PageChange 이벤트 핸들러
const handlePageChange = async (pageNumber) => {
  unitPage.value = await unitService.searchUnitResponsePage(
    selectedVocabulary.value.value,
    {
      page: pageNumber - 1,
      size: 3,
    }
  );
};

// 단위 등록 버튼 클릭 이벤트
const onClickAddButton = async () => {
  try {
    await unitService.registerUnit(
      selectedVocabulary.value.value,
      unitAddFormData
    );

    // 추가된 상태의 최신 데이터 갱신
    unitPage.value = await unitService.searchUnitResponsePage(
      selectedVocabulary.value.value
    );

    Object.assign(unitAddFormData, {
      subject: null,
    });

    unitDialogControl.showAddDialog = false;
    currentPage.value = 1;

    setTimeout(() => {
      VDialogMessage("단위 등록을 완료했습니다.");
    }, 200);
  } catch (err) {
    console.error(err);

    setTimeout(() => {
      VDialogMessage("단위 등록을 실패했습니다.");
    }, 200);
  }
};

// 단위 삭제 다이얼로그 Open
const openDeleteDialog = async (unitId) => {
  const unit = await unitService.searchOneUnitResponse(unitId);
  Object.assign(unitDeleteFormData, {
    id: unit.id,
    subject: unit.subject,
    wordCount: 0, // Todo: 나중에 추가 구현 필요
  });
  unitDialogControl.showDeleteDialog = true;
};

// 단위 삭제 버튼 클릭 이벤트
const onClickDeleteButton = async () => {
  try {
    await unitService.deleteUnit(unitDeleteFormData.id);

    Object.assign(unitDeleteFormData, {
      id: null,
      subject: null,
      wordCount: 0,
    });

    unitDialogControl.showDeleteDialog = false;

    // 삭제나 수정한 단어장의 페이지를 조회해 본다.
    unitPage.value = await unitService.searchUnitResponsePage(
      selectedVocabulary.value.value,
      {
        page: currentPage.value - 1,
        size: 3,
      }
    );

    // 조회해본 페이지가 데이터가 없으면
    if (unitPage.value.content.length <= 0) {
      // 앞 페이지로 이동하고, 조회해본 페이지가 1페이지 라면 앞 페이지가 없으므로 1페이지로 설정한다.
      currentPage.value = currentPage.value === 1 ? 1 : currentPage.value - 1;
    }

    // 조회해본 페이지가 데이터가 있으면 그대로 조회
    await handlePageChange(currentPage.value);

    setTimeout(() => {
      VDialogMessage("단위 삭제를 완료했습니다.");
    }, 200);
  } catch (err) {
    console.error(err);
    setTimeout(() => {
      VDialogMessage("단위 삭제를 실패했습니다.");
    }, 200);
  }
};

// 단위 수정 다이얼로그 Open
const openUpdateDialog = async (unitId) => {
  const unit = await unitService.searchOneUnitResponse(unitId);

  Object.assign(unitUpdateFormData, {
    id: unit.id,
    subject: unit.subject,
    wordCount: 0,
  });

  unitDialogControl.showUpdateDialog = true;
};

// 단위 수정 버튼 클릭 이벤트
const onClickUpdateButton = async () => {
  try {
    await unitService.modifyUnit(unitUpdateFormData.id, unitUpdateFormData);

    Object.assign(unitUpdateFormData, {
      id: null,
      subject: null,
      wordCount: 0,
    });

    unitDialogControl.showUpdateDialog = false;

    unitPage.value = await unitService.searchUnitResponsePage(
      selectedVocabulary.value.value
    );

    setTimeout(() => {
      VDialogMessage("단위 수정을 완료했습니다.");
    }, 200);
  } catch (err) {
    console.error(err);

    setTimeout(() => {
      VDialogMessage("단위 수정을 실패했습니다.");
    }, 200);
  }
};

// watch
watch(
  () => selectedVocabulary.value.value,
  async () => {
    // Unit Table Rendering...
    const selectedVocabularyId = selectedVocabulary.value.value;
    Object.assign(
      unitPage.value,
      await unitService.searchUnitResponsePage(selectedVocabularyId, {
        page: 0,
        size: 3,
      })
    );
    currentPage.value = 1;
  }
);

onMounted(async () => {});
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
