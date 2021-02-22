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

import org.assertj.neo4j.api.beta.testing.Builders;
import org.assertj.neo4j.api.beta.testing.builders.RecordBuilder;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.assertj.neo4j.api.beta.type.ValueType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Values;
import org.neo4j.driver.summary.ResultSummary;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Patrick Allain - 19/02/2021
 */
class DriverResultAssertTests {

    private static final List<RecordBuilder> SAMPLE_RECORDS_BUILDERS = Arrays.asList(
            Builders.record()
                    .node("n", Builders.node().id(1).property("name", "n-1").build())
                    .relation("r", Builders.relation("KNOWS").id(2).build())
                    .value("n.name", "n-1"),
            Builders.record()
                    .node("n", Builders.node().id(3).property("name", "n-2").build())
                    .relation("r", Builders.relation("KNOWS").id(4).build())
                    .value("n.name", "n-2")
    );

    private static class BaseResultTests {

        protected final Result result;
        private final List<RecordBuilder> recordBuilders;
        protected DriverResultAssert assertions;

        public BaseResultTests(RecordBuilder... builders) {
            this.recordBuilders = Arrays.asList(builders);
            this.result = Mockito.mock(Result.class);
        }

        @BeforeEach
        void setUp() {
            testCase(this.recordBuilders.toArray(new RecordBuilder[0]));
        }

        protected void testCase(final RecordBuilder... recordBuilders) {
            testCase(null, recordBuilders);
        }

        @SuppressWarnings("unchecked")
        protected void testCase(final ResultSummary summary, final RecordBuilder... recordBuilders) {
            final List<Record> records = Arrays.stream(recordBuilders)
                    .map(RecordBuilder::build)
                    .collect(Collectors.toList());
            reset(result);
            when(result.list(any())).then((args) -> records.stream()
                    .map(args.getArgument(0, Function.class))
                    .collect(Collectors.toList())
            );
            when(result.keys()).thenReturn(records.stream()
                    .map(Record::keys)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList())
            );
            when(result.consume()).thenReturn(summary);
            assertions = new DriverResultAssert(result);
        }

        @AfterEach
        protected void tearDown() {

            verify(this.result).list(any());
            verify(this.result).keys();
            verify(this.result).consume();

            verifyNoMoreInteractions(this.result);
        }
    }

    @Nested
    class HasRecordSizeTests extends BaseResultTests {

        HasRecordSizeTests() {
            super(SAMPLE_RECORDS_BUILDERS.toArray(new RecordBuilder[0]));
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.hasRecordSize(4));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll("Expected size:<4> but was:<2> in:");
        }

        @Test
        void should_pass() {
            // WHEN
            final DriverResultAssert result = assertions.hasRecordSize(2);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    class HasColumnSizeTests extends BaseResultTests {

        HasColumnSizeTests() {
            super(SAMPLE_RECORDS_BUILDERS.toArray(new RecordBuilder[0]));
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.hasColumnSize(14));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll("Expected size:<14> but was:<3> in:");
        }

        @Test
        void should_pass() {
            // WHEN
            final DriverResultAssert result = assertions.hasColumnSize(3);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    class HasColumnsTests extends BaseResultTests {

        HasColumnsTests() {
            super(SAMPLE_RECORDS_BUILDERS.toArray(new RecordBuilder[0]));
        }

        @Test
        void should_fail() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.hasColumns("n", "r", "missing"));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll("Expecting ArrayList:");
        }

        @Test
        void should_pass() {
            // WHEN
            final DriverResultAssert result = assertions.hasColumns("n", "r", "n.name");

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    class HaveTypeTests extends BaseResultTests {

        HaveTypeTests() {
            super(SAMPLE_RECORDS_BUILDERS.toArray(new RecordBuilder[0]));
        }

        @Test
        void should_fail() {
            // GIVEN
            testCase(
                    Builders.record().node("n", Builders.node().id(1).build()),
                    Builders.record().relation("n", Builders.relation("KNOWS").id(4).build()),
                    Builders.record().value("n", Values.value(3.14))
            );

            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveType("n", ObjectType.RELATIONSHIP));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting database objects:",
                            "to be of type:",
                            "but some database objects are from another type:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final DriverResultAssert result = assertions.haveType("n", ObjectType.NODE);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    class HaveValueTypeTests extends BaseResultTests {

        HaveValueTypeTests() {
            super(SAMPLE_RECORDS_BUILDERS.toArray(new RecordBuilder[0]));
        }

        @Test
        void should_fail_when_column_is_not_value() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveValueType("n", ValueType.BOOLEAN));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting database objects:",
                            "to be of type:",
                            "but some database objects are from another type:"
                    );
        }

        @Test
        void should_fail_when_bad_value_type_for_column() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveValueType("n.name", ValueType.FLOAT));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting values:",
                            "to have a value type:",
                            "but some values have a different value type:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final DriverResultAssert result = assertions.haveValueType("n.name", ValueType.STRING);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    class HaveValueInstanceOfTests extends BaseResultTests {

        HaveValueInstanceOfTests() {
            super(SAMPLE_RECORDS_BUILDERS.toArray(new RecordBuilder[0]));
        }

        @Test
        void should_fail_when_column_is_not_value() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveValueInstanceOf("n", Double.class));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting database objects:",
                            "to be of type:",
                            "but some database objects are from another type:"
                    );
        }

        @Test
        void should_fail_when_bad_value_type_for_column() {

            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.haveValueInstanceOf("n.name", Double.class));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting values:",
                            "to have values instance of:",
                            "but some values have value which are not instance of this class:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final DriverResultAssert result = assertions.haveType("n", ObjectType.NODE);

            // THEN
            assertThat(result).isSameAs(assertions);
        }

    }

    @Nested
    class AsListOfTests extends BaseResultTests {

        AsListOfTests() {
            super(SAMPLE_RECORDS_BUILDERS.toArray(new RecordBuilder[0]));
        }

        @Test
        void should_fail_when_column_dont_exists() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.asListOf("bad_column", String.class));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting ArrayList:",
                            "to contain:",
                            "but could not find the following element(s):"
                    );
        }

        @Test
        void should_fail_when_not_objects_are_not_value() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.asListOf("r", String.class));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting database objects:",
                            "to be of type:",
                            "but some database objects are from another type:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ChildrenListAssert<String, DriverResultAssert, DriverResultAssert> result =
                    assertions.asListOf("n.name", String.class);

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getActual()).contains("n-1", "n-2");
        }

        @Test
        void should_be_navigable() {
            // WHEN
            final ChildrenListAssert<String, DriverResultAssert, DriverResultAssert> result =
                    assertions.asListOf("n.name", String.class);

            // THEN
            assertThat(result.toParentAssert()).isSameAs(assertions);
        }

    }

    @Nested
    class AsListOfSingleColumnTests extends BaseResultTests {

        AsListOfSingleColumnTests() {
            super(
                    Builders.record().value("n.name", "n-1"),
                    Builders.record().value("n.name", "n-2")
            );
        }

        @Test
        void should_fail_when_multiple_columns() {
            // GIVEN
            testCase(
                    Builders.record().value("n.name", "n-1").value("n.owner", "o-1"),
                    Builders.record().value("n.name", "n-2").value("n.owner", "o-2")
            );

            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.asListOf(String.class));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expected size:<1> but was:<2> in:"
                    );
        }

        @Test
        void should_fail_when_column_is_not_value() {
            // GIVEN
            testCase(
                    Builders.record().node("n", Builders.node().id(1).property("k", "val-1").build()),
                    Builders.record().node("n", Builders.node().id(2).property("k", "val-2").build())
            );

            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.asListOf(String.class));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting database objects:",
                            "to be of type:"
                    );
        }

        @Test
        void should_fail_when_value_is_not_instance_of() {
            // WHEN
            final Throwable throwable = catchThrowable(() -> assertions.asListOf(Long.class));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(AssertionError.class)
                    .hasMessageContainingAll(
                            "Expecting values:",
                            "to have values instance of:",
                            "but some values have value which are not instance of this class:"
                    );
        }

        @Test
        void should_pass() {
            // WHEN
            final ChildrenListAssert<String, DriverResultAssert, DriverResultAssert> result =
                    assertions.asListOf(String.class);

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getActual()).contains("n-1", "n-2");
        }

        @Test
        void should_be_navigable() {
            // WHEN
            final ChildrenListAssert<String, DriverResultAssert, DriverResultAssert> result =
                    assertions.asListOf("n.name", String.class);

            // THEN
            assertThat(result.toParentAssert()).isSameAs(assertions);
        }

    }

}
