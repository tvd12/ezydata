package com.tvd12.ezydata.mongodb.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoryImplementer;
import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.mongodb.repository.EzySimpleMongoRepository;
import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.util.Next;

public class EzyMongoRepositoryImplementer 
		extends EzyAbstractRepositoryImplementer  {
	
	public EzyMongoRepositoryImplementer(Class<?> clazz) {
		super(clazz);
	}
	
	@Override
	protected String makeAbstractMethodContent(EzyMethod method) {
		EzyQuery anno = method.getAnnotation(EzyQuery.class);
		String queryString = getQueryString(method);
		EzyBody body = new EzyFunction(method).body();
		EzyInstruction createQueryInstruction = new EzyInstruction("\t", "\n", false)
				.variable(EzyQLQuery.class, "query")
				.equal()
				.append("this.newQueryBuilder()")
				.append("\n\t\t.parameterCount(").append(method.getParameterCount()).append(")")
				.append("\n\t\t.query(").string(queryString).append(")");
		body.append(createQueryInstruction);
		String nextArg = null;
		int paramCount = method.getParameterCount();
		if(paramCount > 0) {
			Class<?> lastParamType = method.getParameterTypes()[paramCount - 1];
			if(Next.class.isAssignableFrom(lastParamType)) {
				-- paramCount;
				nextArg = "arg" + paramCount;
			}
		}
		for(int i = 0 ; i < paramCount ; ++i) {
			body.append(new EzyInstruction("\t\t", "\n", false)
					.append(".parameter(")
						.append(i).append(",").append("arg").append(i)
					.append(")"));
		}
		body.append(new EzyInstruction("\t\t", "\n").append(".build()"));

		String methodName = method.getName();
		Class<?> returnType = method.getReturnType();
		EzyInstruction answerInstruction = new EzyInstruction("\t", "\n");
		if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FIND)) {
			if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FIND_LIST))
				answerInstruction.answer().cast(returnType, "this.findListWithQuery(query, " + nextArg + ")");
			else	
				answerInstruction.answer().cast(entityType, "this.findOneWithQuery(query)");
		}
		else if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FETCH_ONE)) {
			Class<?> resultType = anno.resultType();
			if(resultType == Object.class)
				resultType = entityType;
			if(methodName.startsWith(EzyDatabaseRepository.PREFIX_FETCH_LIST))
				answerInstruction.answer().cast(returnType, 
						"this.aggregateListWithQuery(query," + resultType.getName() + ".class, " + nextArg + ")");
			else	
				answerInstruction.answer().cast(resultType, 
						"this.aggregateOneWithQuery(query," + resultType.getName() + ".class)");
		}
		else if(methodName.startsWith(EzyDatabaseRepository.PREFIX_COUNT)) {
			if(returnType != int.class && returnType != long.class)
				throw new IllegalArgumentException("count method must return int or long, error method: " + method);
			body.append(new EzyInstruction("\t", "\n")
					.variable(long.class, "answer").equal().append("0L"));
				body.append(new EzyInstruction("\t", "\n")
						.append("answer = this.countWithQuery(query, " + nextArg + ")"));
				answerInstruction.answer()
					.append(returnType == long.class ? "answer" : "(int)answer");
		}
		else if(methodName.startsWith(EzyDatabaseRepository.PREFIX_UPDATE) ||
				methodName.startsWith(EzyDatabaseRepository.PREFIX_DELETE)) {
			if(returnType != int.class && returnType != void.class)
				throw new IllegalArgumentException("update or delete method must return int or void, error method: " + method);
			body.append(new EzyInstruction("\t", "\n")
					.variable(int.class, "answer").equal().append("0"));
			if(methodName.startsWith(EzyDatabaseRepository.PREFIX_UPDATE)) {
				body.append(new EzyInstruction("\t", "\n")
						.append("answer = this.updateWithQuery(query)"));
			}
			else {
				body.append(new EzyInstruction("\t", "\n")
						.append("answer = this.deleteWithQuery(query)"));
			}
			answerInstruction.answer().append("answer");
		}
		else {
			throw newInvalidMethodException(method);
		}
		if(returnType != void.class)
			body.append(answerInstruction);
		return body.function().toString();
	}

	@Override
	protected Class<?> getBaseRepositoryInterface() {
		return EzyMongoRepository.class;
	}

	@Override
	protected Class<?> getSuperClass() {
		return EzySimpleMongoRepository.class;
	}
	
}
