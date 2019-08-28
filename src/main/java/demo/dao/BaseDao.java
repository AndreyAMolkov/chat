package demo.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import demo.model.Client;
import demo.model.Data;
import demo.model.Message;
import demo.model.Topic;

public abstract class BaseDao implements Dao {
	@PersistenceContext
	private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

	public BaseDao() {

	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public Client getClientById(Long idClient) {
		return em.find(Client.class, idClient);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
    public Topic getTopicById(Long id) {
        return em.find(Topic.class, id);
	}
    
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Message getMessageById(Long id) {
        return em.find(Message.class, id);
    }
    
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public Data getDataById(Long idData) {
		return em.find(Data.class, idData);
	}

	@SuppressWarnings("unchecked")
	@Override
    public List<Client> getAllClients() {
		return em.createQuery("SELECT c FROM Client c").getResultList();

	}
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Topic> getAllTopicsNotSort() {
        return em.createQuery("SELECT c FROM Topic c").getResultList();

    }
    
	@Override
    @Transactional(propagation = Propagation.REQUIRED)
	public void merge(Client client) {
		em.merge(client);
	}

	@Override
    @Transactional
	public void add(Object obj) {
		em.persist(obj);
	}

	@Override
    @Transactional
    public void removeClient(Long id) {
        Client c = getClientById(id);
		em.remove(c);
	}
    
    @Override
    @Transactional
    public boolean removeTopic(Long id) {
        Topic t = getTopicById(id);
        if (t != null) {
            em.remove(t);
            return true;
        }
        return false;
    }
    
}
