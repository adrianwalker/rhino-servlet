$(document).ready(function() {
  $('#ok').click(function() {

    var name = $('#name').val();

    $.ajax({
      type: "POST",
      url: "hello.servlet.js",
      data: {name: name}
    }).done(function(json) {
      json = JSON.parse(json);
      $('#message').append(json.message).append('<br/>');
    });
  });
});
