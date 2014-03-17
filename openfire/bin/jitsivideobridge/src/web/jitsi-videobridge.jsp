<%--
 - Jitsi VideoBridge, OpenSource video conferencing.
 -
 - Distributable under LGPL license.
 - See terms of license at gnu.org.
--%>
<%@ page import="org.jitsi.videobridge.openfire.*" %>
<%@ page import="org.jivesoftware.openfire.*" %>
<%@ page import="org.jivesoftware.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%

    boolean update = request.getParameter("update") != null;
    String errorMessage = null;

    // Get handle on the plugin
    PluginImpl plugin = (PluginImpl) XMPPServer.getInstance()
        .getPluginManager().getPlugin("jitsivideobridge");

    if (update)
    {
        String minPort = request.getParameter("minport");
        if (minPort != null) {
            minPort = minPort.trim();
            try
            {
                int port = Integer.valueOf(minPort);

                if(port >= 1 && port <= 65535)
                    JiveGlobals.setProperty(
                        PluginImpl.MIN_PORT_NUMBER_PROPERTY_NAME, minPort);
                else
                    throw new NumberFormatException("out of range port");

            }
            catch (Exception e)
            {
                errorMessage = LocaleUtils.getLocalizedString(
                    "config.page.configuration.error.minport",
                    "jitsivideobridge");
            }
        }
        String maxPort = request.getParameter("maxport");
        if (maxPort != null) {
            maxPort = maxPort.trim();
            try
            {
                int port = Integer.valueOf(maxPort);

                if(port >= 1 && port <= 65535)
                    JiveGlobals.setProperty(
                        PluginImpl.MAX_PORT_NUMBER_PROPERTY_NAME, maxPort);
                else
                    throw new NumberFormatException("out of range port");
            }
            catch (Exception e)
            {
                errorMessage = LocaleUtils.getLocalizedString(
                    "config.page.configuration.error.maxport",
                    "jitsivideobridge");
            }
        }
        
	String username = request.getParameter("username"); 
        JiveGlobals.setProperty(PluginImpl.USERNAME_PROPERTY_NAME, username);	
        
	String password = request.getParameter("password"); 	
        JiveGlobals.setProperty(PluginImpl.PASSWORD_PROPERTY_NAME, password);	
        
	String enabled = request.getParameter("enabled"); 	
        JiveGlobals.setProperty(PluginImpl.RECORD_PROPERTY_NAME, enabled);        
    }

%>
<html>
<head>
   <title><fmt:message key="config.page.title" /></title>

   <meta name="pageID" content="jitsi-videobridge-settings"/>
</head>
<body>
<% if (errorMessage != null) { %>
<div class="error">
    <%= errorMessage%>
</div>
<br/>
<% } %>

<div class="jive-table">
    <form action="jitsi-videobridge.jsp" method="post">
        <table class="jive-table" cellpadding="0" cellspacing="0" border="0" width="50%">
            <thead>
            <tr>
                <th colspan="2"><fmt:message key="config.page.configuration.title"/></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><label class="jive-label"><fmt:message key="config.page.configuration.min.port"/>:</label><br>
                </td>
                <td align="left">
                    <input name="minport" type="text" maxlength="5" size="5"
                           value="<%=plugin.getMinPort()%>"/>
                </td>
            </tr>
            <tr>
                <td><label class="jive-label"><fmt:message key="config.page.configuration.max.port"/>:</label><br>
                </td>
                <td align="left">
                    <input name="maxport" type="text" maxlength="5" size="5"
                           value="<%=plugin.getMaxPort()%>"/>
                </td>
            </tr>
            <tr>
                <td><label class="jive-label"><fmt:message key="config.page.configuration.username"/>:</label><br>
                </td>
                <td align="left">
                    <input name="username" type="text" maxlength="16" size="16"
                           value="<%=JiveGlobals.getProperty(PluginImpl.USERNAME_PROPERTY_NAME, "jitsi")%>"/>
                </td>
            </tr>     
            <tr>
                <td><label class="jive-label"><fmt:message key="config.page.configuration.password"/>:</label><br>
                </td>
                <td align="left">
                    <input name="password" type="password" maxlength="16" size="16"
                           value="<%=JiveGlobals.getProperty(PluginImpl.PASSWORD_PROPERTY_NAME, "jitsi")%>"/>
                </td>
            </tr>  
	    <tr>
		    <td  nowrap colspan="2">
			<input type="radio" value="false" name="enabled" <%= ("false".equals(JiveGlobals.getProperty(PluginImpl.RECORD_PROPERTY_NAME, "false")) ? "checked" : "") %>>
			<b><fmt:message key="config.page.configuration.record.disabled" /></b> - <fmt:message key="config.page.configuration.record.disabled_description" />
		    </td>
	    </tr>   
	    <tr>
		    <td  nowrap colspan="2">
			<input type="radio" value="true" name="enabled" <%= ("true".equals(JiveGlobals.getProperty(PluginImpl.RECORD_PROPERTY_NAME, "false")) ? "checked" : "") %>>
			<b><fmt:message key="config.page.configuration.record.enabled" /></b> - <fmt:message key="config.page.configuration.record.enabled_description" />
		    </td>
	    </tr> 	    
            <tr>
                <th colspan="2"><input type="submit" name="update"
                                       value="<fmt:message key="config.page.configuration.submit" />"></th>
            </tr>
            </tbody>
        </table>
    </form>
</div>

</body>
</html>