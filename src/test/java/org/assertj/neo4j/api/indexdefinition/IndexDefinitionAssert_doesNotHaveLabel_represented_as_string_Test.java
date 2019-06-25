package org.assertj.neo4j.api.indexdefinition;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.schema.IndexDefinition;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Checks <code>{@link org.assertj.neo4j.api.IndexDefinitionAssert#doesNotHaveLabel(String)}</code> behavior.
 *
 * @author Agathe Vaisse
 * @author Gwenaelle Rispal
 */

public class IndexDefinitionAssert_doesNotHaveLabel_represented_as_string_Test {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private IndexDefinition indexDefinition = mock(IndexDefinition.class);

    @Test
    public void should_pass_if_index_definition_has_not_label() {
        given_index_definition_with_label("Captain Marvel");

        assertThat(indexDefinition).doesNotHaveLabel("Nebula");
    }

    @Test
    public void should_fail_if_index_definition_is_null() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Expecting actual not to be null");

        assertThat((IndexDefinition) null).doesNotHaveLabel(("Thanos"));
    }

    @Test
    public void should_fail_if_label_value_is_null() {
        expectedException.expect(IllegalArgumentException.class);

        assertThat(indexDefinition).doesNotHaveLabel((String) null);
    }

    @Test
    public void should_fail_if_index_definition_does_have_label() {
        expectedException.expect((AssertionError.class));

        given_index_definition_with_label("Rocket Raccoon");

        assertThat(indexDefinition).doesNotHaveLabel("Scarlet Witch");
    }

    private void given_index_definition_with_label(String value) {
        Label label = Label.label(value);
        when(indexDefinition.getLabel()).thenReturn(label);
    }
}
