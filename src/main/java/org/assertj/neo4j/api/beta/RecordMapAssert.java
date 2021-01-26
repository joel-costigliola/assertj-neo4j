package org.assertj.neo4j.api.beta;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.ObjectAssert;

import java.util.Map;

/**
 * @author patouche - 26/01/2021
 */
public class RecordMapAssert extends AbstractAssert<RecordMapAssert, Map<String, Object>> {

    public RecordMapAssert(Map<String, Object> stringObjectMap) {
        super(stringObjectMap, RecordMapAssert.class);
    }
}
