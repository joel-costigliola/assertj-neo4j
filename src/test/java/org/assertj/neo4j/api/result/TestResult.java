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
package org.assertj.neo4j.api.result;

import org.neo4j.graphdb.ExecutionPlanDescription;
import org.neo4j.graphdb.Notification;
import org.neo4j.graphdb.QueryExecutionType;
import org.neo4j.graphdb.QueryStatistics;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class TestResult implements Result {

  private final Iterator<Map<String, Object>> items;

  public TestResult(Collection<Map<String, Object>> items) {
    this.items = items.iterator();
  }

  @Override
  public QueryExecutionType getQueryExecutionType() {
    return null;
  }

  @Override
  public List<String> columns() {
    return null;
  }

  @Override
  public <T> ResourceIterator<T> columnAs(String s) {
    return null;
  }

  @Override
  public boolean hasNext() {
    return items.hasNext();
  }

  @Override
  public Map<String, Object> next() {
    return items.next();
  }

  @Override
  public void close() {

  }

  @Override
  public QueryStatistics getQueryStatistics() {
    return null;
  }

  @Override
  public ExecutionPlanDescription getExecutionPlanDescription() {
    return null;
  }

  @Override
  public String resultAsString() {
    return null;
  }

  @Override
  public void writeAsStringTo(PrintWriter printWriter) {

  }

  @Override
  public void remove() {
    items.remove();
  }

  @Override
  public Iterable<Notification> getNotifications() {
    return null;
  }

  @Override
  public <VisitationException extends Exception> void accept(ResultVisitor<VisitationException> resultVisitor) throws VisitationException {

  }
}
