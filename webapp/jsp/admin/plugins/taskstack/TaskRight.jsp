<jsp:useBean id="taskright" scope="session" class="fr.paris.lutece.plugins.taskstack.web.TaskRightJspBean" />
<% String strContent = taskright.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>