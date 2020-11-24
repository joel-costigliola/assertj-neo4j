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

import org.junit.Test;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRecord;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.internal.value.StringValue;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * @author patouche - 29/09/2020
 */
public class DriverRecordListAssertTest {

    private static final List<Record> RECORDS = asList(
        new InternalRecord(
            asList("key-1", "key-2"),
            new Value[]{
                new NodeValue(
                    new InternalNode(1L, asList("label", "label-2"),
                        new HashMap<String, Value>() {{
                            put("prop-1", new StringValue("value-1"));
                            put("prop-2", new StringValue("value-2"));
                        }})
                )}
        ),
        new InternalRecord(
            asList("key-tool", "key-3 !"),
            new Value[]{
                new NodeValue(
                    new InternalNode(2L, asList("label", "label-rouge"),
                        new HashMap<String, Value>() {{
                            put("prop-1", new StringValue("value-1"));
                            put("prop-riete", new StringValue("prive"));
                        }})
                )}
        )
    );

    public static final DriverRecordListAssert NODE_ASSERT = new DriverRecordListAssert(RECORDS);

    @Test
    public void haveProperty_should_succeed() {
        // WHEN
        final DriverRecordListAssert label = NODE_ASSERT.haveProperty("prop-1");

        // THEN
        assertThat(label)
            .isSameAs(NODE_ASSERT);
    }

    @Test
    public void haveProperty_should_failed_when_one_nodes_dont_have_the_property() {
        // WHEN
        final DriverRecordListAssert label = NODE_ASSERT.haveProperty("prop-riete");

        // THEN
        assertThat(label)
            .isSameAs(NODE_ASSERT);
    }

    @Test
    public void haveLabel_should_succeed() {
        // WHEN
        final DriverRecordListAssert label = NODE_ASSERT.haveLabel("label");

        // THEN
        assertThat(label)
            .isSameAs(NODE_ASSERT);
    }

    @Test
    public void haveLabel_should_failed_when_one_nodes_dont_have_the_label() {
        // WHEN
        final Throwable throwable = catchThrowable(() -> NODE_ASSERT.haveLabel("label-rouge"));

        // THEN
        assertThat(throwable)
            .isInstanceOf(AssertionError.class)
            .hasMessageStartingWith("All nodes don't have the label 'label-rouge'")
            .hasMessageContaining("* Node[0] : [label]")
            .hasMessageContaining("* Node[1] : [label, label-rouge]");
    }

    @Test
    public void onNode_should_failed_if_node_with_id_does_not_exist() {
        // WHEN

        final Throwable throwable = catchThrowable(() -> NODE_ASSERT.onNode(42, (i) -> {}));

        // THEN
        assertThat(throwable)
            .isNotNull()
            .hasMessage("TODO");
    }

}
