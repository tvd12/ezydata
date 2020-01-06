package com.tvd12.ezydata.morphia.testing;

import org.testng.annotations.Test;

import com.tvd12.ezydata.mongodb.loader.EzyFileMongoClientLoader;
import com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezyfox.io.EzyMaps;
import com.tvd12.test.base.BaseTest;

public class EzyFileMongoClientLoaderTest extends BaseTest {

	@Test
	public void test() {
		EzyFileMongoClientLoader loader = new EzyFileMongoClientLoader()
				.filePath("src/test/resources/mongodb_config.properties")
				.property(EzyMongoClientLoader.DATABASE, "test")
				.properties(EzyMaps.newHashMap(EzyMongoClientLoader.DATABASE, "test"));
		loader.load().close();
		EzyFileMongoClientLoader.load("src/test/resources/mongodb_config.properties").close();
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		EzyFileMongoClientLoader.load("src/test/resources/mongodb_config.propertieszzzz");
	}
	
}
