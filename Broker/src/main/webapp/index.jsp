<%--
  ~ Licensed to the Apache Software Foundation (ASF) under one
  ~        or more contributor license agreements.  See the NOTICE file
  ~        distributed with this work for additional information
  ~        regarding copyright ownership.  The ASF licenses this file
  ~        to you under the Apache License, Version 2.0 (the
  ~        "License"); you may not use this file except in compliance
  ~        with the License.  You may obtain a copy of the License at
  ~
  ~          http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~        Unless required by applicable law or agreed to in writing,
  ~        software distributed under the License is distributed on an
  ~        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~        KIND, either express or implied.  See the License for the
  ~        specific language governing permissions and limitations
  ~        under the License.
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