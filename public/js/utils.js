function showView(idView){
    $(".notification").hide();
    $(".view").hide();
    $(".details").hide();
    let tablesBody = $("tbody");
    let selects = $("select");
    let resultLists = $(".result-list");
    
    for(let i = 0 ; i < tablesBody.length ;i++){
        tablesBody[i].innerHTML = "";
    }
    for(let i = 0 ; i < selects.length ;i++){
        selects[i].innerHTML = "";
    }
    for(let i = 0 ; i < resultLists.length ;i++){
        resultLists[i].innerHTML = "";
    }
    $("#types-menu").innerHTML ="";
    $(idView).show();

    
}

function showDetails(idDetail){
    $(".details").hide();
    $(idDetail).show();
}

function showStateBtn(idBtn){
    $(".btn-state").hide();
    $(idBtn).show();
}

function hideAllStateBtn(){
    $(".btn-state").hide();
    $("#btn-close-quote").hide();
    $("#btn-cancel-quote").hide();
}

function selectResult(idResult){
    $(".result-item").removeClass("bg-light");
    $(idResult).addClass("bg-light");
}


export {
    showView,
    showDetails,
    showStateBtn,
    hideAllStateBtn,
    selectResult
}

