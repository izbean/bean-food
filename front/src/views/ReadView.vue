<script setup lang="ts">

import {onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const props = defineProps({
  postId: {
    type: Number,
    required: true
  }
});

const post = ref({
  id: 0,
  title: "",
  content: ""
})

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
  });
});

const router = useRouter();

const moveToEdit = () => {
  router.push({name: "edit", params:{postId: props.postId}})
}
</script>

<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.title }}</h2>
      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2022-01-01</div>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="content">{{ post.content }}</div>
    </el-col>
  </el-row>

  <el-row>
    <el-col class="mt-3">
      <el-button type="warning" @click="moveToEdit()" class="d-flex justify-content-end">수정</el-button>
    </el-col>
  </el-row>

</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
}

.content {
  font-size: 0.85rem;
  margin-top: 4px;
  color: #7e7e7e;
  white-space: break-spaces;
  line-height: 1.5;
}

.sub {
  margin-top: 10px;
  font-size: 0.78rem;

  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}
</style>