AssertJ assertions for Neo4j 2+
===============================

Provides assertions like AssertJ for [Neo4j](http://www.neo4j.org/) 2 or higher.

This module have been written by **Florent Biville**, big thanks to him! Great Work!

* [Quick start](#quickstart)
* [Latest news](#news)
* [Javadoc](#javadoc)
* [Tips & tricks](#tip)
* [Using both AssertJ Core assertions and Neo4J assertions](#core-and-neo4j-assertions)
* [Contributing](#contributing)

## <a name="quickstart"/>Quick start

To start using Neo4j assertions :

1 - Add this dependency snippet to your project

```xml
<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-neo4j</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
```

You also need to provide neo4j 2+ dependencies.

2 - statically import `org.assertj.neo4j.api.Assertions.assertThat` and use your preferred IDE code completion after `assertThat.` !

Some examples from [PathAssertionExamples](https://github.com/joel-costigliola/assertj-examples/blob/master/src/test/java/org/assertj/examples/neo4j/PathAssertionExamples.java):

```java
import static org.assertj.neo4j.api.Assertions.assertThat;

// initialization omitted for brevity ...
Path bulmaToMasterRoshiPath = dragonBallGraph.findShortestPathBetween("Bulma", "Master Roshi");

Node bulmaNode = dragonBallGraph.findCharacter("Bulma");
Node masterRoshiNode = dragonBallGraph.findCharacter("Master Roshi");
Relationship trainingFromSonGoku = dragonBallGraph.findTrainingFrom("Son Goku");

// you can test several Path properties such as length, start/end node and last relationship
assertThat(bulmaToMasterRoshiPath).hasLength(3)
                                  .startsWithNode(bulmaNode)
                                  .endsWithNode(masterRoshiNode)
                                  .endsWithRelationship(trainingFromSonGoku);
```

Note that you can find other working examples in [Neo4j example package](https://github.com/joel-costigliola/assertj-examples/tree/master/src/test/java/org/assertj/examples/neo4j) from [assertj-examples](https://github.com/joel-costigliola/assertj-examples/) project.

## <a name="news"/>Latest News

**2014-01-02 : 1.0.0 release**

**Assertions available**
* Node assertions
* Path assertions
* Relationship assertions
* PropertyContainer assertions

## <a name="javadoc"/>Javadoc of latest release

Latest javadoc release : [**AssertJ Neo4j javadoc**](http://joel-costigliola.github.io/assertj/neo4j/api/index.html).

## <a name="tip"/>Tips & tricks

None yet, but waiting for you dear user ;-)

## <a name="core-and-neo4j-assertions"/>Using both AssertJ [Core assertions](https://github.com/joel-costigliola/assertj-core) and Neo4j assertions

You will have to make two static import : one for `Assertions.assertThat` to get **core** assertions and one `Assertions.assertThat` for **Neo4j** assertions.

```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.Assertions.assertThat;

// initialization omitted for brevity ...
Node firstDisciple = ...;

// assertThat comes from org.assertj.neo4j.api.Assertions.assertThat static import
assertThat(firstDisciple).hasPropertyKey("name")
                         .hasProperty("name", "Son Goku")
                         .doesNotHavePropertyKey("firstName")
                         .doesNotHaveProperty("name", "Bulma");

// assertThat comes from org.assertj.core.api.Assertions.assertThat static import
assertThat("hello world").startsWith("hello");
```

## <a name="contributing"/>Contributing

Thanks for your interest, check our [contributor's guidelines](CONTRIBUTING.md).

Again, huge thanks to **Florent Biville** for **assertj-neo4j 1.0.0**.


