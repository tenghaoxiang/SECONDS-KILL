var host = "http://127.0.0.1:8080"
var global_login_url = ""  //全局扫描登录

//下单
function save_order(id) {
    var token = $.cookie("token");
    console.info(token);
    // if (!token || token == "") {
    //     //去登录
    //     window.location.href = global_login_url;
    //
    // }
    //下单接口
    // var url = host + "/stock/decrease?token=" + token + "&id=" + id;
    // $("#pay_img").attr("src", url);
    $.ajax({
        type: 'get',
        url: host + "/second-kill/start?token=" + token + "&id=" + id,
        dataType: 'json',
        success: function (res) {
            // console.info(res.message)
            $("#myModalLabel").html(res.message)
        }
    })


}


$(function () {


    //获取视频列表
    function get_list() {

        $.ajax({
            type: 'get',
            url: host + "/stock/list?size=30&page=1",
            dataType: 'json',
            success: function (res) {
                var data = res.data;
                // console.info(data.length)
                for (var i = 0; i < data.length; i++) {
                    var stock = data[i];
                    var price = stock.price;
                    var sale = stock.sale;
                    var count = stock.count;

                    var template = "<div class='col-sm-6 col-md-3'><div class='thumbnail'>" +
                        "<img src='" + stock.coverImg + "' alt='通用的占位符缩略图'>" +
                        "<div class='caption'><h3>" + stock.name + "</h3><p>价格:" + price + "元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总销量:" + sale + "件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库存:"+count+"件</p>" +
                        "<p><a href='' onclick='save_order(" + stock.id + ")' data-toggle='modal' data-target='#myModal' class='btn btn-primary' role='button'>立刻秒杀</a></p></div></div></div>"
                    $(".row").append(template);
                }
            }
        })
    }


    //获取微信扫描地址
    function get_wechat_login() {
        //获取当前页面地址
        var current_page = window.location.href;
        $.ajax({
            type: 'get',
            url: host + "/login/url?state=" + current_page,
            dataType: 'json',
            success: function (res) {
                //console.info(res.data)
                $("#login").attr("href", res.data);
                global_login_url = res.data;
            }
        })

    }

    //获取url上的参数
    function get_params() {
        var url = window.location.search;//获取?后面的字符串
        var obj = new Object();
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            //console.log(str);
            strs = str.split("&")
            for (var i = 0; i < strs.length; i++) {
                obj[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
            }

        }
//		console.log(obj)
        return obj;
    }


    //设置头像和昵称
    function set_user_info() {
        var user_info = get_params();
        var head_img = $.cookie('head_img')
        var name = $.cookie('name')

        if (JSON.stringify(user_info) != '{}') {
            //对象不为空
            var name = user_info['name'];
            var head_img = user_info['head_img']
            var token = user_info['token']

            $("#login").html(name)
            $("#head_img").attr("src", head_img);
            $.cookie('token', token, {expires: 7, path: '/'})
            $.cookie('head_img', head_img, {expires: 7, path: '/'})
            $.cookie('name', name, {expires: 7, path: '/'})

        } else if (name && name != "") {

            $("#login").html(name)
            $("#head_img").attr("src", head_img);
        }

    }


    get_list();
    get_wechat_login();
    get_params();
    set_user_info();

})