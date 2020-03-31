package com.tvd12.ezydata.morphia.testing.query;

import org.testng.annotations.Test;

import com.tvd12.ezydata.morphia.query.impl.EzyMorphiaUpdateOperations;
import com.tvd12.ezydata.morphia.testing.BaseMongoDBTest;
import com.tvd12.ezydata.morphia.testing.data.Cat;
import com.tvd12.ezydata.morphia.testing.data.Kitty;
import com.tvd12.ezyfox.collect.Lists;

import dev.morphia.query.UpdateOperations;

public class EzySimpleUpdateOperationsTest extends BaseMongoDBTest {

	@Test
	public void test() {
		UpdateOperations<Cat> realOperations = DATASTORE.createUpdateOperations(Cat.class);
		EzyMorphiaUpdateOperations<Cat> proxyOperations = new EzyMorphiaUpdateOperations<>(realOperations);
		
		proxyOperations.addToSet("valueSet", "a");
		proxyOperations.addToSet("valueSet", Lists.newArrayList("b", "c"));
		proxyOperations.dec("age");
		proxyOperations.dec("age", 3);
		proxyOperations.disableValidation();
		proxyOperations.enableValidation();
		proxyOperations.inc("age");
		proxyOperations.inc("age", 10);
		proxyOperations.max("age", 100);
		proxyOperations.min("age", 0);
		proxyOperations.push("valueSet", "d");
		proxyOperations.push("valueSet", Lists.newArrayList("e", "f"));
		proxyOperations.push("age", Lists.newArrayList("g", "h"), options -> options.sort(1).slice(5).position(0));
		proxyOperations.push("kitties", new Kitty(100L), options -> options.sort("age", 1));
		proxyOperations.removeAll("valueList", "d");
		proxyOperations.removeAll("valueList", Lists.newArrayList("a", "b", "c"));
		proxyOperations.removeFirst("valueList");
		proxyOperations.removeLast("valueList");
		proxyOperations.setOnInsert("free", 100);
		proxyOperations.unset("unset");
		
		
	}
	
}
