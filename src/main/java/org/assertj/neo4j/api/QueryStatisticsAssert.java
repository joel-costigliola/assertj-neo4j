package org.assertj.neo4j.api;

import static org.assertj.neo4j.error.ShouldNotHaveLabel.shouldNotHaveLabel;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.neo4j.error.QueryStatisticsErrorMessageFactory;
import org.neo4j.graphdb.QueryStatistics;

public class QueryStatisticsAssert extends AbstractAssert<QueryStatisticsAssert, QueryStatistics> {

	protected QueryStatisticsAssert(QueryStatistics actual) {
		super(actual, QueryStatisticsAssert.class);
	}

	protected QueryStatistics getActual() {
		return actual;
	}

	public QueryStatisticsAssert containsUpdates() {
		Objects.instance().assertNotNull(info, actual);
		
		if (!actual.containsUpdates()) {
			throw Failures.instance().failure(info, QueryStatisticsErrorMessageFactory.shouldContainsUpdates());
		}
		return this;			
	}

	
}
