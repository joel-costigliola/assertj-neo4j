package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractAssert;
import org.assertj.neo4j.api.beta.type.Request;
import org.assertj.neo4j.api.beta.util.Wip;

/**
 * @author patouche - 27/11/2020
 */
public class DriverRequestAssert extends AbstractAssert<DriverRequestAssert, Request> {

    public DriverRequestAssert(final Request request) {
        super(request, DriverRequestAssert.class);
    }

    public DriverRequestAssert hasSingleNodeResult() {
        throw Wip.TODO(this);
    }
}
