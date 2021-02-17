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
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.neo4j.api.beta.type.DbEntity;

/**
 * @author Patrick Allain - 15/02/2021
 */
public class EntityRepresentation extends StandardRepresentation implements Representation {

    private final boolean abbreviate;

    private EntityRepresentation(boolean abbreviate) {
        this.abbreviate = abbreviate;
    }

    public static Representation abbreviate() {
        return new EntityRepresentation(true);
    }

    public static Representation full() {
        return new EntityRepresentation(false);
    }

    @Override
    public String toStringOf(Object object) {
        if (this.abbreviate && object instanceof DbEntity) {
            return ((DbEntity) object).abbreviate();
        }
        return super.toStringOf(object);
    }


    @Override
    protected boolean hasAlreadyAnUnambiguousToStringOf(Object obj) {
        if (obj instanceof DbEntity) {
            return true;
        }
        return super.hasAlreadyAnUnambiguousToStringOf(obj);
    }
}
