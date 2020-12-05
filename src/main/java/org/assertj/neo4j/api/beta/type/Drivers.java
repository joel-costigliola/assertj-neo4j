package org.assertj.neo4j.api.beta.type;

/**
 * @author patouche - 27/11/2020
 */
public final class Drivers {

    /**
     * Create a new {@link Nodes.DbNodeBuilder} to compare with the node you have in the database.
     *
     * @return a new {@link Nodes.DbNodeBuilder} instance
     */
    public static Nodes.DbNodeBuilder node() {
        return new Nodes.DbNodeBuilder();
    }

    /**
     * Create a new {@link Relationships.DbRelationshipBuilder} to compare with the relationships you have in the
     * database.
     *
     * @return a new {@link Relationships.DbRelationshipBuilder} instance
     */
    public static Relationships.DbRelationshipBuilder relation() {
        return new Relationships.DbRelationshipBuilder();
    }

    /**
     * Create a new {@link Relationships.DbRelationshipBuilder} with a specified type to compare with the
     * relationships you have in the database.
     *
     * @param type the relationships type
     * @return a new {@link Relationships.DbRelationshipBuilder} instance
     */
    public static Relationships.DbRelationshipBuilder relation(final String type) {
        return relation().type(type);
    }
}
