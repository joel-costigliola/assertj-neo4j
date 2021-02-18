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

import java.util.function.Function;

/**
 * Converter.
 *
 * @param <I> the input type
 * @param <O> the output type
 */
class ConverterType<I, O> implements Converter<O> {

    private final Class<I> fromClass;
    private final Function<I, O> function;

    ConverterType(final Class<I> fromClass, final Function<I, O> function) {
        this.fromClass = fromClass;
        this.function = function;
    }

    @Override
    public boolean support(Object object) {
        return fromClass.isInstance(object);
    }

    @SuppressWarnings("unchecked")
    public O convert(final Object input) {
        if (!fromClass.isInstance(input)) {
            throw new UnsupportedOperationException("Cannot convert object " + input + " as it isn't a instance "
                                                    + "of " + fromClass);
        }
        return this.function.apply((I) input);
    }
}
