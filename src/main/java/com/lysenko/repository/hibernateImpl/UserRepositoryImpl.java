package com.lysenko.repository.hibernateImpl;

import com.lysenko.config.HibernateConfig;
import com.lysenko.entity.File;
import com.lysenko.entity.User;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User update(Integer integer, User user) {
        try (Session session = HibernateConfig.getSession()) {
            Transaction transaction = session.beginTransaction();
            User userFromDb = session.get(User.class, integer);
            userFromDb.setName(user.getName());
            session.update(userFromDb);
            transaction.commit();
        } catch (Exception e) {
            throw new MyHibernateException(String.format("Something goes wrong during update %s: %s", user, e));
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return HibernateConfig.getSession().createQuery("select u from User u", User.class).list();
    }
}
