package com.lysenko.repository.hibernateImpl;

import com.lysenko.config.HibernateConfig;
import com.lysenko.entity.Event;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.repository.EventRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    @Override
    public Event update(Integer integer, Event event) {
        try (Session session = HibernateConfig.getSession()) {
            Transaction transaction = session.beginTransaction();
            Event fileFromDb = session.get(Event.class, integer);
            fileFromDb.setFile(event.getFile());
            fileFromDb.setUser(event.getUser());
            session.update(fileFromDb);
            transaction.commit();
        } catch (Exception e) {
            throw new MyHibernateException(String.format("Something goes wrong during update %s: %s", event, e));
        }
        return event;
    }

    @Override
    public List<Event> findAll() {
        return HibernateConfig.getSession().createQuery("select e from Event e", Event.class).list();
    }
}
