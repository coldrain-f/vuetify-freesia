<template>
  <v-card flat>
    <v-card-text>
      <!-- SELECT를 지우고 ADMIN 메뉴에서 고르도록 변경하면 깔끔할듯. -->
      <!-- <v-select 
        label="Admin"
        variant="underlined"
        v-model="selectedAdminCategory"
        :items="adminCategories"
        append-icon="mdi-magnify"
        @click:append="console.log('Hello World!')"
        hide-details
        class="mb-2" /> -->
      
      <v-text-field 
        label="Search"
        variant="underlined"
        append-inner-icon="mdi-magnify"
        @click:append-inner="console.log('Hello World!')"
      ></v-text-field>
      <v-table style="overflow-x: auto;">
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
              <v-btn variant="text" size="small" icon="mdi-trash-can-outline"></v-btn>
              <v-btn variant="text" size="small" icon="mdi-pencil-outline"></v-btn>
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
      <v-btn variant="flat" size="default" class="w-100">
        <v-icon start icon="mdi-plus"></v-icon>
        Add item
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { ref } from "vue";

const adminCategories = ref(['Vocabulary', 'Unit', 'Word']);

const selectedAdminCategory = ref('Vocabulary');

const vocabularyList = ref([
  { id: 3, title: '단어가 읽기다 기본편', language: 'English' },
  { id: 2, title: '단어가 읽기다 실력편', language: 'English' },
  { id: 1, title: 'JLPT N5', language: 'Japanese' },
])

</script>