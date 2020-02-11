package uk.mattjlewis.thorntail.testapp.services.jpa;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import uk.mattjlewis.thorntail.testapp.model.BaseEntity;

public class BaseEntityRepository {
	public static <E extends BaseEntity> E create(EntityManager entityManager, E entity) {
		Date now = new Date();
		entity.setCreated(now);
		entity.setLastUpdated(now);
	
		entityManager.persist(entity);
		
		return entity;
	}
	
	public static <E> List<E> findAll(EntityManager entityManager, Class<E> entityClass) {
		//return entityManager.createQuery(entityManager.getCriteriaBuilder().createQuery(entityClass)).getResultList();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		// Create a query
		CriteriaQuery<E> query = cb.createQuery(entityClass);
		// Set the root class
		Root<E> root = query.from(entityClass);
		// Is this required?
		query = query.select(root);
		// Perform the query
		return entityManager.createQuery(query).getResultList();
	}

	public static <E, ID> E findById(EntityManager entityManager, Class<E> entityClass, ID id) {
		E entity = entityManager.find(entityClass, id);
		if (entity == null) {
			throw new EntityNotFoundException(entityClass.getSimpleName() + " not found for id " + id);
		}
		
		return entity;
	}

	public static <E, ID> Optional<E> findOptionalById(EntityManager entityManager, Class<E> entityClass, ID id) {
		return Optional.ofNullable(entityManager.find(entityClass, id));
	}
	
	public static <E extends BaseEntity, ID> E update(EntityManager entityManager, ID id, E entity) {
		var current = findById(entityManager, entity.getClass(), id);
		
		entity.setCreated(current.getCreated());
		entity.setLastUpdated(new Date());
		
		return entityManager.merge(entity);
	}

	public static <E, ID> void delete(EntityManager entityManager, Class<E> entityClass, ID id) {
		entityManager.remove(findById(entityManager, entityClass, id));
	}
}
