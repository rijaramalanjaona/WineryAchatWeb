package com.ingesup.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ingesup.exception.DaoException;
import com.ingesup.winery.ejb.client.RemoteClientManager;
import com.ingesup.winery.ejb.commande.RemoteCommandeManager;
import com.ingesup.winery.ejb.detail.RemoteDetailCommandeManager;
import com.ingesup.winery.ejb.produit.RemoteProduitManager;
import com.ingesup.winery.entity.Client;
import com.ingesup.winery.entity.Commande;
import com.ingesup.winery.entity.DetailCommande;
import com.ingesup.winery.entity.Produit;

public class ApplicationAchat extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Context ctx;
    private RemoteProduitManager produitManager;
    private RemoteClientManager clientManager;
    private RemoteCommandeManager commandeManager;
    private RemoteDetailCommandeManager detailCommandeManager;

    private String urlInscription;
    private String urlIdentification;
    private String urlCommande;
    private String urlHistoriqueAchat;

    private String nom, prenom, login, password, mail, adresse;
    private String produitId[], prix[], quantite[];
    private String total;

    public void init() {
	Properties props = new Properties();
	props.setProperty("java.naming.provider.url", "localhost:1099");
	props.setProperty("java.naming.factory.initial",
		"org.jnp.interfaces.NamingContextFactory");
	props.setProperty("java.naming.factory.url.pkgs",
		"org.jboss.naming:org.jnp.interfaces");

	try {
	    ctx = new InitialContext(props);
	    produitManager = (RemoteProduitManager) ctx
		    .lookup(RemoteProduitManager.JNDI_NAME);

	    clientManager = (RemoteClientManager) ctx
		    .lookup(RemoteClientManager.JNDI_NAME);

	    commandeManager = (RemoteCommandeManager) ctx
		    .lookup(RemoteCommandeManager.JNDI_NAME);

	    detailCommandeManager = (RemoteDetailCommandeManager) ctx
		    .lookup(RemoteDetailCommandeManager.JNDI_NAME);

	} catch (NamingException e) {
	    e.printStackTrace();
	}

	urlInscription = getServletConfig().getInitParameter("urlInscription");
	urlIdentification = getServletConfig().getInitParameter(
		"urlIdentification");
	urlCommande = getServletConfig().getInitParameter("urlCommande");
	urlHistoriqueAchat = getServletConfig().getInitParameter(
		"urlHistoriqueAchat");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse rep)
	    throws ServletException, IOException {
	req.setCharacterEncoding("UTF-8");
	String action = req.getParameter("action");
	if (action == null) {
	    action = "identification";
	}
	System.out.println("Action demandee : " + action);

	if (action.equals("identification")) {
	    getServletContext().getRequestDispatcher(urlIdentification)
		    .forward(req, rep);
	} else if (action.equals("identifier")) {
	    login = req.getParameter("login");
	    password = req.getParameter("password");
	    Client clientLogOn = clientManager.isIdentificationOk(login,
		    password);
	    if (clientLogOn != null) {
		req.setAttribute("client", clientLogOn);
		req.setAttribute("liste", produitManager.getAll());
		HttpSession session = req.getSession();
		session.setAttribute("clientLogOn", clientLogOn);
		getServletContext().getRequestDispatcher(urlCommande).forward(
			req, rep);
	    } else {
		throw new DaoException("Login ou password incorrect!", 3);
	    }

	} else if (action.equals("inscription")) {
	    getServletContext().getRequestDispatcher(urlInscription).forward(
		    req, rep);
	} else if (action.equals("insererClient")) {
	    nom = req.getParameter("nom");
	    prenom = req.getParameter("prenom");
	    login = req.getParameter("login");
	    password = req.getParameter("password");
	    mail = req.getParameter("mail");
	    adresse = req.getParameter("adresse");

	    ArrayList<String> erreurs = verifQualite();
	    if (erreurs.size() == 0) {

		Client client = new Client();
		client.setNom(nom);
		client.setPrenom(prenom);
		client.setLogin(login);
		client.setPassword(password);
		client.setMail(mail);
		client.setAdresse(adresse);
		client = clientManager.save(client);

		getServletContext().getRequestDispatcher(urlIdentification)
			.forward(req, rep);

	    } else {
		String msg = "";
		for (String erreur : erreurs) {
		    msg += erreur + "<br />";
		}
		throw new DaoException(msg, 2);
	    }
	} else if (action.equals("achat")) {
	    HttpSession session = req.getSession();
	    if (session.getAttribute("clientLogOn") != null) {
		Client clientLogOn = (Client) session
			.getAttribute("clientLogOn");
		req.setAttribute("client", clientLogOn);
		req.setAttribute("liste", produitManager.getAll());
		getServletContext().getRequestDispatcher(urlCommande).forward(
			req, rep);
	    } else {
		getServletContext().getRequestDispatcher(urlIdentification)
			.forward(req, rep);
	    }
	} else if (action.equals("commander")) {
	    HttpSession session = req.getSession();
	    if (session.getAttribute("clientLogOn") != null) {
		produitId = req.getParameterValues("produitId");
		prix = req.getParameterValues("prix");
		quantite = req.getParameterValues("quantite");
		total = req.getParameter("total");

		if (Float.parseFloat(total) > 0) {

		    Client clientLogOn = (Client) session
			    .getAttribute("clientLogOn");
		    Commande commande = new Commande();
		    commande.setClient(clientLogOn);
		    commande.setDate(new Date());
		    commande.setTotal(Float.parseFloat(total));
		    commande.setEtat("En cours");
		    commande = commandeManager.save(commande);

		    for (int i = 0; i < produitId.length; i++) {
			if (Integer.parseInt(quantite[i]) > 0) {
			    Produit produit = produitManager.getById(Long
				    .parseLong(produitId[i]));

			    DetailCommande detailCommande = new DetailCommande();
			    detailCommande.setCommande(commande);
			    detailCommande.setPrixJour(Float
				    .parseFloat(prix[i]));
			    detailCommande.setProduit(produit);
			    detailCommande.setQuantite(Long
				    .parseLong(quantite[i]));
			    detailCommandeManager.save(detailCommande);

			}
		    }

		    // gestion stock
		    try {

			QueueConnection connection;
			QueueSession queueSession;
			MessageProducer clientJMS;
			ObjectMessage msg;

			Queue file;

			// 1 creer une connection
			QueueConnectionFactory facto = (QueueConnectionFactory) ctx
				.lookup("ConnectionFactory");
			connection = facto.createQueueConnection();

			// 2 creer une session
			queueSession = connection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);
			msg = queueSession.createObjectMessage();
			msg.setObject(commande);

			// 3 creer client JMS
			file = (Queue) ctx.lookup("/queue/wineryCmd");
			clientJMS = queueSession.createProducer(file);

			// 4 envoi du msg
			clientJMS.send(msg);
		    } catch (JMSException e) {
			e.printStackTrace();
		    } catch (NamingException e) {
			e.printStackTrace();
		    }

		    // affichage historique commandes
		    List<Commande> listeCommande = commandeManager
			    .getByIdClient(clientLogOn.getId());
		    List<Commande> listeCommandeClientLogOn = new ArrayList<Commande>();
		    if (listeCommande != null && !listeCommande.isEmpty()) {

			for (Commande commandeClient : listeCommande) {
			    List<DetailCommande> listeDetailCommande = detailCommandeManager
				    .getByIdCommande(commandeClient.getId());
			    if (listeDetailCommande != null
				    && !listeDetailCommande.isEmpty()) {
				commandeClient
					.setDetailCommande(listeDetailCommande);
				listeCommandeClientLogOn.add(commandeClient);
			    }
			}
			clientLogOn.setCommande(listeCommandeClientLogOn);
		    }

		    req.setAttribute("client", clientLogOn);
		    req.setAttribute("listeCommandeClientLogOn",
			    listeCommandeClientLogOn);
		    getServletContext()
			    .getRequestDispatcher(urlHistoriqueAchat).forward(
				    req, rep);
		}
	    } else {
		getServletContext().getRequestDispatcher(urlIdentification)
			.forward(req, rep);
	    }
	} else if (action.equals("historiqueAchat")) {
	    HttpSession session = req.getSession();
	    if (session.getAttribute("clientLogOn") != null) {
		Client clientLogOn = (Client) session
			.getAttribute("clientLogOn");
		List<Commande> listeCommande = commandeManager
			.getByIdClient(clientLogOn.getId());
		List<Commande> listeCommandeClientLogOn = new ArrayList<Commande>();
		if (listeCommande != null && !listeCommande.isEmpty()) {

		    for (Commande commandeClient : listeCommande) {
			List<DetailCommande> listeDetailCommande = detailCommandeManager
				.getByIdCommande(commandeClient.getId());
			if (listeDetailCommande != null
				&& !listeDetailCommande.isEmpty()) {
			    commandeClient
				    .setDetailCommande(listeDetailCommande);
			    listeCommandeClientLogOn.add(commandeClient);
			}
		    }
		    clientLogOn.setCommande(listeCommandeClientLogOn);
		}

		req.setAttribute("client", clientLogOn);
		req.setAttribute("listeCommandeClientLogOn",
			listeCommandeClientLogOn);
		getServletContext().getRequestDispatcher(urlHistoriqueAchat)
			.forward(req, rep);
	    } else {
		getServletContext().getRequestDispatcher(urlIdentification)
			.forward(req, rep);
	    }
	} else if (action.equals("deconnexion")) {
	    HttpSession session = req.getSession();
	    session.setAttribute("clientLogOn", null);
	    getServletContext().getRequestDispatcher(urlIdentification)
		    .forward(req, rep);
	}
    }

    public void doPost(HttpServletRequest req, HttpServletResponse rep)
	    throws ServletException, IOException {
	doGet(req, rep);
    }

    private ArrayList<String> verifQualite() {
	ArrayList<String> res = new ArrayList<String>();
	if (nom.length() == 0) {
	    res.add("Le nom ne doit pas etre vide...");
	}
	if (prenom.length() == 0) {
	    res.add("Le prénom ne doit pas etre vide...");
	}
	if (login.length() == 0) {
	    res.add("Le login ne doit pas etre vide...");
	}
	if (password.length() == 0) {
	    res.add("Le password ne doit pas etre vide...");
	}
	if (mail.length() == 0) {
	    res.add("Le mail ne doit pas etre vide...");
	}
	if (!mail
		.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$")) {
	    res.add("Le mail n'est pas valide...");
	}
	if (adresse.length() == 0) {
	    res.add("L'adresse ne doit pas etre vide...");
	}
	return res;
    }
}
