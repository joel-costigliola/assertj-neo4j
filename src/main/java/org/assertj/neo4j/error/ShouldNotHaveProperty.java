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
import org.neo4j.graphdb.PropertyContainer;

public class ShouldNotHaveProperty extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldNotHaveProperty}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param key the key used in the failed assertion to compare the actual property key to.
   * @param value the value used in the failed assertion to compare the actual property value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotHaveProperty(PropertyContainer actual, String key, Object value) {
    return new ShouldNotHaveProperty(actual, key, value);
  }

  private ShouldNotHaveProperty(PropertyContainer actual, String key, Object value) {
    super("\nExpecting:\n  <%s>\nnot to have property with key:\n  <%s>\nand value:\n  <%s>\nbut actually found such property %s",
        actual, key, value, StandardComparisonStrategy.instance());
  }
}
