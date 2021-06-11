import {getData, postData, deleteData, updateData} from "./utilsAPI.js";
import { showView } from "./utils.js";

$(document).ready(function () {
    
    $("#form-register").submit(e => {
        e.preventDefault();
        
        if (isFormValid()) {
            console.log("je rentre");
            var jsonUserToRegister = {
                username: $("#new-username").val(),
                lastName: $("#lastname").val(),
                firstName: $("#firstname").val(),
                city: $("#city").val(),
                email: $("#email").val(),
                hashedPassword: $("#new-password").val()
            };
            const data = {
                user: JSON.stringify(jsonUserToRegister),
            };
            postData("users", data, onPostRegister, onErrorRegister);
        } 
        
    })
    
});

function onPostRegister(){
    $("#form-register")[0].reset();
    $("#success-register").html("Thanks for registering ! Please await confirmation from the admins.");
    $("#success-register").show();
    
}

function onErrorRegister(response) {
    let errorMsg="";
    switch (response.status) {
        case 400:
        errorMsg = "Please provide non empty credentials and/or valid email.";
        break;
        case 409:
        errorMsg = "Username or email already taken.";
        break;
    }
    $("#error-register").html(errorMsg);
    $("#error-register").show();
    
}



function isFormValid(){
    let regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$";
    if($("#new-username").val() === "" ||  $("#lastname").val() === "" || $("#firstname").val() === "" ||
        $("#city").val() ==="" || $("#email").val() ===""|| $("#new-password").val() === "" ||
        $("#password-confirm").val() === ""){

        $("#error-register").html("Please provide non empty/valid inputs.");
        $("#error-register").show();
        return false;
    }
    
    if(!$("#email").val().match(regexEmail)){
        $("#error-register").html("Please provide valid email.");
        $("#error-register").show();
        return false;
    }
    if($("#new-password").val() !== $("#password-confirm").val()){
        $("#error-register").html("Please provide matching passwords.");
        $("#error-register").show();
        return false;
    }
    
    return true;
}
