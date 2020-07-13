<template>
    <div class="main-fluid">
      <router-view :key="key"></router-view>
    </div>
</template>

<script>
    export default {
        name: '',
        computed: {
            key() {
                return this.$route.path
            }
        }
    }
</script>

<style scoped>
  .main-fluid {
    position: relative;
    margin: 0 auto;
  }
  .main-card{
    margin-bottom: 0;
    border-radius: 2px;
    background-color: #fff;
    box-shadow: 0 1px 2px 0 rgba(0,0,0,.05);
  }
</style>
