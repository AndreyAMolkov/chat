package demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "Topic")
@Table(name = "topics")
public class Topic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;

    private String nameOfTopic;
	@Transient
	private List<Message> sortList;

	@Autowired
	@javax.persistence.Transient
	private Message message;
    
    @Transient
    private String link;
    
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "topics_id")
	private List<Message> histories;
    
    private LocalDateTime date;

	public Topic() {
		setHistories(histories);
	}
    
    public Topic(String nameOfTopic) {
        setNameOfTopic(nameOfTopic);
        setHistories(histories);
    }
    public Long getId() {
        return id;
	}

    public void setId(Long id) {
        this.id = id;
	}

	public String getHistoriesSize() {
		if (histories == null) {
			return "empty";
		}
		int result = 0;
		try {
			result = getHistories().size();
		} catch (IllegalStateException e) {

		}
		return String.valueOf(result);
	}

	public List<Message> getHistories() {
		if (histories == null) {
			this.histories = new ArrayList<>();
		}
		return histories;
	}

	public List<Message> getSortHistories() {
		return sortHistoriesLastDateFirst(getCopy());
	}

	private List<Message> getCopy() {
		return getHistories().stream().map(Message::storyForSort).collect(Collectors.toList());
	}

	public List<Message> sortHistoriesLastDateFirst(List<Message> historiesOld) {
		Collections.sort(historiesOld, (story1, story2) -> story2.getDate().compareTo(story1.getDate()));
		return historiesOld;
	}

	public void setHistories(List<Message> historiesNew) {
		if (this.histories == null)
			this.histories = new ArrayList<>(5);

		if (historiesNew == null) {
			historiesNew = new ArrayList<>(5);
		}
		historiesNew.stream().forEach(this::setHistories);
	}

	public void setHistories(Message message) {
        this.histories.add(message);
        setDate();
	}

	@Override
	public String toString() {
        return "[" + "number=" + id + "]";
	}

    public String getNameOfTopic() {
        return nameOfTopic;
    }
    
    public void setNameOfTopic(String nameOfTopic) {
        this.nameOfTopic = nameOfTopic;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public LocalDateTime getDateNotNullForSort() {
        if (date == null) {
            return LocalDateTime.MIN;
        }
        return date;
    }
    public void setDate() {
        this.date = LocalDateTime.now();
    }
    
    public String getLink() {
        
        return "/Chat/client/showHistories/" + getId();
    }
    
    public void setLinkDelete(String link) {
        this.link = link;
    }
    
}
