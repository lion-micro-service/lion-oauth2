import Vue from 'vue';
import router from "./router/index";
import store from './store/index';
import Index from './Index.vue';
import ant,{FormModel}  from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';;
Vue.use(ant);
Vue.use(FormModel);
new Vue({
    store,
    router,
    render:h=>h(Index)
}).$mount("#app")