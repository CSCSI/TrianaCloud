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

<html>
<head>
    <title>Result Viewer</title>
    <!--<link rel="stylesheet" type="text/css" href="/Broker/style.css">    -->
    <script src="js/jquery-1.7.1.js" type="text/javascript"></script>
    <!--<script src="/Broker/js/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script>-->
    <!--<script src="/Broker/js/showHide.js" type="text/javascript"></script>-->
    <script>
        // Andy Langton's show/hide/mini-accordion - updated 23/11/2009
        // Latest version @ http://andylangton.co.uk/jquery-show-hide

        // this tells jquery to run the function below once the DOM is ready
        $(document).ready(function () {

// choose text for the show/hide link - can contain HTML (e.g. an image)
            var showText = 'Show';
            var hideText = 'Hide';

// initialise the visibility check
            var is_visible = false;

// append show/hide links to the element directly preceding the element with a class of "toggle"
            $('.toggle').prev().append(' (<a href="#" class="toggleLink">' + showText + '</a>)');

// hide all of the elements with a class of 'toggle'
            $('.toggle').hide();

// capture clicks on the toggle links
            $('a.toggleLink').click(function () {

// switch visibility
                is_visible = !is_visible;

// change the link depending on whether the element is shown or hidden
                $(this).html((!is_visible) ? showText : hideText);

// toggle the display - uncomment the next line for a basic "accordion" style
//$('.toggle').hide();$('a.toggleLink').html(showText);
                $(this).parent().next('.toggle').toggle('slow');

// return false so any link destination is not followed
                return false;

            });
        });
    </script>
</head>
<body>
<div id="results">
    <jsp:include page="results?action=results"/>
</div>
</body>
</html>