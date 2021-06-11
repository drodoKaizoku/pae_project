import {getData, postData, deleteData, updateData} from "./utilsAPI.js";
import {showView} from "./utils.js";
import {onGetMenuTypes} from "./home.js";
let picturesBeforeAmenagement = [];
let tableNotLinkedClients= [];
$(document).ready(function () {   
    $("#picturesBeforeAmenagement")[0].onchange = function (e) {
        encodeImagetoBase64(e.target.files[0]);
    };

    $("#form-search-clients").submit(e=>{
        let url = "searchClient?name"
    });

    $("#introduce-new-type").submit(e=>{
        e.preventDefault();
        console.log("je suis la");
        console.log($("#type-name").val());
        
        
        if($("#type-name").val() !== ""){
            const data={
                type : $("#type-name").val()
            };
            postData("types",data,localStorage.getItem("token"),onPostType);
        } else{
            $("#error-insert-type").html("Please provide non empty input.");
            $("#error-insert-type").show();
        }
    });
    
    $("#btn-insert-quote").on('click',()=>{
        if(isValidForm()){
            let quoteToRegister = {
                client: $("#client-list option:selected").val(),
                fullAmount: $("#quote-amount").val(),
                dateQuote: "date-" + $("#quote-date").val(),
                workPeriod: $("#duration").val(),
                listType: getCheckedType(),
                picturesBefore: picturesBeforeAmenagement
            };
            const data = {
                user: $("#user-to-link-list option:selected").val(),
                quote: JSON.stringify(quoteToRegister)
            };
            postData("/quotes", data,localStorage.getItem("token") ,onPostQuote, onErrorQuote);
        }   
    })

    $("#btn-show-insert-client").on('click',()=>{
         showView("#view-insert-client");   
    })


});

function onPostType(){
    let list = document.getElementById("menu-types");
    list.innerHTML = "";
    getData("types",localStorage.getItem("token"),onGetTypes);
    getData("types", localStorage.getItem("token"), onGetMenuTypes);
    $("#introduce-new-type")[0].reset();
    $("#success-insert-type").html("New type has been introduced.");
    $("#success-insert-type").show();
}

function onPostQuote(){
    //A MODIF 
    picturesBeforeAmenagement = [];
    $('#introduce-quote-form')[0].reset();
    $("#pictures-previews").text("");
    $("#success-gestion-worker").html("Quote has been introduced.");
    $("#success-gestion-worker").show();
}

function onErrorQuote(response) {
    let errorMsg="";
    switch (response.status) {
        case 400:
            errorMsg = "Invalid dates";
            break;
        case 401:
            errorMsg = "Wrong Credentials";
            break;
        case 409:
            errorMsg = "This client is already linked to an user";
            break;
        case 500:
            errorMsg = "Server Error";
            break;
    }
    $("#error-gestion-worker").html(errorMsg);
    $("#error-gestion-worker").show();
}

function isValidForm(){
    if($("#client-list option:selected").val() == "" || $("#quote-amount").val() == "" || 
    $("#quote-amount").val() <= 0 || $("#quote-date").val() == ""   || $("#duration").val() == ""){
        $("#error-gestion-worker").html("Please provide non empty/valid inputs.");
        $("#error-gestion-worker").show();
        return false;
    }
    return true;
}

function getCheckedType() {
    let listTypeChecked = new Array();    
    let items = document.getElementsByName("type-id-introduce-quote");
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == "checkbox" && items[i].checked == true) {
            listTypeChecked.push(items[i].value);
        }
    }
    return listTypeChecked;
}

function onGetClient(response) {
    let tableau = response.clients;
    let clientList = document.getElementById("client-list");
    let optionDefault = document.createElement("option");
    optionDefault.value = "";
    optionDefault.text = "Choose a client";
    clientList.appendChild(optionDefault);
    for (let i = 0; i < tableau.length; i++) {
        let client = tableau[i];
        
        let newOption = document.createElement("option");
        newOption.setAttribute("id",i);
        newOption.value = client.idClient;
        newOption.text = client.firstName + " " + client.lastName;

        clientList.appendChild(newOption);
    } 
}

function onGetUsersNotLinked(response){
    let tableau = response.users;
    console.log(tableau);
    let userList = document.getElementById("user-to-link-list");
    let optionEmpty = document.createElement("option");
    optionEmpty.value = "";
    optionEmpty.text = "Choose user";
    userList.appendChild(optionEmpty);
    for (let i = 0; i < tableau.length; i++) {
        let user = tableau[i];
        
        let newOption = document.createElement("option");
        newOption.setAttribute("id",i);
        newOption.value = user.idUser;
        newOption.text = user.firstName + " " + user.lastName;

        userList.appendChild(newOption);
    } 

}

function onGetTypes(response) {
    let tableau = response.types;
    console.log(response.types);
    
    let menuType = document.getElementById("menu-types-gestion");
    
    menuType.innerHTML="";
    for (let i = 0; i < tableau.length; i++) {
        let type = tableau[i];
        
        let li = document.createElement("li");
        li.setAttribute("class","mx-1");
        
        let span = document.createElement("span");
        
        let input = document.createElement("input");
        input.setAttribute("type","checkbox");
        input.setAttribute("class","mr-1");
        input.setAttribute("value",type.idProjectType);
        input.setAttribute("name","type-id-introduce-quote");
        
        span.appendChild(input);
        span.append(type.projectTypeName)
        li.appendChild(span);
        
        menuType.appendChild(li);
    }
}

function onGetUnconfirmedUsers(response){
   
    let tableau = response.users
    let tableBody = document.getElementById("table-body-unconfirmed-users");
    for(let i = 0; i < tableau.length ; i++){
        let user = tableau[i];
        let tr = document.createElement("tr");
        tr.setAttribute("id","row-user-"+i);
        let th = document.createElement("th");
        th.setAttribute("scope","row");
        th.value = user.idUser;
        th.innerHTML = user.idUser;

        let tdUsername = document.createElement("td");
        tdUsername.innerHTML = user.username;
        let tdLastname = document.createElement("td");
        tdLastname.innerHTML = user.lastName;
        let tdFirstname = document.createElement("td");
        tdFirstname.innerHTML = user.firstName;
        
        
        let tdCheckBox = document.createElement("td");
        let div = document.createElement("div");
        div.setAttribute("class","form-group");

        let checkbox = document.createElement("input");
        checkbox.setAttribute("type","checkbox");
        
        let tdSelect= document.createElement("td");

        let select = document.createElement("select");
        select.setAttribute("class","form-control");
        select.setAttribute("id","select-client-"+i);
        let optionEmpty = document.createElement("option");
        optionEmpty.value = "";
        optionEmpty.text = "Choose client";
        select.appendChild(optionEmpty);

        for(let i = 0; i < tableNotLinkedClients.length; i++){
            let newOption = document.createElement("option");
            newOption.value = tableNotLinkedClients[i].idClient;
            newOption.text = tableNotLinkedClients[i].lastName + " " +tableNotLinkedClients[i].firstName;
            select.appendChild(newOption);
        }

        let tdButton = document.createElement("td");
        let button = document.createElement("button");
        button.setAttribute("type","button");
        button.setAttribute("class","btn btn-secondary");
        button.setAttribute("id","btn-confirm-"+i);
        button.innerHTML = "Confirm";

        tr.appendChild(th);
        tr.appendChild(tdUsername);
        tr.appendChild(tdLastname);
        tr.appendChild(tdFirstname);
        tr.appendChild(tdCheckBox);
        tr.appendChild(tdSelect);
        tr.appendChild(tdButton);
        tdCheckBox.appendChild(div);
        div.appendChild(checkbox);
        tdSelect.appendChild(select);
        tdButton.appendChild(button);
        tableBody.appendChild(tr); 

        button.addEventListener('click',()=>{
            const data={
                worker : checkbox.checked, 
                user : user.idUser,
                client : $("#select-client-"+i+" option:selected").val()
            }
            updateData("users",localStorage.getItem("token"),data,()=>{
                onPostUpdateUser("#row-user-"+i);
            });
            

        })
    }
}

function onPostUpdateUser(rowId){
    $(rowId).detach();
}

function onGetNotLinkedClients(response){
    tableNotLinkedClients = response.clients;
    getData("users?request=searchUnconfirmedUser",localStorage.getItem("token"),onGetUnconfirmedUsers)
}

function encodeImagetoBase64(file) {
    var reader = new FileReader();
    
    reader.onloadend = function () {
    
        picturesBeforeAmenagement.push(reader.result);
        let imgPreview = document.createElement("img");
        imgPreview.style.maxWidth = "100px";
        imgPreview.style.maxHeight = "100px";
        imgPreview.setAttribute("src", reader.result);
        $("#pictures-previews").append(imgPreview);
    };
    reader.readAsDataURL(file);
}


export {
    onGetClient,
    onGetTypes,
    onGetNotLinkedClients,
    onGetUsersNotLinked
}


