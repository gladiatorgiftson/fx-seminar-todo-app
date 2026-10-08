<script setup lang="ts">
import { computed, unref } from 'vue'
import { useSlideContext } from '@slidev/client'

// $nav is the locally injected nav: per slide in print/export mode, global in the live show.
const { $nav } = useSlideContext()
const route = computed<any>(() => unref($nav.value.currentSlideRoute))
const page = computed<number>(() => unref($nav.value.currentPage))
const fm = computed<any>(() => route.value?.meta?.slide?.frontmatter ?? {})
const layer = computed<string>(() => fm.value.layer ?? '')
const on = (l: string) => layer.value === l || layer.value === 'all'
</script>

<template>
  <div v-if="page !== 1" class="deck-foot">
    <span>Modern Full Stack Development, II CSE C</span>
    <span class="layers" title="front end, back end, database">
      <i :class="{ 'on-fe': on('fe') }" />
      <i :class="{ 'on-be': on('be') }" />
      <i :class="{ 'on-db': on('db') }" />
    </span>
  </div>
</template>
