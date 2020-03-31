package com.tvd12.ezydata.morphia.query.impl;

import java.util.concurrent.TimeUnit;

import com.tvd12.ezyfox.database.query.EzyFindAndModifyOptions;

import dev.morphia.FindAndModifyOptions;

public final class EzyMorphiaFindAndModifyOptions implements EzyFindAndModifyOptions {

	private final FindAndModifyOptions options;
	
	public EzyMorphiaFindAndModifyOptions(FindAndModifyOptions options) {
		this.options = options;
	}

	@Override
	public boolean isRemove() {
		return options.isRemove();
	}

	@Override
	public EzyFindAndModifyOptions remove(boolean remove) {
		options.remove(remove);
		return this;
	}

	@Override
	public boolean isUpsert() {
		return options.isUpsert();
	}

	@Override
	public EzyFindAndModifyOptions upsert(boolean upsert) {
		options.upsert(upsert);
		return this;
	}

	@Override
	public boolean isReturnNew() {
		return options.isReturnNew();
	}

	@Override
	public EzyFindAndModifyOptions returnNew(boolean returnNew) {
		options.returnNew(returnNew);
		return this;
	}

	@Override
	public long getMaxTime(TimeUnit timeUnit) {
		return options.getMaxTime(timeUnit);
	}

	@Override
	public EzyFindAndModifyOptions maxTime(long maxTime, TimeUnit timeUnit) {
		options.maxTime(maxTime, timeUnit);
		return this;
	}
	
}
