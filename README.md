![Java CI with Maven](https://github.com/kmandalas/spring-mongodb-graphlookup/workflows/Java%20CI%20with%20Maven/badge.svg)

# Description
This a simplistic Dockerized Spring Boot app for testing hierarchical data retrieval (and other operations) with MongoDB.

# References
In order to be able to follow-up better the reasoning, check the following stuff:
- https://docs.mongodb.com/manual/applications/data-models-tree-structures/
- https://www.slideshare.net/mongodb/webinar-working-with-graph-data-in-mongodb
- https://docs.mongodb.com/manual/reference/operator/aggregation/graphLookup/

# Prerequisites
In order to execute the app:
- Install Docker and Docker Compose :whale:

In order to build, test etc:
- Apache Maven 3.5.x and above
- Java 11 and above

# Usage
When the application starts it loads sample data in MongoDB (see [nodes.json](https://github.com/kmandalas/spring-mongodb-graphlookup/blob/master/mongo-init/data-import/nodes.json)).
The data are like a "forest of trees" i.e. multiple trees under a "virtual root" node with id (`nodeId`) having the value "-1".
Each tree is identified by its `treeId`.

## Endpoints 

Method	| Path	| Description
------------- | ------------------------- | ------------- |
GET	| /app/{treeId}	| retrieve a whole hierarchical structure by treeId
GET	| /app/{treeId}/set/{nodeId}	| retrieve a sub-tree of a treeId, starting from the node with a given nodeId
POST | under development | add a new node to a given tree (arbitrary depth)
PUT | under development | update an existing node
DELETE | under development | delete a node and its descendants
GET | under development | compare subtrees with [Javers](https://javers.org/)

You can have a view of a whole tree from the imported tree-structure by performing an HTTP-GET operation:
- http://localhost:8080/app/10001

Then you may retrieve sub-trees by performing am HTTP-GET operation on the following URL:
- http://localhost:8080/app/1001/st/100

## Build/Test
Run Integration Tests by executing:
```    
mvn clean verify
```
## Execution
Run the application normally with:
```
docker-compose up
```

Stop the application with:
```
docker-compose down
```
This way the containers are disposed and cleanup is performed.
  
# Additional information

## $graphLookup example
Get all dimensions and their descendants of a whole tree
```
db.node.aggregate([ 
{ $match: { treeId: 1001, $and: [ { nodeId: 100 } ] } },
{
 $graphLookup: {
    from: "node",
    startWith: "$nodeId",
    connectFromField: "nodeId",
    connectToField: "parentId",
    restrictSearchWithMatch: {"treeId": 1001},
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
    startWith: "nodeId",
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
- nodeId
- treeId
- parentId
