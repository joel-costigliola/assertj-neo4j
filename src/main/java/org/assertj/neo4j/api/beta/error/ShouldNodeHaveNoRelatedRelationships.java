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
import org.assertj.neo4j.api.beta.type.Relationships.DbRelationship;
import org.assertj.neo4j.api.beta.util.EntityUtils;
import org.assertj.neo4j.api.beta.util.Presentations;
import org.assertj.neo4j.api.beta.util.Utils;

import java.util.List;
import java.util.function.Function;

/**
 * @author patouche - 13/02/2021
 */
public class ShouldNodeHaveNoRelatedRelationships
        extends BasicEntityErrorMessageFactory<DbNode> {

    protected ShouldNodeHaveNoRelatedRelationships(
            final String direction,
            final DbNode actual,
            final List<DbRelationship> relationships) {
        super(
                "%nExpecting node:%n"
                + "  <%s>%n"
                + "to have no " + direction + " relationships but found:%n"
                + "  <%s>",
                actual,
                ArgDetail.included(Utils.title(direction) + " relationships:", EntityUtils.sorted(relationships)));
    }

    public static ShouldNodeHaveNoRelatedRelationships createIncoming(final DbNode actual,
                                                                      final List<DbRelationship> relationships) {
        return new ShouldNodeHaveNoRelatedRelationships("incoming", actual, relationships);
    }

    public static ShouldNodeHaveNoRelatedRelationships createOutgoing(final DbNode actual,
                                                                      final List<DbRelationship> relationships) {
        return new ShouldNodeHaveNoRelatedRelationships("outgoing", actual, relationships);
    }

    private static GroupingEntityErrorFactory<DbNode> elements(
            final List<DbNode> actual,
            final String direction,
            final Function<DbNode, EntityErrorMessageFactory<DbNode>> mapper,
            final List<DbRelationship> relationships) {
        return new BasicGroupingEntityErrorFactory<>(
                actual,
                mapper,
                "%nExpecting nodes:%n"
                + "  <%1$s>%n"
                + "to have no " + direction + " relationships but found:%n"
                + "  <%4$s>%n"
                + "which are " + direction + " relationships to nodes:",
                Presentations.outputIds(relationships)
        );
    }

    public static GroupingEntityErrorFactory<DbNode> incomingElements(
            final List<DbNode> actual, final List<DbRelationship> relationships) {
        return elements(
                actual, "incoming",
                (e) -> createIncoming(e, EntityUtils.areIncomingForNode(e, relationships)),
                relationships
        );
    }

    public static GroupingEntityErrorFactory<DbNode> outgoingElements(
            final List<DbNode> actual, final List<DbRelationship> relationships) {
        return elements(
                actual, "outgoing",
                (e) -> createOutgoing(e, EntityUtils.areOutgoingForNode(e, relationships)),
                relationships
        );
    }
}
