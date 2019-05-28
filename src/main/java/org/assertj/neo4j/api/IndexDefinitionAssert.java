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
 * Copyright 2013-2017 the original author or authors.
 */

package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.IndexDefinition;

import static org.assertj.neo4j.error.ShouldHaveLabel.shouldHaveLabel;

/**
 * Assertions for Neo4J {@link org.neo4j.graphdb.Node}
 *
 * @author Agathe Vaisse
 * @author Gwenaelle Rispal
 */
public class IndexDefinitionAssert extends AbstractAssert<IndexDefinitionAssert, IndexDefinition> {

  protected IndexDefinitionAssert(IndexDefinition actual) {
    super(actual, IndexDefinitionAssert.class);
  }

  public IndexDefinition getActual() {
    return actual;
  }

  public IndexDefinitionAssert hasLabel(Label label) {

    if (label == null) {
      throw new IllegalArgumentException("The label to look for should not be null");

    }

    if (!actual.getLabel().equals(label)) {
      throw Failures.instance().failure(info, shouldHaveLabel(actual, label.name()));
    }
    return this;
  }

}

