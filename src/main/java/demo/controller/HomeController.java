package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import demo.constant.Constants;
import demo.dao.ChatException;
import demo.dao.Dao;
import demo.model.Client;
import demo.model.Credential;
import demo.model.Data;
import demo.model.InfoProblem;
import demo.model.Topic;
import demo.service.Password;
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Controller
public class HomeController {
	@Autowired
	private ModelAndView model;
	@Autowired(required = true)
	private Credential credential;

	@Autowired
	private Data data;

	@Autowired(required = true)
	private Client client;
    
    @Autowired(required = true)
    private Topic topic;

	@Autowired
	private InfoProblem infoProblem;

	private String rolePrincipal;

	private Long idPrincipal;

	@Autowired
	private Dao dao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView start() {
		model.setViewName(Constants.REDIRECT + "home");
		return model;
	}
    @RequestMapping(value = "/client/showHistories/{idTopic}", method = RequestMethod.GET)
    public ModelAndView showClientHistoryGet(@PathVariable Long idTopic) {
    model.setViewName(Constants.MESSAGE_SHOW);
    loadMessages(idTopic);
    return model;
}
    
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "accessDenied", required = false) String accessDenied

	) {
		if (accessDenied != null) {
			model.addObject("accessDenied", accessDenied);
		}

		model.setViewName(Constants.PAGE_LOGIN);
		return model;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home() {
		updateDatePrincipal();
        if ("ROLE_CLIENT".equals(rolePrincipal) || "ROLE_ADMIN".equals(rolePrincipal)) {
			loadOneClient(idPrincipal);
            loadAllTopics();
			model.setViewName(Constants.CLIENT_SHOW);
			return model;
		}
        model.setViewName(Constants.REDIRECT + "login");
        handlerEvents("Access denied");
        return model;
	}

    @RequestMapping(value = "/client/addTopic", method = {RequestMethod.POST})
    public ModelAndView addTopic(@RequestParam(value = "idClient") Long idClient,
        @RequestParam(value = "nameOfTopic") String nameOfTopic) {
		try {
            dao.newTopic(idPrincipal, nameOfTopic);
            loadOneClient(idPrincipal);
            
            model.setViewName(Constants.CLIENT_SHOW);
		} catch (CannotCreateTransactionException e1) {
			handlerEvents(Constants.CONNECTION_REFUSED);
		}
        loadAllTopics();
		return model;
	}

	@RequestMapping(value = "/client/showClient", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView showClientForClient(@RequestParam(value = "idTopic", required = false) Long idTopic) {
		model.setViewName(Constants.CLIENT_SHOW);
        if (idTopic != null) {
            loadCurrentTopic(idTopic);
		}
        loadAllTopics();
		return model;
	}

    @RequestMapping(value = "/register/edit", method = {RequestMethod.POST})
	public ModelAndView editClientForAdmin(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "credential.id", required = false) Long idCredential,
			@RequestParam(value = "data.id", required = false) Long idData,
			@RequestParam(value = "credential.login") String login,
			@RequestParam(value = "credential.password") String password,
			@RequestParam(value = "data.firstName") String firsName,
			@RequestParam(value = "data.secondName") String secondName,
			@RequestParam(value = "data.lastName") String lastName) {
        String role = "CLIENT";
        String empty = "";
        String resultCheck = Password.check(password);
        
        client.setId(id);
        data.setAllData(idData, firsName, secondName, lastName);
        credential.setAllData(idCredential, login, password, role);
        client.setData(data);
        client.setCredential(credential);
        if (!empty.equals(resultCheck)) {
            handlerEvents("password isn't correct: " + resultCheck);
            model.setViewName("clientForm");
            model.addObject(Constants.CLIENT, client);
            return model;
        }
		try {
			if (dao.findLoginInBd(login)) {
				if (id != null) {
					if (!login.equals(dao.nameLoginClientOwner(id))) {
						handlerEvents("login isn't unique");
						model.setViewName(Constants.FORM_CLIENT);
						model.addObject(Constants.CLIENT, client);
						return model;
					}

				} else {
					handlerEvents("login isn't unique");
					model.setViewName("clientForm");
					model.addObject(Constants.CLIENT, client);
					return model;
				}
			}
		} catch (NoResultException e) {
            handlerEvents(e.getMessage());
		} catch (CannotCreateTransactionException e) {
			handlerEvents(Constants.CANNOT_CREATE_TRANSACTION);
			model.addObject(Constants.CLIENT, client);
			return model;
		} catch (PersistenceException e) {
			handlerEvents(Constants.CONNECTION_REFUSED);
			model.addObject(Constants.CLIENT, client);
			return model;
		}
		if (client.getId() == null) {
			dao.add(client);
		} else {
			dao.merge(client);
		}
        model.setViewName(Constants.PAGE_LOGIN);
		return model;
	}

    @RequestMapping(value = "/register/newClient", method = RequestMethod.GET)
	public ModelAndView newClient(HttpServletRequest request) {
        model.setViewName(Constants.FORM_CLIENT);
		model.addObject(Constants.CLIENT, new Client());
		return model;
	}

    @RequestMapping(value = "/client/deleteTopic", method = RequestMethod.POST)
    public ModelAndView deleteTopic(@RequestParam(value = "idTopic") Long idTopic) {
        if ("ROLE_ADMIN".equals(rolePrincipal)) {
		try {
            Boolean flag = dao.removeTopic(idTopic);
            
            if (!flag) {
                handlerEvents(Constants.NOT_FOUND_TOPIC + idTopic);
            }
		} catch (CannotCreateTransactionException e1) {
			handlerEvents(Constants.CONNECTION_REFUSED);
		}
        } else {
            handlerEvents("Access denied");
        }
        loadOneClient(idPrincipal);
        loadAllTopics();
        model.setViewName(Constants.CLIENT_SHOW);
		return model;
	}
    
    @RequestMapping(value = "/client/topicAddMessage", method = RequestMethod.POST)
    public ModelAndView addMessageToTopic(@RequestParam(value = "idTopic") Long idTopic,
        @RequestParam(value = "story") String story) {
			try {
            dao.addMessageToTopic(idTopic, story, idPrincipal);
			} catch (CannotCreateTransactionException e1) {
				handlerEvents(Constants.CONNECTION_REFUSED);
			} catch (Exception e) {
            handlerEvents("Error of sending");
			}
        
        model.setViewName(Constants.MESSAGE_SHOW);
        loadMessages(idTopic);
		return model;
	}

    @RequestMapping(value = "/client/removeMessage", method = RequestMethod.POST)
    public ModelAndView removeMessage(@RequestParam(value = "idTopic") Long idTopic,
        @RequestParam(value = "idMessage") Long idMessage) {
        try {
            dao.removeMessage(idMessage, idPrincipal, rolePrincipal);
        } catch (ChatException e) {
            handlerEvents(e.getMessage());
        }
        
        loadMessages(idTopic);
        model.setViewName(Constants.MESSAGE_SHOW);
        
        return model;
    }

	private void updateDatePrincipal() {
		String userName = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			Authentication principal = SecurityContextHolder.getContext().getAuthentication();
			userName = principal.getName();
			try {
                this.idPrincipal = dao.findCredentialByName(userName).getIdClient();
			} catch (CannotCreateTransactionException e) {
				handlerEvents("Connection rejected, try again later ...");
			}
			this.rolePrincipal = ((principal.getAuthorities().toArray())[0]).toString();
		}
	}

	private void handlerEvents(String dataForPass) {
		infoProblem.setCause(dataForPass);
		model.addObject("error", infoProblem);
	}
    private void loadAllTopics() {

        List<Topic> list = null;
        try {
            list = dao.getAllTopics();
        } catch (IllegalStateException | PersistenceException e) {
            handlerEvents(Constants.CONNECTION_REFUSED);
        }
        model.addObject("topics", list);
    }
	private Client loadOneClient(Long id) {

		if (id == null) {
			handlerEvents("Error id = null");
			return client;
		}

		try {
			client = dao.getClientById(id);
            
		} catch (PersistenceException e) {
			handlerEvents(Constants.CONNECTION_REFUSED);
		}
		model.addObject("client", client);

		return client;
	}
    
    private Topic loadMessages(Long idTopic) {
        
        if (idTopic == null) {
            handlerEvents("Error idTopic = null");
            handlerEvents(Constants.NOT_FOUND_TOPIC + idTopic);
            return topic;
        }
        
        try {
            topic = dao.getTopicById(idTopic);
        } catch (PersistenceException e) {
            handlerEvents(Constants.CONNECTION_REFUSED);
        }
        
        model.addObject("topic", topic);
        
        return topic;
    }
    
    private Topic loadCurrentTopic(Long id) {
		try {
            topic = dao.getTopicById(id);
			if (topic == null) {
                handlerEvents(Constants.NOT_FOUND_TOPIC + id);
			}
            model.addObject("currentTopic", topic);
		} catch (PersistenceException e) {
			handlerEvents(Constants.CONNECTION_REFUSED);
		}

		return topic;
	}

	public InfoProblem getInfoProblem() {
		return infoProblem;
	}
}