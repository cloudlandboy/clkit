/**
 * $1
 * @author: clboy
 * @date: 2023-12-05 22:12:24
 * @Copyright (c) 2023 by syl@clboy.cn, All Rights Reserved. 
 */
import { cloneDeep, pick } from 'lodash-es'

export class FieldDef {
    template;
    constructor(template) {
        this.template = template;
    }

    getObj() {
        return cloneDeep(this.template);
    }
}

export function copyProperties(source, target) {
    return pick(source, Object.keys(target))
}

export function toElTree(obj, labelFormat) {
    const data = [];
    for (const key in obj) {
        const value = obj[key];
        const isLeafNode = (typeof value) !== 'object';
        data.push({
            label: labelFormat(key, value, isLeafNode),
            value: value,
            children: isLeafNode ? [] : toElTree(value, labelFormat)
        });
    }
    return data;
}