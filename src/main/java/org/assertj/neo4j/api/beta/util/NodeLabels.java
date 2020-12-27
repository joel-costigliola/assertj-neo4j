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
package org.assertj.neo4j.api.beta.util;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Streams;
import org.assertj.neo4j.api.beta.error.Missing;
import org.assertj.neo4j.api.beta.type.Nodes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 14/11/2020
 */
public final class NodeLabels {

    public static boolean hasLabels(final Nodes.DbNode node, final Iterable<String> expectedLabels) {
        return !node.getLabels().containsAll(Lists.newArrayList(expectedLabels));
    }

    /**
     * Test if one of the provided nodes don't have onf of the expected labels.
     *
     * @param nodes          the list of nodes
     * @param expectedLabels the expected labels
     * @return {@code true} if one of the nodes don't have a expected labels. {@code false} otherwise.
     */
    public static boolean haveLabels(final List<Nodes.DbNode> nodes, final Iterable<String> expectedLabels) {
        return nodes.stream().anyMatch(e -> hasLabels(e, expectedLabels));
    }

    /**
     * Create a new
     *
     * @param node
     * @param labels
     * @return
     */
    public static Missing<Nodes.DbNode, String> missing(final Nodes.DbNode node, final Iterable<String> labels) {
        final List<String> missingLabels = Streams.stream(labels)
                .filter(expectedLabel -> !node.getLabels().contains(expectedLabel))
                .collect(Collectors.toList());
        return new Missing<>(node, missingLabels);
    }

    public static List<Missing<Nodes.DbNode, String>> missing(final List<Nodes.DbNode> nodes,
                                                              final List<String> labels) {
        return nodes.stream()
                .map(node -> NodeLabels.missing(node, labels))
                .filter(Missing::hasMissing)
                .collect(Collectors.toList());
    }

}
