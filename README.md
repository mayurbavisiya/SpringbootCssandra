Execute below script to run demo with cassandra in cql shell
#######################################################################

create keyspace testdata with replication={'class':'SimpleStrategy', 'replication_factor':1};

use testdata;
 
CREATE TABLE test(
   id timeuuid PRIMARY KEY,
   name text,
   description text
);

Create index idx_name on  testdata.test(name);