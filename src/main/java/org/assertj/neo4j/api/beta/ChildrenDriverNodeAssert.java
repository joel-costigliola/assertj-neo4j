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
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.loader.DataLoader;

import java.util.List;
import java.util.Objects;

/**
 * Children assertion on {@link DbRelationship}.
 *
 * @param <ROOT_ASSERT>   the root assertion type
 * @param <PARENT_ASSERT> the parent assertion type
 * @author Patrick Allain - 02/01/2021
 */
//@formatter:off
public class ChildrenDriverNodeAssert<PARENT_ASSERT extends ParentAssert<ROOT_ASSERT>, ROOT_ASSERT>
        extends AbstractNodesAssert<ChildrenDriverNodeAssert<PARENT_ASSERT, ROOT_ASSERT>,
                                    ChildrenDriverNodeAssert<ChildrenDriverNodeAssert<PARENT_ASSERT, ROOT_ASSERT>, ROOT_ASSERT>,
                                    PARENT_ASSERT,
                                    ROOT_ASSERT>
        implements Adoptable<ROOT_ASSERT> {
//@formatter:on

    protected ChildrenDriverNodeAssert(final List<DbNode> entities,
                                       final DataLoader<DbNode> loader,
                                       final PARENT_ASSERT parentAssert) {
        super(
                ChildrenDriverNodeAssert.class,
                entities,
                loader,
                ChildrenDriverNodeAssert::new,
                Objects.requireNonNull(parentAssert, "The parent assertion cannot be null."),
                Objects.requireNonNull(parentAssert.toRootAssert(), "The root assertion cannot be null.")
        );
    }

    /** {@inheritDoc} */
    @Override
    public <NEW_PARENT extends ParentAssert<ROOT_ASSERT>> ChildrenDriverNodeAssert<NEW_PARENT, ROOT_ASSERT> withParent(final NEW_PARENT parentAssert) {
        return new ChildrenDriverNodeAssert<>(actual, dataLoader, parentAssert);
    }

}
