# Description
This a simplistic Dockerized Spring Boot app for testing hierarchical data retrieval (and other operations) with MongoDB.

# References
In order to be able to follow-up better the reasoning, check the following stuff:
- https://docs.mongodb.com/manual/applications/data-models-tree-structures/
- https://www.slideshare.net/mongodb/webinar-working-with-graph-data-in-mongodb
- https://docs.mongodb.com/manual/reference/operator/aggregation/graphLookup/

# Prerequisites
- docker & docker-compose
- Apache Maven 3.5.x and above
- Java 1.8 and above

# Usage
When the application starts it loads sample data in MongoDB (see [node.json](https://github.com/kmandalas/spring-mongodb-graphlookup/blob/master/mongo-init/data-import/node.json)).
The data are like a "forest of trees" i.e. multiple trees with roots being defined by fields name `changesetId`.

You can have a view of the imported tree-structure by performing an HTTP-GET operation:
- http://localhost:8080/app/25080022 (TODO: implement the endpoint)

Then you may retrieve sub-trees by performing am HTTP-GET operation on the following URL:
- http://localhost:8081/app/25080022/st/23978341

Run Integration test by executing:
```    
mvn clean verify
```

Run the application normally with:
```
./docker-compose up
```
    
# Additional information

## $graphLookup example
Get all dimensions and their descendants by a field named e.g. `changesetId`:
```
db.node.aggregate([ 
{ $match: {$and: [ {"parentId": {$eq: []}}, {"changesetId": 2} ]} },
{
 $graphLookup: {
    from: "node",
    startWith: "$masterId",
    connectFromField: "masterId",
    connectToField: "parentId",
    restrictSearchWithMatch: {"changesetId": 2},
    as: "children"
 }
}
]);
```

## Create View
In case you want to create views, an example is given below (to be executed once-off):
```
db.createView("treeView", "node", [
{
 $graphLookup: {
    from: "node",
    startWith: "$masterId",
    connectFromField: "masterId",
    connectToField: "parentId",
    maxDepth: 0,
    as: "children"
 }
}
]);
```

## Schema indexes
In MongoDB, indexes on the following fields (based on the sample data) are necessary for achieving performance:
- masterId
- changesetId
- parentId