<jsp:useBean id="tasksearch" scope="session" class="fr.paris.lutece.plugins.taskstack.web.TaskSearchJspBean" />
<% String strContent = tasksearch.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
