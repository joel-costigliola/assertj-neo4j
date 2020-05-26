package org.assertj.neo4j.api;

import org.neo4j.driver.Driver;

/**
 * @author patouche - 5/26/20.
 */
public class DriverAssertions {

    public static BaseDriverAssert assertThat(Driver driver) {
        return new BaseDriverAssert(driver);
    }


}
