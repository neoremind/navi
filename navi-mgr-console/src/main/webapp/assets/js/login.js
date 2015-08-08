(function ($) {

    //$("#indexMain").attr("href", "/");

    getSession2Redirect();

    // 发送登录请求
    $(".form-submit").on("click", function () {

        var me = this;
        var username = $("#name").val();
        var pwd = MD5($("#password").val());
        var remember = $("#inlineCheckbox2").is(':checked') ? true : false;

        // 验证
        if (username.length <= 0 || !pwd) {
            $("#loginError").show();
            return;
        }

        $.ajax({
            type: "POST",
            url: "/userLogin",
            data: {
                "username": username,
                "password": pwd,
                "remember": remember
            },
            dataType: "json"
        }).done(function (result) {
            if (result.status === 0) {
                window.VISITOR = result.data;
                $("#loginError").hide();
                headShowInit();
                window.location.href = "/main.html";
            } else {
                Util.input.whiteError($("#loginError"), result);
                $("#loginError").show();
            }
        });
    });

})(jQuery);