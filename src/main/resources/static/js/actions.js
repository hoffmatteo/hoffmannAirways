var routeTime = 0;

$(document).ready( function() {
    $("#DepartureTimePicker").val(new Date().toDateInputValue());
    updateArrivalTime();

});
function updateArrivalTime() {
    var date = new Date($("#DepartureTimePicker").val());
    date.addHours(routeTime);
    $("#ArrivalTimePicker").val(date.toDateInputValue());
}

function setFlightDuration() {
    var connection = $("#selectFlightroute").text();
    var strings = connection.split(',');
    var flightTime = strings[1];
    if(flightTime != null) {
        var result = flightTime.substring(1, flightTime.length - 1);

        routeTime = parseFloat(result[0]);
    }
    else {
        routeTime = 0;
    }
    updateArrivalTime();

}
//TODO quelle angeben
//https://stackoverflow.com/a/1050782
Date.prototype.addHours = function(h) {
    this.setTime(this.getTime() + (h*60*60*1000));
    return this;
}
//TODO quelle
//https://stackoverflow.com/a/13052187
Date.prototype.toDateInputValue = (function() {
    var date = new Date(this);
    var day = date.getDate(),
        month = date.getMonth() + 1,
        year = date.getFullYear(),
        hour = date.getHours(),
        min  = date.getMinutes();

    month = (month < 10 ? "0" : "") + month;
    day = (day < 10 ? "0" : "") + day;
    hour = (hour < 10 ? "0" : "") + hour;
    min = (min < 10 ? "0" : "") + min;

    var today = year + "-" + month + "-" + day + "T" + hour + ":" + min;
    return today;

});