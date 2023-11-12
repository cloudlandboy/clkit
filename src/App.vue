<script setup>
import { onMounted } from 'vue';
import Clipboard from 'clipboard';
import dateTime from './components/date-time.vue';
import lanPortScan from './components/lan-port-scan.vue';
import process from './components/process.vue';
import faker from './components/faker.vue';
import officeConvert from './components/office-convert.vue';
import crudCodeGen from './components/code_gen/crud-code-gen.vue';
import textPrefixSuffix from './components/text-prefix-suffix.vue';
import javaClass from './components/java-class.vue';
onMounted(() => {
  new Clipboard('.copy-btn', {
    text: function (trigger) {
      return trigger.previousElementSibling;
    }
  });
});
</script>

<template>
  <dateTime></dateTime>
  <lanPortScan></lanPortScan>
  <process></process>
  <faker></faker>
  <officeConvert></officeConvert>
  <crudCodeGen></crudCodeGen>
  <javaClass></javaClass>
  <textPrefixSuffix></textPrefixSuffix>
</template>