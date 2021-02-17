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
package org.assertj.neo4j.api.beta.util;

import org.assertj.core.presentation.Representation;
import org.assertj.neo4j.api.beta.testing.Samples;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Patrick Allain - 15/02/2021
 */
class EntityRepresentationTests {

    @Nested
    class ToStringOfTests {

        @Test
        void should_format_entity_when_abbreviate() {
            // GIVEN
            final Representation representation = EntityRepresentation.abbreviate();

            // WHEN
            final String result = representation.toStringOf(Samples.NODE);

            // THEN
            assertThat(result).isEqualTo("NODE{id=42L}");
        }

        @Test
        void should_format_entity_when_full() {
            // GIVEN
            final Representation representation = EntityRepresentation.full();

            // WHEN
            final String result = representation.toStringOf(Samples.NODE);

            // THEN
            assertThat(result).isEqualTo("NODE{id=42L, "
                                         + "labels=[\"LBL_1\", \"LBL_2\", \"LBL_3\", \"LBL_4\"], "
                                         + "properties={"
                                         + "date=DATE{2020-02-03}, "
                                         + "localtime=LOCAL_TIME{04:05:06.000000007}, "
                                         + "duration=DURATION{P0M0DT259200S}, "
                                         + "datetime=DATE_TIME{2020-02-03T04:05:06.000000007+11:00[Australia/Sydney]}, "
                                         + "boolean=BOOLEAN{true}, "
                                         + "string=STRING{\"str-value\"}, "
                                         + "double=FLOAT{4.2}, "
                                         + "point_3d=POINT{Point{srid=0, x=42.0, y=12.0, z=69.0}}, "
                                         + "point_2d=POINT{Point{srid=0, x=42.0, y=12.0}}, "
                                         + "time=TIME{04:05:06.000000007Z}, "
                                         + "localdatetime=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}, "
                                         + "long=INTEGER{42}}"
                                         + "}");
        }

        @Test
        void should_use_default() {
            // GIVEN
            final Representation representation = EntityRepresentation.abbreviate();

            // WHEN
            final String result = representation.toStringOf(Samples.OFFSET_DATE_TIME);

            // THEN
            assertThat(result).isEqualTo("2020-02-03T04:05:06.000000007+11:00 (java.time.OffsetDateTime)");
        }

    }

    @Nested
    class UnambiguousToStringOfTests {

        @Test
        void should_format_entity_when_abbreviate() {
            // GIVEN
            final Representation representation = EntityRepresentation.abbreviate();

            // WHEN
            final String result = representation.unambiguousToStringOf(Samples.NODE);

            // THEN
            assertThat(result).isEqualTo("NODE{id=42L}");
        }

        @Test
        void should_format_entity_when_full() {
            // GIVEN
            final Representation representation = EntityRepresentation.full();

            // WHEN
            final String result = representation.unambiguousToStringOf(Samples.NODE);

            // THEN
            assertThat(result).isEqualTo("NODE{id=42L, "
                                         + "labels=[\"LBL_1\", \"LBL_2\", \"LBL_3\", \"LBL_4\"], "
                                         + "properties={"
                                         + "date=DATE{2020-02-03}, "
                                         + "localtime=LOCAL_TIME{04:05:06.000000007}, "
                                         + "duration=DURATION{P0M0DT259200S}, "
                                         + "datetime=DATE_TIME{2020-02-03T04:05:06.000000007+11:00[Australia/Sydney]}, "
                                         + "boolean=BOOLEAN{true}, "
                                         + "string=STRING{\"str-value\"}, "
                                         + "double=FLOAT{4.2}, "
                                         + "point_3d=POINT{Point{srid=0, x=42.0, y=12.0, z=69.0}}, "
                                         + "point_2d=POINT{Point{srid=0, x=42.0, y=12.0}}, "
                                         + "time=TIME{04:05:06.000000007Z}, "
                                         + "localdatetime=LOCAL_DATE_TIME{2020-02-03T04:05:06.000000007}, "
                                         + "long=INTEGER{42}}"
                                         + "}");
        }

        @Test
        void should_use_default() {
            // GIVEN
            final Representation representation = EntityRepresentation.abbreviate();

            // WHEN
            final String result = representation.unambiguousToStringOf(Samples.OFFSET_DATE_TIME);

            // THEN
            assertThat(result).isEqualTo("2020-02-03T04:05:06.000000007+11:00 (java.time.OffsetDateTime)");
        }

    }

}
