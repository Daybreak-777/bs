import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/manager',
      name: '/manager',
      meta: {title: "管理"},
      children: [
        { path: 'home', name: 'home', meta: { title: "主页" }, component: () => import('../views/Home.vue')},
        { path: 'supplier', name: 'supplier', meta: { title: "供应商信息" }, component: () => import('../views/modules/BasicData/supplier.vue')},
        { path: 'material', name: 'material', meta: { title: "物料信息" }, component: () => import('../views/modules/BasicData/material.vue')},
        { path: 'workshop', name: 'workshop', meta: { title: "生产车间信息" }, component: () => import('../views/modules/BasicData/workshop.vue')},
        { path: 'product', name: 'product', meta: { title: "产品信息" }, component: () => import('../views/modules/BasicData/product.vue')},

        { path: 'IqcIncomingInspection', name: 'IqcIncomingInspection', meta: { title: "来料检验列表" }, component: () => import('../views/modules/IqcIncomingInspection/IqcIncomingInspection.vue')},
        { path: 'IqcIncomingInspectionCharts', name: 'IqcIncomingInspectionCharts', meta: { title: "来料检验数据统计" }, component: () => import('../views/modules/IqcIncomingInspection/IqcIncomingInspectionCharts.vue')},
        { path: 'IqcIncomingInspectionCharts1', name: 'IqcIncomingInspectionCharts1', meta: { title: "来料检验数据统计" }, component: () => import('../views/modules/test/IqcIncomingInspectionCharts1.vue')},
        { path: 'IqcIncomingInspectionCharts2', name: 'IqcIncomingInspectionCharts2', meta: { title: "来料检验数据统计" }, component: () => import('../views/modules/test/IqcIncomingInspectionCharts2.vue')},
        { path: 'IqcIncomingInspectionCharts3', name: 'IqcIncomingInspectionCharts3', meta: { title: "来料检验数据统计" }, component: () => import('../views/modules/test/IqcIncomingInspectionCharts3.vue')},
        { path: 'IqcIncomingInspectionCharts4', name: 'IqcIncomingInspectionCharts4', meta: { title: "来料检验数据统计" }, component: () => import('../views/modules/test/IqcIncomingInspectionCharts4.vue')},
        { path: 'IqcIncomingInspectionCharts123', name: 'IqcIncomingInspectionCharts123', meta: { title: "来料检验数据统计" }, component: () => import('../views/modules/test/IqcIncomingInspectionCharts123.vue')},

        { path: 'IqcIncomingInspectionForm', name: 'IqcIncomingInspectionForm', meta: { title: "来料检验表单" }, component: () => import('../views/modules/IqcIncomingInspection/IqcIncomingInspectionForm.vue')},

        { path: 'PqcProcessInspection', name: 'PqcProcessInspection', meta: { title: "工序检验列表" }, component: () => import('../views/modules/PqcProcessInspection/PqcProcessInspection.vue')},
        { path: 'PqcProcessInspectionCharts', name: 'PqcProcessInspectionCharts', meta: { title: "工序检验数据统计" }, component: () => import('../views/modules/PqcProcessInspection/PqcProcessInspectionCharts.vue')},
        { path: 'PqcProcessInspectionForm', name: 'PqcProcessInspectionForm', meta: { title: "工序检验表单" }, component: () => import('../views/modules/PqcProcessInspection/PqcProcessInspectionForm.vue')},

        { path: 'FqcProductInspection', name: 'FqcProductInspection', meta: { title: "产品检验列表" }, component: () => import('../views/modules/FqcProductInspection/FqcProductInspection.vue')},
        { path: 'FqcProductInspectionCharts', name: 'FqcProductInspectionCharts', meta: { title: "产品检验数据统计" }, component: () => import('../views/modules/FqcProductInspection/FqcProductInspectionCharts.vue')},
        { path: 'FqcProductInspectionForm', name: 'FqcProductInspectionForm', meta: { title: "产品检验表单" }, component: () => import('../views/modules/FqcProductInspection/FqcProductInspectionForm.vue')},

        { path: 'test', name: 'test', meta: { title: "数据测试页面"}, component: () => import('../views/modules/test/Test.vue')},
        { path: 'data', name: 'data', meta: { title: "数据统计页面"}, component: () => import('../views/modules/test/Data.vue')},
        { path: 'employee', name: 'employee', meta: { title: '员工信息'}, component: () => import('../views/Employee.vue')},
        { path: 'admin', name: 'admin', meta: { title: '管理员信息'}, component: () => import('../views/Admin.vue')},
        { path: 'person', name: 'person', meta: { title: '个人信息'}, component: () => import('../views/Person.vue')},
        { path: 'password', name: 'password', meta: { title: '修改密码'}, component: () => import('../views/Password.vue')}
      ],
      component: () => import('../views/Manager.vue')
    },
    { path: '/login', name: 'Login', meta: { title: "登录页面"}, component: () => import('../views/Login.vue')},
    { path: '/register', name: 'Register', meta: { title: "注册页面"}, component: () => import('../views/Register.vue')},
    { path: '/404', name: 'NotFound', meta: { title: "404找不到页面"}, component: () => import('../views/404.vue')},
    { path: '/:pathMatch(.*)', redirect: '/404'}
  ],
})

// 表示页面跳转之前的一些操作
router.beforeEach((to, from, next) => {
  document.title = to.meta.title;
  next() // 必须调用这个函数才能实现跳转
})
export default router
