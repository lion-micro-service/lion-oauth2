import {createApp} from 'vue';
import router from "./router/index";
import store from './store/index';
import Index from './Index.vue';
import ant  from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
const app = createApp(Index);
app.use(ant).use(router).use(store).mount("#app");
