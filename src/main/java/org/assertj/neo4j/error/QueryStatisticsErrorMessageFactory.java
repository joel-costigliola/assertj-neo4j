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

public class QueryStatisticsErrorMessageFactory extends BasicErrorMessageFactory {

  private QueryStatisticsErrorMessageFactory(String format, Object... arguments) {
    super(format, arguments);
  }

  public static QueryStatisticsErrorMessageFactory shouldContainsUpdates() {
    return new QueryStatisticsErrorMessageFactory("Expecting actual to contain updates but does not");
  }

}
