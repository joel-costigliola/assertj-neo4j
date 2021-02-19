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
package org.assertj.neo4j.api.beta.type;

import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;

import java.text.ChoiceFormat;
import java.text.Format;

/**
 * Object type.
 *
 * @author Patrick Allain - 02/11/2020
 */
public enum ObjectType {

    NODE(Node.class, new ChoiceFormat("1#node| 1<nodes")),
    RELATIONSHIP(Relationship.class, new ChoiceFormat("1#relationship| 1<relationships")),
    VALUE(Value.class, new ChoiceFormat("1#value| 1<values")),

    // FIXME: NOT USED
    PATH(Path.class, new ChoiceFormat("1#path| 1<paths")),
    RECORD(Record.class, new ChoiceFormat("1#record| 1<records")),

    ;
    private final Class<?> clazz;
    private final Format fmt;

    ObjectType(final Class<?> clazz, final Format fmt) {
        this.clazz = clazz;
        this.fmt = fmt;
    }

    public String format(double i) {
        return fmt.format(i);
    }

}
