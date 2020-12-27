package org.assertj.neo4j.api.beta.type;

import java.util.Objects;

/**
 * DB Value wrap an object within its type.
 *
 * @author patouche - 23/12/2020
 */
public class DbValue {

    private final ValueType type;
    private final Object content;

    private DbValue(ValueType type, Object content) {
        this.type = type;
        this.content = content;
    }

    static DbValue propValue(final ValueType type, final Object value) {
        return new DbValue(type, value);
    }

    public ValueType getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbValue that = (DbValue) o;
        return type == that.type && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, content);
    }

    @Override
    public String toString() {
        return "PropertyValue{type=" + type + ", content=" + content + '}';
    }
}
