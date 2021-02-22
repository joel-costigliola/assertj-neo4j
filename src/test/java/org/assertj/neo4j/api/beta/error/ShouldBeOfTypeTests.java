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
package org.assertj.neo4j.api.beta.error;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.neo4j.api.beta.testing.Randomize;
import org.assertj.neo4j.api.beta.testing.Samples;
import org.assertj.neo4j.api.beta.type.DbNode;
import org.assertj.neo4j.api.beta.type.DbObject;
import org.assertj.neo4j.api.beta.type.DbRelationship;
import org.assertj.neo4j.api.beta.type.DbValue;
import org.assertj.neo4j.api.beta.type.ObjectType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 20/02/2021
 */
class ShouldBeOfTypeTests {

    @Nested
    class CreateTests {

        @Test
        void should_generate_error_message() {
            // GIVEN
            final ObjectType type = ObjectType.VALUE;
            final DbNode actual = Samples.NODE;

            // WHEN
            final ErrorMessageFactory result = ShouldObjectBeOfType.create(actual, type);

            // THEN
            assertThat(result.create()).isEqualToNormalizingNewlines(
                    "\nExpecting node:\n"
                    + "  <NODE{id=42L, labels=[\"LBL_1\", \"LBL_2\", \"LBL_3\", \"LBL_4\"], "
                    + "properties={date=DATE{2020-02-03}, localtime=LOCAL_TIME{04:05:06.000000007}, "
                    + "duration=DURATION{P0M0DT259200S}, datetime=DATE_TIME{2020-02-03T04:05:06"
                    + ".000000007+11:00[Australia/Sydney]}, boolean=BOOLEAN{true}, string=STRING{\"str-value\"}, "
                    + "double=FLOAT{4.2}, point_3d=POINT{Point{srid=0, x=42.0, y=12.0, z=69.0}}, "
                    + "point_2d=POINT{Point{srid=0, x=42.0, y=12.0}}, time=TIME{04:05:06.000000007Z}, "
                    + "localdatetime=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}, long=INTEGER{42}}}>\n"
                    + "to have type:\n"
                    + "  <VALUE>\n"
                    + "but current type is:\n"
                    + "  <NODE>\n"
            );
        }
    }

    @Nested
    class ElementsTests {

        @Test
        void should_generate_an_aggregate_error_message() {
            // GIVEN
            final ObjectType expectedType = ObjectType.RELATIONSHIP;
            final DbNode obj1 = Samples.NODE;
            final DbRelationship obj2 = Samples.RELATIONSHIP;
            final DbValue obj3 = DbValue.fromObject("value");
            final DbValue obj4 = DbValue.fromObject(true);
            final DbValue obj5 = DbValue.fromObject("other-value");
            final DbValue obj6 = DbValue.fromObject(3.14);

            final List<DbObject<?>> actual = Randomize.listOf(obj1, obj2, obj3, obj4, obj5, obj6);

            // WHEN
            final ErrorMessageFactory error = ShouldObjectBeOfType
                    .elements(new ArrayList<>(actual), expectedType)
                    .notSatisfies(Randomize.listOf(obj1, obj3, obj4, obj5, obj6));

            // THEN
            assertThat(error.create()).isEqualToNormalizingNewlines(
                    "\nExpecting database objects:\n"
                    + "  <[NODE{id=42L, labels=[\"LBL_1\", \"LBL_2\", \"LBL_3\", \"LBL_4\"], "
                    + "properties={date=DATE{2020-02-03}, localtime=LOCAL_TIME{04:05:06.000000007}, "
                    + "duration=DURATION{P0M0DT259200S}, datetime=DATE_TIME{2020-02-03T04:05:06"
                    + ".000000007+11:00[Australia/Sydney]}, boolean=BOOLEAN{true}, string=STRING{\"str-value\"}, "
                    + "double=FLOAT{4.2}, point_3d=POINT{Point{srid=0, x=42.0, y=12.0, z=69.0}}, "
                    + "point_2d=POINT{Point{srid=0, x=42.0, y=12.0}}, time=TIME{04:05:06.000000007Z}, "
                    + "localdatetime=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}, long=INTEGER{42}}},\n"
                    + "    RELATIONSHIP{id=42L, type='SAMPLE_TYPE', start=null, end=null, "
                    + "properties={date=DATE{2020-02-03}, localtime=LOCAL_TIME{04:05:06.000000007}, "
                    + "duration=DURATION{P0M0DT259200S}, datetime=DATE_TIME{2020-02-03T04:05:06"
                    + ".000000007+11:00[Australia/Sydney]}, boolean=BOOLEAN{true}, string=STRING{\"str-value\"}, "
                    + "double=FLOAT{4.2}, point_3d=POINT{Point{srid=0, x=42.0, y=12.0, z=69.0}}, "
                    + "point_2d=POINT{Point{srid=0, x=42.0, y=12.0}}, time=TIME{04:05:06.000000007Z}, "
                    + "localdatetime=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}, long=INTEGER{42}}},\n"
                    + "    FLOAT{3.14},\n"
                    + "    STRING{\"other-value\"},\n"
                    + "    STRING{\"value\"},\n"
                    + "    BOOLEAN{true}]>\n"
                    + "to be of type:\n"
                    + "  <RELATIONSHIP>\n"
                    + "but some database objects are from another type:\n"
                    + "\n"
                    + "  1) NODE{id=42L, labels=[\"LBL_1\", \"LBL_2\", \"LBL_3\", \"LBL_4\"], "
                    + "properties={date=DATE{2020-02-03}, localtime=LOCAL_TIME{04:05:06.000000007}, "
                    + "duration=DURATION{P0M0DT259200S}, datetime=DATE_TIME{2020-02-03T04:05:06"
                    + ".000000007+11:00[Australia/Sydney]}, boolean=BOOLEAN{true}, string=STRING{\"str-value\"}, "
                    + "double=FLOAT{4.2}, point_3d=POINT{Point{srid=0, x=42.0, y=12.0, z=69.0}}, "
                    + "point_2d=POINT{Point{srid=0, x=42.0, y=12.0}}, time=TIME{04:05:06.000000007Z}, "
                    + "localdatetime=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}, long=INTEGER{42}}}\n"
                    + "    - Current type: NODE\n"
                    + "\n"
                    + "  2) FLOAT{3.14}\n"
                    + "    - Current type: VALUE\n"
                    + "\n"
                    + "  3) STRING{\"other-value\"}\n"
                    + "    - Current type: VALUE\n"
                    + "\n"
                    + "  4) STRING{\"value\"}\n"
                    + "    - Current type: VALUE\n"
                    + "\n"
                    + "  5) BOOLEAN{true}\n"
                    + "    - Current type: VALUE\n"
            );
        }

    }

}
