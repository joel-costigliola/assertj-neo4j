AssertJ assertions for Neo4J 2+
===============================

Provides assertions like AssertJ for [Neo4j](http://www.neo4j.org/) 2 or higher.

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
   <version>1.0.0-SNAPSHOT</version>
   <scope>test</scope>
 </dependency>
```

2 - statically import `Assertions.assertThat` and use your preferred IDE code completion after `assertThat.` !

Some examples : 

```java
import static org.assertj.neo4j.api.Assertions.assertThat;
...
TODO : add some assertions examples
```

Note that you can find working examples in [Neo4jAssertionsExamples.java](https://github.com/joel-costigliola/assertj-examples/blob/master/src/test/java/org/assertj/examples/Neo4jAssertionsExamples.java) from [assertj-examples](https://github.com/joel-costigliola/assertj-examples/) project.

## <a name="news"/>Latest News
 
**2013-??-?? : 1.0.0 release :**
* TO BE COMPLETED

## <a name="javadoc"/>Javadoc of latest release

TODO upload when ready : Latest javadoc release : [**AssertJ Neo4j javadoc**](http://joel-costigliola.github.io/assertj/neo4j/api/index.html).

## <a name="tip"/>Tips & tricks

TO BE COMPLETED

## <a name="core-and-neo4j-assertions"/>Using both AssertJ [Core assertions](https://github.com/joel-costigliola/assertj-core) and Neo4j assertions

You will have to make two static import : one for `Assertions.assertThat` to get **core** assertions and one `Assertions.assertThat` for **Neo4j** assertions.

```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.neo4j.api.Assertions.assertThat;
...
// assertThat comes from org.assertj.neo4j.api.Assertions.assertThat static import
TODO : add example

// assertThat comes from org.assertj.core.api.Assertions.assertThat static import
assertThat("hello world").startsWith("hello");
```

## <a name="contributing"/>Contributing

Thanks for your interest, check our [contributor's guidelines](CONTRIBUTING.md).


