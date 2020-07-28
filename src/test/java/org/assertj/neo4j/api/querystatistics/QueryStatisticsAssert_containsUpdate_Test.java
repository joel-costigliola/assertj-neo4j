package org.assertj.neo4j.api.querystatistics;

import static org.assertj.neo4j.api.Assertions.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import org.assertj.neo4j.api.Assertions;
import org.assertj.neo4j.api.QueryStatisticsAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.neo4j.graphdb.QueryStatistics;

public class QueryStatisticsAssert_containsUpdate_Test {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	QueryStatistics mock = Mockito.mock(QueryStatistics.class);

	@Test
	public void should_pass_if_query_statistics_does_contain_updates() {
		Mockito.when(mock.containsUpdates()).thenReturn(true);
		Assert.assertThat(Assertions.assertThat(mock).containsUpdates(), instanceOf(QueryStatisticsAssert.class));
	}

	@Test
	public void should_fail_if_query_statistics_does_not_contain_updates() {
		expectedException.expect(AssertionError.class);
		expectedException.expectMessage("Expecting actual to contain updates but doesn't");
		Mockito.when(mock.containsUpdates()).thenReturn(false);
		Assertions.assertThat(mock).containsUpdates();
	}

	@Test
	public void should_fail_with_custom_message_if_query_statistics_does_not_contain_updates() {
		expectedException.expect(AssertionError.class);
		expectedException.expectMessage("Custom error FTW");
		Mockito.when(mock.containsUpdates()).thenReturn(false);
		assertThat(mock).overridingErrorMessage("Custom error FTW").containsUpdates();
	}
	
	@Test
	public void should_fail_if_query_statistics_is_null() {
		expectedException.expect(AssertionError.class);
		expectedException.expectMessage("Expecting actual not to be null");
		assertThat((QueryStatistics)null).containsUpdates();
	}
	
	

}
