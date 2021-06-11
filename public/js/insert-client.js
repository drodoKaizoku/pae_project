import {getData, postData, deleteData, updateData} from "./utilsAPI.js";
import {showView} from "./utils.js";
$(document).ready(function () {
    $("#form-insert-client").submit(e => {
        e.preventDefault();
        if(isFormValid()){
            let client = {
                lastName: $("#client-firstname").val(),
                firstName: $("#client-lastname").val(),
                streetName: $("#street-name").val(),
                streetNumber: $("#street-number").val(),
                postBox: $("#post-box").val(),
                city: $("#client-city").val(),
                postCode: $("#post-code").val(),
                email: $("#client-email").val(),
                phoneNumber: $("#client-phone").val()
            };
            const data ={
                client: JSON.stringify(client)
            };
            postData("clients", data,localStorage.getItem("token"), onPostClient, onErrorClient);
        }

    })

});

function onPostClient(){
    $("#form-insert-client")[0].reset();

    $("#success-insert-client").html("Client was successfully created.");
    $("#success-insert-client").show();
}

function onErrorClient(response) {
    let errorMsg="";
    switch (response.status) {
        case 400:
            errorMsg = "Please provide non empty/valid inputs."
            break;
        case 409:
            errorMsg = "This client already exists."
            break;
        case 500:
            errorMsg = "Server error.";
            break;
    }
    $("#error-insert-client").html(errorMsg);
    $("#error-insert-client").show();
}

function isFormValid(){
    if($("#client-firstname")=== ""|| $("#client-lastname")===""||$("#client-email")===""||$("#client-phone")===""|| $("#street-name")===""||$("#street-number")===""||
    $("#post-code")==="" ||$("#post-box")===""){
        $("#error-insert-client").html("Please provide non empty/valid inputs.");
        $("#error-insert-client").show();
        return false;
    }
    return true;
}

