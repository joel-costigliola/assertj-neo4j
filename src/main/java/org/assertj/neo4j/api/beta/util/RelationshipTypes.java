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

import org.assertj.core.util.Preconditions;
import org.assertj.neo4j.api.beta.type.Relationships;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author patouche - 25/11/2020
 */
public final class RelationshipTypes {

    private static String validName(final String type) {
        Preconditions.checkNotNullOrEmpty(type, "Type should not be null or empty");
        return type;
    }

    public static boolean is(final String type, Relationships.DbRelationship relationships) {
        return validName(type).equals(relationships.getType());
    }

    public static boolean isNot(final String type, Relationships.DbRelationship relationships) {
        return !is(type, relationships);
    }

    public static boolean are(final String type, List<Relationships.DbRelationship> relationships) {
        validName(type);
        return relationships.stream().allMatch(r -> RelationshipTypes.is(type, r));
    }

    public static List<Relationships.DbRelationship> others(
            final String type, final List<Relationships.DbRelationship> relationships) {
        return relationships.stream().filter(i -> RelationshipTypes.isNot(type, i)).collect(Collectors.toList());
    }
}
