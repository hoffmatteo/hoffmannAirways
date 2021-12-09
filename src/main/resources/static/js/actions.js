var routeTime = 1.5;
function sendAlrt() {
    console.log("test");
    //$("#ArrivalTimePicker").val($("#DepartureTimePicker").val().date);
    var date = new Date($("#DepartureTimePicker").val());
    date.addHours(routeTime);
    console.log(date);

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
    $("#ArrivalTimePicker").val(today);

}

function setTime() {
    routeTime = 2.5;
}

Date.prototype.addHours = function(h) {
    this.setTime(this.getTime() + (h*60*60*1000));
    return this;
}