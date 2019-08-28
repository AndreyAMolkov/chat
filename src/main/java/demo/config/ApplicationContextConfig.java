package demo.config;

import org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import javax.sql.DataSource;
import demo.dao.Dao;
import demo.dao.DaoImp;
import demo.model.Client;
import demo.model.Credential;
import demo.model.Data;
import demo.model.InfoProblem;
import demo.model.Message;
import demo.model.Topic;

@Configuration
@EnableWebMvc
@ComponentScan("demo.*")
@EnableTransactionManagement
@Import({FormLoginSecurityConfig.class})
public class ApplicationContextConfig implements TransactionManagementConfigurer {
    
    @Bean(name = "loginEntity")
    public Credential getCredential() {
        return new Credential();
    }
    
    @Bean(name = "data")
    public Data getData() {
        return new Data();
    }
    
    @Bean(name = "client")
    public Client getClient() {
        return new Client();
    }
    
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public InfoProblem infoProblem() {
        return new InfoProblem();
    }
    
    @Bean(name = "story")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Message getStory() {
        return new Message();
    }
    
    @Bean(name = "topic")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Topic getTopic() {
        return new Topic();
    }
    
    @Bean(name = "model")
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ModelAndView getModel() {
        return new ModelAndView();
    }
    
    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
    
    @Bean(name = "dataSourse")
    @Primary
    public DataSource getDataSource() {
        
        DriverAdapterCPDS cpds = new DriverAdapterCPDS();
        try {
            cpds.setDriver("org.gjt.mm.mysql.Driver");
        } catch (ClassNotFoundException e) {
            // log.error(nameMethod + Constants.ONE_PARAMETERS, "Error", e);
        }
        cpds.setUrl("jdbc:mysql://localhost:3306/chat?useSSL=false&allowPublicKeyRetrieval=true");
        cpds.setUser("root");
        cpds.setPassword("1234");
        SharedPoolDataSource tds = new SharedPoolDataSource();
        tds.setConnectionPoolDataSource(cpds);
        tds.setMaxTotal(10);
        tds.setMaxConnLifetimeMillis(50);
        
        return tds;

    }
    
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
        
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.addAnnotatedClass(Topic.class);
        sessionBuilder.addAnnotatedClass(Client.class);
        sessionBuilder.addAnnotatedClass(Data.class);
        sessionBuilder.addAnnotatedClass(Credential.class);
        sessionBuilder.addAnnotatedClass(Message.class);
        sessionBuilder.setProperty("hibernate.dialect", "MySQL57InnoDB");
        // sessionBuilder.setProperty("hibernate.show_sql", "false");
        sessionBuilder.setProperty("hbm2ddl.auto", "update");
        // sessionBuilder.setProperty("hibernate.use_sql_comments", "false");
        sessionBuilder.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        return sessionBuilder.buildSessionFactory();
    }

    @Autowired
    @Bean(name = "dao")
    public Dao getDao() {
        return new DaoImp();
    }

    @Override
    @Bean(name = "transactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory(getDataSource()));
        return transactionManager;
    }
}