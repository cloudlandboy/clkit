import { snakeCase, lowerFirst } from "lodash";
import { javaTypeMaping, unknowJavaToMysqlType, javaToMysqlTypeMapping, types } from "./java-types";

export class MultipleClassError extends Error {
    constructor() {
        super('代码中包含多个类,不支持解析');
    }
}

export class JavaFieldInfo {
    isArray = false;
    modifiers = [];
    annotations = []
    lowerFirstJavaType;

    constructor(name, javaType, comment) {
        this.name = name;
        this.comment = comment || '';
        this.javaType = javaType;
        this.lowerFirstJavaType = lowerFirst(javaType);
    }

}

export class JavaClassInfo {
    name = 'Class';
    comment = '';
    modifiers = [];
    annotations = []
    classNumber = 0;
    fieldList = [];

    toJson(filter) {
        const jsonObj = {};
        this.fieldList.forEach(field => {
            if (filter && !filter(field)) {
                return;
            }
            if (field.isArray) {
                jsonObj[field.name] = [];
                return;
            }
            const type = javaTypeMaping[field.lowerFirstJavaType];
            jsonObj[field.name] = type ? type.defaultJsonValue : types.object.defaultJsonValue;
        });
        return jsonObj;
    }

    toSql(filter, mode) {
        let fieldList = this.fieldList;
        if (filter) {
            fieldList = fieldList.filter(filter);
        }
        if (fieldList.length === 0) {
            return '';
        }
        const tableName = '`' + snakeCase(this.name) + '`';
        if (mode === 'ct') {
            return this.toCreateTable(tableName, fieldList);
        }
        if (mode === 'ac') {
            return this.toAddColumn(tableName, fieldList);
        }
    }

    static findJsrAnnoStringMax(field) {
        const anno = field.annotations.find(anno => anno.name === 'Size' || anno.name === 'Length');
        if (!anno) {
            return -1;
        }
        const max = anno.properties.find(p => p.key === 'max');
        if (!max) {
            return -1;
        }
        return max.value;
    }

    static convertFieldToSqlColumnStatement(field) {
        let mysqlType = javaToMysqlTypeMapping[field.lowerFirstJavaType] || unknowJavaToMysqlType;
        if (field.isArray && mysqlType !== unknowJavaToMysqlType) {
            mysqlType = javaToMysqlTypeMapping.string;
        }

        let param = mysqlType.param;
        if (mysqlType === javaToMysqlTypeMapping.string) {
            const max = this.findJsrAnnoStringMax(field);
            if (max > 0) {
                param = `(${max})`;
            }
        }
        const columnName = '`' + snakeCase(field.name) + '`';
        return `${columnName} ${mysqlType.type}${param} COMMENT '${field.comment}'`;
    }

    toCreateTable(tableName, fieldList) {
        const open = `CREATE TABLE ${tableName} (`;
        const close = `) COMMENT = '${this.comment}';`
        let idFields = [];
        const columns = fieldList.map(field => {
            if (field.annotations.some(anno => anno.name === 'Id' || anno.name === 'TableId') || field.name === 'id') {
                idFields.push(field);
            }
            return JavaClassInfo.convertFieldToSqlColumnStatement(field);
        })

        if (idFields.length > 0) {
            const ids = idFields.map(f => '`' + f.name + '`').join(',');
            columns.push(`PRIMARY KEY (${ids})`);
        }
        return `${open} \n\t${columns.join(',\n\t')} \n${close} `;
    }

    toAddColumn(tableName, fieldList) {
        const open = `ALTER TABLE ${tableName}`;
        const close = ';';
        const columns = fieldList.map(field => {
            return `ADD COLUMN ${JavaClassInfo.convertFieldToSqlColumnStatement(field)}`;
        })

        return `${open}\n\t${columns.join(',\n\t')}${close}`;
    }
}