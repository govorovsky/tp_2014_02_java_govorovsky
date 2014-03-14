<!DOCTYPE html>
<html>
<head>
    <title>Auth Page</title>
</head>
<body>
<#if errorMsg??>
    <p id="error" style="color:red;">${errorMsg}</p>
</#if>

<form method="post">
    <label for=login title="Username: ">
        <input id="login" type="text" name="username"/>
    </label>
    <br />
    <label for="password" title="Password: ">
        <input id="password" type="text" name="password"/>
    </label>
    <input type="submit" value="Log in!" />
</form>
<div>
<img src="/jetty-logo-80x22.png"/>
</div>
</body>
</html>