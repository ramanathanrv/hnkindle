<%@ page import="hnkindle.Subscription" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Subscription List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Subscription</g:link></span>
        </div>
        <div class="body">
            <h1>Subscription List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="email" title="Email" />
                        
                   	        <g:sortableColumn property="subscribed" title="Subscribed" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${subscriptionInstanceList}" status="i" var="subscriptionInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${subscriptionInstance.id}">${fieldValue(bean:subscriptionInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:subscriptionInstance, field:'email')}</td>
                        
                            <td>${fieldValue(bean:subscriptionInstance, field:'subscribed')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${subscriptionInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
