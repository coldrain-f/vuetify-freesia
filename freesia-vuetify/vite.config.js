// Plugins
import vue from "@vitejs/plugin-vue";
import vuetify, { transformAssetUrls } from "vite-plugin-vuetify";

// Utilities
import { defineConfig } from "vite";
import { fileURLToPath } from "node:url";
import { resolve } from "node:path";

// https://vitejs.dev/config/

const __filename = fileURLToPath(import.meta.url);
const __dirname = resolve(__filename, "..");

export default defineConfig({
  plugins: [
    vue({
      template: { transformAssetUrls },
    }),
    // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vite-plugin
    vuetify({
      autoImport: true,
      styles: {
        configFile: "src/styles/settings.scss",
      },
    }),
  ],
  define: { "process.env": {} },
  resolve: {
    alias: {
      // "@": fileURLToPath(new URL("./src", import.meta.url)),
      "@": resolve(__dirname, "src"),
    },
    extensions: [".js", ".json", ".jsx", ".mjs", ".ts", ".tsx", ".vue"],
  },
  server: {
    port: 3001,
  },
});
