/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright 2013-2014 the original author or authors.
 */
package org.assertj.neo4j.error;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.neo4j.cypher.javacompat.ExecutionResult;

public class ShouldHaveRowAtIndex extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldHaveProperty}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param rowIndex the row index used in the failed assertion to find the column names/values.
   * @param resultSize the size of the actual execution result.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveRowAtIndex(Object actual, int rowIndex, int resultSize) {
    return new ShouldHaveRowAtIndex(actual, rowIndex, resultSize);
  }

  private ShouldHaveRowAtIndex(Object actual, int givenIndex, int actualSize) {
    super("\nExpecting:\n  <%s>\nto include row at index:  <%s>\nbut has size:  <%s>", actual, givenIndex, actualSize,
        StandardComparisonStrategy.instance());
  }

}
