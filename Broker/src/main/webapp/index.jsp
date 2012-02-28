<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title></title></head>
<body>
<h2>Submit data to broker</h2>

<form action="broker" method="post" enctype="multipart/form-data">
    <!--XML Scan data:<br/>
    <textarea id="task" name="task" rows="20" cols="80"></textarea>  -->
    <label for="task">Input file:</label>
    <input type="file" id="task" name="task" size="40">  </br>
    Routing Key:</br>
    <textarea id="routingkey" name="routingkey" rows="1" cols="40"></textarea> </br>
    Number of tasks:</br>
    <textarea id="numtasks" name="numtasks" rows="1" cols="5"></textarea>  </br>
    <br/>
    Name of tasks:</br>
    <textarea id="name" name="name" rows="1" cols="5"></textarea>  </br>
    <input type="submit" value="Go"/>
</form>
</body>
</html>