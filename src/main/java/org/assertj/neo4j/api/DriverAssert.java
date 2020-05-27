package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractAssert;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author patouche - 5/26/20.
 */
public class DriverAssert extends AbstractAssert<DriverAssert, Driver> {

    public DriverAssert(final Driver driver) {
        super(driver, DriverAssert.class);
    }

    private DriverResultAssert executing(final String query) {
        try (Session session = this.actual.session()) {
            final Result result = session.run(query, TransactionConfig.builder()
                    .build());
            return new DriverResultAssert(actual, result);
        }
    }

    public DriverAssert executing(final String query, final Consumer<DriverResultAssert> fn) {
        final DriverResultAssert executing = this.executing(query).as("Query results of '%s'", query);
        fn.accept(executing);
        return myself;
    }

    public DriverAssert withDataset(final String... queries) {
        Stream.of(queries).forEach(q -> {
            try (final Session session = this.actual.session()) {
                session.run(q, TransactionConfig.builder().build());
            }
        });
        return myself;
    }

    public DriverAssert when(Runnable runnable) {
        runnable.run();
        return myself;
    }
}
