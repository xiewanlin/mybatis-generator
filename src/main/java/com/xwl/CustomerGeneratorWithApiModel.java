package com.xwl;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.util.StringUtility;

public class CustomerGeneratorWithApiModel extends CustomGenerator {
    private static int position = 0;

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        boolean isNull = introspectedColumn.isNullable();
        String comments = introspectedColumn.getRemarks();
        String columnName = introspectedColumn.getActualColumnName();
        String defaultValue = introspectedColumn.getDefaultValue();
        int length = introspectedColumn.getLength();
        boolean autoIncreasement = introspectedColumn.isAutoIncrement();
        boolean isIdentity = introspectedColumn.isIdentity();
        boolean isBlob = introspectedColumn.isBLOBColumn();
        JavaTypeResolverDefaultImpl javaTypeResolverDefault = new JavaTypeResolverDefaultImpl();
        String jdbcType = javaTypeResolverDefault.calculateJdbcTypeName(introspectedColumn);
        field.addJavaDocLine("/**");
        if (!this.suppressColumnName && StringUtility.stringHasValue(columnName))
            field.addJavaDocLine(String.format(" * @column %s", new Object[] { columnName }));
        if (!this.suppressDataType &&
                StringUtility.stringHasValue(jdbcType)) {
            StringBuilder stringBuilder = new StringBuilder();
            if (length > 0) {
                stringBuilder.append(String.format(" * @type %s(%d)", new Object[] { jdbcType, Integer.valueOf(length) }));
            } else {
                stringBuilder.append(String.format(" * @type %s", new Object[] { jdbcType }));
            }
            if (isIdentity)
                stringBuilder.append(" @pk");
            if (autoIncreasement)
                stringBuilder.append(" @autoIncrease");
            if (!isNull)
                stringBuilder.append(" @required");
            field.addJavaDocLine(stringBuilder.toString());
        }
        if (!this.suppressComment && StringUtility.stringHasValue(comments)) {
            String[] remarkLines = comments.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines)
                field.addJavaDocLine(" *   " + remarkLine);
        }
        field.addJavaDocLine(" */");
        field.addAnnotation("@ApiModelProperty(value = \"" + comments + "\", position = " + position++ + ")");
    }

    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        position = 0;
        topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
        topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        String remarks = introspectedTable.getRemarks();
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(String.format(" * @table %s", new Object[] { tableName }));
        if (!this.suppressComment && StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines)
                topLevelClass.addJavaDocLine(" *   " + remarkLine);
        }
        topLevelClass.addJavaDocLine(" */");
        topLevelClass.addAnnotation("@ApiModel(description = \"" + remarks + "\")");
    }
}
