<template>
  <router-view v-slot="{ Component }">
    <keep-alive>
      <component :is="Component"  v-if="$route.meta.keepAlive"/>
    </keep-alive>
    <component :is="Component"  v-if="!$route.meta.keepAlive"/>
  </router-view>
</template>

<script lang="ts">
    import {Options, Vue} from 'vue-property-decorator';
    @Options({
      components:{}
    })
    export default class Index extends Vue{
    }
</script>

<style lang="less" scoped>
</style>


