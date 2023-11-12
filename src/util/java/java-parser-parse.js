import javaParse from "java-parser";
import { parse as commentParse } from "comment-parser";
import { JavaClassInfo, JavaFieldInfo, MultipleClassError } from "./java-class-info";


class ClassInfoCollector extends javaParse.BaseJavaCstVisitorWithDefaults {
    constructor(classInfo) {
        super();
        this.classInfo = classInfo;
        this.validateVisitor();
    }

    ordinaryCompilationUnit(ctx) {
        const leadingComments = this.findProperty(ctx.typeDeclaration, 'leadingComments');
        if (leadingComments) {
            this.classInfo.comment = this.leadingComments(leadingComments[0]);
        }
        super.ordinaryCompilationUnit(ctx);
    }

    classDeclaration(ctx) {
        if (this.classInfo.classNumber > 0) {
            throw new MultipleClassError();
        }
        const typeIdentifier = this.findProperty(ctx.normalClassDeclaration, 'typeIdentifier');
        const identifier = this.findProperty(typeIdentifier, 'Identifier');
        this.classInfo.name = this.findProperty(identifier, 'image');
        this.classInfo.classNumber++;
        super.classDeclaration(ctx);
    }

    classBody(ctx) {
        if (ctx.classBodyDeclaration) {
            ctx.classBodyDeclaration.forEach(cbd => {
                this.classBodyDeclaration(cbd);
            });
        }
    }

    classBodyDeclaration(ctx) {
        const classMemberDeclaration = this.findProperty(ctx, 'classMemberDeclaration');
        const fieldDeclaration = this.findProperty(classMemberDeclaration, 'fieldDeclaration');
        if (!fieldDeclaration) {
            return;
        }
        let comment = '';
        if (ctx.leadingComments) {
            comment = this.leadingComments(ctx.leadingComments[0]);
        }
        this.classInfo.fieldList.push(this.fieldDeclaration(fieldDeclaration[0].children, comment));
    }

    fieldDeclaration(ctx, comment) {
        const fieldName = this.findProperty(ctx, 'variableDeclaratorList.variableDeclarator.variableDeclaratorId.Identifier.image');
        const unannType = ctx.unannType;
        let filedType = 'Object';
        let isArray = false;
        const primitiveType = this.findProperty(unannType, 'unannPrimitiveTypeWithOptionalDimsSuffix');
        if (primitiveType) {
            //基本类型
            isArray = !!this.findProperty(primitiveType, 'dims');
            const numericType = this.findProperty(primitiveType, 'unannPrimitiveType.numericType');
            if (numericType) {
                //数字类型
                filedType = this.findProperty(numericType, 'children.@_.children.@_.image');
            } else if (this.findProperty(primitiveType, 'unannPrimitiveType.Boolean')) {
                filedType = 'boolean';
            }
        } else {
            const unannReferenceType = this.findProperty(unannType, 'unannReferenceType');
            isArray = !!this.findProperty(unannReferenceType, 'dims');
            filedType = this.findProperty(unannReferenceType, 'unannClassOrInterfaceType.unannClassType.Identifier.image');
        }

        const filedInfo = new JavaFieldInfo(fieldName, filedType, comment);
        filedInfo.isArray = isArray;
        filedInfo.modifiers = [];
        filedInfo.annotations = [];

        ctx.fieldModifier.forEach(m => {
            if (m.children.annotation) {
                filedInfo.annotations.push(this.annotation(m.children.annotation));
            } else {
                filedInfo.modifiers.push(this.findProperty(m, 'children.@_.image'));
            }
        })

        return filedInfo;
    }

    annotation(ctx) {
        return {
            name: this.findProperty(ctx, 'typeName.Identifier.image')
        }
    }

    leadingComments(ctx) {
        if (ctx.tokenType.name === 'TraditionalComment') {
            return commentParse(ctx.image)[0].description;
        } else if (ctx.tokenType.name === 'LineComment') {
            return ctx.image.substring(2);
        }
        return '';
    }

    findProperty(ctx, propertyDot) {
        const properies = propertyDot.split('.');
        let value = ctx;
        for (const property of properies) {
            if ((typeof value) !== 'object' || value === null) {
                return undefined;
            }
            if (Array.isArray(value)) {
                value = value[0];
            }
            if (property === '@_') {
                value = Object.values(value)[0];
            } else if (value.hasOwnProperty(property)) {
                value = value[property];
            } else if (value.children) {
                value = value.children[property];
            } else {
                return undefined;
            }
        }
        return value;
    }
}

export default function parse(text) {
    const cst = javaParse.parse(text);
    const classInfo = new JavaClassInfo();
    const classInfoCollector = new ClassInfoCollector(classInfo);
    classInfoCollector.visit(cst);
    return classInfo;
}