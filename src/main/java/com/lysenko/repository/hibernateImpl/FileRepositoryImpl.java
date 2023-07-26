package com.lysenko.repository.hibernateImpl;

import com.lysenko.config.HibernateConfig;
import com.lysenko.entity.File;
import com.lysenko.exception.MyHibernateException;
import com.lysenko.repository.FileRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class FileRepositoryImpl implements FileRepository {

    @Override
    public File update(Integer integer, File file) {
        try (Session session = HibernateConfig.getSession()) {
            Transaction transaction = session.beginTransaction();
            File fileFromDb = session.get(File.class, integer);
            fileFromDb.setName(file.getName());
            session.update(fileFromDb);
            transaction.commit();
        } catch (Exception e) {
            throw new MyHibernateException(String.format("Something goes wrong during update %s: %s", file, e));
        }
        return file;
    }

    @Override
    public List<File> findAll() {
        return HibernateConfig.getSession().createQuery("select f from File f", File.class).list();
    }
}
