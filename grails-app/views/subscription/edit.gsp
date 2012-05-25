<%@ page import="hnkindle.Subscription" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Subscription</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Subscription List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Subscription</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Subscription</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${subscriptionInstance}">
            <div class="errors">
                <g:renderErrors bean="${subscriptionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${subscriptionInstance?.id}" />
                <input type="hidden" name="version" value="${subscriptionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="email">Email:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:subscriptionInstance,field:'email','errors')}">
                                    <g:textField name="email" value="${subscriptionInstance?.email}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="subscribed">Subscribed:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:subscriptionInstance,field:'subscribed','errors')}">
                                    <g:checkBox name="subscribed" value="${subscriptionInstance?.subscribed}" />
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
