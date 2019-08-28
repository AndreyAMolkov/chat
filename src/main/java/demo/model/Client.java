package demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "Client")
@Table(name = "clients")
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "data_id")
	private Data data;
	@Autowired
	@javax.persistence.Transient
    private Message message;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "credential_id")
	private Credential credential;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "client_id")
	private List<Topic> topics;
	private String nameFromData;

	public Client(Credential credential, Data data) {
        setTopics(new ArrayList<Topic>());
		setData(data);
		setCredential(credential);
	}

	public Client() {
	}

	public Client(Long id, Data data, Credential credential) {
		super();
		this.id = id;
		this.data = data;
		this.credential = credential;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Data getData() {
		if (data == null) {
			setData(new Data());
		}
		return this.data;
	}

	public void setData(Data data) {
		if (data == null) {
			data = new Data();
		}
		this.data = data;
		data.setClient(this);
        this.nameFromData = data.getFullName();
	}

	public Credential getCredential() {
		if (credential == null) {
			setCredential(new Credential());
		}
		return credential;
	}

	public void setCredential(Credential credential) {
		if (credential == null) {
			credential = new Credential();
		}
		this.credential = credential;
		credential.setClient(this);
	}

    public List<Topic> getTopics() {
		if (topics == null) {
			this.topics = new ArrayList<>();
		}
		return topics;
	}

    public void setTopics(List<Topic> topics) {
		if (topics == null) {
            this.topics = new ArrayList<>();
		}
	}

    public void setTopics(Topic topic) {
		if (topic == null) {
			topic = new Topic();
		}
        getTopics().add(topic);
	}

	public String getNameFromData() {
		return nameFromData;
	}

	public void setNameFromData(String nameFromData) {
		this.nameFromData = nameFromData;
	}

	@Override
	public String toString() {
        return "Client [id=" + id + ", dataOfClient=" + data + ", Credential=" + credential + ", topics=" + topics
				+ "]";
	}

    public Message getMessage() {
        return message;
	}

    public void setMessage(Message message) {
        this.message = message;
	}

}
