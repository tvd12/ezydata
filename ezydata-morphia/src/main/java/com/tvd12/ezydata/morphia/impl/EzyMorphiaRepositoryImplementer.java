package com.tvd12.ezydata.morphia.impl;

import com.tvd12.ezydata.mongodb.bean.EzySimpleRepositoryImplementer;
import com.tvd12.ezydata.morphia.EzyDatastoreAware;
import com.tvd12.ezydata.morphia.repository.EzyDatastoreRepository;

import dev.morphia.Datastore;

public class EzyMorphiaRepositoryImplementer extends EzySimpleRepositoryImplementer {

	public EzyMorphiaRepositoryImplementer(Class<?> clazz) {
		super(clazz);
	}

	@Override
	protected void setRepoComponent(Object repo, Object template) {
		Datastore datastore = (Datastore)template;
		EzyDatastoreAware datastoreAware = (EzyDatastoreAware)repo; 
		datastoreAware.setDatastore(datastore);
	}
	
	@Override
	protected Class<?> getSuperClass() {
		return EzyDatastoreRepository.class;
	}
	
}
