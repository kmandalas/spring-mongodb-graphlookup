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
The data are like a "forest of trees" i.e. multiple trees under a "virtual root" node with id (`masterId`) having the value "-1".
There is also the concept of versioning (i.e. different versions of the whole structure), identified by the field: `changesetId`. 
At the given sample, only one changeset exists.   
**Note**: in this particular sample, some nodes can belong to multiple parents (Not to be confused with path enumeration or other approaches). 
The only "hierarchical" information we need to maintain in the database is the immediate parent(s).

## Endpoints 

Method	| Path	| Description
------------- | ------------------------- | ------------- |
GET	| /app/{changesetId}	| retrieve a whole hierarchical structure by changesetId
GET	| /app/{changesetId}/set/{masterId}	| retrieve a sub-tree of a changesetId, starting from the node with a given id
GET | under development | compare subtrees with [Javers](https://javers.org/)
POST | under development | add a new node to a given tree (arbitrary depth)
PUT | under development | update an existing node
DELETE | under development | delete a node and its descendants

You can have a view of a whole tree from the imported tree-structure by performing an HTTP-GET operation:
- http://localhost:8080/app/25080022

Then you may retrieve sub-trees by performing am HTTP-GET operation on the following URL:
- http://localhost:8080/app/25080022/st/23978341

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

## Under investigation
- [ ] the usage/reliability of `docker-compose-maven-plugin`
- [x] Q: why the imported data remain attached to the MongoDB container although no volume is explicitly mounted etc. A: had to use `docker-compose down` and not Ctrl-C for terminating
- [ ] the integration tests are not pushed yet
- [ ] pending endpoints
