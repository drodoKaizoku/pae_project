function postData(url, data,token,onPost, onError) {
  $(".notification").hide();
  $.ajax({
    type: "post",
    url: url,
    headers : {Authorization : token},
    data: data,
    success: onPost,
    error: onError
  });
}

function getData(url = "", token, onGet, onError) {
  $(".notification").hide();
  $.ajax({
    type: "get",
    url: url,
    headers : {Authorization : token},
    success: onGet,
    error: onError
  });
}

function deleteData(url = "", token, data, onDelete, onError) {
  $(".notification").hide();
  $.ajax({
    type: "delete",
    url: url,
    data: data,
    success: onDelete,
    error: onError
  });

}

function updateData(url = "", token, data = {}, onPut, onError) {
  $(".notification").hide();
  $.ajax({
    type: "put",
    headers : {'Authorization' : token},
    url: url,
    data: data,
    success: onPut,
    error: onError
  });
}

export {
  getData,
  postData,
  deleteData,
  updateData
}
