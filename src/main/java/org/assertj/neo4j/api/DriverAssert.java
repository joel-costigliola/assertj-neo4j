package org.assertj.neo4j.api;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;

import java.util.function.Consumer;

/**
 * @author patouche - 5/26/20.
 */
interface DriverAssert {

    Driver getDriver();




}
