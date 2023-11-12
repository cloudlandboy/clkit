import { JavaClassInfo, JavaFieldInfo, MultipleClassError } from "./java-class-info";
import { types } from "./java-types";
import { parse as commentParse } from "comment-parser";
import { parse as doParse } from "chevrotain-java";

const annoValueTypeMapping = {
    "BOOLEAN_LITERAL": types.boolean,
    "FLOAT_LITERAL": types.decimal,
    "DECIMAL_LITERAL": types.int,
    "CHAR_LITERAL": types.string,
    "STRING_LITERAL": types.string,
}

function collectClassBody(classBody, classInfo) {
    classBody.declarations.forEach(item => {
        if (item.declaration.type !== 'FIELD_DECLARATION') {
            return
        }
        const fieldName = item.declaration.variableDeclarators.list[0].id.id.value;
        let javaType = 'Object';
        const typeType = item.declaration.typeType;
        if (typeType.type === 'IDENTIFIER' || typeType.type === 'PRIMITIVE_TYPE') {
            javaType = typeType.value;
        } else if (typeType.type === 'TYPE_TYPE') {
            javaType = typeType.value.value;
        } else if (typeType.type === 'CLASS_OR_INTERFACE_TYPE_ELEMENT') {
            javaType = typeType.name.value
        }

        const cm = convertModifiers(item.modifiers);
        const fieldInfo = new JavaFieldInfo(fieldName, javaType, cm.comment);
        fieldInfo.isArray = !!typeType.dimensions;
        fieldInfo.annotations = cm.annotations;
        fieldInfo.modifiers = cm.modifiers;
        classInfo.fieldList.push(fieldInfo);
    })

}

function convertModifiers(modifiers) {
    const res = {
        comment: '',
        annotations: [],
        modifiers: []
    }
    if (Array.isArray(modifiers)) {
        modifiers.forEach(m => {
            if (m.comments) {
                const text = m.comments[0].value;
                if (text.startsWith('//')) {
                    res.comment = text.substring(2);
                } else {
                    res.comment = commentParse(text)[0].description;
                }
            }

            if (m.type === 'ANNOTATION') {
                res.annotations.push({
                    name: m.name.name[0].value,
                    properties: converAnnoProperties(m.values)
                });
                return
            }
            res.modifiers.push(m.value);
        })
    }
    return res;
}

function converAnnoProperties(values) {
    if (!Array.isArray(values)) {
        return [];
    }
    return values.map(item => {

        const res = {
            key: 'value',
            value: item.value,

        };

        if (res.value.value) {
            res.key = item.key.value;
            res.value = res.value.value;
        }

        const valueType = annoValueTypeMapping[item.value.type];
        if (valueType) {
            res.valueType = valueType;
            res.value = valueType.parseStringValue(res.value);
        } else {
            res.value = undefined;
            res.valueType = undefined;
        }
        return res;
    });
}

export default function parse(text) {
    const res = doParse(text);
    if (res.types.length > 1) {
        throw new MultipleClassError();
    }

    const classDesc = res.types[0];
    const cm = convertModifiers(classDesc.modifiers);
    const classInfo = new JavaClassInfo();
    classInfo.name = classDesc.declaration.name.value;
    classInfo.annotations = cm.annotations;
    classInfo.modifiers = cm.modifiers;
    classInfo.comment = cm.comment;
    collectClassBody(classDesc.declaration.body, classInfo);
    return classInfo;
}