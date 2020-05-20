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
package org.assertj.neo4j.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveRelationshipType extends BasicErrorMessageFactory {

  private ShouldHaveRelationshipType(Object actual, String other) {
    super("\nExpecting:\n  <%s>\nto have relationship type:\n  <%s>\n", actual, other);
  }

  /**
   * Creates a new </code>{@link org.assertj.neo4j.error.ShouldHaveRelationshipType}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param relationshipTypeName the relationship type name used in the failed assertion to compare the actual
   *          relationship type name to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveRelationshipType(Object actual, String relationshipTypeName) {
    return new ShouldHaveRelationshipType(actual, relationshipTypeName);
  }

}
