package demo.model;

import org.springframework.context.annotation.Scope;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Message")
@Table(name = "messages")
@Scope(value = "prototype")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String topic;
	private LocalDateTime date;
    private String story;
    private String clientName;
    private Long idClient;

    private Message(Long id, String topic, LocalDateTime date, String story, String clientName, Long idClient) {
		this.id = id;
        this.topic = topic;
		setDate(date);
        this.story = story;
        this.clientName = clientName;
        this.idClient = idClient;
	}

	public Message() {
	}

    public void input(String story, String clientName, Long idClient) {
		setDate(date);
        setStory(story);
        setClientName(clientName);
        setIdClient(idClient);
        
	}

	public Long getId() {
		return id;
	}

    public String getTopic() {
        return topic;
	}

    protected void setTopic(String topic) {
        this.topic = topic;
	}

	public LocalDateTime getDate() {
		return date;
	}

	private void setDate(LocalDateTime date) {
		if (date == null) {
			date = LocalDateTime.now();
		}
		this.date = date;
	}


    public String getStory() {
        return story;
	}

    private void setStory(String story) {
        this.story = story;
	}

    public String getClientName() {
        return clientName;
	}

    private void setClientName(String clientName) {
        this.clientName = clientName;
	}

	@Override
	public String toString() {
        return "StoryOfTopic [id=" + id + ", topic=" + topic + ", date=" + date + " " + story + " " + story + " "
            + "clientName" + clientName + "]";
	}

	protected Message storyForSort() {
        return new Message(id, topic, date, story, clientName, idClient);

	}
    
    public Long getIdClient() {
        return idClient;
    }
    
    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }
}
