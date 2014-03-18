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

import static org.assertj.neo4j.error.ShouldHaveRowAtIndex.shouldHaveRowAtIndex;

final class SearchResultRow<T> {

  private final int visitedLines;
  private final T row;

  public SearchResultRow(int visitedLines, T row) {
    this.visitedLines = visitedLines;
    this.row = row;
  }

  public int getVisitedLines() {
    return visitedLines;
  }

  public T getValue() {
    return row;
  }
}
