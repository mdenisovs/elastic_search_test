# Elastic search commands example

### Install Elastic search with docker: https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html

Single-node cluster:

#### `docker network create elastic`

#### `docker pull docker.elastic.co/elasticsearch/elasticsearch:7.17.21`

#### `docker run --name es01-test --net elastic -p 127.0.0.1:9200:9200 -p 127.0.0.1:9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.17.21`

### Install kibana with docker: https://www.elastic.co/guide/en/kibana/7.17/docker.html

#### `docker pull docker.elastic.co/kibana/kibana:7.17.21`

#### `docker run --name kib01-test --net elastic -p 127.0.0.1:5601:5601 -e "ELASTICSEARCH_HOSTS=http://es01-test:9200" docker.elastic.co/kibana/kibana:7.17.21`

### To access Kibana, go to http://localhost:5601.

### Logstash

`docker pull docker.elastic.co/logstash/logstash:7.17.21`

`docker run --rm -it -v ~/pipeline/:/usr/share/logstash/pipeline/ docker.elastic.co/logstash/logstash:7.17.21`

### Getting started (java client): https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.13/getting-started-java.html

Some console commands:

`PUT products
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  }
}`

`GET _cat/shards/products?v`

`POST products/_search 
{
  "query": {
    "match": {
      "name": "guitar"
    }
  }
}`

`GET /btc_usd_1h/_count`

`DELETE /my-index-000001`