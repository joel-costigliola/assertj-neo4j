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

import org.assertj.core.api.FactoryBasedNavigableListAssert;
import org.assertj.neo4j.api.beta.error.ShouldAllHaveLabel;
import org.assertj.neo4j.api.beta.error.ShouldAllHaveProperty;
import org.assertj.neo4j.api.beta.util.Wip;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Assertions on a list of {@link Record} result. A {@link Record} wrap {@link Node} or
 * {@link org.neo4j.driver.types.Relationship}
 *
 * @author patouche - 5/26/20.
 */
public class DriverRecordListAssert
        extends FactoryBasedNavigableListAssert<DriverRecordListAssert, List<Record>, Record, RecordAssert> {

    public static final RecordAssert.Factory RECORD_ASSERT_FACTORY = new RecordAssert.Factory();

    public DriverRecordListAssert(final List<Record> records) {
        super(records, DriverRecordListAssert.class, RECORD_ASSERT_FACTORY);
    }

    private Stream<Node> nodes() {
        return this.actual.stream().map(r -> r.values().get(0).asNode());
    }

    public DriverRecordListAssert haveProperty(final String key) {
        isNotEmpty();
        if (!this.nodes().allMatch(n -> n.asMap().containsKey(key))) {
            /* throw */
            throwAssertionError(ShouldAllHaveProperty.create(key, nodes()));
        }
        throw Wip.TODO(this);
    }

    public DriverRecordListAssert haveLabel(final String labelName) {
        isNotEmpty();
        if (!this.nodes().allMatch(n -> n.hasLabel(labelName))) {
            /* throw */
            throwAssertionError(ShouldAllHaveLabel.create(labelName, nodes()));
        }
        return this.myself;
    }

    public DriverRecordListAssert onNode(final int id, final Consumer<DriverNodeAssert> consumer) {
        isNotEmpty();
        final Node node = nodes()
                .filter(n -> Objects.equals(n.id(), id))
                .findFirst()
                .orElseThrow(() -> Wip.TODO(this));

//        final Object apply = consummer.accept(new DriverNodeAssert(node));

        throw Wip.TODO(this);
//        return this.myself;
    }
}
