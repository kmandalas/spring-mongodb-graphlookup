FROM mongo
COPY data-import/ /data-import/
RUN chmod +x /data-import/import.sh
CMD mongoimport --host mongocontainer --db test --collection node --type json --file /data-import/node.json --jsonArray
