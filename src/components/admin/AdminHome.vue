<template>
  <v-card flat>
    <v-card-text>
      <v-select
        label="Language"
        variant="underlined"
        v-model="selectedAdminCategory"
        :items="adminCategories"
        chips
      />
      <v-table style="overflow-x: auto">
        <thead>
          <tr>
            <th>#</th>
            <th>Title</th>
            <th>Language</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="vocabulary in vocabularyList" :key="vocabulary.id">
            <td style="width: 5%">{{ vocabulary.id }}</td>
            <td style="width: 50%">{{ vocabulary.title }}</td>
            <td style="width: 20%">{{ vocabulary.language }}</td>
            <td style="width: 25%">
              <v-btn
                variant="text"
                size="small"
                icon="mdi-trash-can-outline"
                @click="vocaDialogControl.showDeleteDialog = true"
              ></v-btn>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="vocaDialogControl.showUpdateDialog = true"
              ></v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>
      <v-divider></v-divider>
    </v-card-text>
    <v-row>
      <v-col cols="12">
        <v-pagination :length="10" rounded="circle"></v-pagination>
      </v-col>
    </v-row>
    <v-card-actions class="mt-5">
      <v-btn variant="flat" size="default" class="w-100" color="primary">
        <v-icon start icon="mdi-plus"></v-icon>
        Add item
      </v-btn>
    </v-card-actions>
  </v-card>

  <!-- 단어장 삭제 다이얼로그 -->
  <v-dialog v-model="vocaDialogControl.showDeleteDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans"> ※ 단어장 삭제 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="vocaDialogControl.showDeleteDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field readonly label="No" model-value="1"></v-text-field>
        <v-text-field
          readonly
          label="Title"
          model-value="단어가 읽기다 기본편"
        ></v-text-field>
        <v-text-field
          readonly
          label="Language"
          model-value="English"
        ></v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%"> DELETE </v-btn>
        <v-btn
          style="width: 48%"
          @click="vocaDialogControl.showDeleteDialog = false"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단어장 수정 다이얼로그 -->
  <v-dialog v-model="vocaDialogControl.showUpdateDialog" width="500">
    <v-card>
      <v-card-text>Hello World!</v-card-text>
      <v-card-actions>
        <v-btn
          color="primary"
          block
          @click="vocaDialogControl.showUpdateDialog = false"
        >
          Close Dialog
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { reactive, ref } from "vue";

// Todo
// 1. 단어장 삭제 다이얼로그에 들어갈 내용 좀 더 고민 필요.
// 2. 단어장, 유닛, 단어를 컴포넌트로 분리하여 관리할지 고민 필요.
// 3. 다이얼로그 마다를 컴포넌트로 분리하여 관리할지 고민 필요.

// 단어장 다이얼로그 컨트롤
const vocaDialogControl = reactive({
  showDeleteDialog: false,
  showUpdateDialog: false,
});

const adminCategories = ref(["Vocabulary", "Unit", "Word"]);

const selectedAdminCategory = ref("Vocabulary");

const vocabularyList = ref([
  { id: 3, title: "단어가 읽기다 기본편", language: "English" },
  { id: 2, title: "단어가 읽기다 실력편", language: "English" },
  { id: 1, title: "JLPT N5", language: "Japanese" },
]);
</script>

<!-- 공통 스타일로 분리 예정  -->
<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");

.noto-sans {
  font-family: "Noto Sans KR", sans-serif;
}
</style>
