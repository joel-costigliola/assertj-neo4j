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

import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.util.Utils;

import java.util.List;

/**
 * Creates an error message indicating that an assertion that verifies a node don't have the expected labels.
 *
 * @author Patrick Allain - 29/09/2020
 */
public class ShouldNodeHaveLabels extends BasicDbErrorMessageFactory<DbNode> {

    private ShouldNodeHaveLabels(final DbNode actual, final Iterable<String> labels) {
        super(
                "%nExpecting node labels:%n  <%2$s>%nto contain:%n  <%3$s>%n"
                + "but could not find the following labels:%n  <%4$s>%n",
                actual,
                ArgDetail.included("Actual labels", actual.getLabels()),
                ArgDetail.excluded(Utils.sorted(labels)),
                ArgDetail.included("Missing labels", Utils.missing(labels, actual.getLabels()))
        );
    }

    public static ShouldNodeHaveLabels create(final DbNode node, final Iterable<String> labels) {
        return new ShouldNodeHaveLabels(node, labels);
    }

    public static GroupingDbErrorFactory<DbNode> elements(final List<DbNode> actual,
                                                          final Iterable<String> labels) {
        return new BasicDbGroupingErrorFactory<>(
                actual,
                node -> create(node, labels),
                "%nExpecting nodes:%n"
                + "  <%1$s>%n"
                + "to have the labels:%n"
                + "  <%4$s>%n"
                + "but some labels are missing for nodes:",
                Utils.sorted(labels)
        );
    }

}
