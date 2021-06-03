<template>
  <div>
      <button @click="home">主页</button>
  </div>
</template>

<script>
import axios from 'axios'
export default {
    data: function (){
        return {
            axiosInstance: {}
        }
    },
    mounted() {
        this.axiosInstance = axios.create({
            baseURL: 'http://localhost:8090',
            auth: {
                username: 'javalover',
                password: '123456'
            }
        })
    },
    methods:{
      home(){
          this.axiosInstance.get('/home').then(res=> {
              console.log('success')
              console.log(res)
          }).catch(err=>{
              console.log('fail')
              console.log(err)
          })
      }
    }
}
</script>
