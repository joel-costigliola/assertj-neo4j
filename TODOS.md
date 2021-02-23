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
  * [X] `hasRecordSize` : Verify that the number of records match the expected one
  * [X] `hasColumnSize` : Verify that the number of columns match the expected one
  * [X] `hasColumns` : Verify that the column names exists
  * [X] `haveType` : Verify that a column is of type NODE, RELATIONSHIP or VALUE
  * [X] `haveValueType` : Verify that a column is of type VALUE and contains value type
  * [X] `haveValueInstanceOf` : Verify that a column is of type VALUE and contains values instance of
  * [X] `asListOf(Class)` : Verify that a single column is return containing values instance of and create a new
    assertion for this column values
  * [X] `asListOf(String, Class)` : Verify that the column name exist and create a new assertion for this column values
  * [ ] `asNodeAssert` : Verify
  * [X] `asRelationshipAsssert` : Extract the relationships for a given column and create a new assertion on
    relationship.
* [ ] Provider assertions on query results
  * [ ] Should support both String
  * [ ] Cypher-DSL constructs
* [ ] Provider higher level experience à la AssertJ-DB
  * [ ] Entities (commons nodes & relationships) :
    * [X] `toParentAssert` : Go to the parent assertion
    * [X] `isEmpty` : Verify that the actual list is empty
    * [X] `isNotEmpty` : Verify that the actual list is not empty
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
    * [X] `extractingProperty` : Extract property by the property key and create an assertion on a list
    * [ ] Add negative assertions
  * [ ] Nodes :
    * [X] `toRootAssert` : Retrieve the root assertion
    * [X] `haveLabels` : Verify that the nodes have the expected labels
    * [X] `filteredOnLabels` : Filter on nodes having the labels
    * [X] `filteredOnLabelMatchingAny` : Filter on nodes having the labels
    * [X] `filteredOnLabelMatchingAll` : Filter on nodes having the labels
    * [X] `Drivers.node()....`
    * [X] `incomingRelationships`
    * [X] `outgoindRelationships`
    * [X] `haveNoIncomingRelationships`
    * [X] `haveNoOutgoingRelationships`
    * [ ] Add negative assertions
  * [ ] Relationships :
    * [X] `toRootAssert` : Retrieve the root assertion
    * [X] `haveType` : Verify that a relationships if of the expected type
    * [X] `Drivers.relationship()....`
    * [X] `toParent`
    * [X] `startingNodes`
    * [X] `endingNodes`
    * [X] `haveNoStartingNodes`
    * [X] `haveNoEndingNodes`
    * [ ] ``
    * [ ] Add negative assertions
  * [ ] Request : See bellow
  * [ ] Changes :
    * [ ] ``

### Others

* [X] Simplify error message factory
* [ ] Support null in composite types
* [ ] Simplify assertions ?
* [X] Allow fluent assertions between multiple type
* [X] Allow navigation on assertions
* [ ] Integrate full path to indicate failure.
