import {getData, postData, deleteData, updateData} from "./utilsAPI.js";
import {showView, showDetails,selectResult} from "./utils.js";
$(document).ready(function () {
    
    $("#search-quote-dashboard-client").submit(e=>{
        e.preventDefault();
        $("#result-list-client").text("");
        let quoteDate = $("#client-date-quote").val();
        let minPrice = $("#client-quote-min-price").val();
        let maxPrice = $("#client-quote-max-price").val();
        let user = JSON.parse(localStorage.getItem("user"));
        
        let url = "quotes?idClient="+ user.client+"&quoteDate="+quoteDate+"&minPrice="+minPrice+"&maxPrice="+maxPrice+"&listType="+JSON.stringify(getCheckedType())+"&request=searchQuoteFiltredBy";
        console.log(url);
        getData(url,localStorage.getItem("token"),onGetClientQuotes);
    });
    
});

function onGetTypesClient(response) {
    let tableau = response.types;
    let menuType = document.getElementById("menu-types-quote-client");
    
    for (let i = 0; i < tableau.length; i++) {
        let type = tableau[i];
        
        let li = document.createElement("li");
        li.setAttribute("class","mx-1");
        
        let span = document.createElement("span");
        
        let input = document.createElement("input");
        input.setAttribute("type","checkbox");
        input.setAttribute("class","mr-1");
        input.setAttribute("value",type.idProjectType);
        input.setAttribute("name","type-id-list-client");
        
        span.appendChild(input);
        span.append(type.projectTypeName)
        li.appendChild(span);
        
        menuType.appendChild(li);
    }
}

function getCheckedType() {
    let listTypeChecked = new Array();    
    let items = document.getElementsByName("type-id");
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == "checkbox" && items[i].checked == true) {
            listTypeChecked.push(items[i].value);
        }
    }
    return listTypeChecked;
}



function onGetClientQuotes(response){
    let quotes = response.quotes;
    let listQuotes = document.getElementById("result-list-client");
    
    for(let i = 0; i < quotes.length; i++){
        let div1 = document.createElement("div");
        div1.setAttribute("id","client-quote-"+i);
        div1.setAttribute("class","container-fluid border ltr result-item");
        let div2 = document.createElement("div");
        div2.setAttribute("class","row "); 
        
        let div3 = document.createElement("div");
        div3.setAttribute("class","container-fluid col-3 px-0"); 
        
        let imgFav = document.createElement("img");
        imgFav.setAttribute("class","w-100");
        let favoritePicture = JSON.parse(quotes[i].favoritePicture);
        
        
        if(quotes[i].favoritePicture !== null){
            imgFav.setAttribute("src",favoritePicture.source);
        } else{
            imgFav.setAttribute("src","");
        }
        
        imgFav.setAttribute("id","imgfav"+i);
        div3.appendChild(imgFav);
        let div4 = document.createElement("div");
        div4.setAttribute("class","container-fluid col-9"); 
        
        let div5 = document.createElement("div");
        div5.setAttribute("class","row my-3");
        
        let div6 = document.createElement("div");
        div6.setAttribute("class","container-fluid col-8");
        
        let span1 = document.createElement("span");
        span1.setAttribute("class","font-weight-bold");
        span1.setAttribute("id","quote-state-"+quotes[i].idQuote)
        span1.innerHTML = "State :"+quotes[i].state;
        
        let div7 = document.createElement("div");
        div7.setAttribute("class","container-fluid col-4 text-right");
        
        let span2 = document.createElement("span");
        span2.setAttribute("class","font-weight-bold");
        span2.setAttribute("id","quote-id-"+i)
        span2.value = quotes[i].idQuote;
        span2.innerHTML = "#"+quotes[i].idQuote;
        
        let div8 = document.createElement("div");
        div8.setAttribute("class","row my-3");
        
        let div8b = document.createElement("div");
        div8b.setAttribute("class","container-fluid col-8");
        
        let span3 = document.createElement("span");
        span3.setAttribute("class","font-weight-bold");
        span3.innerHTML = quotes[i].dateQuote;
        
        let div8c = document.createElement("div");
        div8c.setAttribute("class","container-fluid col-4 text-right");
        
        let span4 = document.createElement("span");
        span4.setAttribute("class","font-weight-bold");
        span4.innerHTML = quotes[i].fullAmount+'€';

        let div9 = document.createElement("div");
        div9.setAttribute("class","row");
        let div9b = document.createElement("div");
        div9b.setAttribute("class","container-fluid");
        let span5 = document.createElement("span");
        span5.setAttribute("class","font-weight-bold");
        let typesQuotesString ="Types: ".fontsize(2);
        let typesQuotes = JSON.parse(quotes[i].listType);
        
        
        for(let i = 0; i < typesQuotes.length ; i++){
            typesQuotesString += typesQuotes[i]+"</br> ";
        }
        span5.innerHTML = typesQuotesString.fontsize(2);
        div9.appendChild(div9b);
        div9b.appendChild(span5);
        
        
        div1.appendChild(div2);
        div2.appendChild(div3);
        div2.appendChild(div4);
        
        div4.appendChild(div5);
        div4.appendChild(div8);
        div4.appendChild(div9)
        
        div5.appendChild(div6);
        div6.appendChild(span1);
        
        div5.appendChild(div7);
        div7.appendChild(span2);
        
        div8.appendChild(div8b);
        div8b.appendChild(span3);
        
        div8.appendChild(div8c);
        div8c.appendChild(span4);
        
        listQuotes.appendChild(div1);  
        
        $("#client-quote-"+i).click(()=> {
            selectResult($("#client-quote-"+i));
            getData("quotes?idQuote="+quotes[i].idQuote+"&request=searchQuotebyId",localStorage.getItem("token"),onPostQuoteClient);
        });
        
    }
    
}

function onPostQuoteClient(response){

    
    $("#quote-state-"+response.quotes[0].idQuote).text("State :" +response.quotes[0].state);
    showDetails("#details-client-quote");
    $("#details-client-quote-pictures-before").text("");
    $("#details-client-quote-pictures-after").text("");
    $("#details-client-quote-id").text("ID: " + response.quotes[0].idQuote);
    $("#details-client-quote-date").text("Date: "+ response.quotes[0].dateQuote);
    $("#details-client-quote-duration").text("Work length: "+ response.quotes[0].workPeriod);
    $("#details-client-quote-total").text("Total: " + response.quotes[0].fullAmount +"€");
    $("#details-client-quote-state").text("State: "+response.quotes[0].state);
    
    if(response.quotes[0].dateStartWork !== null){
        $('#details-client-quote-date-start').text("Date Start Work:"+response.quotes[0].dateStartWork);
    } else {
        $('#details-client-quote-date-start').text("Date Start Work: No date");
    }
    
    let tablePicturesBefore = JSON.parse(response.quotes[0].picturesBefore);
    let tablePicturesAfter = JSON.parse(response.quotes[0].picturesAfter);
    let favoritePicture = JSON.parse(response.quotes[0].favoritePicture);

    if(response.quotes[0].favoritePicture !== null){
        $("#details-client-quote-favorite-picture").attr("src",favoritePicture.source);
    } else{
        $("#details-client-quote-favorite-picture").attr("src","");
    }


    $("#details-client-quote-type").html("");
    let htmlType ="Quote types: <br>";

    let tableTypes = JSON.parse(response.quotes[0].listType);
    for (let i = 0 ; i < tableTypes.length ; i++){
        htmlType += tableTypes[i]+"<br>"
    }
    $("#details-client-quote-type").append(htmlType);
    
    
    let listPicturesBefore = document.getElementById("details-client-quote-pictures-before");
    for(let j = 0; j <tablePicturesBefore.length; j++){
        let img = document.createElement("img");
        img.setAttribute("src",tablePicturesBefore[j].source);
        img.setAttribute("style","width: 200px; height: 130px");
        listPicturesBefore.appendChild(img);
    }
    
    let listPicturesAfter = document.getElementById("details-client-quote-pictures-after");
    for(let j = 0; j <tablePicturesAfter.length; j++){
        let img = document.createElement("img");
        img.setAttribute("src",tablePicturesAfter[j].source);
        img.setAttribute("style","width: 200px; height: 130px");
        listPicturesAfter.appendChild(img);
    }
}

export {
    onGetClientQuotes,
    onGetTypesClient
}