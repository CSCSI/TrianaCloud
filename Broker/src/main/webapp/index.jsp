<%--
  ~ Copyright (c) 2012, SHIWA
  ~
  ~     This file is part of TrianaCloud.
  ~
  ~     TrianaCloud is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     (at your option) any later version.
  ~
  ~     TrianaCloud is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with TrianaCloud.  If not, see <http://www.gnu.org/licenses/>.
  --%>

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