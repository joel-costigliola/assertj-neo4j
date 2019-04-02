package org.assertj.neo4j.api.index;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeAssert_isWriteable_Test {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Index index = mock(Index.class);

    @Test
    public void should_pass_if_index_is_writeable() {
        when(index.isWriteable()).thenReturn(true);

        assertThat(index).isWriteable();
    }

    @Test
    public void should_fail_if_index_is_not_writeable() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Expecting index");
        expectedException.expectMessage("to be writeable");

        when(index.isWriteable()).thenReturn(false);

        assertThat(index).isWriteable();
    }

    @Test
    public void should_fail_if_index_is_null() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage("Expecting actual not to be null");

        assertThat((Index) null).isWriteable();
    }
}
