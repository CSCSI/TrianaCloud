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