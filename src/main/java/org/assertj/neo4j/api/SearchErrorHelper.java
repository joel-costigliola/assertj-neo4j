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
package org.assertj.neo4j.api;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;

import static java.lang.String.format;
import static org.assertj.neo4j.error.ShouldHaveRowAtIndex.shouldHaveRowAtIndex;

class SearchErrorHelper {

  public static void checkIndexInBound(int rowIndex, int rowCount, AssertionInfo info, Object actual) {
    if (rowIndex >= rowCount) {
      throw Failures.instance().failure(info, shouldHaveRowAtIndex(actual, rowIndex, rowCount));
    }
  }

  public static void checkIndexAccess(int rowIndex, Integer previousIndex, Class<?> assertionClass) {
    if (rowIndex < 0) {
      throw new IllegalArgumentException(
          format("\nThe specified row index should be positive, but was <%s>.", rowIndex));
    }

    if (previousIndex != null && rowIndex <= previousIndex) {
      throw new IllegalArgumentException(format(ExecutionResultAssert.SUBSEQUENT_CALL_ERROR_MESSAGE,
          assertionClass.getSimpleName(), previousIndex, rowIndex));
    }
  }
}
