var previousSelection = "flightRow";

function onConnection() {
    $("." + previousSelection).each(function () {
        $(this).attr('hidden', true);
    });
    var flightNumber = $("#selectFlightroute").val();
    console.log(flightNumber);

    $("." + flightNumber).each(function () {
        console.log($(this).val());
        $(this).attr('hidden', false);
    });

    previousSelection = flightNumber;
}
