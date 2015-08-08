(function($) {

    getSession();

    var zooEnv = "offline";

    $("#zoo-env-list").on('click', 'li a', function(e) {
        zooEnv = $(this).attr('rel');
        $("#envChoice").text($(this).text() + "-服务节点树:");
        $("#pageHtmlInfo").attr("src", "");
        showZooTree();
    });

    showZooTree();

    var setting = {
        callback: {
            beforeClick: beforeClick,
            onClick: onClick
        }
    };

    var log, className = "dark";

    function beforeClick(treeId, treeNode, clickFlag) {
        // className = (className === "dark" ? "" : "dark");
        // showLog("[ " + getTime() + " beforeClick ]&nbsp;&nbsp;" + treeNode.name);
        return (treeNode.leaf == true);
    }

    function onClick(event, treeId, treeNode, clickFlag) {
        // showLog("[ " + getTime() + " onClick ]&nbsp;&nbsp;clickFlag = " + clickFlag + " (" + (clickFlag === 1 ? "single selected" : (clickFlag === 0 ? "<b>cancel selected</b>" : "<b>multi selected</b>")) + ")");
        // 必须是IP:PORT才可以点击查看
        if (treeNode.name.indexOf(":") > 0) {
            servieUrl = "http://" + treeNode.name + "/service_api";
            url = "navi/page/getPageHtml?url=" + servieUrl;
            $.ajax({
                type: "GET",
                url: url,
                dataType: "json"
            }).done(function(result) {
                $("#pageHtmlInfo").show();
                if (result.status == 0) {
                    if (result.status == 0) {
                        if (zooEnv == "online") {
                            $("#mainlist_error").text("点击左侧服务节点树叶子节点可以查看接口定义（由于线上JPaaS地址外网不可连接，故只列出服务信息）, url=" + servieUrl);
                            //$("#pageHtmlInfo").attr("src", servieUrl);
                            document.getElementById('pageHtmlInfo').src = "data:text/html;charset=utf-8," + escape(result.data.html);
                        } else {
                            $("#mainlist_error").text("点击左侧服务节点树叶子节点可以查看接口定义, url=" + servieUrl);
                            $("#pageHtmlInfo").attr("src", servieUrl);
                        }
                    } else {
                        $("#mainlist_error").text("无法打开页面, url=" + servieUrl);
                    }
                } else {
                    console.log("status != 0, " + result);
                    $("#mainlist_error").text("连接拒绝访问, url=" + servieUrl);
                }
            }).fail(function() {
                console.log(result);
                $("#mainlist_error").text("尝试获取页面错误, url=" + servieUrl);
            });
        }
    }

    function showLog(str) {
        if (!log) log = $("#log");
        log.append("<li class='" + className + "'>" + str + "</li>");
        if (log.children("li").length > 8) {
            log.get(0).removeChild(log.children("li")[0]);
        }
    }

    function getTime() {
        var now = new Date(),
            h = now.getHours(),
            m = now.getMinutes(),
            s = now.getSeconds();
        return (h + ":" + m + ":" + s);
    }

    function showZooTree() {
        $("#loading").show();
        $("#mainlist_error").text("服务节点树加载中...");
        url = "navi/zoo/getZooTree?env=" + zooEnv;
        $.ajax({
            type: "GET",
            url: url,
            dataType: "json"
        }).done(function(result) {
            if (result.status == 0) {
                $.fn.zTree.init($("#treeDemo"), setting, result.data.zooNodes);
                $("#mainlist_error").text("点击左侧服务节点树叶子节点可以查看接口定义");
            } else {
                console.log("status != 0, " + result);
                $("#mainlist_error").text(result.statusInfo.global);
            }
            $("#loading").hide();
        }).fail(function() {
            $("#loading").hide();
            $("#mainlist_error").text("加载Zookeeper节点失败");
        });
    }

})(jQuery);