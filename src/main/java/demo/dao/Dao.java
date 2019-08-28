package demo.dao;

import java.util.List;
import demo.model.Client;
import demo.model.Credential;
import demo.model.Data;
import demo.model.Message;
import demo.model.Topic;

public interface Dao {

    public void merge(Client client);

    public void removeClient(Long idClient);

    public Client getClientById(Long idClient);
	
    public Topic getTopicById(Long idTopic);
	
    public Data getDataById(Long idData);

    public Credential findCredentialByName(String username);

    public void add(Object obj);

    public void newTopic(Long idClient, String nameOfTopic);

    public List<Client> getAllClients();
    
    public List<Topic> getAllTopics();
    
    public List<Topic> getAllTopicsNotSort();

    public void addMessageToTopic(Long idTopic, String story, Long idClient);

    public boolean removeTopic(Long id);

    public Boolean findLoginInBd(String login);

    public Object nameLoginClientOwner(Long idClientOwner);
    
    public Message getMessageById(Long id);
    
    boolean removeMessage(Long idMessage, Long idClient, String role) throws ChatException;
}
