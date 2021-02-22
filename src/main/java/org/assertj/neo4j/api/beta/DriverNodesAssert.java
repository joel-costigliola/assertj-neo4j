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
package org.assertj.neo4j.api.beta;

import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;
import org.assertj.neo4j.api.beta.type.loader.Nodes;
import org.assertj.neo4j.api.beta.util.DbRepresentation;

import java.util.List;

/**
 * @author Patrick Allain - 08/11/2020
 */
//@formatter:off
public final class DriverNodesAssert
        extends AbstractNodesAssert<DriverNodesAssert,
                                    DriverNodesAssert,
                                    DriverNodesAssert,
                                    DriverNodesAssert> {
//@formatter:on

    /**
     * Create new assertions on {@link Nodes}.
     *
     * @param nodes the nodes to assert
     */
    public DriverNodesAssert(final Nodes nodes) {
        this(nodes.load(), nodes, null);
        this.info.useRepresentation(DbRepresentation.abbreviate());
    }

    private DriverNodesAssert(final List<DbNode> entities,
                              final DataLoader<DbNode> nodes,
                              final DriverNodesAssert parent) {
        super(
                DriverNodesAssert.class,
                entities,
                nodes,
                DriverNodesAssert::new,
                parent,
                rootAssert(parent)
        );
    }

    /** {@inheritDoc} */
    @Override
    public DriverNodesAssert toRootAssert() {
        return rootAssert().orElse(this);
    }
}
