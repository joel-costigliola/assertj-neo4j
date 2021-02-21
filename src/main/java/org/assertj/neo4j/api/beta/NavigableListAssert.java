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

import java.util.List;

/**
 * @author Patrick Allain - 26/01/2021
 */
//@formatter:off
public class NavigableListAssert<ACTUAL, PARENT extends ParentalAssert, ROOT>
        extends AbstractListAssert<NavigableListAssert<ACTUAL, PARENT, ROOT>, List<ACTUAL>, ACTUAL, ObjectAssert<ACTUAL>>
        implements Navigable<PARENT, ROOT> {
//@formatter:on

    public NavigableListAssert(List<  ACTUAL> actual) {
    super(actual, NavigableListAssert.class);
}

    @Override
    public PARENT toParentAssert() {
        return null;
    }

    @Override
    public ROOT toRootAssert() {
        return null;
    }

    @Override
    protected ObjectAssert<ACTUAL> toAssert(ACTUAL value, String description) {
        return null;
    }

    @Override
    protected NavigableListAssert<ACTUAL, PARENT, ROOT> newAbstractIterableAssert(Iterable<? extends ACTUAL> iterable) {
        return null;
    }
}
