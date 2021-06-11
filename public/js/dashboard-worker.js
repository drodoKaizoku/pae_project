import {getData, postData, deleteData, updateData} from "./utilsAPI.js";
import {showView,showDetails,showStateBtn,hideAllStateBtn,selectResult} from "./utils.js";

const INTRODUCED = "Introduced";
const ORDER_CONFIRMED = "Order confirmed";
const DATE_CONFIRMED = "Date start of work confirmed";
const CANCELLED = "Cancelled";
const MID_BILL_SENT = "Bill mid-work sent";
const END_BILL_SENT = "Bill end-work sent";
const VISIBLE = "Visible";
const CLOSED = "Closed";


let currentQuote;
let picturesAfterAmenagement = new Array();
let position = 0;
$(document).ready(function ($) {
    $("#quote-add-picture-after")[0].onchange = function (e) {
        encodeImagetoBase64(e.target.files[0]);
    }

    $("#btn-add-picture").click(()=>{
        addPictureToQuote();
    });

    $("#btn-tab-clients").click(() => {
        switchTab("#btn-tab-clients");
        showSearchFilters("#form-search-clients-worker");
        $("#result-list").text("");
        getData("searchClients?request=findClientFiltred",localStorage.getItem("token"),onGetClientsResult)
    });

    $("#btn-tab-users").click(e => {
        switchTab("#btn-tab-users");
        showSearchFilters("#form-search-user-worker");
        $("#result-list").text("");
        getData("users?request=searchUserFiltredBy",localStorage.getItem("token"),onGetUsersResult)
    });

    $("#btn-tab-quotes").click(() => {
        switchTab("#btn-tab-quotes");
        showSearchFilters("#form-search-quotes-worker");
        let menuType = document.getElementById("menu-search-quote-type");
        menuType.innerHTML = "";
        $("#result-list").text("");
        getData("types",localStorage.getItem("token"),onGetTypes);
        let url = "quotes?clientName="+""+"&quoteDate="+""+"&minPrice="+""+"&maxPrice="+"&request=searchQuoteFiltredBy";

        getData(url,localStorage.getItem("token"),onGetQuotesResult);
    });

    $("#form-search-clients-worker").submit(e=>{
        e.preventDefault();
        $("#result-list").text("");
        let name = $("#search-client-name").val();
        let postCode = $("#search-client-post-code").val();
        let city = $("#search-client-city").val();
        let url = "searchClients?name="+name+"&postCode="+postCode+"&city="+city+"&request=findClientFiltred";
        getData(url,localStorage.getItem("token"),onGetClientsResult);
    });

    $("#form-search-user-worker").submit(e=>{
        e.preventDefault();
        $("#result-list").text("");
        let name = $("#search-user-name").val();
        let city = $("#search-user-city").val();
        let url = "users?name="+name+"&city="+city+"&request=searchUserFiltredBy";
        getData(url,localStorage.getItem("token"),onGetUsersResult);
    });

    $("#form-search-quotes-worker").submit(e=>{
        e.preventDefault();
        $("#result-list").text("");
        let clientName = $("#search-quote-client-name").val();
        let quoteDate = $("#search-quote-date").val();
        let minPrice = $("#search-quote-min-price").val();
        let maxPrice = $("#search-quote-max-price").val();
        let url = "quotes?clientName="+clientName+"&quoteDate="+quoteDate+"&minPrice="+minPrice+"&maxPrice="+maxPrice+"&listType="+JSON.stringify(getCheckedType())+"&request=searchQuoteFiltredBy";

        getData(url,localStorage.getItem("token"),onGetQuotesResult);

    });

    $("#form-push-back-date-start-work").submit(e=>{
        e.preventDefault();
        const data = {
            idQuote : currentQuote.idQuote,
            date : $("#quote-date-start-work-input").val(),
            request : "addQuoteStartWork"
        };
        if($("#quote-date-start-work-input").val() !== ""){
            updateData("quotes",localStorage.getItem("token"),data,onSuccessQuote);
        } else{
            $("#error-dashboard-worker-quote").html("Please provide non empty date");
            $("#error-dashboard-worker-quote").show();
        }
    });

    $("#btn-confirm-start-work-date").click(()=>{
        const data = {
            dateStart : currentQuote.dateStartWork,
            idQuote : currentQuote.idQuote,
            state : DATE_CONFIRMED,
            request : "setStateQuote"
        };
        updateData("quotes",localStorage.getItem("token"),data,onSuccessQuote);
    });

    $("#btn-bill-mid-work-sent").click(()=>{
        const data = {
            idQuote : currentQuote.idQuote,
            state : MID_BILL_SENT,
            request : "setStateQuote"
        };
        updateData("quotes",localStorage.getItem("token"),data,onSuccessQuote);

    });

    $("#btn-bill-end-work-sent").click(()=>{
        const data = {
            idQuote : currentQuote.idQuote,
            state : END_BILL_SENT,
            request : "setStateQuote"
        };
        updateData("quotes",localStorage.getItem("token"),data,onSuccessQuote);
    });

    $(".class-checkbox-picture").hide();
    $("#btn-make-quote-visible").click(()=>{
        $(".class-checkbox-picture").show();
        $("#btn-validate-picture").show();
        $("#btn-make-quote-visible").hide();

    });

    $("#btn-validate-picture").click(()=>{
        let arrayPicture = new Array();
        let checkboxPicture = $("input:checkbox[class=class-checkbox-picture]");
        $(".class-checkbox-picture").hide();
        checkboxPicture.each(function(){
            if($(this).prop("checked")){
                arrayPicture.push($(this).val());
            }
        });
        const data = {
            listOfPicture : JSON.stringify(arrayPicture),
            request : "SetPictureVisible",
            idQuote : currentQuote.idQuote
        }


        updateData("pictures",localStorage.getItem("token"),data,onSuccessQuote);

    });

    $("#btn-close-quote").click(()=>{
        const data = {
            idQuote : currentQuote.idQuote,
            state : CLOSED,
            request : "setStateQuote"
        };
        updateData("quotes",localStorage.getItem("token"),data,onSuccessQuote);

    });
    $("#btn-cancel-quote").click(()=>{
        const data = {
            idQuote : currentQuote.idQuote,
            state : CANCELLED,
            request : "setStateQuote"
        };
        updateData("quotes",localStorage.getItem("token"),data,onSuccessQuote);
    });

    $("#btn-bill-down-payement-sent").click(()=>{
        const data = {
            idQuote : currentQuote.idQuote,
            state : ORDER_CONFIRMED,
            request : "setStateQuote"

        };
        if(currentQuote.dateStartWork !== null){
            updateData("quotes",localStorage.getItem("token"),data,onSuccessQuote);
        } else{
            $("#error-dashboard-worker-quote").html("You have to enter a start date.");
            $("#error-dashboard-worker-quote").show();
        }
    });


    $("#quote-choose-favorite-picture").click(()=>{
        $("#quote-validate-favorite-picture").show();
        $("#quote-choose-favorite-picture").hide();
        $("input[type=radio].class-radio-btn").show();
        $(this).hide();
    });

    $("#quote-validate-favorite-picture").click(()=>{
        $("#quote-choose-favorite-picture").show();
        $("#quote-validate-favorite-picture").hide();
        $("input[type=radio].class-radio-btn").hide();
        let radioBtn = document.getElementsByName("group-radio-btn");
        let value ;

        for(let i = 0; i< radioBtn.length; i++){
            if(radioBtn[i].type==='radio' && radioBtn[i].checked){
                value = radioBtn[i].value;

            }
        }
        if(value !== undefined){

            const data ={
                idPicture : value,
                idQuote : currentQuote.idQuote
            }
            updateData("pictures?request=addFavoritePicture",localStorage.getItem("token"),data,onSuccessQuote);
        }
    });
});

function onGetTypesSelect(response) {
    let k = 0
    let tableau = response.types;
    let listType = document.createElement("SELECT");

    for (let i = 0; i < tableau.length; i++) {
        let type = tableau[i];

        let newOption = document.createElement("option");

        newOption.value = type.idProjectType;
        newOption.text = type.projectTypeName;

        listType.appendChild(newOption);
    }
    for( let j = 0; j < picturesAfterAmenagement.length; j++){

        $("#div-principal"+j).append(listType);
        listType.setAttribute("id","option-picture-input-id"+j);
    }

}

function onSuccessQuote(){

    picturesAfterAmenagement=[];
    position = 0;
    getData("quotes?idQuote="+currentQuote.idQuote+"&request=searchQuotebyId",localStorage.getItem("token"),onGetPostQuotes);

}

function addPictureToQuote(){
    if(picturesAfterAmenagement.length !== 0){
        let checkboxPicture = $("input:checkbox[class=new-pic]")

        for(let i = 0 ; i < picturesAfterAmenagement.length; i++){
            picturesAfterAmenagement[i].typeProject = $("#option-"+picturesAfterAmenagement[i].idPictureHtml+ " option:selected").val();
        }

        const data1 = {
            picturesAfter : JSON.stringify(picturesAfterAmenagement),
            idQuote : currentQuote.idQuote
        }
        postData("/pictures",data1,localStorage.getItem("token"),onSuccessQuote);
    }
}

function getCheckedType() {
    let listTypeChecked = new Array();
    let items = document.getElementsByName("type-id-list");
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == "checkbox" && items[i].checked == true) {
            listTypeChecked.push(items[i].value);
        }
    }
    return listTypeChecked;
}

function onGetTypes(response){

    let tableau = response.types;
    let menuType = document.getElementById("menu-search-quote-type");

    for (let i = 0; i < tableau.length; i++) {
        let type = tableau[i];

        let li = document.createElement("li");
        li.setAttribute("class","mx-1");

        let span = document.createElement("span");

        let input = document.createElement("input");
        input.setAttribute("type","checkbox");
        input.setAttribute("class","mr-1");
        input.setAttribute("value",type.idProjectType);
        input.setAttribute("name","type-id-list");

        span.appendChild(input);
        span.append(type.projectTypeName)
        li.appendChild(span);

        menuType.appendChild(li);
    }

}

function switchTab(tabId) {
    $(".tab").addClass("btn-secondary").removeClass("btn-primary");
    $(tabId).addClass("btn-primary").removeClass("btn-secondary");
}

function showSearchFilters(formId) {
    $(".form-search").hide();
    $(formId).show();
}

function onGetQuotesResult(response){
    let table = response.quotes;
    let listQuotes = document.getElementById("result-list");
    for(let i = 0; i < table.length; i++){
        let divPrincipal = document.createElement("div");
        divPrincipal.setAttribute("class","container-fluid border ltr ml-2 result-item");
        divPrincipal.setAttribute("id","quote-search-result-"+i);
        divPrincipal.setAttribute("value",table[i].idQuote);

        let imgFav = document.createElement("img");
        imgFav.setAttribute("class","float-left col-3 px-0 w-100");
        let favoritePicture = JSON.parse(table[i].favoritePicture);

        if(table[i].favoritePicture !== null){
            imgFav.setAttribute("src",favoritePicture.source);
            imgFav.setAttribute("class","mw-100px px-1 py-1 border");
        } else{
            imgFav.setAttribute("src","");
            imgFav.setAttribute("class","mw-100px px-1 py-1 border");
        }

        let divRow = document.createElement("div");
        divRow.setAttribute("class","row");

        let divContainer = document.createElement("div");
        divContainer.setAttribute("class","container-fluid col-9");

        let divContainerImg = document.createElement("div");
        divContainerImg.setAttribute("class","container-fluid col-3");
        divContainerImg.appendChild(imgFav);

        let div  = document.createElement("div");
        div.setAttribute("class","row");
        let divx = document.createElement("div");
        divx.setAttribute("class","container-fluid");
        let spanxa = document.createElement("span");
        spanxa.setAttribute("class","font-weight-bold");
        spanxa.innerHTML = "Client: "+ JSON.parse(table[i].objectClient).firstName +" "+JSON.parse(table[i].objectClient).lastName;

        let div1 = document.createElement("div");
        div1.setAttribute("class","row")
        let div1a = document.createElement("div");
        div1a.setAttribute("class","container-fluid col-9");
        let span1a = document.createElement("span");
        span1a.innerHTML = "State: " +table[i].state;
        span1a.setAttribute("class","font-weight-bold");
        span1a.setAttribute("id","quote-statut-id"+table[i].idQuote);
        let div1b = document.createElement("div");
        div1b.setAttribute("class","container-fluid col-3 text-right");
        let span1b = document.createElement("span");
        span1b.innerHTML = "#"+table[i].idQuote;
        span1b.setAttribute("class","font-weight-bold");

        let div2 = document.createElement("div");
        div2.setAttribute("class","row");
        let div2a = document.createElement("div");
        div2a.setAttribute("class","container-fluid")
        let span2a = document.createElement("span");
        span2a.setAttribute("class","font-weight-bold");
        span2a.innerHTML = "Date of quote: "+ table[i].dateQuote;
        let div3 = document.createElement("div");
        div3.setAttribute("class","row");
        let div3a = document.createElement("div");
        div3a.setAttribute("class","container-fluid");
        let span3a = document.createElement("span");
        span3a.setAttribute("class","font-weight-bold");
        span3a.innerHTML = "Price: "+table[i].fullAmount;

        let typesQuotesString ="";
        let typesQuotes = JSON.parse(table[i].listType);
        
        for(let i = 0; i < typesQuotes.length ; i++){
            typesQuotesString += typesQuotes[i]+"</br> ";
        }
        
        
        let div4 = document.createElement("div");
        div4.setAttribute("class","row");
        let div4a = document.createElement("div");
        div4a.setAttribute("class","container-fluid");
        let span4a = document.createElement("span");
        span4a.setAttribute("class","font-weight-bold");
        span4a.innerHTML = "Types: ".fontsize(2)+ typesQuotesString.fontsize(2);

        div4.appendChild(div4a);
        div4a.appendChild(span4a);

        div.appendChild(divx);
        divx.appendChild(spanxa);



        div1.appendChild(div1a)
        div1.appendChild(div1b);

        div1a.appendChild(span1a);
        div1b.appendChild(span1b);


        div2.appendChild(div2a);
        div2a.appendChild(span2a);


        div3.appendChild(div3a);
        div3a.appendChild(span3a);

        divContainer.appendChild(div);
        divContainer.appendChild(div1);
        divContainer.appendChild(div2);
        divContainer.appendChild(div3);
        divContainer.appendChild(div4);

        divRow.appendChild(divContainerImg);
        divRow.appendChild(divContainer);

        divPrincipal.appendChild(divRow);
        listQuotes.appendChild(divPrincipal);
        $("#quote-search-result-"+i).click(()=> {
            $("#pictures-after-previews").text("");
            position = 0;
            picturesAfterAmenagement =[];
            getData("quotes?idQuote="+table[i].idQuote+"&request=searchQuotebyId",localStorage.getItem("token"),onGetPostQuotes);
        });
    }
}

function onGetPostQuotes(response){
    let quote = response.quotes[0];
    currentQuote = response.quotes[0];

    let doc = document.getElementById("quote-statut-id"+currentQuote.idQuote);
    if(doc !== null){
        doc.innerHTML = "State: "+ currentQuote.state;
    }
    $("#details-quote-type").html("");
    let htmlType ="Quote types: <br>";
    let tableType = JSON.parse(quote.listType);
    for (let i = 0 ; i < tableType.length ; i++){
        htmlType += tableType[i] +"<br>";
    }
    $("#details-quote-type").append(htmlType);
    $("#pictures-after-previews").text("");
    $("#details-worker-quote-pictures-before").text("");
    $("#details-worker-quote-pictures-after").text("");

    $("#details-quote-client-name").text("Client: " + JSON.parse(currentQuote.objectClient).firstName +" "+  JSON.parse(currentQuote.objectClient).lastName);
    //selectResult($("#quote-search-result-"+i));
    showDetails("#details-quote");
    let state = quote.state;

    $("#form-push-back-date-start-work").hide();
    $("#btn-cancel-quote").hide();
    $("#btn-close-quote").hide();
    $("#btn-precedent").hide();
    let dateStart = quote.dateStartWork;
    if(dateStart === null){
        dateStart = "";
    }
    $("#details-quote-id").text("ID: " + quote.idQuote);
    $("#details-quote-date").text("Date: " + quote.dateQuote);
    $("#details-quote-date-start").text("Date Start Work: " + dateStart) ;
    $("#details-quote-work-length").text("Work Length: " + quote.workPeriod);
    $("#details-quote-price").text("Total: " + quote.fullAmount+ "€");
    if(currentQuote !== null){
        $("#details-quote-state").text("State: " + currentQuote.state);
    } else{
        $("#details-quote-state").text("State: " + quote.state);
    }

    let tablePicturesBefore = JSON.parse(quote.picturesBefore);
    let tablePicturesAfter = JSON.parse(quote.picturesAfter);
    let favoritePicture = JSON.parse(quote.favoritePicture);

    let listPictureBefore = document.getElementById("details-worker-quote-pictures-before");
    let listPicturesAfter = document.getElementById("details-worker-quote-pictures-after");
    let favoritePictureHtml = document.getElementById("details-worker-quote-favorite-picture");
    let formRadioBtn = document.createElement("form");
    listPicturesAfter.appendChild(formRadioBtn);
    if(quote.favoritePicture !== null){
        favoritePictureHtml.setAttribute("src",favoritePicture.source);
    } else{
        favoritePictureHtml.setAttribute("src"," ");
    }

    for(let j = 0; j <tablePicturesBefore.length; j++){
        let img = document.createElement("img");
        img.setAttribute("src",tablePicturesBefore[j].source);
        img.setAttribute("style","width: 200px; height: 130px");
        listPictureBefore.appendChild(img);
    }
    for(let j = 0; j <tablePicturesAfter.length; j++){
        let divPicture = document.createElement("div");
        divPicture.setAttribute("id","div-picture-"+j);
        let divShowed = document.createElement("div");
        let spanShowed = document.createElement("span");
        if(tablePicturesAfter[j].visible == true){
            spanShowed.innerHTML = "VISIBLE";
        } else{
            spanShowed.innerHTML = "NON VISIBLE";
        }
        divShowed.appendChild(spanShowed);

        let radioBtn = document.createElement("INPUT");
        radioBtn.setAttribute("type","radio");
        radioBtn.setAttribute("class","class-radio-btn");
        radioBtn.value = tablePicturesAfter[j].idPicture;
        radioBtn.setAttribute("name","group-radio-btn");
        radioBtn.setAttribute("id",tablePicturesAfter[j].idPicture )
        let labelRadio = document.createElement("label");
        labelRadio.setAttribute("for",tablePicturesAfter[j].idPicture);
        let img = document.createElement("img");
        img.setAttribute("src",tablePicturesAfter[j].source);
        img.setAttribute("style","width: 200px; height: 130px");
        divPicture.appendChild(img)
        divPicture.appendChild(radioBtn);
        divPicture.appendChild(divShowed);
        formRadioBtn.appendChild(divPicture);
        let checkBox = document.createElement("input");
        checkBox.setAttribute("type","checkbox");
        checkBox.setAttribute("value",tablePicturesAfter[j].idPicture);
        checkBox.setAttribute("class","class-checkbox-picture");
        divPicture.appendChild(checkBox);
    }

    $("input[type=radio].class-radio-btn").hide();
    $("#quote-validate-favorite-picture").hide();
    $("#quote-choose-favorite-picture").hide();
    $("#quote-picture-favorite-after").hide();

    if(quote.dateStartWork === null || !isBeforeToday(quote.dateStartWork)){
        $("#btn-cancel-quote").show();
    } else{
        $("#btn-cancel-quote").hide();
    }
    $("#btn-add-picture").hide();
    $("#btn-validate-picture").hide();
    $(".class-checkbox-picture").hide();
    switch(state){
        case INTRODUCED:
        showStateBtn($("#btn-bill-down-payement-sent"));
        $("#form-push-back-date-start-work").show();
        $("#btn-delete-start-date").show();
        break;
        case ORDER_CONFIRMED:
        showStateBtn($("#btn-confirm-start-work-date"));
        $("#form-push-back-date-start-work").show();
        $("#btn-delete-start-date").show();
        break;
        case DATE_CONFIRMED:
        if(quote.workPeriod > 15 ){
            showStateBtn($("#btn-bill-mid-work-sent"));
        } else{
            showStateBtn($("#btn-bill-end-work-sent"));
        }
        $("#btn-delete-start-date").hide();
        break;
        case MID_BILL_SENT:
        showStateBtn($("#btn-bill-end-work-sent"));
        $("#btn-delete-start-date").hide();
        break;
        case END_BILL_SENT:
        showStateBtn($("#btn-make-quote-visible"));
        $("#quote-picture-favorite-after").show();
        $("#btn-close-quote").show();
        $("#btn-delete-start-date").hide();
        $("#btn-add-picture").show();
        $("#quote-choose-favorite-picture").show();
        break;
        case VISIBLE:
        $("#quote-picture-favorite-after").show();
        $("#btn-close-quote").hide();
        $("#btn-delete-start-date").hide();
        $("#pictures-after-previews").text("");
        $("#quote-picture-favorite-after").hide();
        hideAllStateBtn();
        break;
        case CLOSED:
        $("#quote-picture-favorite-after").hide();
        $("#pictures-after-previews").text("");
        $("#btn-delete-start-date").hide();
        hideAllStateBtn();
        break;
        case CANCELLED:
        $("#quote-picture-favorite-after").hide();
        $("#btn-delete-start-date").hide();
        hideAllStateBtn();
        break;
    };

}

function isBeforeToday(date) { // date => String format dd-MM-yyyy
    const day = Number(date.substring(0, 2));
    const month = Number(date.substring(3, 5));
    const year = Number(date.substring(6, 10));
    const now = new Date();
    if(year > now.getFullYear()) return false;
    if(year < now.getFullYear()) return true;
    if(month > now.getMonth()+1) return false;
    if(month < now.getMonth()+1) return true;
    if(day > now.getDate()) return false;
    if(day < now.getDate()) return true;
    return false;
}

function onGetUsersResult(response){
    let table = response.users;
    let listUsers = document.getElementById("result-list");
    for(let i = 0; i < table.length; i++){
        let divPrincipal = document.createElement("div");
        divPrincipal.setAttribute("class","container-fluid border ltr ml-2 result-item");
        divPrincipal.setAttribute("id","user-search-result-"+i);
        divPrincipal.value = table[i].idUser;

        let div1 = document.createElement("div");
        div1.setAttribute("class","row")
        let div1a = document.createElement("div");
        div1a.setAttribute("class","container-fluid col-9");
        let span1a = document.createElement("span");
        span1a.innerHTML = "Lastname: " +table[i].lastName;
        span1a.setAttribute("class","font-weight-bold");
        let div1b = document.createElement("div");
        div1b.setAttribute("class","container-fluid col-3 text-right");
        let span1b = document.createElement("span");
        span1b.innerHTML = "#"+table[i].idUser;
        span1b.setAttribute("class","font-weight-bold");

        let div2 = document.createElement("div");
        div2.setAttribute("class","row");
        let div2a = document.createElement("div");
        div2a.setAttribute("class","container-fluid")
        let span2a = document.createElement("span");
        span2a.setAttribute("class","font-weight-bold");
        span2a.innerHTML = "Firstname: "+ table[i].firstName;
        let div3 = document.createElement("div");
        div3.setAttribute("class","row");
        let div3a = document.createElement("div");
        div3a.setAttribute("class","container-fluid");
        let span3a = document.createElement("span");
        span3a.setAttribute("class","font-weight-bold");
        span3a.innerHTML = "Email: "+table[i].email;

        let div4 = document.createElement("div");
        div4.setAttribute("class","row");
        let div4a = document.createElement("div");
        div4a.setAttribute("class","container-fluid");
        let span4a = document.createElement("span");
        span4a.setAttribute("class","font-weight-bold");
        span4a.innerHTML = "Username: "+table[i].username;


        divPrincipal.appendChild(div1);
        div1.appendChild(div1a)
        div1.appendChild(div1b);

        div1a.appendChild(span1a);
        div1b.appendChild(span1b);

        divPrincipal.appendChild(div2);
        div2.appendChild(div2a);
        div2a.appendChild(span2a);

        divPrincipal.appendChild(div3);
        div3.appendChild(div3a);
        div3a.appendChild(span3a);

        divPrincipal.appendChild(div4);
        div4.appendChild(div4a);
        div4a.appendChild(span4a);

        listUsers.appendChild(divPrincipal);

        $("#user-search-result-"+i).click(()=> {
            let dateRegistration = table[i].registrationDate;

            let isWorker;
            if(table[i].worker){
                isWorker = "YES";
            } else{
                isWorker ="NO";
            }
            selectResult($("#user-search-result-"+i));
            showDetails("#details-user");
            $("#details-user-id").text("ID: " + $("#user-search-result-"+i).val());
            $("#details-user-lastname").text("Lastname: "+table[i].lastName);
            $("#details-user-firstname").text("Firstname: "+ table[i].firstName);
            $("#details-user-worker").text("Worker: "+ isWorker);
            $("#details-user-email").text("Email: "+ table[i].email);
            $("#details-user-registration-date").text("Registration date: "+ JSON.stringify(dateRegistration.dayOfMonth)+"-"+JSON.stringify(dateRegistration.monthValue) +"-" +JSON.stringify(dateRegistration.year));
        });

    }
}

function onGetClientsResult(response){
    let table = response.clients;
    let listClients = document.getElementById("result-list");
    for(let i = 0; i < table.length; i++){
        let divPrincipal = document.createElement("div");
        divPrincipal.setAttribute("class","container-fluid border ltr ml-2 result-item");
        divPrincipal.setAttribute("id","client-search-result-"+i);
        divPrincipal.value = table[i].idClient;

        let div1 = document.createElement("div");
        div1.setAttribute("class","row")
        let div1a = document.createElement("div");
        div1a.setAttribute("class","container-fluid col-9");
        let span1a = document.createElement("span");
        span1a.innerHTML = "Lastname: " +table[i].lastName;
        span1a.setAttribute("class","font-weight-bold");
        let div1b = document.createElement("div");
        div1b.setAttribute("class","container-fluid col-3 text-right");
        let span1b = document.createElement("span");
        span1b.innerHTML = "#"+table[i].idClient;
        span1b.setAttribute("class","font-weight-bold");

        let div2 = document.createElement("div");
        div2.setAttribute("class","row");
        let div2a = document.createElement("div");
        div2a.setAttribute("class","container-fluid")
        let span2a = document.createElement("span");
        span2a.setAttribute("class","font-weight-bold");
        span2a.innerHTML = "Firstname: "+ table[i].firstName;
        let div3 = document.createElement("div");
        div3.setAttribute("class","row");
        let div3a = document.createElement("div");
        div3a.setAttribute("class","container-fluid");
        let span3a = document.createElement("span");
        span3a.setAttribute("class","font-weight-bold");
        span3a.innerHTML = "Email: "+table[i].email;
        divPrincipal.appendChild(div1);
        div1.appendChild(div1a)
        div1.appendChild(div1b);

        div1a.appendChild(span1a);
        div1b.appendChild(span1b);

        divPrincipal.appendChild(div2);
        div2.appendChild(div2a);
        div2a.appendChild(span2a);

        divPrincipal.appendChild(div3);
        div3.appendChild(div3a);
        div3a.appendChild(span3a);

        listClients.appendChild(divPrincipal);

        $("#client-search-result-"+i).click(()=> {
            selectResult($("#client-search-result-"+i));
            showDetails("#details-client");
            $("#details-client-id").text("ID: " + $("#client-search-result-"+i).val());
            $("#details-client-lastname").text("Lastname: "+table[i].lastName);
            $("#details-client-firstname").text("Firstname: "+ table[i].firstName);
            $("#details-client-city").text("City: "+ table[i].city);
            $("#details-client-email").text("Email: "+ table[i].email);
            $("#details-client-phone-number").text("Phone Number: "+table[i].phoneNumber);
            let url = "quotes?idClient=" + table[i].idClient +"&request=getQuotesByClient";
            $("#client-quotes-list").text("");

            getData(url,localStorage.getItem("token"),onGetQuotesClient);
        });
    }
}
function onGetClientsAutocomplete(response) {
    let  table = response.clients;
    let clientsNamesLists = new Array();
    let clientPostCodeLists = new Array();
    let clientCityList = new Array();
    for(let i = 0; i < table.length; i++) {
        if (!clientsNamesLists.includes(table[i].lastName)){
            clientsNamesLists.push(table[i].lastName);
        }
        if (!clientPostCodeLists.includes(table[i].postCode)){
            clientPostCodeLists.push(table[i].postCode);
        }
        if (!clientCityList.includes(table[i].city)){
            clientCityList.push(table[i].city);
        }
    }
    autocomplete(document.getElementById("search-quote-client-name"), clientsNamesLists);
    autocomplete(document.getElementById("search-client-name"), clientsNamesLists);
    autocomplete(document.getElementById("search-client-post-code"), clientPostCodeLists);
    autocomplete(document.getElementById("search-client-city"), clientCityList);
}
function onGetUsersAutocomplete(response) {
    let  table = response.users;
    let usersNamesList = new Array();
    let usersCityList = new Array();
    for(let i = 0; i < table.length; i++) {
        if (!usersNamesList.includes(table[i].lastName)) {
            usersNamesList.push(table[i].lastName);
        }
        if (!usersCityList.includes(table[i].city)) {
            usersCityList.push(table[i].city);
        }
    }
    autocomplete(document.getElementById("search-user-city"), usersCityList);
    autocomplete(document.getElementById("search-user-name"), usersNamesList);
}

function onGetQuotesClient(response){
    let table = response.quotes;
    hideAllStateBtn();

    let listQuotes = document.getElementById("client-quotes-list");
    let divPrincipal = document.createElement("div");
    divPrincipal.setAttribute("class","container-fluid result-item");
    divPrincipal.setAttribute("id","clients-quote")
    let divRowPrincipal = document.createElement("div");
    divRowPrincipal.setAttribute("class","row");

    listQuotes.appendChild(divPrincipal);
    divPrincipal.appendChild(divRowPrincipal);
    for(let i = 0; i < table.length; i++){


        let divQuotePrincipal = document.createElement("div");
        divQuotePrincipal.setAttribute("class","col-4 border ltr result-item");
        divQuotePrincipal.setAttribute("id","client-quote-detail-"+i);
        let divRowQuote = document.createElement("div");
        divRowQuote.setAttribute("class","row");
        divQuotePrincipal.appendChild(divRowQuote);

        let div1 = document.createElement("div");
        div1.setAttribute("class","container-fluid col-3 px-0");
        let picFav = document.createElement("img");
        if(JSON.parse(table[i].favoritePicture) !== null){
            picFav.setAttribute("src",JSON.parse(table[i].favoritePicture).source);
            picFav.setAttribute("style","width: 130px; height: 80px");
        }
        else{
            picFav.setAttribute("src","");
        }
        div1.appendChild(picFav);
        divRowQuote.appendChild(div1);

        let div2 = document.createElement("div");
        div2.setAttribute("class","container-fluid col-9");

        let div2a = document.createElement("div");
        div2a.setAttribute("class","row my-3");
        let div2a1 = document.createElement("div")
        div2a1.setAttribute("class","container-fluid col-8");
        div2a.appendChild(div2a1);
        let spanDiv2a1 = document.createElement("span");
        spanDiv2a1.setAttribute("class","font-weight-bold");
        spanDiv2a1.innerHTML = "State: " +table[i].state;
        div2a1.appendChild(spanDiv2a1);

        let div2a2 = document.createElement("div");
        div2a2.setAttribute("class","container-fluid col-4 text-right");
        div2a.appendChild(div2a2);
        let spanDiv2a2 = document.createElement("span");
        spanDiv2a2.setAttribute("class","font-weight-bold");
        spanDiv2a2.innerHTML = "#"+table[i].idQuote;
        div2a2.appendChild(spanDiv2a2);

        let div2b = document.createElement("div");
        div2b.setAttribute("class","row my-3");

        let div2b1 = document.createElement("div");
        div2b1.setAttribute("class","container-fluid col-8");
        div2b.appendChild(div2b1);
        let spanDiv2b1 = document.createElement("span");
        spanDiv2b1.setAttribute("class","font-weight-bold");
        spanDiv2b1.innerHTML = table[i].dateQuote;
        div2b1.appendChild(spanDiv2b1);

        let div2b2 = document.createElement("div");
        div2b2.setAttribute("class","container-fluid col-4 text-right");
        div2b.appendChild(div2b2);
        let spanDiv2b2 = document.createElement("span");
        spanDiv2b2.innerHTML=table[i].fullAmount+"€";
        div2b2.appendChild(spanDiv2b2);
        let div2c = document.createElement("div");
        div2c.setAttribute("class","row my-3");
        let spanDiv2c1 = document.createElement("span");
        spanDiv2c1.setAttribute("class","font-weight-bold");


        let typesQuotesString ="Types :";
        let typesQuotes = JSON.parse(table[i].listType);
        
        for(let i = 0; i < typesQuotes.length ; i++){
            typesQuotesString += typesQuotes[i]+"</br> ";
        }

        spanDiv2c1.innerHTML = typesQuotesString.fontsize(2) ;
        div2c.appendChild(spanDiv2c1);

        div2.appendChild(div2a);
        div2.appendChild(div2b);
        div2.appendChild(div2c);

        divRowQuote.appendChild(div2);
        divRowPrincipal.appendChild(divQuotePrincipal);

        $("#client-quote-detail-"+i).click(()=> {
            position = 0;
            picturesAfterAmenagement =[];
            getData("quotes?idQuote="+table[i].idQuote+"&request=searchQuotebyId",localStorage.getItem("token"),onGetPostQuotes);
            showDetails("#details-quote");
        });
    }

}

function encodeImagetoBase64(file) {

    var reader = new FileReader();
    reader.onload = function () {

        var tmp = {
            idPictureHtml : null,
            picture : reader.result,
            typeProject : null,
            isChecked : false
        }
        let divPrinc =document.createElement("div");
        divPrinc.setAttribute("id","div-principal"+position)
        let div = document.createElement("div");

        let imgPreview = document.createElement("img");
        imgPreview.style.maxWidth = "100px";
        imgPreview.style.maxHeight = "100px";
        imgPreview.setAttribute("src", reader.result);
        div.appendChild(imgPreview);
        tmp.idPictureHtml = "picture-input-id"+position;
        position++;
        picturesAfterAmenagement.push(tmp);
        divPrinc.appendChild(imgPreview);
        $("#pictures-after-previews").append(divPrinc);
        getData("types",localStorage.getItem("token"),onGetTypesSelect);
    };
    reader.readAsDataURL(file);

}
function autocomplete(inp, arr) {
    var currentFocus;
    inp.addEventListener("input", function (e) {
        var a, b, i, val = this.value;
        closeAllLists();
        if (!val) {
            return false;
        }
        currentFocus = -1;
        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        this.parentNode.appendChild(a);
        for (i = 0; i < arr.length; i++) {
            if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                b = document.createElement("DIV");
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length);
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                b.addEventListener("click", function (e) {
                    inp.value = this.getElementsByTagName("input")[0].value;
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });

    inp.addEventListener("keydown", function (e) {
        var x = document.getElementById(this.id + "autocomplete-list");
        if (x) x = x.getElementsByTagName("div");
        if (e.keyCode == 40) {
            currentFocus++;
            addActive(x);
        } else if (e.keyCode == 38) {
            currentFocus--;
            addActive(x);
        } else if (e.keyCode == 13) {
            e.preventDefault();
            if (currentFocus > -1) {
                if (x) x[currentFocus].click();
            }
        }
    });

    function addActive(x) {
        if (!x) return false;
        removeActive(x);
        if (currentFocus >= x.length) currentFocus = 0;
        if (currentFocus < 0) currentFocus = (x.length - 1);
        x[currentFocus].classList.add("autocomplete-active");
    }

    function removeActive(x) {
        for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }

    function closeAllLists(elmnt) {
        var x = document.getElementsByClassName("autocomplete-items");
        for (var i = 0; i < x.length; i++) {
            if (elmnt != x[i] && elmnt != inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }
    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });
}
export{
    onGetClientsResult,
    onGetClientsAutocomplete,
    onGetUsersAutocomplete
}
