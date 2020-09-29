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
package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.neo4j.error.QueryStatisticsErrorMessageFactory;
import org.neo4j.graphdb.QueryStatistics;

/**
 * Assertions for Neo4J {@link QueryStatistics}
 *
 * @author Lydiane Uhlen
 * @author Agathe Vaisse
 */
public class QueryStatisticsAssert extends AbstractAssert<QueryStatisticsAssert, QueryStatistics> {

    protected QueryStatisticsAssert(QueryStatistics actual) {
        super(actual, QueryStatisticsAssert.class);
    }

    protected QueryStatistics getActual() {
        return actual;
    }

    /**
     * Verifies that the actual {@link QueryStatistics} contains updates</br>
     * <p>
     * Example :
     *
     * <pre>
     * GraphDatabaseService graph = new TestGraphDatabaseFactory().newImpermanentDatabase();
     * QueryStatistics queryStatistics = graph.execute(&quot;MATCH (n) SET n.foo = 'bar'&quot;).getQueryStatistics();
     *
     * assertThat(queryStatistics).containsUpdates();
     * </pre>
     * </p>
     * <p>
     * <p>
     * If the <code>label</code> is {@code null}, an {@link IllegalArgumentException} is thrown.
     * <p>
     *
     * @return this {@link QueryStatisticsAssert} for assertions chaining
     * @throws AssertionError if the actual {@link QueryStatistics} does not contain updates
     */
    public QueryStatisticsAssert containsUpdates() {//HasRelationshipDelete(int)
        Objects.instance().assertNotNull(info, actual);

        if (!actual.containsUpdates()) {
            throw Failures.instance().failure(info, QueryStatisticsErrorMessageFactory.shouldContainsUpdates());
        }
        return this;
    }

    public QueryStatisticsAssert hasRelationshipDeleted(int statsdeleted) {
        //this.actual is the 
        int relationshipsDeleted = this.actual.getRelationshipsDeleted(); //ctr+alt+v = extraction variables
        //delete 
        boolean result = (relationshipsDeleted == statsdeleted);
        if (!result) {
            throw new AssertionError("RelationshipDeleted equals " + relationshipsDeleted + " but expected " + statsdeleted)
        }
        return this;
    }

}
