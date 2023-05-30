import { defineStore } from "pinia";
import { ref } from "vue";

export const useThemeStore = defineStore("theme", () => {
  const theme = ref("primary");

  const changeTheme = (theme) => {
    theme.value = theme;
  };

  return {
    theme,
    changeTheme,
  };
});
