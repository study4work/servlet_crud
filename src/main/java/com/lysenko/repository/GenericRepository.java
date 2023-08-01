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
        T fileFromBd;
        try (Session session = HibernateConfig.getSession()) {
            Transaction transaction = session.beginTransaction();
            fileFromBd = session.get(entityClass, (Serializable) id);
            transaction.commit();
        } catch (Exception e) {
            throw new MyHibernateException("Can't find entity by id" + id);
        }
        return fileFromBd;
    }

    List<T> findAll();

    default T delete(Class<T> entityClass, ID id) {
        T fileFromDb;
        try (Session session = HibernateConfig.getSession()) {
            Transaction transaction = session.beginTransaction();
            fileFromDb = session.get(entityClass, (Serializable) id);
            session.delete(fileFromDb);
            transaction.commit();
        } catch (Exception e) {
            throw new MyHibernateException(String.format("Something goes wrong during delete file with id: %d: %s", id, e));
        }
        return fileFromDb;
    }
}
