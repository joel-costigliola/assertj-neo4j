package org.assertj.neo4j.api.beta.type;

/**
 * @author pallain - 27/11/2020
 */
public enum ValueType {

    NUMBER(Long.class),
    STRING(String.class),
    ;

    ValueType(final Class<?> clazz) {

    }
}
