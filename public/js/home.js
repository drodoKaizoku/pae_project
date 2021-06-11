import {getData} from "./utilsAPI.js";


let types;


function onGetMenuTypes(response) {
    types = response.types;
    let menu = document.getElementById("types-menu");
    menu.innerHTML = "";
    let allTypes = document.createElement("button");
    allTypes.setAttribute("class", "dropdown-item");
    allTypes.setAttribute("type", "button");
    allTypes.append("All Types");
    menu.appendChild(allTypes);
    allTypes.addEventListener("click", function () {
        getData("pictures?request=randomPictures", localStorage.getItem("token"), onGetPictures);
        document.getElementById("dropdownMenuButton").innerText = "Project Types";
    });
    for (let i = 0; i < types.length; i++) {
        let type = types[i];
        let button = document.createElement("button");
        button.setAttribute("class", "dropdown-item");
        button.setAttribute("type", "button");
        button.setAttribute("value", type.idProjectType);
        button.append(type.projectTypeName);
        button.addEventListener("click", function () {
            
            getData("pictures?request=picturesOfAType&type=" + type.idProjectType, localStorage.getItem("token"), onGetPictures);
            document.getElementById("dropdownMenuButton").innerText = type.projectTypeName;

        });
        menu.appendChild(button);
    }
}


function onGetPictures(response) {
    $("#carousel").empty();
    let pictures = response.pictures;
    if (pictures.length === 0) {
        $("#no-pictures-message").html("Pictures of this type are not yet available");
        $("#no-pictures-message").show();
        
    } else {
        let carousel = document.getElementById("carousel");
        let indicators = document.createElement("ol");
        indicators.setAttribute("class", "carousel-indicators");
        for (let i = 0; i < pictures.length; i++) {
            let indicator = document.createElement("li");
            indicator.setAttribute("data-target", "#carousel");
            indicator.setAttribute("data-slide-to", i);
            if (i === 0) {
                indicator.setAttribute("class", "active");
            } else {
                indicator.setAttribute("class", "none");
            }
            indicators.appendChild(indicator);
        }
        carousel.appendChild(indicators);

        let inner = document.createElement("div");
        inner.setAttribute("class", "carousel-inner");
        for (let i = 0; i < pictures.length; i++) {
            let picture = pictures[i];
            let carouselItem = document.createElement("div");
            if (i === 0) {
                carouselItem.setAttribute("class", "carousel-item active");
            } else {
                carouselItem.setAttribute("class", "carousel-item");
            }
            let image = document.createElement("img");
            image.setAttribute("class", "d-block w-100");
            image.setAttribute("src", picture.source);
            image.setAttribute("alt", "project picture");
            let caption = document.createElement("div");
            caption.setAttribute("class", "carousel-caption d-none d-md-block");
            let type = document.createElement("h5");
            for (let i = 0; i < types.length; i++) {
                if (types[i].idProjectType === picture.projectType) {
                    type.append(types[i].projectTypeName);
                }
            }
            caption.appendChild(type);
            carouselItem.appendChild(image);
            carouselItem.appendChild(caption);
            inner.appendChild(carouselItem);
        }
        carousel.appendChild(inner);
        let previous = document.createElement("a");
        previous.setAttribute("class", "carousel-control-prev");
        previous.setAttribute("href", "#carousel");
        previous.setAttribute("role", "button");
        previous.setAttribute("data-slide", "prev");
        let previousSpan = document.createElement("span");
        previousSpan.setAttribute("class", "carousel-control-prev-icon");
        previousSpan.setAttribute("aria-hidden", "true");
        let previousSpan2 = document.createElement("span");
        previousSpan2.setAttribute("class", "sr-only");
        previousSpan2.append("Previous");
        previous.appendChild(previousSpan);
        previous.appendChild(previousSpan2);

        let next = document.createElement("a");
        next.setAttribute("class", "carousel-control-next");
        next.setAttribute("href", "#carousel");
        next.setAttribute("role", "button");
        next.setAttribute("data-slide", "next");
        let nextSpan = document.createElement("span");
        nextSpan.setAttribute("class", "carousel-control-next-icon");
        nextSpan.setAttribute("aria-hidden", "true");
        let nextSpan2 = document.createElement("span");
        nextSpan2.setAttribute("class", "sr-only");
        nextSpan2.append("Next");
        next.appendChild(nextSpan);
        next.appendChild(nextSpan2);

        carousel.appendChild(previous);
        carousel.appendChild(next);

    }
}

$(document).ready(function () {
    getData("types", localStorage.getItem("token"), onGetMenuTypes);
    getData("pictures?request=randomPictures", localStorage.getItem("token"), onGetPictures);
});

export {
    onGetMenuTypes,
    onGetPictures
}
