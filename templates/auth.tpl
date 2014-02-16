<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<body>
<#if errorMsg??>
    <p style="color:red;">${errorMsg}</p>
</#if>

<form method="post">
    <label for=login title="Username: ">
        <input id="login" type="text" name="login"/>
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