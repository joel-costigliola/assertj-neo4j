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
 * Copyright 2013-2019 the original author or authors.
 */
package org.assertj.neo4j.api.constraintedefinition;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.ConstraintDefinition;

import static org.junit.Assert.assertNotNull;
import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConstraintDefinitionAssert_doesNotHaveLabel_Test {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ConstraintDefinition constraintDefinition = mock(ConstraintDefinition.class);

    @Test
    public void  should_pass_if_constraint_definition_does_not_have_label(){
        given_constraint_definition_with_label("CanibalCorps");

        assertNotNull(assertThat(constraintDefinition).doesNotHaveLabel(Label.label("Lorie")));
    }

    @Test
    public void should_fail_if_constraint_definition_is_null() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Expecting actual not to be null");

        assertThat((ConstraintDefinition) null).doesNotHaveLabel(Label.label("System of a down"));
    }

    @Test
    public void should_fail_if_label_value_is_null(){
        expectedException.expect(IllegalArgumentException.class);

        assertThat(constraintDefinition).doesNotHaveLabel((Label) null);
    }

    @Test
    public void should_fail_if_constraint_definition_has_label() {
        expectedException.expect(AssertionError.class);

        given_constraint_definition_with_label("The Beatles");

        assertThat(constraintDefinition).doesNotHaveLabel(Label.label("The Beatles"));
    }

    private void given_constraint_definition_with_label(String value) {
        Label label = Label.label(value);
        when(constraintDefinition.getLabel()).thenReturn(label);
    }

}
