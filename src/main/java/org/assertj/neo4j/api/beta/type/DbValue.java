/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2013-2020 the original author or authors.
 */
package org.assertj.neo4j.api.beta.type;

import java.util.Objects;

/**
 * Wrap an object within its type.
 *
 * @author Patrick Allain - 23/12/2020
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
