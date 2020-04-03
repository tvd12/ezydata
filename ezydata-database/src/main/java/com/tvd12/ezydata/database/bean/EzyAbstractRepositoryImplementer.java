package com.tvd12.ezydata.database.bean;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezyfox.asm.EzyFunction;
import com.tvd12.ezyfox.asm.EzyInstruction;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.reflect.EzyMethod;
import com.tvd12.ezyfox.reflect.EzyMethods;
import com.tvd12.ezyfox.util.EzyLoggable;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractRepositoryImplementer extends EzyLoggable {

	protected final EzyClass clazz;
	protected Class<?> idType;
	protected Class<?> entityType;
	
	@Setter
	protected static boolean debug; 
	protected static final AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzyAbstractRepositoryImplementer(Class<?> clazz) {
		this(new EzyClass(clazz));
	}
	
	public EzyAbstractRepositoryImplementer(EzyClass clazz) {
		this.clazz = clazz;
	}
	
	public Object implement(Object template) {
		try {
			return doimplement(template);
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}

	protected Object doimplement(Object template) throws Exception {
		Class[] idAndEntityTypes = getIdAndEntityTypes();
		idType = idAndEntityTypes[0];
		entityType = idAndEntityTypes[1];
		ClassPool pool = ClassPool.getDefault();
		String implClassName = getImplClassName();
		CtClass implClass = pool.makeClass(implClassName);
		EzyClass superClass = new EzyClass(getSuperClass());
		implClass.setSuperclass(pool.get(superClass.getName()));
		for(EzyMethod method : getAbstractMethods()) {
			String methodContent = makeAbstractMethodContent(method);
			printMethodContent(methodContent);
			implClass.addMethod(CtNewMethod.make(methodContent, implClass));
		}
		String getEntityTypeMethodContent = makeGetEntityTypeMethodContent(entityType);
		printMethodContent(getEntityTypeMethodContent);
		implClass.addMethod(CtNewMethod.make(getEntityTypeMethodContent, implClass));
		implClass.setInterfaces(new CtClass[] { pool.get(clazz.getName()) });
		Class answerClass = implClass.toClass();
		implClass.detach();
		Object repo = answerClass.newInstance();
		setRepoComponent(repo, template);
		return repo;
	}
	
	protected abstract void setRepoComponent(Object repo, Object template);
	
	protected Collection<EzyMethod> getAbstractMethods() {
		return clazz.getMethods(m -> m.isAnnotated(EzyQuery.class));
	}
	
	protected String makeAbstractMethodContent(EzyMethod method) {
		return "";
	}
	
	protected String makeGetEntityTypeMethodContent(Class entityType) {
		return new EzyFunction(getEntityTypeMethod())
				.body()
					.append(new EzyInstruction("\t", "\n")
							.answer()
							.clazz(entityType, true))
					.function()
				.toString();
	}
	
	protected EzyMethod getEntityTypeMethod() {
		Method method = EzyMethods.getMethod(getSuperClass(), "getEntityType");
		return new EzyMethod(method);
	}
	
	protected abstract Class<?> getSuperClass();
	
	protected String getImplClassName() {
		return clazz.getName() + "$EzyDatabaseRepository$EzyAutoImpl$" + COUNT.incrementAndGet();
	}
	
	protected Class[] getIdAndEntityTypes() {
		return EzyGenerics.getGenericInterfacesArguments(clazz.getClazz(), getBaseRepositoryInterface(), 2);
	}
	
	protected Class<?> getBaseRepositoryInterface() {
		return EzyDatabaseRepository.class;
	}
	
	protected void printMethodContent(String methodContent) {
		if(debug) 
			logger.info("reader: method content \n{}", methodContent);
	}
	
}
