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




