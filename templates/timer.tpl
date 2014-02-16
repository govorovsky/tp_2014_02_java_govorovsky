<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Timer</title>
<script src="/refresher.js"></script>
</head>
<body onload='setInterval(function(){refresh()}, ${refreshPeriod} ); setClientTime();'>
<p>Client time: <span id='ClientTime'></span></p>
<p>Server time: ${time}</p>
<p>Your id on the server: ${userId}</p>
<div><img src="/jetty-logo-80x22.png"/>
</div>
</body>
</html>