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

import org.assertj.neo4j.api.beta.util.Formats;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstract class for any neo4j entity.
 *
 * @author Patrick Allain - 09/11/2020
 */
public abstract class DbEntity<I extends DbEntity<I>> extends DbObject<I> {

    protected Long id;

    protected final Map<String, DbValue> properties;

    protected DbEntity(final ObjectType recordType, final Long id, final Map<String, DbValue> properties) {
        super(recordType);
        this.id = id;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    @Deprecated
    public void setId(final Long id) {
        this.id = id;
    }

    public Map<String, DbValue> getProperties() {
        return properties;
    }

    public DbValue getProperty(final String key) {
        return this.properties.get(key);
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

    @Override
    public String abbreviate() {
        return objectType + "{id=" + Formats.number(id) + '}';
    }


    @Override
    public String toString() {
        return detailed();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final DbEntity dbEntity = (DbEntity) o;
        return Objects.equals(id, dbEntity.id)
               && Objects.equals(properties, dbEntity.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, properties);
    }

    protected static abstract class DbEntityBuilder<T extends DbEntity, B extends DbEntityBuilder<T, B>> {

        private final B myself;
        protected Long id = null;
        protected final Map<String, DbValue> properties = new HashMap<>();

        protected DbEntityBuilder(final Class<B> selfType) {
            this.myself = selfType.cast(this);
        }

        public B id(final int id) {
            return this.id((long) id);
        }

        public B id(final Long id) {
            this.id = id;
            return myself;
        }

        public B property(final String key, final Object value) {
            this.properties.put(key, DbValue.fromObject(value));
            return myself;
        }

        public B properties(final Map<String, Object> properties) {
            properties.forEach(this::property);
            return myself;
        }

        public abstract T build();

    }

}
