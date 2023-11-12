export const types = {
    boolean: {
        key: 'boolean',
        defaultJsonValue: false,
        parseStringValue: (str) => Boolean(str)
    },
    decimal: {
        key: 'decimal',
        defaultJsonValue: "0.00",
        parseStringValue: (str) => Number(str.replace(/[dfDF]/, ''))

    },
    bigInt: {
        key: 'bigInt',
        defaultJsonValue: "0",
        parseStringValue: (str) => Number(str.replace(/[lL]/, ''))

    },
    int: {
        key: 'int',
        defaultJsonValue: 0,
        parseStringValue: (str) => Number(str)
    },
    string: {
        key: 'string',
        defaultJsonValue: "",
        parseStringValue: (str) => {
            if (str.length == 3 && str.startsWith('\'')) {
                return str[1];
            }
            if (str.startsWith('"')) {
                str = str.substring(1);
            }

            if (str.endsWith('"')) {
                str = str.substring(0, str.length - 1);
            }
            return str;
        }

    },
    array: {
        key: 'array',
        defaultJsonValue: [],
        parseStringValue: (str) => JSON.parse(str)
    },
    object: {
        key: 'object',
        defaultJsonValue: {},
        parseStringValue: (str) => JSON.parse(str)
    }
}

export const javaTypeMaping = {
    'boolean': types.boolean,
    'double': types.decimal,
    'float': types.decimal,
    'bigDecimal': types.decimal,
    'bigInteger': types.bigInt,
    'number': types.bigInt,
    'long': types.bigInt,
    'int': types.int,
    'integer': types.int,
    'short': types.int,
    'byte': types.int,
    'year': types.int,
    'localDateTime': types.string,
    'localDate': types.string,
    'date': types.string,
    'collection': types.array,
    'list': types.array,
    'arrayList': types.array,
    'linkedList': types.array,
    'vector': types.array,
    'stack': types.array,
    'copyOnWriteArrayList': types.array,
    'set': types.array,
    'hashSet': types.array,
    'linkedHashSet': types.array,
    'treeSet': types.array,
    'enumSet': types.array,
    'copyOnWriteArraySet': types.array,
    'queue': types.array,
    'priorityQueue': types.array,
    'arrayDeque': types.array,
    'linkedBlockingQueue': types.array,
    'deque': types.array,
    'arrayDeque': types.array,
    'string': types.string,
    'char': types.string,
    'character': types.string
}

export const unknowJavaToMysqlType = {
    type: "BLOB",
    param: ""
}

export const javaToMysqlTypeMapping = {
    "int": {
        type: "INT",
        param: "(11)"
    },
    "integer": {
        type: "INT",
        param: "(11)"
    },
    "long": {
        type: "BIGINT",
        param: "(20)"
    },
    "short": {
        type: "SMALLINT",
        param: "(6)"
    },
    "byte": {
        type: "TINYINT",
        param: "(4)"
    },
    "float": {
        type: "FLOAT",
        param: ""
    },
    "double": {
        type: "DOUBLE",
        param: ""
    },
    "bigDecimal": {
        type: "DECIMAL",
        param: "(10, 2)"
    },
    "localDate": {
        type: "DATE",
        param: ""
    },
    "date": {
        type: "DATETIME",
        param: ""
    },
    "localDateTime": {
        type: "DATETIME",
        param: ""
    },
    "localTime": {
        type: "TIME",
        param: ""
    },
    "boolean": {
        type: "TINYINT",
        param: "(1)"
    },
    "string": {
        type: "VARCHAR",
        param: "(255)"
    },
    "character": {
        type: "VARCHAR",
        param: "(255)"
    },
    "char": {
        type: "CHAR",
        param: ""
    },
}
