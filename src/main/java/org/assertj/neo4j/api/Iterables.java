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
package org.assertj.neo4j.api;

import java.util.List;
import java.util.stream.Collectors;

class Iterables {

  public static <T> List<T> difference(Iterable<T> left, Iterable<T> right) {
    return Streams.create(left).filter(key -> Streams.create(right).noneMatch(key::equals)).collect(Collectors.toList());
  }

  public static <T> List<T> intersection(Iterable<T> left, Iterable<T> right) {
    return Streams.create(left).filter(key -> Streams.create(right).anyMatch(key::equals)).collect(Collectors.toList());
  }
}
