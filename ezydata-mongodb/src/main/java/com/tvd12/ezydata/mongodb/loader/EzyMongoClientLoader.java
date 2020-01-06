/**
 * 
 */
package com.tvd12.ezydata.mongodb.loader;

import com.mongodb.MongoClient;

public interface EzyMongoClientLoader {

	String URI				= "database.mongo.uri";
    String HOST         	= "database.mongo.host";
    String PORT         	= "database.mongo.port";
    String USERNAME     	= "database.mongo.username";
    String PASSWORD     	= "database.mongo.password";
    String DATABASE     	= "database.mongo.database";
    String COLLECTION   	= "database.mongo.collection";
    
    MongoClient load();
    
}
