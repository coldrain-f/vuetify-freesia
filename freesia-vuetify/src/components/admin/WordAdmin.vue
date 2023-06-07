<template>
  <v-card flat>
    <v-card-text>
      <v-table style="overflow-x: auto">
        <thead>
          <tr>
            <th>Word</th>
            <th>Meaning</th>
            <th>POS</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="word in wordList" :key="word.id">
            <td style="width: 32.5%">{{ word.name }}</td>
            <td style="width: 32.5%">{{ word.meaning }}</td>
            <td style="width: 10%">{{ word.pos }}</td>
            <td style="width: 25%">
              <v-btn
                variant="text"
                size="small"
                icon="mdi-trash-can-outline"
                @click="() => (wordDialogControl.showDeleteDialog = true)"
              ></v-btn>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="() => (wordDialogControl.showUpdateDialog = true)"
              ></v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>

      <v-row>
        <v-col cols="12">
          <v-pagination :length="10" rounded="circle"></v-pagination>
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
        @click="() => (wordDialogControl.showAddDialog = true)"
      >
        <v-icon start icon="mdi-plus"></v-icon>
        Add item
      </v-btn>
    </v-card-actions>
  </v-card>

  <!-- 단어 삭제 다이얼로그 -->
  <v-dialog v-model="wordDialogControl.showDeleteDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-error"> ※ 단어 삭제 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (wordDialogControl.showDeleteDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field readonly label="Word" model-value="spice" />
        <v-text-field readonly label="Meaning" model-value="양념" />
        <v-select
          :items="['adj', 'v', 'n']"
          model-value="n"
          label="Part of speech"
          readonly
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%"> DELETE </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (wordDialogControl.showDeleteDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단어 수정 다이얼로그 -->
  <v-dialog v-model="wordDialogControl.showUpdateDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-info"> ※ 단어 수정 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (wordDialogControl.showUpdateDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field
          label="Word"
          model-value="spice"
          append-inner-icon="mdi-file-document-edit-outline"
        />
        <v-text-field
          label="Meaning"
          model-value="양념"
          append-inner-icon="mdi-file-document-edit-outline"
        />
        <v-select
          append-inner-icon="mdi-file-document-edit-outline"
          model-value="n"
          label="Part of speech"
          :items="['adj', 'v', 'n']"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="info" style="width: 48%"> UPDATE </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (wordDialogControl.showUpdateDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- 단어 등록 다이얼로그 -->
  <v-dialog v-model="wordDialogControl.showAddDialog" width="500">
    <v-card>
      <template #title>
        <span class="noto-sans text-primary"> ※ 단어 등록 </span>
      </template>
      <template #append>
        <v-btn
          variant="text"
          icon="mdi-close"
          @click="() => (wordDialogControl.showAddDialog = false)"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field label="Word" />
        <v-text-field label="Meaning" />
        <v-select
          model-value="adj"
          label="Part of speech"
          :items="['adj', 'v', 'n']"
        />
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="primary" style="width: 48%"> ADD </v-btn>
        <v-btn
          style="width: 48%"
          @click="() => (wordDialogControl.showAddDialog = false)"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useThemeStore } from "@/stores/theme";

const themeStore = useThemeStore();

// 단어 다이얼로그 컨트롤
const wordDialogControl = reactive({
  showDeleteDialog: false,
  showUpdateDialog: false,
  showAddDialog: false,
});

const wordList = ref([
  { id: 3, name: "spice", meaning: "양념", pos: "n" },
  { id: 2, name: "delicious", meaning: "맛있는", pos: "adj" },
  { id: 1, name: "cook", meaning: "요리하다", pos: "v" },
]);
</script>
