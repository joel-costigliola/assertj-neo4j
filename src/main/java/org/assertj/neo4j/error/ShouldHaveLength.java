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
 * Copyright 2013-2019 the original author or authors.
 */
package org.assertj.neo4j.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.neo4j.graphdb.Path;

public class ShouldHaveLength extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link org.assertj.neo4j.error.ShouldHaveLength}</code>.
   * 
   * @param path the actual value in the failed assertion.
   * @param length the expected path length
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveLength(Path path, int length) {
    return new ShouldHaveLength(path, length);
  }

  private ShouldHaveLength(Path actual, int length) {
    super("\nExpecting:\n  <%s>\nto have length:\n  <%s>\n%s", actual, length, StandardComparisonStrategy.instance());
  }
}
