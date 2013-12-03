<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Achat</title>
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
		.centrer {
			text-align:center;
		}
		.menu {
	    	font-size: 0.8em;
			text-align:right;
			margin:20px;
		}
	</style>
	
	<script type="text/javascript">
	<!--
		function testQuantite(quantite){
			var test = isNaN(quantite.value);	
			if ( (test == true) || (quantite.value < 0 ) || (quantite.value.indexOf('.') >= 0 ) ){
				alert("La quantite doit etre un nombre entier positif!");
				quantite.value = 0;
				quantite.setfocus = true;
			}
		} 
						
		 function calculTotal(quantite, sousTotal, prixUnitaire, total){
			// test si quantite > 0
			testQuantite(quantite);

			// reinitialiser la valeur de TOT et sousTOT 
			total.value = (total.value * 1) - (sousTotal.value * 1);
			
			// Calcul des nouvelles valeurs
			sousTotal.value = prixUnitaire * quantite.value; 		//qté cmd* PU
			
			total.value = (total.value * 1) + (sousTotal.value * 1);
		}
		
	//-->	
	</script>
</head>
<body>
	<div class="menu">
		<c:out value="${client.prenom}" />&nbsp;<c:out value="${client.nom}" />&nbsp;|&nbsp;
		<a href="<c:url value="/?action=achat" />">Achat</a>&nbsp;|&nbsp;
		<a href="<c:url value="/?action=historiqueAchat" />">Historique achat</a>&nbsp;|&nbsp;
		<a href="<c:url value="/?action=inscription" />">Inscription</a>&nbsp;|&nbsp;
		<a href="<c:url value="/?action=deconnexion" />">D&eacute;connexion</a>&nbsp;|&nbsp;
	</div>
	
	<h1>Achat</h1>
	<form action="<c:url value="/?action=commander" />" method="post">
		<table id="tab">
		<tr>
			<th>Appellation</th>
			<th>Cuvee</th>
			<th>Millesime</th>
			<th>Prix</th>
			<th>Producteur</th>
			<th>Quantité | Sous total</th>
		</tr>
		<c:forEach var="produit" items="${liste}">
	    <tr>
	    	<td><c:out value="${produit.appellation}" /></td>
	    	<td><c:out value="${produit.cuvee}" /></td>
	    	<td><c:out value="${produit.millesime}" /></td>
	    	<td><c:out value="${produit.prix}" /></td>
	    	<td><c:out value="${produit.producteur}" /></td>
	    	<td align="center">
	    		<input type="hidden" name="produitId" value="<c:out value="${produit.id}" />" />
	    		<input type="hidden" name="prix" value="<c:out value="${produit.prix}" />" />
	    		<input type="text" name="quantite" value="0" size="3" onchange="calculTotal(this, this.nextSibling, <c:out value="${produit.prix}" />, document.getElementById('total'))" /><input type="text" name="sousTotal" value="0" readonly="readonly" size="6" />
	    	</td>
	    </tr>	
		</c:forEach>
		<tr>
			<td colspan="6" align="right">Total <input type="text" name="total" id="total" value="0" readonly="readonly" size="6" /> €</td>
		</tr>
		<tr>
			<td colspan="6" align="center"><input type="submit" value="Commander"/></td>
		</tr>
	</table>
	</form>
</body>
</html>