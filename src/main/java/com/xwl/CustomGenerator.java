package com.xwl;

import java.util.Properties;
import java.util.Set;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.util.StringUtility;

public class CustomGenerator implements CommentGenerator {
    protected Properties properties = new Properties();

    protected boolean suppressComment = false;

    protected boolean suppressDataType = false;

    protected boolean suppressColumnName;

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        this.suppressComment = StringUtility.isTrue(properties.getProperty("suppressComment"));
        this.suppressDataType = StringUtility.isTrue(properties.getProperty("suppressDataType"));
        this.suppressColumnName = StringUtility.isTrue(properties.getProperty("suppressColumnName"));
    }

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
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {}

    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
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
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {}

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {}

    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {}

    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {}

    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {}

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {}

    public void addJavaFileComment(CompilationUnit compilationUnit) {}

    public void addComment(XmlElement xmlElement) {}

    public void addRootComment(XmlElement rootElement) {}

    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {}

    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {}

    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {}

    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {}

    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {}
}
