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
package org.assertj.neo4j.api.beta.util;

/**
 * WORK IN PROGRESS.
 *
 * TODO : SHOULD BE DELETED !
 *
 * @author Patrick Allain - 14/11/2020
 */
public class Wip {

    private static <T> RuntimeException todo(final String msg) {
        throw new RuntimeException("TODO: " + msg);
    }

    public static <T> RuntimeException TODO(final Class<T> clazz) {
        return todo(clazz.getName());
    }

    public static <T> RuntimeException TODO(final T myself) {
        return TODO(myself.getClass());
    }

    public static <T> RuntimeException TODO(final T myself, final String message) {
        return todo(myself.getClass().getName() + " => " + message);
    }
}