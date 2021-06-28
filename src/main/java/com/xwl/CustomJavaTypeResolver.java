package com.xwl;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public class CustomJavaTypeResolver extends JavaTypeResolverDefaultImpl {
    public CustomJavaTypeResolver() {
        this.typeMap.put(Integer.valueOf(-6), new JavaTypeResolverDefaultImpl.JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class

                .getName())));
    }
}
