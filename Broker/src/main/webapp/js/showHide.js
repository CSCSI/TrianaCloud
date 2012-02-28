// Andy Langton's show/hide/mini-accordion - updated 11/01/2012
// modified 15/10/2011 by Johannes Georgi
// Latest version @ http://andylangton.co.uk/jquery-show-hide

// this tells jquery to run the function below once the DOM is ready
$(document).ready(function () {

// choose text for the show/hide link - can contain HTML (e.g. an image)
    var showText = 'show';
    var hideText = 'hide';

// initialise the visibility check
//var is_visible = false;

// append show/hide links to the element directly preceding the element with a class of "toggle"
    $('.toggle').prev().append(' ...' + showText + '');
    $('.toggle').prev().data('is_visible', true);

// hide all of the elements with a class of 'toggle'
    $('.toggle').hide();

// capture clicks on the toggle links
    $('a.togglelink').click(function () {

// switch visibility
        $(this).data('is_visible', !$(this).data('is_visible'));

// change the link depending on whether the element is shown or hidden
        $(this).html((!$(this).data('is_visible')) ? showText : hideText);

// toggle the display - uncomment the next line for a basic "accordion" style
//$('.toggle').hide();$('a.togglelink').html(showText);
        $(this).parent().next('.toggle').toggle('slow');

// return false so any link destination is not followed
        return false;

    });
})