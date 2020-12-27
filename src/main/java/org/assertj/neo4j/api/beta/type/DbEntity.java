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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstract class for any neo4j entity.
 *
 * @author patouche - 09/11/2020
 */
public abstract class DbEntity<T> {

    protected final RecordType recordType;

    protected Long id;

    protected final Map<String, DbValue> properties;

    protected DbEntity(final RecordType recordType, final Long id, final Map<String, DbValue> properties) {
        this.recordType = recordType;
        this.id = id;
        this.properties = properties;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Map<String, DbValue> getProperties() {
        return properties;
    }

    public List<String> getPropertyKeys() {
        return this.properties.keySet().stream().distinct().sorted().collect(Collectors.toList());
    }

    public Object getPropertyValue(final String key) {
        return Optional.ofNullable(properties.get(key)).map(DbValue::getContent).orElse(null);
    }

    public ValueType getPropertyType(final String key) {
        return Optional.ofNullable(properties.get(key)).map(DbValue::getType).orElse(null);
    }

    public abstract T withoutId();

    /**
     * Design for unified {@link #toString()} representation.
     */
    protected String entityRepresentation(final String prefix) {
        if (id == null) {
            return recordType + "{" +  prefix + ", properties=" + properties + '}';
        }
        return recordType + "{id=" + id + ", " + prefix + ", properties=" + properties + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DbEntity<?> dbEntity = (DbEntity<?>) o;
        return recordType == dbEntity.recordType
               && Objects.equals(id, dbEntity.id)
               && Objects.equals(properties, dbEntity.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordType, id, properties);
    }

}
