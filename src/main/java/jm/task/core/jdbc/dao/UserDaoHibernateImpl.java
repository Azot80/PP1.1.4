package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                String sql = "CREATE TABLE IF NOT EXISTS users " +
                        "(id BIGINT(19) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(45) NOT NULL, lastName VARCHAR(45) NOT NULL, " +
                        "age TINYINT(0) NOT NULL)";

                session.beginTransaction();
                session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                }
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                String sql = "DROP TABLE IF EXISTS users";

                session.beginTransaction();
                session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                }
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                User user = new User();
                user.setName(name);
                user.setLastName(lastName);
                user.setAge(age);

                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                }
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                User selectedUser = session.load(User.class, id);
                session.delete(selectedUser);
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                }
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("SELECT a FROM User a", User.class).getResultList();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            try {
                session.beginTransaction();
                session.createSQLQuery("TRUNCATE TABLE users").executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction().isActive()) {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                }
            }
        }
    }
}
