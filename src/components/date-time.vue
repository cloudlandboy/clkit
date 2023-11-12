<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span>格式化当前时间 </span>
                <el-icon class="show-info-btn" @click="formatPlaceholderListVisible = true">
                    <InfoFilled />
                </el-icon>
            </div>
        </template>
        <div class="kit-item">
            <el-form-item label="表达式: ">
                <el-input v-model="inputPattern" @change="pattern = inputPattern" />
            </el-form-item>
        </div>
        <div class="kit-item">
            <span>{{ formatResult }}</span>
            <copyPrevContentButton></copyPrevContentButton>
        </div>

        <el-drawer v-model="formatPlaceholderListVisible" title="支持的格式化占位符列表" direction="rtl">
            <table>
                <thead>
                    <tr>
                        <th>占位符</th>
                        <th>输出</th>
                        <th>详情</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><code>YY</code></td>
                        <td>18</td>
                        <td>两位数的年份</td>
                    </tr>
                    <tr>
                        <td><code>YYYY</code></td>
                        <td>2018</td>
                        <td>四位数的年份</td>
                    </tr>
                    <tr>
                        <td><code>M</code></td>
                        <td>1-12</td>
                        <td>月份，从 1 开始</td>
                    </tr>
                    <tr>
                        <td><code>MM</code></td>
                        <td>01-12</td>
                        <td>月份，两位数</td>
                    </tr>
                    <tr>
                        <td><code>MMM</code></td>
                        <td>Jan-Dec</td>
                        <td>缩写的月份名称</td>
                    </tr>
                    <tr>
                        <td><code>MMMM</code></td>
                        <td>January-December</td>
                        <td>完整的月份名称</td>
                    </tr>
                    <tr>
                        <td><code>D</code></td>
                        <td>1-31</td>
                        <td>月份里的一天</td>
                    </tr>
                    <tr>
                        <td><code>DD</code></td>
                        <td>01-31</td>
                        <td>月份里的一天，两位数</td>
                    </tr>
                    <tr>
                        <td><code>d</code></td>
                        <td>0-6</td>
                        <td>一周中的一天，星期天是 0</td>
                    </tr>
                    <tr>
                        <td><code>dd</code></td>
                        <td>Su-Sa</td>
                        <td>最简写的星期几</td>
                    </tr>
                    <tr>
                        <td><code>ddd</code></td>
                        <td>Sun-Sat</td>
                        <td>简写的星期几</td>
                    </tr>
                    <tr>
                        <td><code>dddd</code></td>
                        <td>Sunday-Saturday</td>
                        <td>星期几</td>
                    </tr>
                    <tr>
                        <td><code>H</code></td>
                        <td>0-23</td>
                        <td>小时</td>
                    </tr>
                    <tr>
                        <td><code>HH</code></td>
                        <td>00-23</td>
                        <td>小时，两位数</td>
                    </tr>
                    <tr>
                        <td><code>h</code></td>
                        <td>1-12</td>
                        <td>小时, 12 小时制</td>
                    </tr>
                    <tr>
                        <td><code>hh</code></td>
                        <td>01-12</td>
                        <td>小时, 12 小时制, 两位数</td>
                    </tr>
                    <tr>
                        <td><code>m</code></td>
                        <td>0-59</td>
                        <td>分钟</td>
                    </tr>
                    <tr>
                        <td><code>mm</code></td>
                        <td>00-59</td>
                        <td>分钟，两位数</td>
                    </tr>
                    <tr>
                        <td><code>s</code></td>
                        <td>0-59</td>
                        <td>秒</td>
                    </tr>
                    <tr>
                        <td><code>ss</code></td>
                        <td>00-59</td>
                        <td>秒 两位数</td>
                    </tr>
                    <tr>
                        <td><code>SSS</code></td>
                        <td>000-999</td>
                        <td>毫秒 三位数</td>
                    </tr>
                    <tr>
                        <td><code>Z</code></td>
                        <td>+05:00</td>
                        <td>UTC 的偏移量，±HH:mm</td>
                    </tr>
                    <tr>
                        <td><code>ZZ</code></td>
                        <td>+0500</td>
                        <td>UTC 的偏移量，±HHmm</td>
                    </tr>
                    <tr>
                        <td><code>A</code></td>
                        <td>AM PM</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><code>a</code></td>
                        <td>am pm</td>
                        <td></td>
                    </tr>
                </tbody>
            </table>
        </el-drawer>
    </el-card>
</template>

<script setup>
import { ref } from 'vue';
import dayjs from 'dayjs'
import copyPrevContentButton from './copy-prev-content-button.vue';


var pattern = 'YYYYMMDDHHmmss';

const inputPattern = ref(pattern);
const formatPlaceholderListVisible = ref(false);
const formatResult = ref(dayjs().format(pattern));

setInterval(() => {
    formatResult.value = dayjs().format(pattern);
}, 1000)
</script>

<style scoped>
.kit-item {
    line-height: 32px;
    padding: 10px;
}

table {
    border-collapse: collapse;
    margin: auto;
}

table,
th,
td {
    border: 1px solid #dfe2e5;
}

th,
td {
    padding: 8px 12px;
    text-align: left;
}
</style>
