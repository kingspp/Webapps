
var loginPageController = {
    init: function(param) {
        loginPageController.wireEvents();

    },
    wireEvents: function() {
        $('#sign-form').submit(function(){
            var url = "/login";
            var data = {};
            data.name=$('#inputUserName').val();
            data.password=$('#inputPassword').val();
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function(data)
                {


                    data = JSON.parse(data);

                    if(data.result=="Login Success"){
                        window.location.href="/index.html";
                    }else alert("Login Failed!!!");
                }
            });
            return false;
        })

    }

    // Reactive methods from page events
    // Callbacks, etc
};
$(document).ready(function() {
    loginPageController.init();
});