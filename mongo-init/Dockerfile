FROM mongo
COPY data-import/ /data-import/
COPY mongo.setup.js /tmp/mongo.setup.js
CMD mongo --host mongo1 /tmp/mongo.setup.js && mongoimport --host=rs0/mongo1:27017,mongo2:27018,mongo3:27019  --db test --collection node --type json --file /data-import/nodes.json --jsonArray
