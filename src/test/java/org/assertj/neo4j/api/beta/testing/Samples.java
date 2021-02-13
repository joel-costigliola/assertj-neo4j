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
package org.assertj.neo4j.api.beta.testing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Patrick Allain - 10/02/2021
 */
public interface Samples {

    ZonedDateTime ZONED_DATE_TIME = ZonedDateTime
            .of(2020, 2, 3, 4, 5, 6, 7, ZoneId.of("Australia/Sydney"));

    LocalDateTime LOCAL_DATE_TIME = ZONED_DATE_TIME.toLocalDateTime();
    LocalDate LOCAL_DATE = ZONED_DATE_TIME.toLocalDate();
    OffsetTime TIME = ZONED_DATE_TIME.toLocalTime().atOffset(ZoneOffset.UTC);
    LocalTime LOCAL_TIME = ZONED_DATE_TIME.toLocalTime();

    List<String> LABELS = Randomize.listOf("LBL_1", "LBL_2", "LBL_3", "LBL_4");

}
