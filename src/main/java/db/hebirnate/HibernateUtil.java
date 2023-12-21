package db.hebirnate;

import db.entity.Goods;
import db.entity.Sales;
import db.entity.Warehouse1;
import db.entity.Warehouse2;
import jakarta.persistence.PersistenceException;
import org.hibernate.*;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;


import java.sql.CallableStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure()
                    .setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy())
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static List<Goods> getAllGoods() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Goods", Goods.class).getResultList();
        }
    }

    public static Goods getGood(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Goods good = session.get(Goods.class, id);
            return good;
        }
    }

    public static void addGood(Goods good) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(good);
            transaction.commit();
        }
    }

    public static void updateGood(Goods good, String newName, Integer newPriority) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            good.setName(newName);
            good.setPriority(newPriority);
            session.merge(good);
            transaction.commit();
        }
    }

    public static boolean deleteGood(Goods good) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(good);
                transaction.commit();
                return true;
            } catch (PersistenceException e) {

                transaction.rollback();
                return false;
            }
        }
    }

    public static List<Sales> getSales() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Sales", Sales.class).getResultList();
        }
    }

    public static boolean addSale(Sales sale) {
        AtomicBoolean success = new AtomicBoolean(true);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.doWork(connection -> {
                    try (CallableStatement statement = connection.prepareCall("{ CALL write_off_goods(?, ?) }")) {
                        statement.setInt(1, sale.getGoods().getId());
                        statement.setInt(2, sale.getGoodCount());
                        statement.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        success.set(false);
                    }
                });
                if (success.get()) {
                    session.persist(sale);
                    transaction.commit();
                } else {
                    transaction.rollback();
                }
            } catch (PersistenceException e) {
                e.printStackTrace();
                transaction.rollback();
                success.set(false);
            }
        }
        return success.get();
    }

    public static List<Warehouse1> getWarehouse1() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Warehouse1", Warehouse1.class).getResultList();
        }
    }

    public static void addToWarehouse1(Warehouse1 warehouse1Item) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(warehouse1Item);
            transaction.commit();
        }
    }

    public static void updateWarehouse1(Warehouse1 warehouse1, Integer goodCount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            warehouse1.setGoodCount(goodCount);
            session.merge(warehouse1);
            transaction.commit();
        }
    }

    public static boolean deleteInWarehouse1(Warehouse1 warehouse1) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(warehouse1);
                transaction.commit();
                return true;
            } catch (PersistenceException e) {
                transaction.rollback();
                return false;
            }
        }
    }

    public static List<Warehouse2> getWarehouse2() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Warehouse2", Warehouse2.class).getResultList();
        }
    }

    public static void addToWarehouse2(Warehouse2 warehouse1Item) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(warehouse1Item);
            transaction.commit();
        }
    }

    public static boolean updateWarehouse2(Warehouse2 warehouse2, Integer goodCount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                warehouse2.setGoodCount(goodCount);
                session.merge(warehouse2);
                transaction.commit();
                return true;
            } catch(Exception e) {
                return false;
            }

        }
    }

    public static boolean deleteInWarehouse2(Warehouse2 warehouse2) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(warehouse2);
                transaction.commit();
                return true;
            } catch (PersistenceException e) {
                transaction.rollback();
                return false;
            }
        }
    }

    public static List<Object[]> getMostPopularGoods(LocalDate since, LocalDate by) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createNativeQuery("SELECT * FROM get_most_popular(:since, :by)");
            query.setParameter("since", since);
            query.setParameter("by", by);

            List<Object[]> result = query.getResultList();

            transaction.commit();
            return result;
        }
    }

    public static List<Object[]> getDatesAndGoodCount(Integer id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createNativeQuery("SELECT * FROM get_dates_and_good_count(:id)");
            query.setParameter("id", id);


            List<Object[]> result = query.getResultList();

            transaction.commit();
            return result;
        }
    }
}

