import Vue from 'vue';
import VueRouter, {RouteConfig} from 'vue-router';

Vue.use(VueRouter);

const routes : Array<RouteConfig> = [{
    path:'/oauth/client',
    name:'参数',
    redirect:'/oauth/client/list'
    },{
        path:'/oauth/client/list',
        name:'参数列表',
        component: () => import('@/oauth/client/views/List.vue'),
        meta: {keepAlive: true }
    }
];

const route = new VueRouter({
    mode:'history',
    base:process.env.BASE_URL,
    routes
})

export default route