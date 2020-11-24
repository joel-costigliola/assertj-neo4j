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
import org.assertj.neo4j.api.beta.output.Representations;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.Nodes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <ENTITY> the type of entity where this error occurred.
 * @author patouche - 29/09/2020
 */
public class ElementsShouldHaveLabels extends BasicErrorMessageFactory {

    public ElementsShouldHaveLabels(
            final List<String> expectedLabels,
            final List<Nodes.DbNode> nodes,
            final List<MissingNodeLabels> havingMissingNodeLabels) {
        super("%nExpecting:%n"
              + "  <%s> to have all the following labels:%n"
              + "  <%s>%n"
              + "but some labels where missing on:%n%n"
              + Strings.escapePercent(describeItems(expectedLabels, havingMissingNodeLabels)),
              Representations.ids(nodes), expectedLabels
        );
    }

    /**
     * Create a new error when there is a missing labels on a {@link org.assertj.neo4j.api.beta.type.Nodes.DbNode}.
     *
     * @param expectedLabels          all expected labels
     * @param nodes                   the database nodes which have missing labels
     * @param havingMissingNodeLabels all missing labels
     * @return a new instance of {@link ElementsShouldHaveLabels}.
     */
    public static <E extends DbEntity<E>> ElementsShouldHaveLabels create(
            final List<String> expectedLabels,
            final List<Nodes.DbNode> nodes,
            final List<MissingNodeLabels> havingMissingNodeLabels) {
        return new ElementsShouldHaveLabels(expectedLabels, nodes, havingMissingNodeLabels);
    }

    private static <E extends DbEntity<E>> String describeItems(
            final List<String> expectedLabels,
            final List<MissingNodeLabels> items) {
        return items.stream()
                .map(item -> describeItem(expectedLabels, item))
                .collect(Collectors.joining(String.format("%n%n")));
    }

    private static String describeItem(final List<String> expectedLabels, final MissingNodeLabels item) {
        final Nodes.DbNode entity = item.getNode();
        return String.format(
                "  - %s have missing labels: %s%n"
                + "    Actual: <%s>%n"
                + "    Expected: <%s>",
                Representations.id(entity), item.getMissingLabels(), entity.getLabels(), expectedLabels
        );
    }

}
