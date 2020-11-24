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

import org.assertj.neo4j.api.beta.type.Nodes;

import java.util.List;
import java.util.Objects;

/**
 * @author pallain - 14/11/2020
 */
public class MissingNodeLabels {

    private final Nodes.DbNode node;

    private final List<String> missingLabels;

    public MissingNodeLabels(final Nodes.DbNode node, final List<String> missingLabels) {
        this.node = node;
        this.missingLabels = missingLabels;
    }

    public Nodes.DbNode getNode() {
        return node;
    }

    public List<String> getMissingLabels() {
        return missingLabels;
    }

    public boolean hasMissing() {
        return !missingLabels.isEmpty();
    }

    @Override
    public String toString() {
        return "MissingNodeLabels{node=" + node + ", missingLabels=" + missingLabels + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MissingNodeLabels that = (MissingNodeLabels) o;
        return Objects.equals(node, that.node)
               && Objects.equals(missingLabels, that.missingLabels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, missingLabels);
    }
}
