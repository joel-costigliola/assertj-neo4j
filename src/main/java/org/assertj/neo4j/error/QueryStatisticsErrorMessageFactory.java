package org.assertj.neo4j.error;

import org.assertj.core.error.BasicErrorMessageFactory;

public class QueryStatisticsErrorMessageFactory extends BasicErrorMessageFactory{

	private QueryStatisticsErrorMessageFactory(String format, Object... arguments) {
		super(format, arguments);
	}
	
	 public static QueryStatisticsErrorMessageFactory shouldContainsUpdates() {
		    return new QueryStatisticsErrorMessageFactory("Expecting actual to contain updates but doesn't");
		  }

}
