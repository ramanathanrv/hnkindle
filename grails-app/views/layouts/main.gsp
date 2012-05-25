
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title><g:layoutTitle default="HNKindle" /></title>
  <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8" />
  <meta name="keywords" content="hacker news, kindle" />
  <meta name="description" content="Read hacker news articles in your kindle." />
  <link rel="stylesheet" type="text/css" href="/css/html.css" media="screen, projection, tv " />
  <link rel="stylesheet" type="text/css" href="/css/layout.css" media="screen, projection, tv" />
  <link rel="stylesheet" type="text/css" href="/css/print.css" media="print" />
  <!-- Conditional comment to apply opacity fix for IE #content background.
       Invalid CSS, but can be removed without harming design -->
  <!--[if gt IE 5]>
  <link rel="stylesheet" type="text/css" href="css/ie.css" media="screen, projection, tv " />
  <![endif]-->
  <g:layoutHead />
</head>

<body>
<!-- #wrapper: centers the content and sets the width -->
<div id="wrapper">
  <!-- #content: applies the white, dropshadow background -->
  <div id="content">
    <!-- #header: holds site title and subtitle -->
    <div id="header">
      <h1><span class="big darkBrown">HN</span>Kindle</h1>
      <h2><span class="highlight">Hacker News articles delivered to Kindle</a></span></h2>
    </div>
    <!-- #menu: topbar menu of the site.  Use the helper classes .two, .three, .four and .five to set
                the widths for 2, 3, 4 and 5 item menus. -->
    <ul id="menu" class="three">
      <li><a title="Manage" href="/subscription/manage">Subscription</a></li>
      <li><a title="Heuristics" href="/subscription/heuristics">Heuristics</a></li>
      <li><a title="About" href="/subscription/about">About</a></li>
    </ul>
    <!-- #page: holds all page content, as well as footer -->
    <div id="page">
                <g:layoutBody />
      <p class="footer">    
       <!-- DO NOT REMOVE -->
        Design by <a href="http://fullahead.org" title="Visit FullAhead">FullAhead</a> + <a href="http://sean-pollock.com">Sean Pollock</a> |  
        <!--^ DO NOT REMOVE ^-->
        Copyright&copy; 2010 YourWebSite.com.      </p>
    </div>
  </div>
</div>
</body>
</html>
