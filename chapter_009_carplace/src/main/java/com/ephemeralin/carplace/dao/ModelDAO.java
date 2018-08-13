package com.ephemeralin.carplace.dao;

import com.ephemeralin.carplace.model.Model;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * The type Model dao.
 */
@Repository
@Log4j2
public class ModelDAO extends DAO<Model> implements IDAO<Model> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public int create(Model entity) {
        sessionFactory.openSession().save(entity);
        return entity.getId();
    }

    @Override
    public Model findById(int id) {
        return sessionFactory.openSession().get(Model.class, id);
    }

    @Override
    public List findAll() {
        Session session = sessionFactory.openSession();
        return session.createQuery("FROM Model ").list();
    }

    @Override
    public Model update(Model entity) {
        Session session = sessionFactory.openSession();
        Model entityUpdate = session.load(Model.class, entity.getId());
        entityUpdate.setName(entity.getName());
        return entityUpdate;
    }

    @Override
    public boolean delete(int id) {
        Model entity = findById(id);
        return super.delete(sessionFactory, entity);
    }

    public List findByCriteria(HashMap<String, Object> criterias) {
        return super.findByCriteria(sessionFactory, criterias);
    }
}
