<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Inscription</title>

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
	
	<form action="<c:url value="/?action=insererClient" />" method="post">
		<fieldset>
			<legend>Inscription d'un client</legend>
			<table>
				<tr>
					<td>Nom :</td>
					<td><input type="text" name="nom" value="" /></td>
				</tr>
				<tr>
					<td>Prénom :</td>
					<td><input type="text" name="prenom" value="" /></td>
				</tr>
				<tr>
					<td>Login :</td>
					<td><input type="text" name="login" value="" /></td>
				</tr>
				<tr>
					<td>Password :</td>
					<td><input type="password" name="password" value="" /></td>
				</tr>
				<tr>
					<td>Mail :</td>
					<td><input type="text" name="mail" value="" /></td>
				</tr>
				<tr>
					<td>Adresse :</td>
					<td><input type="text" name="adresse" value="" size="60" /></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="submit" value="Envoyer"/></td>
				</tr>
			</table>
		</fieldset>
			
	</form>
</body>
</html>