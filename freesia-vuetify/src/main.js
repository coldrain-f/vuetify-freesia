/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Components
import App from "./App.vue";

// Composables
import { createApp } from "vue";

// Plugins
import { registerPlugins } from "@/plugins";
import { createPinia } from "pinia";
import FlagIcon from "vue-flag-icon";

// 전역 상태 관리를 위한 pinia 생성
const pinia = createPinia();
const app = createApp(App);

registerPlugins(app);

// app에 pinia 등록
app.use(pinia);
app.use(FlagIcon);
app.mount("#app");
