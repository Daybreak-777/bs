<template>
  <div>

    <div style="margin-bottom: 20px">
      <el-button type="primary" @click="router.back()">返回主页</el-button>
    </div>

    <div style="font-size: 20px; font-weight: bold; text-decoration: underline; font-style: italic; color: red; margin-bottom: 20px">
      Vue从0到1！
    </div>

    <div style="font-size: 30px; margin-bottom: 20px">
      {{ a }} {{ b }}
    </div>

    <div style="margin-bottom: 20px">
      {{ data.a }}
      {{ data.b }}
      {{ data.c }}
    </div>

    <div style="margin-bottom: 20px">
      <input v-model="data.name"/>
    </div>

    <div style="margin-bottom: 20px">
      <span style="color: red" v-if=" data.name === '佩奇'">小猪佩奇</span>
      <span style="color: blue" v-else-if=" data.name === '乔治'">小猪乔治</span>
      <span style="color: hotpink" v-else>小猪朵朵</span>
    </div>

    <div style="display: flex; margin-bottom: 20px">
      <div @click="show(item)" style="width: 300px; height:300px; background-color: green; margin-bottom: 10px; margin-right: 10px; text-align: center; line-height: 300px; font-size: 30px;" v-for="item in data.arr">
        {{ item }}
      </div>

      <div>
        <select style="width: 200px">
          <option v-for="item in data.fruits">{{ item }}</option>
        </select>
      </div>
    </div>

    <div style="margin-bottom: 20px">
      <button @click="click">点我加好运</button>
    </div>

    <div>
      <div :style="data.box"></div>

      <div>
        <img :src="data.img" alt="">
      </div>
    </div>

    <div style="margin: 30px">
      <el-button @click="clickData" :style="{ color: 'red'}" type="primary">Primary</el-button>
      <el-button type="success" plain>Success</el-button>
      <el-button type="info" plain size="large">Info</el-button>
      <el-button type="warning" loading color="#626aef" style="color: yellow">Warning</el-button>
      <el-button type="danger" :bg="true" text>Danger</el-button>
    </div>

    <div style="margin: 30px">
      <el-icon :size="20">
        <Edit />
      </el-icon>

      <span style="margin-left: 30px">
        <el-icon size="15" color="red"><View /></el-icon> 100
      </span>

      <el-button type="danger" :icon="Delete" circle />

      <el-input
          v-model="input2"
          style="width: 240px"
          placeholder="Type something"
          :prefix-icon="Search"
      />

    </div>

  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from "vue";
import {Delete, Edit, Search, View} from "@element-plus/icons-vue";
import router from "@/router/index.js";

const a = ref(1)
const b = ref("Daybreak7")

const data = reactive({
  id: router.currentRoute.value.query.id,
  username: router.currentRoute.value.query.username,
  a: 123,
  b: "daybreak7",
  c: false,
  name: "佩奇",
  arr: [1, 2, 3],
  fruits: ["苹果", "香蕉", "橘子"],
  box: {
    width: '100px',
    height: '100px',
    backgroundColor: 'red'
  },
  img: "https://www.runoob.com/wp-content/uploads/2017/01/vue.png"
})

onMounted(() => {
  alert("页面加载完成")
})
const show = (value) => {
  alert(value)
}

const click = () => {
  alert("好运+1")
}

const clickData = () => {
  alert("我当前使用的是Element-Plus组合的点击事件")
}

console.log("获取传递过来的id：" + data.id)
console.log("获取传递过来的username：" + data.username)
</script>