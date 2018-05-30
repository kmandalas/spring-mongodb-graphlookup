
# Create View (you run this once-off)
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

# $graphLookup (changesetId is passed as parameter)
```
db.node.aggregate([ 
{ $match: {$and: [ {"parentId": {$eq: []}}, {"changesetId": 2} ]} },
{
 $graphLookup: {
    from: "treeView",
    startWith: "$nodeId",
    connectFromField: "nodeId",
    connectToField: "parentId",
    maxDepth: 0,
    as: "children"
 }
}
]);
```


