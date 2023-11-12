import { cloneDeep } from 'lodash-es'

export class FieldDef {
    template;
    constructor(template) {
        this.template = template;
    }

    getObj() {
        return cloneDeep(this.template);
    }
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