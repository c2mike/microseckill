<template>
  <div class="about">

    <el-input
            placeholder="请输入手机号"
            v-model="input"
            clearable>
    </el-input>
    <el-row>
      <el-button type="primary" @click="storePhone()">提交</el-button>
    </el-row>
  </div>
</template>

<script>
    import { Message } from 'element-ui';
    export default {
        data() {
            return {
                input: ''
            }
        },
        methods:{
          storePhone()
          {
              if(this.input)
              {
                  this.$store.commit('setPhone',this.input);
                  Message.success("输入成功");
                  this.$router.replace("/");
              }
              else{
                  Message.warning("请输入电话号码");
              }
          }
        }
    }
</script>
