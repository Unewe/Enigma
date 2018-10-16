$(document).ready(function() {
	
	$(".auth_button").click(function() {
		$(".headerwindow").slideToggle();
	});

	var time = $(".timer").text();
	time = parseInt(time);
    setInterval(function () {

    	if(time >= 0) {
            $(".toHide").show();
        }

        time-=1;

        if(time == -1) {
            window.location.reload();
        }
		if(time >= 0) {
        	var minuts = "";
        	if(Math.floor(time/60)) {
        		minuts = Math.floor(time/60) + " мин. "
			} else {
        		minuts = "";
			}
            $(".timer").text(minuts + "" + time%60 + " сек.");
		}


    }, 1000);


    $(window).scroll(function () {

        var top = $(document).scrollTop();
        if (top > 46) $(".floatingInput").addClass("fixed");
        else  $(".floatingInput").removeClass("fixed");
    });
});