package com.lysenko.config;

import com.lysenko.entity.Event;
import com.lysenko.entity.File;
import com.lysenko.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {

    private static SessionFactory sessionFactory;

    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Создание объекта Configuration с настройками Hibernate
                Configuration configuration = new Configuration();

                // Указание настроек подключения к базе данных
                configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                configuration.setProperty("hibernate.connection.url", "jdbc:mysql://127.0.01:3306/crud");
                configuration.setProperty("hibernate.connection.username", "postgres");
                configuration.setProperty("hibernate.connection.password", "Qq111111");

                // Добавление классов, которые будут сопоставлены с таблицами
                configuration.addAnnotatedClass(Event.class);
                configuration.addAnnotatedClass(File.class);
                configuration.addAnnotatedClass(User.class);

                // Создание SessionFactory
                sessionFactory = configuration.buildSessionFactory();

            } catch (Exception e) {
                throw new RuntimeException("Failed to build SessionFactory " + e);
            }
        }
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }
}
