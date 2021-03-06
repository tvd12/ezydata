package com.tvd12.ezydata.jpa.query;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezydata.database.query.EzyQueryCondition;
import com.tvd12.ezydata.database.query.EzyQueryConditionChain;
import com.tvd12.ezydata.database.query.EzyQueryConditionGroup;
import com.tvd12.ezydata.database.query.EzyQueryMethod;
import com.tvd12.ezydata.database.query.EzyQueryMethodConverter;
import com.tvd12.ezydata.database.query.EzyQueryMethodType;

public class EzyJpaQueryMethodConverter 
		implements EzyQueryMethodConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public String toQueryString(
			Class entityClass, EzyQueryMethod method) {
		StringBuilder builder = new StringBuilder("SELECT ");
		if(method.getType() == EzyQueryMethodType.COUNT)
			builder.append("COUNT(e)");
		else
			builder.append("e");
		
		builder.append(" FROM ")
			.append(entityClass.getSimpleName()).append(" e");
		
		EzyQueryConditionChain conditionChain = method.getConditionChain();
		if(conditionChain.size() > 0) {
			builder.append(" ")
				.append("WHERE");
			convert(builder, conditionChain);
		}
		return builder.toString();
	}
	
	private void convert(
			StringBuilder builder, 
			EzyQueryConditionChain conditionChain) {
		List<EzyQueryConditionGroup> conditionGroups = conditionChain.getConditionGroups();
		AtomicInteger parameterCount = new AtomicInteger();
		for(int i = 0 ; i < conditionGroups.size() ; ++i) {
			builder.append(" ");
			convert(builder, parameterCount, conditionGroups.get(i));
			if(i < conditionGroups.size() - 1)
				builder.append(" OR");
		}
	}
	
	private void convert(
			StringBuilder builder, 
			AtomicInteger parameterCount,
			EzyQueryConditionGroup conditionGroup) {
		List<EzyQueryCondition> conditions = conditionGroup.getConditions();
		if(conditions.size() > 1)
			builder.append("(");
		for(int i = 0 ; i < conditions.size() ; ++i) {
			convert(builder, parameterCount, conditions.get(i));
			if(i < conditions.size() - 1)
				builder.append(" AND ");
		}
		if(conditions.size() > 1)
			builder.append(")");
	}
	
	private void convert(
			StringBuilder builder, 
			AtomicInteger parameterCount,
			EzyQueryCondition condition) {
		builder.append("e.")
			.append(condition.getField())
			.append(" ")
			.append(condition.getOperation().getSign())
			.append(" ")
			.append("?")
			.append(parameterCount.getAndIncrement());
	}
	
}
