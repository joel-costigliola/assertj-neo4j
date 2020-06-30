package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.neo4j.graphdb.QueryStatistics;

public class QueryStatisticsAssert extends AbstractAssert<QueryStatisticsAssert, QueryStatistics> {

	protected QueryStatisticsAssert(QueryStatistics actual) {
		super(actual, QueryStatisticsAssert.class);
	}

	public QueryStatistics getActual() {
		return actual;
	}

}
