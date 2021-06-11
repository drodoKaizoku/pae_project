import {getData, postData, deleteData, updateData} from "./utilsAPI.js";
import {showView} from "./utils.js";
import {onGetMenuTypes} from "./home.js"
import {onGetClient, onGetTypes,onGetNotLinkedClients,onGetUsersNotLinked} from "./gestion-worker.js";
import {onGetClientQuotes,onGetTypesClient} from "./dashboard-client.js";
import { onGetClientsResult,onGetClientsAutocomplete,onGetUsersAutocomplete} from "./dashboard-worker.js";
$(document).ready(function () {
    $("#btn-login").on('click', ()=>{
        showView("#view-login");
    })
    $("#btn-register").on('click', ()=>{
        showView("#view-register");
    })
    $("#btn-logout").on('click',()=>{
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        getData("types", localStorage.getItem("token"), onGetMenuTypes);
        $(".online-worker").hide();
        $(".online-client").hide();
        $(".online").hide();
        $(".offline").show();
        showView("#view-home");
    })

    $("#btn-dashboard-worker").on('click',()=>{
        clearForm();
        showView("#view-dashboard-worker");
        getData("searchClients?request=findClientFiltred",localStorage.getItem("token"),onGetClientsResult);
        getData("searchClients?request=findClientFiltred",localStorage.getItem("token"),onGetClientsAutocomplete);
        getData("users?request=searchUserFiltredBy",localStorage.getItem("token"),onGetUsersAutocomplete);
    })

    $("#btn-gestion-worker").on('click',()=>{
        showView("#view-gestion-worker");
        clearForm();
        getData("searchClients?request=findClientFiltred", localStorage.getItem("token"), onGetClient);
        getData("types",localStorage.getItem("token"),onGetTypes);
        getData("clients",localStorage.getItem("token"),onGetNotLinkedClients);
        getData("users?request=searchUserNotLinked",localStorage.getItem("token"),onGetUsersNotLinked)
    })

    $("#btn-dashboard-client").on('click',()=>{
        showView("#view-dashboard-client");
        clearForm();
        let token = localStorage.getItem("token");
        let user = JSON.parse(localStorage.getItem("user"));
        if(user.client !== null){
            console.log(user.client);
            let url = "quotes?request=getQuotesByClient";
            getData(url, token,onGetClientQuotes);
            getData("types",token,onGetTypesClientDashboard);
        }     
    })

});

function clearForm(){
    $('#introduce-quote-form')[0].reset();
    $("#pictures-previews").text("");
}

function onGetTypesClientDashboard(response) {
    let tableau = response.types;
    console.log(response.types);
    
    let menuType = document.getElementById("menu-types");
    
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
        input.setAttribute("name","type-id");
        
        span.appendChild(input);
        span.append(type.projectTypeName)
        li.appendChild(span);
        
        menuType.appendChild(li);
    }
}



