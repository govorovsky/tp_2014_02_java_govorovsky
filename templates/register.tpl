<!DOCTYPE html>
<html>
<head>
    <title>Registration Page</title>
</head>
<body>
<#if errorMsg??>
    <p id="error" style="color:red;">${errorMsg}</p>
</#if>

<form method="post">
    <label for=username title="Username: ">
        <input id="username" type="text" name="username"/>
    </label>
    <br/>
    <label for="password" title="Password: ">
        <input id="password" type="text" name="password"/>
    </label>
    <input type="submit" value="Register!"/>
</form>
<div>
    <img src="/jetty-logo-80x22.png"/>
</div>
</body>
</html>