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

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.util.Strings;
import org.assertj.neo4j.api.beta.type.Nodes;
import org.assertj.neo4j.api.beta.util.Entities;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 29/09/2020
 */
public class ElementsShouldHaveLabels extends BasicErrorMessageFactory {

    public ElementsShouldHaveLabels(
            final List<Nodes.DbNode> actual, final List<String> expectedLabels,
            final List<Missing<Nodes.DbNode, String>> missingActual) {
        super("%nExpecting nodes:%n"
              + "  <%s> to have all the following labels:%n"
              + "  <%s>%n"
              + "but some labels were missing on:%n%n"
              + Strings.escapePercent(describeItems(expectedLabels, missingActual)),
              Entities.outputIds(actual), expectedLabels
        );
    }

    /**
     * Create a new error when there is a missing labels on a {@link org.assertj.neo4j.api.beta.type.Nodes.DbNode}.
     *
     * @param actual                   the database nodes which have missing labels
     * @param expectedLabels          all expected labels
     * @param missingActual all missing labels
     * @return a new instance of {@link ElementsShouldHaveLabels}.
     */
    public static ElementsShouldHaveLabels create(
            final List<Nodes.DbNode> actual, final List<String> expectedLabels,
            final List<Missing<Nodes.DbNode, String>> missingActual) {
        return new ElementsShouldHaveLabels(actual, expectedLabels, missingActual);
    }

    private static String describeItems(
            final List<String> expectedLabels, final List<Missing<Nodes.DbNode, String>> items) {
        return items.stream()
                .map(item -> describeItem(expectedLabels, item))
                .collect(Collectors.joining(String.format("%n%n")));
    }

    private static String describeItem(final List<String> expectedLabels, final Missing<Nodes.DbNode, String> missing) {
        final Nodes.DbNode entity = missing.getEntity();
        final List<String> actualLabels = entity.getLabels().stream().sorted().collect(Collectors.toList());
        return String.format(
                "  - %s have missing labels: %s%n"
                + "    Actual: <%s>%n"
                + "    Expected: <%s>",
                Entities.outputId(entity), missing.getData(), actualLabels, expectedLabels
        );
    }

}
