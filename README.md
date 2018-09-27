# Description
This a Docker-based mini-app for testing tree/hierarchical data retrieval and CRUD operations performance with MongoDB.

# References
- https://docs.mongodb.com/manual/applications/data-models-tree-structures/
- https://www.slideshare.net/mongodb/webinar-working-with-graph-data-in-mongodb
- https://docs.mongodb.com/manual/reference/operator/aggregation/graphLookup/

# $graphLookup example
Get all dimensions and their descendants by a field named e.g. `changesetId`:
```
db.node.aggregate([ 
{ $match: {$and: [ {"parentId": {$eq: []}}, {"changesetId": 2} ]} },
{
 $graphLookup: {
    from: "node",
    startWith: "$nodeId",
    connectFromField: "nodeId",
    connectToField: "parentId",
    restrictSearchWithMatch: {"changesetId": 2},
    as: "children"
 }
}
]);
```

# Usage
- Requires docker & docker-compose
- Apache Maven 3.5.x and above
- Java 1.8 and above

Run Integration test by executing:
```    
mvn clean verify
```

Run the application normally with:
```
./docker-compose up
```
    
# Additional
TODO

## Create View
To be executed once-off:
```
db.createView("treeView", "node", [
{
 $graphLookup: {
    from: "node",
    startWith: "$nodeId",
    connectFromField: "nodeId",
    connectToField: "parentId",
    maxDepth: 0,
    as: "children"
 }
}
]);
```

## Schema indexes
In MongoDB, add indexes on fields:
- nodeId
- changesetId
- parentId


