import {showView} from "./utils.js";
import {getData} from "./utilsAPI.js";
import {onGetMenuTypes, onGetPictures} from "./home.js";

let token;

$(document).ready(function () {
    token = localStorage.getItem("token");

    if (token) {
        let user = JSON.parse(localStorage.getItem("user"));
        $(".offline").hide();
        $(".online").show();
        if (user.worker == true) {
            $(".online-client").hide();
            $(".online-worker").show();
        } else {

            $(".online-worker").hide();
            $(".online-client").show();
        }
    } else {
        $(".online").hide();
        $(".online-worker").hide();
        $(".online-client").hide();
        $(".offline").show();
    }
    showView("#view-home");
    $("#btn-home").on('click', () => {
        showView("#view-home");
        getData("types", localStorage.getItem("token"), onGetMenuTypes);
        document.getElementById("dropdownMenuButton").innerText = "Project Types";
        getData("pictures?request=randomPictures", localStorage.getItem("token"), onGetPictures);
    });

});


