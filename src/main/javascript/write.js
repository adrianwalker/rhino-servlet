function write(response, json) {
  response.setContentType("text/html");
  out = response.getWriter();
  json = JSON.stringify(json);
  out.println(json);
}
