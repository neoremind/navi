(function($) {

    // 登出
    $("#signout").on("click", function() {
        $.ajax({
            type: "GET",
            url: "/userLogout",
            dataType: "json"
        }).done(function(result) {
            if (result.status == 0) {
                VISITOR = {};
                getSession();
            }
        });
    });

})(jQuery);