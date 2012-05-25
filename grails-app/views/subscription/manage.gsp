<html>
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
	<title>Manage your subscription to HNKindle</title>
	</head>
	<body>
		<g:if test="${flash.message}"><p id="message">${flash.message}</p></g:if>
		<p>Enter your kindle address (use free.kindle.com). Alternatively, you can also use your regular email.</p>
    	<g:form method="post">
    		Email <input type="text" name="email"> 
    		<g:actionSubmit value="Subscribe" action="subscribe" />
    		<g:actionSubmit value="Unsubscribe" action="unsubscribe" />
		</g:form>    		
		<p> 
		<strong>Important: </strong>Please read these points before you subsribe:
			<ul>
				<li>Documents will not be delivered to kindle unless you whitelist the sender email (you will be notified by Amazon)</li>
				<li>You will approximately receive about 10 articles a day. This might vary from time to time.</li>
				<li>You are strongly recommended to use your regular email and see if you like the service before signing up your kindle address. Also it is prudent to setup a filter in you mail box.</li>
			</ul>
		</p>
		<p>
			<h3>Privacy Policy:</h3>
			<ul>
				<li>Your email address will not be shared with anyone. No spam or promotional mails will be sent from this application.</li>
				<li>You can unsubscribe anytime</li>
			</ul>
		</p>
    </body>
</html>