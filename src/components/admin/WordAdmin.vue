<template>
  <v-card flat>
    <v-card-text>
      <v-table style="overflow-x: auto">
        <thead>
          <tr>
            <th>#</th>
            <th>Title</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="unit in unitList" :key="unit.id">
            <td style="width: 5%">{{ unit.id }}</td>
            <td style="width: 50%">{{ unit.title }}</td>
            <td style="width: 25%">
              <v-btn
                variant="text"
                size="small"
                icon="mdi-trash-can-outline"
                @click="unitDialogControl.showDeleteDialog = true"
              ></v-btn>
              <v-btn
                variant="text"
                size="small"
                icon="mdi-pencil-outline"
                @click="unitDialogControl.showUpdateDialog = true"
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
        color="primary"
        @click="unitDialogControl.showAddDialog = true"
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
          @click="unitDialogControl.showDeleteDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field readonly label="No" model-value="1"></v-text-field>
        <v-text-field
          readonly
          label="Title"
          model-value="Unit 01 - 일상1"
        ></v-text-field>
        <v-text-field
          readonly
          label="Subword"
          model-value="40개"
        ></v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="error" style="width: 48%"> DELETE </v-btn>
        <v-btn
          style="width: 48%"
          @click="unitDialogControl.showDeleteDialog = false"
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
          @click="unitDialogControl.showUpdateDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field label="No" model-value="1" readonly></v-text-field>
        <v-text-field
          append-inner-icon="mdi-file-document-edit-outline"
          label="Title"
          model-value="Unit 01 - 일상1"
        ></v-text-field>
        <v-text-field
          label="Subword"
          model-value="40개"
          readonly
        ></v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="info" style="width: 48%"> UPDATE </v-btn>
        <v-btn
          style="width: 48%"
          @click="unitDialogControl.showUpdateDialog = false"
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
          @click="unitDialogControl.showAddDialog = false"
        >
        </v-btn>
      </template>
      <v-card-text class="mt-5">
        <v-text-field label="Title"></v-text-field>
      </v-card-text>
      <v-card-actions class="d-flex justify-center">
        <v-btn color="primary" style="width: 48%"> ADD </v-btn>
        <v-btn
          style="width: 48%"
          @click="unitDialogControl.showAddDialog = false"
        >
          CANCEL
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { reactive, ref } from "vue";

// Unit 다이얼로그 컨트롤
const unitDialogControl = reactive({
  showDeleteDialog: false,
  showUpdateDialog: false,
  showAddDialog: false,
});

const unitList = ref([
  { id: 3, title: "Unit 03 - 학교 생활2" },
  { id: 2, title: "Unit 02 - 학교 생활1" },
  { id: 1, title: "Unit 01 - 일상" },
]);
</script>
