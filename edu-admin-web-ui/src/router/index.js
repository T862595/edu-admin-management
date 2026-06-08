import { createRouter, createWebHistory } from "vue-router";
import Clazz from "@/views/clazz/index.vue";
import Stu from "@/views/stu/index.vue";
import Dept from "@/views/dept/index.vue";
import Emp from "@/views/emp/index.vue";
import Notice from "@/views/notice/index.vue";
import Login from "@/views/login/index.vue";
import Layout from "@/views/layout/index.vue";

const routes = [
  {
    path: "/",
    component: Layout,
    redirect: "/dept",
    children: [
      { path: "/clazz", component: Clazz },
      { path: "/stu", component: Stu },
      { path: "/dept", component: Dept },
      { path: "/emp", component: Emp },
      { path: "/notice", component: Notice },
    ],
  },
  { path: "/login", component: Login },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
