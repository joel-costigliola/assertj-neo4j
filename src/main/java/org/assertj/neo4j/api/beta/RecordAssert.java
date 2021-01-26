package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractAssert;
import org.neo4j.driver.Record;

/**
 * @author patouche - 26/01/2021
 */
public class RecordAssert extends AbstractAssert<RecordAssert, Record> {
    public RecordAssert(Record record) {
        super(record, RecordAssert.class);
    }
}
