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

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.util.VisibleForTesting;

import java.util.List;

/**
 * @author Patrick Allain - 26/01/2021
 */
//@formatter:off
public class ChildrenListAssert<ACTUAL, PARENT_ASSERT extends ParentalAssert, ROOT_ASSERT>
        extends AbstractListAssert<ChildrenListAssert<ACTUAL, PARENT_ASSERT, ROOT_ASSERT>, List<ACTUAL>, ACTUAL, ObjectAssert<ACTUAL>>
        implements Navigable<PARENT_ASSERT, ROOT_ASSERT>, Adoptable<ROOT_ASSERT> {private final PARENT_ASSERT parentAssert;private final ROOT_ASSERT rootAssert;
//@formatter:on

    public ChildrenListAssert(List<ACTUAL> actual, PARENT_ASSERT parentAssert, ROOT_ASSERT rootAssert) {
        super(actual, ChildrenListAssert.class);
        this.parentAssert = parentAssert;
        this.rootAssert = rootAssert;
    }

    @VisibleForTesting
    public List<ACTUAL> getActual() {
        return this.actual;
    }

    /** {@inheritDoc} */
    @Override
    public PARENT_ASSERT toParentAssert() {
        return this.parentAssert;
    }

    /** {@inheritDoc} */
    @Override
    public ROOT_ASSERT toRootAssert() {
        return this.rootAssert;
    }

    @Override
    protected ObjectAssert<ACTUAL> toAssert(ACTUAL value, String description) {
        return null;
    }

    @Override
    protected ChildrenListAssert<ACTUAL, PARENT_ASSERT, ROOT_ASSERT> newAbstractIterableAssert(Iterable<?
            extends ACTUAL> iterable) {
        return null;
    }

    @Override
    public <NEW_PARENT extends ParentalAssert> Navigable<NEW_PARENT, ROOT_ASSERT> withParent(NEW_PARENT parentAssert) {
        return new ChildrenListAssert<>(actual, parentAssert, toRootAssert());
    }
}
