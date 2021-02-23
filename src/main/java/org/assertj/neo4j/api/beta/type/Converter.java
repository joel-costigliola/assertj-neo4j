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

import java.util.Arrays;
import java.util.List;

/**
 * @param <O> the output type
 * @author Patrick Allain - 18/02/2021
 */
interface Converter<O> {

    /**
     * Check if the converter support this object.
     *
     * @param object the object to convert
     * @return {@code true} if the object is supported by the current converter. {@code false} otherwise.
     */
    boolean support(final Object object);

    /**
     * Convert an object into the expected output type.
     *
     * @param input the object to convert
     * @return the output or {@code null} if the underlying implementation don't throw an {@link Exception}
     * @throws UnsupportedOperationException if the conversion cannot be done by the converter
     */
    O convert(final Object input);

    class CompositeConverter<O> implements Converter<O> {

        private final List<Converter<? extends O>> converters;

        CompositeConverter(Converter<? extends O>... converters) {
            this(Arrays.asList(converters));
        }

        CompositeConverter(List<Converter<? extends O>> converters) {
            this.converters = converters;
        }

        @Override
        public boolean support(Object object) {
            return this.converters.stream().anyMatch(c -> c.support(object));
        }

        @Override
        public O convert(final Object input) {
            if (input == null) {
                return null;
            }
            return this.converters.stream()
                    .filter(c -> c.support(input))
                    .findFirst()
                    .map(c -> c.convert(input))
                    .orElse(null);
        }
    }

}

