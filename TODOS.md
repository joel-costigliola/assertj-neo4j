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

## Tasks

### Goals

* [ ] Provider assertions on the main "raw" Java Bold driver types (Records...)
  * [ ] `haveColumnSize`
  * [ ] `isNode`
  * [ ] `isRelationships`
  * [ ] ``
* [ ] Provider assertions on query results
  * [ ] Should support both String 
  * [ ] Cypher-DSL constructs
* [ ] Provider higher level experience à la AssertJ-DB
  * [ ] Entities (commons nodes & relationships) :
    * [X] `toParentAssert` : Go to the parent assertion
    * [X] `ignoringIds`: Create a new assertion that will not compare the entity with theirs ids
    * [X] `havePropertyKeys`: Verify that all entities have the expected keys
    * [X] `havePropertyOfType` : Verify that all entities have a property with a value matching the provided type
    * [X] `havePropertySize`: Verify that all entities have the expected number of properties
    * [X] `havePropertyValue` : Verify that all entities have the property value
    * [X] `havePropertyValueMatching` : Verify that all entities have the property matching the provided predicate
    * [X] `haveListPropertyOfType` : Verify that all entities have a list property with elements of the provided type
    * [X] `contains` : Verify that there is an entity that is equal to the one provided
    * [X] `filteredOn` : Filter entities on a predicate. Create a new assertion
    * [X] `filteredOnPropertyExists` : Filter entities that have the provided property key
    * [X] `filteredOnPropertyValue` : Filter entities that have the provided property key
    * [ ] Add negative assertions
  * [ ] Nodes :
    * [ ] `toRootAssert` : Retrieve the root assertion
    * [X] `haveLabels` : Verify that the nodes have the expected labels
    * [X] `filteredOnLabels` : Filter on nodes having the labels
    * [X] `Drivers.node()....`
    * [X] `incomingRelationships`
    * [X] `outgoindRelationships`
    * [X] `haveNoIncomingRelationships`
    * [X] `haveNoOutgoingRelationships`
    * [ ] Add negative assertions
  * [ ] Relationships :
    * [ ] `toRootAssert` : Retrieve the root assertion
    * [X] `haveType` : Verify that a relationships if of the expected type
    * [X] `Drivers.relationship()....`
    * [ ] `toParent`
    * [ ] `startingNodes`
    * [ ] `endingNodes`
    * [ ] `haveNoStartingNodes`
    * [ ] `haveNoEndingNodes`
    * [ ] ``
    * [ ] Add negative assertions
  * [ ] Request : See bellow
  * [ ] Changes :
    * [ ] ``
  
### Others

* [ ] Simplify error message factory
* [ ] 
* [ ] Simplify assertions
