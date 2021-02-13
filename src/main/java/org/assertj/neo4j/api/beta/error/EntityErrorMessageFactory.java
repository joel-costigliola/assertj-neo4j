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
package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.type.DbEntity;

import java.util.List;
import java.util.Objects;

/**
 * Factory for entity error messages.
 * <p/>
 * This factory is intent to be attach to parent message factory to have a nice message with the list of all entities
 * which didn't satisfies the condition.
 *
 * @author Patrick Allain - 03/02/2021
 */
interface EntityErrorMessageFactory<ENTITY extends DbEntity<ENTITY>> extends ErrorMessageFactory {

    /**
     * Retrieve the actual entity that will is related to the {@link ErrorMessageFactory}.
     *
     * @return the entity.
     */
    ENTITY entity();

    /**
     * Provide details about the failing assertion.
     * <p/>
     * This method will be use to describe a list of entities that don't fill the assertion.
     *
     * @return the error details
     */
    List<ArgDetail> details();

    /**
     * Argument details. Only the {@link ArgDetail} having a title will be included in the {@link #details()} list.
     */
    class ArgDetail {

        private final String title;
        private final Object value;

        private ArgDetail(final String title, final Object value) {
            this.title = title;
            this.value = value;
        }

        /**
         * Create a new {@link ArgDetail} for an {@link EntityErrorMessageFactory} that will be included in the result
         * {@link #details()}.
         *
         * @param title the title of error detail.
         * @param value the error detail value.
         * @return a new {@link ArgDetail}
         */
        static ArgDetail included(final String title, final Object value) {
            return new ArgDetail(Objects.requireNonNull(title, "Title cannot be null for included argument@"), value);
        }

        /**
         * Create a new {@link ArgDetail} for an {@link EntityErrorMessageFactory} that will not be included in the
         * result {@link #details()}.
         *
         * @param value the error detail value.
         * @return a new {@link ArgDetail}
         */
        static ArgDetail excluded(final Object value) {
            return new ArgDetail(null, value);
        }

        public boolean isIncluded() {
            return this.title != null;
        }

        public String title() {
            return title;
        }

        public Object value() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ArgDetail that = (ArgDetail) o;
            return Objects.equals(title, that.title)
                   && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, value);
        }

        @Override
        public String toString() {
            return "ErrorDetail{title='" + title + "', value=" + value + '}';
        }
    }
}
