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
package org.assertj.neo4j.api.beta.error;

import org.assertj.neo4j.api.beta.type.Nodes.DbNode;
import org.assertj.neo4j.api.beta.type.RecordType;
import org.assertj.neo4j.api.beta.util.Entities;

import java.util.List;

/**
 * @author patouche - 29/09/2020
 */
public class ElementsShouldHaveLabels extends DbEntitiesMessageFactory<DbNode, List<String>, Missing<DbNode, String>> {

    private ElementsShouldHaveLabels(
            final List<DbNode> actual, final List<String> expectedLabels,
            final List<Missing<DbNode, String>> missingActual) {
        super(
                RecordType.NODE,
                "to have all the following labels",
                "but some labels were missing on",
                actual, expectedLabels, missingActual, itemFactory()
        );
    }

    private static ItemMessageFactory<List<String>, Missing<DbNode, String>> itemFactory() {
        return (expected, missing) -> String.format(
                "  - %s have missing labels: %s%n"
                + "      Expected: %s"
                + "      Actual  : %s%n",
                Entities.outputId(missing.getEntity()), missing.getData(), expected,
                missing.getEntity().getSortedLabels()
        );
    }

    /**
     * Create a new error when there is a missing labels on a {@link DbNode}.
     *
     * @param actual         the database nodes which have missing labels
     * @param expectedLabels all expected labels
     * @param missingActual  all missing labels
     * @return a new instance of {@link ElementsShouldHaveLabels}.
     */
    public static ElementsShouldHaveLabels create(
            final List<DbNode> actual, final List<String> expectedLabels,
            final List<Missing<DbNode, String>> missingActual) {
        return new ElementsShouldHaveLabels(actual, expectedLabels, missingActual);
    }

}
