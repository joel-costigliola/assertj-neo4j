package org.assertj.neo4j.api.beta;

import org.assertj.core.presentation.Representation;

/**
 * @author patouche - 15/02/2021
 */
public interface ParentAssert {

    /**
     * Retrieve the {@link Representation} for render any object in error message.
     *
     * @return the currenlty use representation.
     */
    Representation representation();

}
