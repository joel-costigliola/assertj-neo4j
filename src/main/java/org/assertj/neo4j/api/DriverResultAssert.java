package org.assertj.neo4j.api;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.MapAssert;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author patouche - 5/26/20.
 */
public class DriverResultAssert extends AbstractIterableAssert<DriverResultAssert,
                                                               Iterable<? extends Map<String, Object>>,
                                                               Map<String, Object>,
                                                               MapAssert<String, Object>> {

    private final Driver driver;

    public DriverResultAssert(final Driver driver, final Result result) {
        // List<Map<String, Object>>
        this(driver, result.list(Record::asMap));
    }

    private DriverResultAssert(final Driver driver, final Iterable<? extends Map<String, Object>> result) {
        // List<Map<String, Object>>
        super(result, DriverResultAssert.class);
        this.driver = driver;
    }

    @Override
    protected MapAssert<String, Object> toAssert(final Map<String, Object> value, final String description) {
        return new MapAssert<>(value).as(description);
    }

    @Override
    protected DriverResultAssert newAbstractIterableAssert(final Iterable<? extends Map<String, Object>> iterable) {
        return new DriverResultAssert(this.driver, iterable);
    }

    public MapAssert<String, Object> single() {
        return hasSize(1).first();
    }

    public DriverResultAssert filteredOnKeys(final String... keys) {
        return filteredOn(m -> Stream.of(keys).allMatch(m::containsKey));
    }

    public DriverResultAssert filteredOnKeyValue(final String key, final Object value) {
        return filteredOn(m -> Objects.equals(m.get(key), value));
    }

}
