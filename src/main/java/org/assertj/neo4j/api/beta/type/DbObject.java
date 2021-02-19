package org.assertj.neo4j.api.beta.type;

import java.util.Objects;

/**
 * @param <I> the type of db object
 * @author patouche - 18/02/2021
 */
public abstract class DbObject<I extends DbObject<I>> implements Representable<I>  {

    protected final ObjectType objectType;

    protected DbObject(final ObjectType objectType) {
        this.objectType = Objects.requireNonNull(objectType, "The object type cannot be null.");
    }

    @Override
    public ObjectType objectType() {
        return objectType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final DbObject dbObject = (DbObject) o;
        return objectType == dbObject.objectType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType);
    }
}
