<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Historique achats</title>
	<style type="text/css">
		h1 { 
	  		font-family: serif;
			color: #996600;
			padding: 0.3em;
			text-align: center;
			letter-spacing: 0.3em;
		}
		#tab
		{
			margin: auto;
		    border: #DDEEFF 2px solid;
		    border-collapse: separate;
		    border-spacing: 2px;
		    empty-cells: hide;
		}
		
		#tab th, fieldset legend
		{
		    color: #996600;
		    background-color: #FFCC66;
		    border: #FFCC66 1px solid;
		    font-variant: small-caps;
		    font-size: 0.8em;
		    letter-spacing: 1px;
		}
		
		#tab td
		{
		    border: #DDEEFF 1px solid;
		    padding-left: 10px;
		    font-size: 0.8em;
		    letter-spacing: 1px;
		}
		
		fieldset
		{
	    	border:2px solid #DDEEFF;
	    	-moz-border-radius:8px;
	    	-webkit-border-radius:8px;	
	    	border-radius:8px;	
	    	width:35%;
	    	margin:20px auto;
	    	font-size: 0.8em;
		    letter-spacing: 1px;
		}
		.cmd {
			text-align:center;
			font-size: 0.8em;
			margin:5px;
		}
		.menu {
	    	font-size: 0.8em;
			text-align:right;
			margin:20px;
		}
	</style>
</head>
<body>

	<div class="menu">
		<c:out value="${client.prenom}" />&nbsp;<c:out value="${client.nom}" />&nbsp;|&nbsp;
		<a href="<c:url value="/?action=achat" />">Achat</a>&nbsp;|&nbsp;
		<a href="<c:url value="/?action=historiqueAchat" />">Historique achat</a>&nbsp;|&nbsp;
		<a href="<c:url value="/?action=inscription" />">Inscription</a>&nbsp;|&nbsp;
		<a href="<c:url value="/?action=deconnexion" />">D&eacute;connexion</a>&nbsp;|&nbsp;
	</div>
	
	<h1>Historique de vos achats</h1>
	<c:forEach var="commande" items="${listeCommandeClientLogOn}">
		<div class="cmd">Date : <c:out value="${commande.date}" /> - Etat : <c:out value="${commande.etat}" /></div>
	
		<table id="tab">
			<tr>
				<th>Appellation</th>
				<th>Cuvee</th>
				<th>Millesime</th>
				<th>Producteur</th>
				<th>Prix</th>
				<th>Quantité</th>
			</tr>
			<c:forEach var="detailCommande" items="${commande.detailCommande}">
			    <tr>
			    	<td><c:out value="${detailCommande.produit.appellation}" /></td>
			    	<td><c:out value="${detailCommande.produit.cuvee}" /></td>
			    	<td><c:out value="${detailCommande.produit.millesime}" /></td>
			    	<td><c:out value="${detailCommande.produit.producteur}" /></td>
			    	<td><c:out value="${detailCommande.prixJour}" /></td>
			    	<td align="center"><c:out value="${detailCommande.quantite}" /></td>
			    </tr>	
			</c:forEach>
			<tr>
				<td colspan="6" align="right">Total : <c:out value="${commande.total}" /> €</td>
			</tr>
		</table>
		<br /><br />
	</c:forEach>
</body>
</html>