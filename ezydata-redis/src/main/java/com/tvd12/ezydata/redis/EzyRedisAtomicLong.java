package com.tvd12.ezydata.redis;

import com.tvd12.ezyfox.builder.EzyBuilder;

import redis.clients.jedis.Jedis;

public class EzyRedisAtomicLong {

	protected final String name;
	protected final Jedis jedis;
	protected final String mapName;
	protected final byte[] nameBytes;
	protected final byte[] mapNameBytes;

	protected EzyRedisAtomicLong(Builder builder) {
		this.name = builder.name;
		this.jedis = builder.jedis;
		this.mapName = builder.mapName;
		this.nameBytes = name.getBytes();
		this.mapNameBytes = mapName.getBytes();
	}
	
	public long addAndGet(long delta) {
		Long answer = jedis.hincrBy(mapNameBytes, nameBytes, delta);
		return answer;
	}
	
	public long incrementAndGet() {
		return addAndGet(1);
	}
	
	public String getName() {
		return name;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyRedisAtomicLong> {
		
		protected String name;
		protected Jedis jedis;
		protected String mapName;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder jedis(Jedis jedis) {
			this.jedis = jedis;
			return this;
		}
		
		public Builder mapName(String mapName) {
			this.mapName = mapName;
			return this;
		}
		
		@Override
		public EzyRedisAtomicLong build() {
			return new EzyRedisAtomicLong(this);
		}
		
		
	}
}
