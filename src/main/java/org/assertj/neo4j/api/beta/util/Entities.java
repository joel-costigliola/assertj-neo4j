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
import org.assertj.neo4j.api.beta.error.MissingNodeLabels;
import org.assertj.neo4j.api.beta.type.DbEntity;
import org.assertj.neo4j.api.beta.type.Nodes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pallain - 14/11/2020
 */
public final class Entities {

    public static boolean hasMissingLabels(
            final Nodes.DbNode node,
            final Iterable<String> expectedLabels) {
        return !node.getLabels().containsAll(Lists.newArrayList(expectedLabels));
    }

    public static boolean haveMissingLabels(
            final List<Nodes.DbNode> nodes,
            final Iterable<String> expectedLabels) {
        return nodes.stream().anyMatch(e -> hasMissingLabels(e, expectedLabels));
    }

    public static MissingNodeLabels missingLabels(
            final Nodes.DbNode node,
            final Iterable<String> expectedLabels) {
        final List<String> missingLabels = Streams.stream(expectedLabels)
                .filter(expectedLabel -> !node.getLabels().contains(expectedLabel))
                .collect(Collectors.toList());
        return new MissingNodeLabels(node, missingLabels);
    }

    public static List<MissingNodeLabels> havingMissingLabels(
            final List<Nodes.DbNode> nodes,
            final List<String> expectedLabels) {
        return nodes.stream()
                .map(node -> Entities.missingLabels(node, expectedLabels))
                .filter(MissingNodeLabels::hasMissing)
                .collect(Collectors.toList());
    }

}
