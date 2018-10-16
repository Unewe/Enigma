$(document).ready(function() {
	
	$(".auth_button").click(function() {
		$(".headerwindow").slideToggle();
	});

    $(".userButton").click(function() {

        ajaxSubmitForm($(this).text());

    });

    $(".deleteUserButton").click(function() {

        ajaxDeleteForm($(this).text());

    });

    $(".changeLeaderButton").click(function() {

        ajaxChangeLeaderForm($(this).text());

    });


});


function ajaxSubmitForm(teammate) {

    $.ajax({
        type: "POST",
        url: "/addTeammate?teammate=" + teammate,
        data: {"teammate": teammate},

        // prevent jQuery from automatically transforming the data into a query string
        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function(data, textStatus, jqXHR) {

            alert(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {

            alert(jqXHR.responseText);

        }
    });

}

function ajaxDeleteForm(teammate) {

    $.ajax({
        type: "POST",
        url: "/deleteTeammate?teammate=" + teammate,
        data: {"teammate": teammate},

        // prevent jQuery from automatically transforming the data into a query string
        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function(data, textStatus, jqXHR) {
            window.location.reload();
        },
        error: function(jqXHR, textStatus, errorThrown) {
            window.location.reload();
        }
    });

}

function ajaxChangeLeaderForm(teammate) {

    $.ajax({
        type: "POST",
        url: "/changeLeader?teammate=" + teammate,
        data: {"teammate": teammate},

        // prevent jQuery from automatically transforming the data into a query string
        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function(data, textStatus, jqXHR) {
            window.location.replace("/userInfo");
        },
        error: function(jqXHR, textStatus, errorThrown) {
            window.location.replace("/userInfo");
        }
    });
}