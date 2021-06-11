import {getData, postData, deleteData, updateData} from "./utilsAPI.js";
import { showView, showDetails } from "./utils.js";

$(document).ready(function () {
    
    $("#form-login").submit(e => {
        e.preventDefault();
        if(isFormValid()){
            const data = {
                username: $("#username").val(),
                password: $("#password").val()
            };
            postData("login", data,localStorage.getItem("token"), onPostLogin, onErrorLogin);
        }
        
    })
    
});

function isFormValid(){
    if($("#username").val() === "" || $("#password").val() === ""){
        $("#error-login").html("Please provide non empty/valid inputs.");
        $("#error-login").show();
        return false;
    }
    return true;
}

function onPostLogin(response) {
    $("#username").val("");
    $("#password").val("");
    localStorage.setItem("token", response.token);
    localStorage.setItem("user", JSON.stringify(response.user));
    let user = JSON.parse(localStorage.getItem("user"));
    $(".offline").hide();
    $(".online").show();
    if(user.worker == true){
        $("#btn-dashboard-worker").click();
        $(".online-client").hide();
        $(".online-worker").show();
        showView("#view-dashboard-worker");
    } else {
        $("#btn-dashboard-client").click();
        $(".online-worker").hide();
        $(".online-client").show();
        showView("#view-dashboard-client");
    }  
}

function onErrorLogin(response) {
    let errorMsg="";
    switch (response.status) {
        case 400:
        errorMsg = "Invalid Username";
        break;
        case 401:
        errorMsg = "Wrong Credentials";
        break;
        case 403:
        errorMsg = "Your account hasn't been confirmed yet";
        break;
        case 500:
        errorMsg = "Server Error";
        break;
    }

    $("#error-login").html(errorMsg);
    $("#error-login").show();
    
}



