var routeTime = 0;

$(document).ready(function () {
    //Source https://stackoverflow.com/questions/8102940/javascript-check-what-page-has-loaded
    window.location.pathname;
    var sPath = window.location.pathname;
    var sPage = sPath.substring(sPath.lastIndexOf('/') + 1);
    if (sPage === "createflight") {
        $("#DepartureTimePicker").val(new Date().toDateInputValue());
        setFlightDuration();
        updateArrivalTime();
    } else {
        setFlightDuration();
    }
});

function updateArrivalTime() {
    var date = new Date($("#DepartureTimePicker").val());
    filterPlanes(date);
    date.addHours(routeTime);
    var arrivalDate = date.toDateInputValue();
    $("#ArrivalTimePicker").val(arrivalDate);

}

function filterPlanes(departureDate) {
    $("#selectAirplane option").each(function () {
        var text = $(this).text();
        var strings = $(this).text().split(':');
        var dateString = strings[1];
        if (dateString != null) {
            console.log("true");
            dateString += ":" + strings[2];
            var planeDate = new Date(dateString);
            if (departureDate < planeDate.addHours(1)) {
                $(this).hide();
            } else {
                $(this).show();
            }
        } else {
            $(this).show();

        }

    });
    $("#selectAirplane").val("default");
}


function setFlightDuration() {
    var connection = $("#selectFlightroute").find(":selected").text();
    var strings = connection.split(':');
    var flightTime = strings[2];
    if (flightTime != null) {
        var result = flightTime.substring(1, flightTime.length - 1);

        routeTime = parseFloat(result);
        if (routeTime != null) {
            console.log("Set route time to " + routeTime);
        } else {
            routeTime = 0;
        }
    } else {
        routeTime = 0;
    }
    updateArrivalTime();
}

//Source: https://stackoverflow.com/a/1050782
Date.prototype.addHours = function (h) {
    this.setTime(this.getTime() + (h * 60 * 60 * 1000));
    return this;
}
//Source: https://stackoverflow.com/a/13052187
Date.prototype.toDateInputValue = (function () {
    var date = new Date(this);
    var day = date.getDate(),
        month = date.getMonth() + 1,
        year = date.getFullYear(),
        hour = date.getHours(),
        min = date.getMinutes();

    month = (month < 10 ? "0" : "") + month;
    day = (day < 10 ? "0" : "") + day;
    hour = (hour < 10 ? "0" : "") + hour;
    min = (min < 10 ? "0" : "") + min;

    var today = year + "-" + month + "-" + day + "T" + hour + ":" + min;
    return today;

});

var index = 1;

//Source: https://shouts.dev/add-or-remove-input-fields-dynamically-using-jquery
function addrow() {
    var html = '';
    html += '<div id="inputFormRow">';
    html += '<div class="input-group mb-3">';
    html += '<input type="text" name="issues[' + index + ']" class="form-control m-input" placeholder="Enter title" autocomplete="off" id="issues' + index + '">';
    html += '<div class="input-group-append">';
    html += '<button id="removeRow" type="button" class="btn btn-danger">Remove</button>';
    html += '</div>';
    html += '</div>';

    $('#newRow').append(html);
    console.log(index);
    index++;
}

// remove row
$(document).on('click', '#removeRow', function () {
    var removedIDString = $(this).closest('#inputFormRow').find("input")[0].id;
    $(this).closest('#inputFormRow').remove();
    issues = "issues";
    var removedID = removedIDString.substring(6);


    if (removedID !== undefined) {
        removedID = parseInt(removedID);
        console.log(removedID);
        if (removedID !== undefined) {
            for (var i = removedID + 1; i < index; i++) {
                var temp = $("#issues" + i)[0];

                temp.name = issues + "[" + (i - 1) + "]";
                temp.id = issues + (i - 1);
            }
        }

    }
    index--;
    
});




