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

import org.assertj.core.api.IterableAssert;
import org.neo4j.graphdb.Result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResultAssert extends IterableAssert<Map<String, Object>> {

  public ResultAssert(Result result) {
    super(convertToIterable(result));
  }

  private static Iterable<Map<String, Object>> convertToIterable(Iterator<Map<String, Object>> iterator) {
    List<Map<String, Object>> result = new ArrayList<>();
    for (Iterator<Map<String, Object>> rows = iterator; rows.hasNext(); ) {
      result.add(rows.next());
    }
    return result;
  }
}
