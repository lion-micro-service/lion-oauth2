import { createRouter,createWebHashHistory}  from 'vue-router'
const routes : Array<any> =[{
        path:'/',
        name:'参数',
        redirect:'/oauth/client/list'
    },{
        path:'/oauth/client/list',
        name:'参数列表',
        component: () => import('@/oauth/client/views/List.vue'),
        meta: {keepAlive: true }
    }
];
const route = createRouter({
    history: createWebHashHistory(),
    routes
})
export default route