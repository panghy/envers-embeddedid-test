package org.hibernate.clementp;

import org.hibernate.cfg.Environment;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.service.BootstrapServiceRegistryBuilder;
import org.junit.*;

import javax.persistence.EntityManager;
import java.util.Properties;

public class HHH7157Test {
  static private Ejb3Configuration cfg = null;
  static private EntityManagerFactoryImpl emf = null;
  private EntityManager em = null;

  @BeforeClass
  public static void init() {
    cfg = new Ejb3Configuration();
    cfg.configure(getConnectionProviderProperties());
    configure(cfg);
    emf = (EntityManagerFactoryImpl) cfg.buildEntityManagerFactory(new BootstrapServiceRegistryBuilder());
  }

  @AfterClass
  public static void destroy() {
    emf.close();
  }

  @Before
  public void setUp() {
    em = emf.createEntityManager();
  }

  @After
  public void tearDown() {
    em.close();
  }

  @Test
  public void testAuditedEmbeddedId() {
    em.getTransaction().begin();
    Person personA = new Person();
    personA.name = "Peter";
    Person personB = new Person();
    personB.name = "Mary";
    em.persist(personA);
    em.persist(personB);
    Constant cons = new Constant();
    cons.id = "USD";
    cons.name = "US Dollar";
    em.persist(cons);
    PersonTuple tuple = new PersonTuple(true, personA, personB, cons);
    em.persist(tuple);
    em.getTransaction().commit();
  }

  private static void configure(Ejb3Configuration cfg) {
    cfg.addAnnotatedClass(PersonTuple.class);
    cfg.addAnnotatedClass(Constant.class);
    cfg.addAnnotatedClass(Person.class);
  }

  public static final String DRIVER = "org.h2.Driver";
  public static final String URL = "jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;MVCC=TRUE";
  public static final String USER = "sa";
  public static final String PASS = "";

  public static Properties getConnectionProviderProperties(String dbName) {
    Properties props = new Properties(null);
    props.put(Environment.DRIVER, DRIVER);
    props.put(Environment.URL, String.format(URL, dbName));
    props.put(Environment.USER, USER);
    props.put(Environment.PASS, PASS);
    props.put(Environment.SHOW_SQL, "true");
    props.put(Environment.FORMAT_SQL, "true");
    props.put(Environment.HBM2DDL_AUTO, "create-drop");
    return props;
  }

  public static Properties getConnectionProviderProperties() {
    return getConnectionProviderProperties("db1");
  }
}