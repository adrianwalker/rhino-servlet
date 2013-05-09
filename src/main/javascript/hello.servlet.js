servlet.load("write.js");

function doGet(request, response) {
  doProcess(request, response);
}

function doPost(request, response) {
  doProcess(request, response);
}

function doProcess(request, response) {

  var name = request.getParameter("name");
  var json = {
    message: "Hello " + name
  };

  write(response, json);
}
