package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.IndexedObjectEnumerableAssert;
import org.assertj.core.api.ListAssert;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;

import java.util.List;
import java.util.Map;

/**
 * @author patouche - 26/01/2021
 */
public class DriverResultAssert  extends AbstractIterableAssert<
        DriverResultAssert, List<Map<String, Object>>, Map<String, Object>, RecordMapAssert> {

    private Result result;

    public DriverResultAssert(final Result result) {
        super(result.list(Record::asMap), DriverResultAssert.class);
    }



    public DriverResultAssert hasColumnNumber(int i) {
        return myself;
    }

    public DriverResultAssert isNode(String n) {
        return myself;
    }

    public DriverNodesAssert asNodesAssert(String s) {
        return null;
    }

    public <T> NavigableListAssert<T, DriverResultAssert, DriverResultAssert> asListOf(Class<T> clazz) {
        return null;
    }

    @Override
    protected RecordMapAssert toAssert(Map<String, Object> value, String description) {
        return null;
    }

    @Override
    protected DriverResultAssert newAbstractIterableAssert(Iterable<? extends Map<String, Object>> iterable) {
        return null;
    }
}
