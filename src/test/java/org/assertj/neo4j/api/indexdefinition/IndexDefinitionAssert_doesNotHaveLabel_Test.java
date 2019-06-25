package org.assertj.neo4j.api.indexdefinition;

import org.assertj.neo4j.api.IndexDefinitionAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.IndexDefinition;
import scala.reflect.internal.tpe.TypeMaps;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Checks <code>{@link org.assertj.neo4j.api.IndexDefinitionAssert#doesNotHaveLabel(Label)}</code> behavior.
 *
 * @author Agathe Vaisse
 * @author Gwenaelle Rispal
 */

public class IndexDefinitionAssert_doesNotHaveLabel_Test {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private IndexDefinition indexDefinition = mock(IndexDefinition.class);

    @Test
    public void should_pass_if_index_definition_does_not_have_label() {
        given_index_definition_with_label("Gamora");

        assertThat(indexDefinition).doesNotHaveLabel(Label.label("Gamora"));
    }

    @Test
    public void should_fail_if_index_definition_is_null() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Expecting actual not to be null");

        assertThat((IndexDefinition) null).doesNotHaveLabel(Label.label("Star Lord"));
    }

    @Test
    public void should_fail_if_label_value_is_null() {
        expectedException.expect(IllegalArgumentException.class);

        assertThat(indexDefinition).doesNotHaveLabel((Label) null);
    }


    @Test
    public void should_fail_if_index_definition_has_label() {
        expectedException.expect(AssertionError.class);

        given_index_definition_with_label("Nick Fury");

        assertThat(indexDefinition).doesNotHaveLabel(Label.label("Nick Fury"));
    }

    private void given_index_definition_with_label(String value) {
        Label label = Label.label(value);
        when(indexDefinition.getLabel()).thenReturn(label);
    }
}

