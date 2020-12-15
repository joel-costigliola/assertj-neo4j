# TODOS


## Features

### Nodes

```java
assertThat(new Nodes(driver, "LABEL"))
    .hasSize(5)
    .haveLabels("LABEL_1", "LABEL_2")
    .havePropertyKeys("KEY_1", "KEY_2")
    .ignoringIds()
    .contains(
        Drivers.node().label("LABEL_1").property("prop-1", "value-1").build(),
        Drivers.node().label("LABEL_1").property("prop-1", "value-1").build()
    )
    .toParent()
```

* Provider assertions on the main "raw" Java Bold driver types (Records...)
* Provider assertions on query results
  * Should support both String and Cypher-DSL constructs
* Provider higher level experience à la AssertJ-DB
  * Nodes
    * [X] `haveLabels`
    * [ ] `havePropertyKeys`
    * [ ] `haveProperties`
    * [X] `ignoringIds`
    * [X] `contains`
      * [X] `Drivers.node()....`
    * [ ] `toParent`
  * Relationships
    * [X] `haveType`: Really usefull ?
    * [X] `havePropertyKeys`
    * [ ] `haveProperties`
    * [ ] `ignoringIds`
    * [ ] `contains`
      * [X] `Drivers.relation()....`
    * [ ] `toParent`
  * Request
    * [ ]
  
