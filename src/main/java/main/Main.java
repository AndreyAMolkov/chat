package main;

import org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import javax.persistence.EntityManager;
import demo.model.Client;
import demo.model.Credential;
import demo.model.Data;
import demo.model.Message;
import demo.model.Topic;

// BEFORE CREATE DATABASE chat via sql and after that
//	this class you can use for fast setting your new schema "Chat" (create properly structure tables)

public class Main {
//private static Logger log = LoggerFactory.getLogger("demo.controller.Main");
	public static void main(String[] args) {
		String nameMethod = "main";
		DriverAdapterCPDS cpds = new DriverAdapterCPDS();
		try {
			cpds.setDriver("org.gjt.mm.mysql.Driver");
		} catch (ClassNotFoundException e) {
//			log.error(nameMethod + Constants.ONE_PARAMETERS, "Error set driver for AdapterCPDS", e);
            System.out.println("First-------------------");
            e.printStackTrace();
		}
        cpds.setUrl("jdbc:mysql://localhost:3306/chat?useSSL=false&allowPublicKeyRetrieval=true");
			cpds.setUser("root");
			cpds.setPassword("1234");
			SharedPoolDataSource tds = new SharedPoolDataSource();
			tds.setConnectionPoolDataSource(cpds);
			tds.setMaxTotal(10);
			tds.setMaxConnLifetimeMillis(50);
			LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(tds);
			sessionBuilder.addAnnotatedClass(Topic.class);
			sessionBuilder.addAnnotatedClass(Client.class);
			sessionBuilder.addAnnotatedClass(Data.class);
			sessionBuilder.addAnnotatedClass(Credential.class);
			sessionBuilder.addAnnotatedClass(Message.class);
			sessionBuilder.setProperty("hibernate.dialect", "MySQL57InnoDB");
			sessionBuilder.setProperty("hibernate.show_sql", "true");
        sessionBuilder.setProperty("hibernate.hbm2ddl.auto", "update");
			sessionBuilder.setProperty("hibernate.use_sql_comments", "true");
			SessionFactory sessionFactory = sessionBuilder.buildSessionFactory();
			try (Session session = sessionFactory.openSession()) {
				EntityManager em = session.getEntityManagerFactory().createEntityManager();
				em.getTransaction().begin();
				Data data = new Data("Maz", "Mazov", "Mazin");
            Credential credential = new Credential("admin", "admin", "ADMIN");
				Client client = new Client(credential, data);
				em.persist(client);
				em.getTransaction().commit();
//				log.debug(nameMethod +Constants.FOUR_PARAMETERS,"create tables and user: ",client.getCredential().getLogin()
//						,"test password: ", "1234", "role: ", client.getCredential().getRole());
            System.out.println(nameMethod + "create tables and user: " + client.getCredential().getLogin()
                + "test password: " + "1234" +
                "role: " + client.getCredential().getRole());
			} catch (Exception e) {
//				log.error(nameMethod + Constants.ONE_PARAMETERS,"Error session",e);
            e.printStackTrace();
            
			}
		
	}

}
