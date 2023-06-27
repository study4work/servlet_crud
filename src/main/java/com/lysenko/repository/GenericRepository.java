package com.lysenko.repository;

import com.lysenko.config.HibernateConfig;
import com.lysenko.exception.MyHibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public interface GenericRepository<T, ID> {

    default T save(T t) {
        try (Session session = HibernateConfig.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(t);
            transaction.commit();
        } catch (Exception e) {
            throw new MyHibernateException(String.format("Something goes wrong during save %s: %s", t, e));
        }
        return t;
    };

    T update(ID id, T t);

    default T findById(Class<T> entityClass, ID id) {
        return HibernateConfig.getSession().get(entityClass, (Serializable) id);
    }

    List<T> findAll();

    default void delete(Class<T> entityClass, ID id) {
        try (Session session = HibernateConfig.getSession()) {
            Transaction transaction = session.beginTransaction();
            T fileFromDb = session.get(entityClass, (Serializable) id);
            session.delete(fileFromDb);
            transaction.commit();
        } catch (Exception e) {
            throw new MyHibernateException(String.format("Something goes wrong during delete file with id: %d: %s", id, e));
        }
    }
}
