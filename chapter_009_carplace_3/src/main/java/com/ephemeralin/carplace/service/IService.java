package com.ephemeralin.carplace.service;

import java.util.List;

/**
 * The interface Service.
 *
 * @param <T> the type parameter
 */
public interface IService<T> {
    /**
     * Create int.
     *
     * @param entity the entity
     * @return the int
     */
    int create(T entity);

    /**
     * Find by id t.
     *
     * @param id the id
     * @return the t
     */
    T findById(int id);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<T> findAll();

    /**
     * Update t.
     *
     * @param entity the entity
     * @return the t
     */
    T update(T entity);

    /**
     * Delete boolean.
     *
     * @param id the id
     */
    void delete(int id);

}
