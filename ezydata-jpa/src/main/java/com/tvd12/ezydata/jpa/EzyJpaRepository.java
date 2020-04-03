package com.tvd12.ezydata.jpa;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezyfox.exception.UnimplementedOperationException;
import com.tvd12.ezyfox.reflect.EzyGenerics;
import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings("unchecked")
public abstract class EzyJpaRepository<I,E> 
		extends EzyLoggable
		implements EzyDatabaseRepository<I,E>, EzyDatabaseContextAware {

	protected final Class<E> entityType;
	protected EntityManager entityManager;
	protected EzyJpaDatabaseContext databaseContext;
	
	public EzyJpaRepository() {
		this.entityType = getEntityType();
	}
	
	@Override
	public void setDatabaseContext(EzyDatabaseContext context) {
		this.databaseContext = (EzyJpaDatabaseContext)context;
		this.entityManager = databaseContext.getEntityManager();
	}
	
	@Override
	public long count() {
		String queryString = new StringBuilder()
				.append("select count(e.id) from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		Query query = entityManager.createQuery(queryString);
		long count = (long)query.getSingleResult();
		return count;
	}

	@Override
	public void save(E entity) {
		entityManager.merge(entity);
	}

	@Override
	public void save(Iterable<E> entities) {
		for(E entity : entities)
			entityManager.merge(entity);
	}

	@Override
	public E findById(I id) {
		E entity = entityManager.find(entityType, id);
		return entity;
	}

	@Override
	public List<E> findListByIds(Collection<I> ids) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id in ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, ids);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public E findByField(String field, Object value) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, value);
		Object entity = query.getSingleResult();
		return (E)entity;
	}

	@Override
	public List<E> findListByField(String field, Object value) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, value);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public List<E> findListByField(String field, Object value, int skip, int limit) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.").append(field).append(" = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, value);
		query.setFirstResult(skip);
		query.setMaxResults(limit);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public List<E> findAll() {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		Query query = entityManager.createQuery(queryString);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public List<E> findAll(int skip, int limit) {
		String queryString = new StringBuilder()
				.append("select e from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setFirstResult(skip);
		query.setMaxResults(limit);
		List<E> list = query.getResultList();
		return list;
	}

	@Override
	public int deleteAll() {
		String queryString = new StringBuilder()
				.append("delete from ")
				.append(entityType.getName()).append(" e ")
				.toString();
		Query query = entityManager.createQuery(queryString);
		int deletedRows = query.executeUpdate();
		return deletedRows;
	}

	@Override
	public void delete(I id) {
		String queryString = new StringBuilder()
				.append("delete from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id = ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, id);
		query.executeUpdate();
	}

	@Override
	public int deleteByIds(Collection<I> ids) {
		String queryString = new StringBuilder()
				.append("delete from ")
				.append(entityType.getName()).append(" e ")
				.append("where e.id in ?0")
				.toString();
		Query query = entityManager.createQuery(queryString);
		query.setParameter(0, ids);
		int deletedRows = query.executeUpdate();
		return deletedRows;
	}
	
	@SuppressWarnings({ "rawtypes" })
	protected Class<E> getEntityType() {
		try {
			Type genericSuperclass = getClass().getGenericSuperclass();
			Class[] genericArgs = EzyGenerics.getTwoGenericClassArguments(genericSuperclass);
			return genericArgs[1];
		}
		catch (Exception e) {
			throw new UnimplementedOperationException("class " + getClass().getName() + " hasn't implemented method 'getEntityType'", e);
		}
	}

}
