package demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import demo.model.Client;
import demo.model.Credential;
import demo.model.Message;
import demo.model.Topic;

@Repository
public class DaoImp extends BaseDao implements Dao {
	@PersistenceContext
	private EntityManager em;

	public Message getStory() {
		return new Message();
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
    public void newTopic(Long idClient, String nameOfTopic) {
        Client client = getClientById(idClient);
        client.setTopics(new Topic(nameOfTopic));
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
    public void addMessageToTopic(Long id, String story, Long idClient) {
        Client client = getClientById(idClient);
        Topic topic = getTopicById(id);
		Message storyInput = getStory();
        storyInput.input(story, client.getNameFromData(), idClient);
		topic.setHistories(storyInput);
	}

	@Override
    @Transactional(readOnly = true)
	public Boolean findLoginInBd(String login) {
		Boolean result = false;
        if (findCredentialByName(login) != null) {
			result = true;
		}
		return result;
	}

	@Override
    @Transactional(readOnly = true)
	public Object nameLoginClientOwner(Long idClientOwner) {
		String name = null;
		Client client = getClientById(idClientOwner);
		if (client != null) {
			name = client.getCredential().getLogin();
		}
		return name;
	}
    
    @Transactional(readOnly = true, rollbackFor = javax.persistence.NoResultException.class)
    @Override
    public Credential findCredentialByName(String username) {
        TypedQuery<Credential> list = null;
        list = em.createQuery("SELECT c from Credential c WHERE c.login = :username", Credential.class)
            .setParameter("username", username);
        return list.getSingleResult();
    }
    
    @Override
    @Transactional
    public boolean removeMessage(Long idMessage, Long idClient, String role) throws ChatException {
        Message m = getMessageById(idMessage);
        if (m == null) {
            return false;
        }
        if ("ROLE_ADMIN".equals(role) || idClient.equals(m.getIdClient())) {
            em.remove(m);
            return true;
        }else {
            throw new ChatException("access denied");
        }

    }
    
    @Override
    public List<Topic> getAllTopics() {
        return sortTopicsLastDateFirst(getAllTopicsNotSort());
    }

    private List<Topic> sortTopicsLastDateFirst(List<Topic> topicsOld) {
        Collections.sort(topicsOld,
            (topic1, topic2) -> topic2.getDateNotNullForSort().compareTo(topic1.getDateNotNullForSort()));
        return topicsOld;
    }
    

}
