# TODOS


## Features

### Nodes

```java
class SampleTests {
    @Test
    void nodes() {
      assertThat(new Nodes(driver, "LABEL"))
              .hasSize(5)
              .haveLabels("LABEL_1", "LABEL_2")
              .havePropertyKeys("KEY_1", "KEY_2")
              .ignoringIds()
              .contains(
                      Drivers.node().label("LABEL_1").property("prop-1", "value-1").build(),
                      Drivers.node().label("LABEL_1").property("prop-1", "value-1").build()
              )
              .toParent();
    }
}

```

* Provider assertions on the main "raw" Java Bold driver types (Records...)
* Provider assertions on query results
  * Should support both String and Cypher-DSL constructs
* Provider higher level experience à la AssertJ-DB
  * Entities (commons nodes & relationships) :
    * [X] `ignoringIds`: Remove id to entities and create a new assertions
    * [X] `havePropertyKeys`: Verify if property's entities have the expected keys
    * [ ] `havePropertyType` IN PROGRESS: Verify if all property's entities have the expected type
    * [ ] `havePropertyCount`: Verify if entities have the number of properties
    * [ ] `haveProperty`
    * [ ] `haveProperties`
    * [X] `contains`
  * Nodes :
    * [X] `haveLabels`
    * [X] `Drivers.node()....`
    * [ ] `toParent`
  * Relationships :
    * [X] `Drivers.relationship()....`
    * [X] `haveType`: Really useful ?
    * [ ] `toParent`
  * Request
    * [ ]
  
