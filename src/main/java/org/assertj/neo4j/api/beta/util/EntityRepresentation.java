package org.assertj.neo4j.api.beta.util;

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.neo4j.api.beta.type.DbEntity;

/**
 * @author patouche - 15/02/2021
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
            return Presentations.outputId((DbEntity) object);
        }
        // if (this.abbreviate && object instanceof )
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
