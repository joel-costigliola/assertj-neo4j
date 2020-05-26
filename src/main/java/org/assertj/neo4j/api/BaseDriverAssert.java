package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;

import java.util.function.Consumer;

/**
 * @author patouche - 5/26/20.
 */
public class BaseDriverAssert
        extends AbstractAssert<BaseDriverAssert, Driver>
        implements DriverAssert {

    public BaseDriverAssert(final Driver driver) {
        super(driver, Driver.class);
    }

    @Override
    public Driver getDriver() {
        return this.actual;
    }

    private DriverResultAssert executing(final String cypherQuery) {
        try (Session session = this.getDriver().session()) {
            final Result result = session.run(cypherQuery, TransactionConfig.builder()
                    .build());
            return new DriverResultAssert(getDriver(), result);
        }
    }


    public BaseDriverAssert executing(final  String cypherQuery, final Consumer<DriverResultAssert> fn) {
        final DriverResultAssert executing = this.executing(cypherQuery);
        fn.accept(executing);
        return myself;
    }

}
