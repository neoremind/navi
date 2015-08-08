var Util = {};
Util.number = {};
Util.lang = {};
Util.string = {};
Util.date = {};
Util.cookie = {};
Util.param = {};
Util.input = {};

/**
 * 对目标数字进行0补齐处理
 *
 * @name Util.number.pad
 * @function
 * @grammar Util.number.pad(source, length)
 * @param {number}
 *            source 需要处理的数字
 * @param {number}
 *            length 需要输出的长度
 *
 * @returns {string} 对目标数字进行0补齐处理后的结果
 */
Util.number.pad = function (source, length) {
    var pre = "", negative = (source < 0), string = String(Math.abs(source));

    if (string.length < length) {
        pre = (new Array(length - string.length + 1)).join('0');
    }

    return (negative ? "-" : "") + pre + string;
};

/**
 * 对目标日期对象进行格式化
 *
 * @name Util.date.format
 * @function
 * @grammar Util.date.format(source, pattern)
 * @param {Date}
 *            source 目标日期对象
 * @param {string}
 *            pattern 日期格式化规则
 * @remark
 *
 * <b>格式表达式，变量含义：</b><br>
 * <br>
 * hh: 带 0 补齐的两位 12 进制时表示<br>
 * h: 不带 0 补齐的 12 进制时表示<br>
 * HH: 带 0 补齐的两位 24 进制时表示<br>
 * H: 不带 0 补齐的 24 进制时表示<br>
 * mm: 带 0 补齐两位分表示<br>
 * m: 不带 0 补齐分表示<br>
 * ss: 带 0 补齐两位秒表示<br>
 * s: 不带 0 补齐秒表示<br>
 * yyyy: 带 0 补齐的四位年表示<br>
 * yy: 带 0 补齐的两位年表示<br>
 * MM: 带 0 补齐的两位月表示<br>
 * M: 不带 0 补齐的月表示<br>
 * dd: 带 0 补齐的两位日表示<br>
 * d: 不带 0 补齐的日表示
 *
 * @returns {string} 格式化后的字符串
 */
Util.date.format = function (source, pattern) {
    if ('string' != typeof pattern) {
        return source.toString();
    }

    function replacer(patternPart, result) {
        pattern = pattern.replace(patternPart, result);
    }

    var pad = Util.number.pad, year = source.getFullYear(), month = source
        .getMonth() + 1, date2 = source.getDate(), hours = source
        .getHours(), minutes = source.getMinutes(), seconds = source
        .getSeconds();

    replacer(/yyyy/g, pad(year, 4));
    replacer(/yy/g, pad(parseInt(year.toString().slice(2), 10), 2));
    replacer(/MM/g, pad(month, 2));
    replacer(/M/g, month);
    replacer(/dd/g, pad(date2, 2));
    replacer(/d/g, date2);

    replacer(/HH/g, pad(hours, 2));
    replacer(/H/g, hours);
    replacer(/hh/g, pad(hours % 12, 2));
    replacer(/h/g, hours % 12);
    replacer(/mm/g, pad(minutes, 2));
    replacer(/m/g, minutes);
    replacer(/ss/g, pad(seconds, 2));
    replacer(/s/g, seconds);

    return pattern;
};

/**
 * 将14位时间格式转化为日期字符串
 *
 * @param {string}
 *            dateNum 日期字符串.
 * @param {string=}
 *            opt_formatType 日期字符串的格式，默认是yyyy-M-d HH:mm:ss.
 * @return {string}
 */
Util.date.dateFormat = function (dateNum, opt_formatType) {
    if ('' === dateNum || '--' === dateNum) {
        return '--';
    }
    var type = opt_formatType || 'yyyy-MM-dd';
    return Util.date.format(Util.date.parseToDate(dateNum), type);
},

    Util.lang.isDate = function (source) {
        return '[object Date]' == Object.prototype.toString.call(
            /** @type {Object} */
            (source));
    };
/**
 * 把20110102235959 14位数字转为DATE对象
 *
 * @param {string}
 *            num 需要转化的数字.
 * @return {Date} 日期对象.
 */
Util.date.parseToDate = function (num) {
    function parse(source) {
        var match = null;
        if (match = /^(\d{4})[-\/]?([01]\d)[-\/]?([0-3]\d)$/.exec(source)) {
            return new Date(parseInt(match[1], 10), parseInt(match[2], 10) - 1,
                parseInt(match[3], 10));
        }
        return null;
    }
    ;

    if (Util.lang.isDate(num)) {
        return num;
    }
    num = num + '';
    var date = parse(num.substring(0, 8));
    if (num.substring(8, 10))
        date.setHours(num.substring(8, 10));
    if (num.substring(10, 12))
        date.setMinutes(num.substring(10, 12));
    if (num.substring(12))
        date.setSeconds(num.substring(12));
    return date;
};
/**
 * 把DATE对象 生成 14位数字，后面是235959，用于ENDTIME
 *
 * @param date
 */
Util.date.parseEndTime = function (date) {
    return Util.date.format(date, 'yyyyMMdd') + '235959';
};

/**
 * 把DATE对象 转为 14位数字 20110102235959
 *
 * @param date
 */
Util.date.dateToNumber = function (date) {
    return Util.date.format(date, 'yyyyMMdd');
};

/**
 * 是否是一个简单的对象
 *
 * @param {*}
 *            source 需要判断的对象.
 * @return {boolean} true是，false不是.
 */
Util.lang.isPlainObject = function (source) {
    return '[object Object]' == Object.prototype.toString.call(
        /** @type {Object} */
        (source));
};

/**
 * 对目标字符串进行格式化
 *
 * @param {string}
 *            source 目标字符串.
 * @param {Object|string}
 *            opts 提供相应数据的对象.
 * @return {string} 格式化后的字符串.
 */
Util.string.format = function (source, opts) {
    source = String(source);

    if ('undefined' != typeof opts) {
        if (Util.lang.isPlainObject(opts)) {
            return source.replace(/\$\{(.+?)\}/g, function (match, key) {
                var replacer = opts[key];
                if ('function' == typeof replacer) {
                    replacer = replacer(key);
                }
                return ('undefined' == typeof replacer ? '' : replacer);
            });
        } else {
            var data = Array.prototype.slice.call(arguments, 1), len = data.length;
            return source.replace(/\{(\d+)\}/g, function (match, index) {
                index = parseInt(index, 10);
                return (index >= len ? match : data[index]);
            });
        }
    }

    return source;
};

/**
 * 验证字符串是否合法的cookie键名
 *
 * @param {string}
 *            source 需要遍历的数组
 * @meta standard
 * @return {boolean} 是否合法的cookie键名
 */
Util.cookie._isValidKey = function (key) {
    // http://www.w3.org/Protocols/rfc2109/rfc2109
    // Syntax: General
    // The two state management headers, Set-Cookie and Cookie, have common
    // syntactic properties involving attribute-value pairs. The following
    // grammar uses the notation, and tokens DIGIT (decimal digits) and
    // token (informally, a sequence of non-special, non-white space
    // characters) from the HTTP/1.1 specification [RFC 2068] to describe
    // their syntax.
    // av-pairs = av-pair *(";" av-pair)
    // av-pair = attr ["=" value] ; optional value
    // attr = token
    // value = word
    // word = token | quoted-string

    // http://www.ietf.org/rfc/rfc2068.txt
    // token = 1*<any CHAR except CTLs or tspecials>
    // CHAR = <any US-ASCII character (octets 0 - 127)>
    // CTL = <any US-ASCII control character
    // (octets 0 - 31) and DEL (127)>
    // tspecials = "(" | ")" | "<" | ">" | "@"
    // | "," | ";" | ":" | "\" | <">
    // | "/" | "[" | "]" | "?" | "="
    // | "{" | "}" | SP | HT
    // SP = <US-ASCII SP, space (32)>
    // HT = <US-ASCII HT, horizontal-tab (9)>

    return (new RegExp(
        "^[^\\x00-\\x20\\x7f\\(\\)<>@,;:\\\\\\\"\\[\\]\\?=\\{\\}\\/\\u0080-\\uffff]+\x24"))
        .test(key);
};
/**
 * 获取cookie的值，不对值进行解码
 *
 * @name Util.cookie.getRaw
 * @function
 * @grammar Util.cookie.getRaw(key)
 * @param {string}
 *            key 需要获取Cookie的键名
 * @meta standard
 * @see Util.cookie.get,Util.cookie.setRaw
 *
 * @returns {string|null} 获取的Cookie值，获取不到时返回null
 */
Util.cookie.getRaw = function (key) {
    if (Util.cookie._isValidKey(key)) {
        var reg = new RegExp("(^| )" + key + "=([^;]*)(;|\x24)"), result = reg
            .exec(document.cookie);

        if (result) {
            return result[2] || null;
        }
    }

    return null;
};
/*
 * Tangram Copyright 2009 Util Inc. All rights reserved.
 * 
 * path: Util/cookie/get.js author: erik version: 1.1.0 date: 2009/11/15
 */

/**
 * 获取cookie的值，用decodeURIComponent进行解码
 *
 * @name Util.cookie.get
 * @function
 * @grammar Util.cookie.get(key)
 * @param {string}
 *            key 需要获取Cookie的键名
 * @remark <b>注意：</b>该方法会对cookie值进行decodeURIComponent解码。如果想获得cookie源字符串，请使用getRaw方法。
 * @meta standard
 * @see Util.cookie.getRaw,Util.cookie.set
 *
 * @returns {string|null} cookie的值，获取不到时返回null
 */
Util.cookie.get = function (key) {
    var value = Util.cookie.getRaw(key);
    if ('string' == typeof value) {
        value = decodeURIComponent(value);
        return value;
    }
    return null;
};
/*
 * Tangram Copyright 2009 Util Inc. All rights reserved.
 * 
 * path: Util/cookie/setRaw.js author: erik version: 1.1.0 date: 2009/11/15
 */

/**
 * 设置cookie的值，不对值进行编码
 *
 * @name Util.cookie.setRaw
 * @function
 * @grammar Util.cookie.setRaw(key, value[, options])
 * @param {string}
 *            key 需要设置Cookie的键名
 * @param {string}
 *            value 需要设置Cookie的值
 * @param {Object}
 *            [options] 设置Cookie的其他可选参数
 * @config {string} [path] cookie路径
 * @config {Date|number} [expires] cookie过期时间,如果类型是数字的话, 单位是毫秒
 * @config {string} [domain] cookie域名
 * @config {string} [secure] cookie是否安全传输
 * @remark
 *
 * <b>options参数包括：</b><br>
 * path:cookie路径<br>
 * expires:cookie过期时间，Number型，单位为毫秒。<br>
 * domain:cookie域名<br>
 * secure:cookie是否安全传输
 *
 * @meta standard
 * @see Util.cookie.set,Util.cookie.getRaw
 */
Util.cookie.setRaw = function (key, value, options) {
    if (!Util.cookie._isValidKey(key)) {
        return;
    }

    options = options || {};
    // options.path = options.path || "/"; // meizz 20100402 设定一个初始值，方便后续的操作
    // berg 20100409 去掉，因为用户希望默认的path是当前路径，这样和浏览器对cookie的定义也是一致的

    // 计算cookie过期时间
    var expires = options.expires;
    if ('number' == typeof options.expires) {
        expires = new Date();
        expires.setTime(expires.getTime() + options.expires);
    }

    document.cookie = key + "=" + value
        + (options.path ? "; path=" + options.path : "")
        + (expires ? "; expires=" + expires.toGMTString() : "")
        + (options.domain ? "; domain=" + options.domain : "")
        + (options.secure ? "; secure" : '');
};
/*
 * Tangram Copyright 2009 Util Inc. All rights reserved.
 * 
 * path: Util/cookie/remove.js author: erik version: 1.1.0 date: 2009/11/15
 */

/**
 * 删除cookie的值
 *
 * @name Util.cookie.remove
 * @function
 * @grammar Util.cookie.remove(key, options)
 * @param {string}
 *            key 需要删除Cookie的键名
 * @param {Object}
 *            options 需要删除的cookie对应的 path domain 等值
 * @meta standard
 */
Util.cookie.remove = function (key, options) {
    options = options || {};
    options.expires = new Date(0);
    Util.cookie.setRaw(key, '', options);
};
/*
 * Tangram Copyright 2009 Util Inc. All rights reserved.
 * 
 * path: Util/cookie/set.js author: erik version: 1.1.0 date: 2009/11/15
 */

/**
 * 设置cookie的值，用encodeURIComponent进行编码
 *
 * @name Util.cookie.set
 * @function
 * @grammar Util.cookie.set(key, value[, options])
 * @param {string}
 *            key 需要设置Cookie的键名
 * @param {string}
 *            value 需要设置Cookie的值
 * @param {Object}
 *            [options] 设置Cookie的其他可选参数
 * @config {string} [path] cookie路径
 * @config {Date|number} [expires] cookie过期时间,如果类型是数字的话, 单位是毫秒
 * @config {string} [domain] cookie域名
 * @config {string} [secure] cookie是否安全传输
 * @remark
 *
 * 1. <b>注意：</b>该方法会对cookie值进行encodeURIComponent编码。如果想设置cookie源字符串，请使用setRaw方法。<br>
 * <br>
 * 2. <b>options参数包括：</b><br>
 * path:cookie路径<br>
 * expires:cookie过期时间，Number型，单位为毫秒。<br>
 * domain:cookie域名<br>
 * secure:cookie是否安全传输
 *
 * @meta standard
 * @see Util.cookie.setRaw,Util.cookie.get
 */
Util.cookie.set = function (key, value, options) {
    Util.cookie.setRaw(key, encodeURIComponent(value), options);
};

/*
 * 获取location中search的值 @return {string} value 返回activityId
 */
Util.param.getActivityId = function () {
    var ids = /activityId=(\d+)&?/.exec(location.search);
    var id = ids ? ids[1] : '';
    return id;
};

/*
 * 获取location中search的值 @return {string} value 返回configId
 */
Util.param.getConfigId = function () {
    var ids = /configId=(\d+)&?/.exec(location.search);
    var id = ids ? ids[1] : '';
    return id;
};

/*
 * 获取location中path的值 @return {string} value 返回pageName
 */
Util.param.getPageName = function () {
    var pageNames = /\/(\w+)\.html/.exec(location.pathname);
    var pageName = pageNames ? pageNames[1] : 'index';
    return pageName;
};

/*
 * 获取location中search的值 @return {string} value 返回status
 */
Util.param.getStatus = function () {
    var stas = /status=(\d+)&?/.exec(location.search);
    var sta = stas ? stas[1] : '';
    return sta;
};

/*
 * 获取location中search的值 @return {string} value 返回创建页面时的序号
 */
Util.param.getCreateIndex = function () {
    var stas = /createIndex=(\d+)&?/.exec(location.search);
    var sta = stas ? stas[1] : '';
    return sta;
};

/*
 * 判断是否是更新 @return {boolean}
 */
Util.param.isUpdate = function () {
    // var typeArr = location.search.match(/type=[a-zA-Z]+/g);
    if (location.search.indexOf("type=update") !== -1) {
        return true;
    }
};

/*
 * 判断是否是创建 @return {boolean}
 */
Util.param.isCreate = function () {
    if (location.search.indexOf("createIndex") !== -1) {
        return true;
    }
};
/*
 * 输出错误信息 @param {Object} $elm jQuery对象 @param {Object} data 返回的对象
 */
Util.input.whiteError = function ($elm, data) {
    var html = '';
    $elm.show();
    if (data.statusInfo.global) {
        $elm.html(data.statusInfo.global);
        return;
    }
    $.each(data.message.field, function (key, value) {
        html += key + "：" + value + "<br/>";
    });
    $elm.html(html);
};

//
// 回车转为br标签
//
Util.input.return2Br = function (str) {
    return str.replace(/\r?\n/g, "<br />");
}


var entityMap = {
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': '&quot;',
    "'": '&#39;',
    "/": '&#x2F;'
};


//
// 回车转为br标签
//
Util.input.escapeHtml = function escapeHtml(string) {
    return String(string).replace(/[&<>"'\/]/g, function (s) {
        return entityMap[s];
    });
}

/**
*
*  MD5 (Message-Digest Algorithm)
*  http://www.webtoolkit.info/
*
**/
 
var MD5 = function (string) {
 
    function RotateLeft(lValue, iShiftBits) {
        return (lValue<<iShiftBits) | (lValue>>>(32-iShiftBits));
    }
 
    function AddUnsigned(lX,lY) {
        var lX4,lY4,lX8,lY8,lResult;
        lX8 = (lX & 0x80000000);
        lY8 = (lY & 0x80000000);
        lX4 = (lX & 0x40000000);
        lY4 = (lY & 0x40000000);
        lResult = (lX & 0x3FFFFFFF)+(lY & 0x3FFFFFFF);
        if (lX4 & lY4) {
            return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
        }
        if (lX4 | lY4) {
            if (lResult & 0x40000000) {
                return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
            } else {
                return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
            }
        } else {
            return (lResult ^ lX8 ^ lY8);
        }
    }
 
    function F(x,y,z) { return (x & y) | ((~x) & z); }
    function G(x,y,z) { return (x & z) | (y & (~z)); }
    function H(x,y,z) { return (x ^ y ^ z); }
    function I(x,y,z) { return (y ^ (x | (~z))); }
 
    function FF(a,b,c,d,x,s,ac) {
        a = AddUnsigned(a, AddUnsigned(AddUnsigned(F(b, c, d), x), ac));
        return AddUnsigned(RotateLeft(a, s), b);
    };
 
    function GG(a,b,c,d,x,s,ac) {
        a = AddUnsigned(a, AddUnsigned(AddUnsigned(G(b, c, d), x), ac));
        return AddUnsigned(RotateLeft(a, s), b);
    };
 
    function HH(a,b,c,d,x,s,ac) {
        a = AddUnsigned(a, AddUnsigned(AddUnsigned(H(b, c, d), x), ac));
        return AddUnsigned(RotateLeft(a, s), b);
    };
 
    function II(a,b,c,d,x,s,ac) {
        a = AddUnsigned(a, AddUnsigned(AddUnsigned(I(b, c, d), x), ac));
        return AddUnsigned(RotateLeft(a, s), b);
    };
 
    function ConvertToWordArray(string) {
        var lWordCount;
        var lMessageLength = string.length;
        var lNumberOfWords_temp1=lMessageLength + 8;
        var lNumberOfWords_temp2=(lNumberOfWords_temp1-(lNumberOfWords_temp1 % 64))/64;
        var lNumberOfWords = (lNumberOfWords_temp2+1)*16;
        var lWordArray=Array(lNumberOfWords-1);
        var lBytePosition = 0;
        var lByteCount = 0;
        while ( lByteCount < lMessageLength ) {
            lWordCount = (lByteCount-(lByteCount % 4))/4;
            lBytePosition = (lByteCount % 4)*8;
            lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount)<<lBytePosition));
            lByteCount++;
        }
        lWordCount = (lByteCount-(lByteCount % 4))/4;
        lBytePosition = (lByteCount % 4)*8;
        lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80<<lBytePosition);
        lWordArray[lNumberOfWords-2] = lMessageLength<<3;
        lWordArray[lNumberOfWords-1] = lMessageLength>>>29;
        return lWordArray;
    };
 
    function WordToHex(lValue) {
        var WordToHexValue="",WordToHexValue_temp="",lByte,lCount;
        for (lCount = 0;lCount<=3;lCount++) {
            lByte = (lValue>>>(lCount*8)) & 255;
            WordToHexValue_temp = "0" + lByte.toString(16);
            WordToHexValue = WordToHexValue + WordToHexValue_temp.substr(WordToHexValue_temp.length-2,2);
        }
        return WordToHexValue;
    };
 
    function Utf8Encode(string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
 
        for (var n = 0; n < string.length; n++) {
 
            var c = string.charCodeAt(n);
 
            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
 
        }
 
        return utftext;
    };
 
    var x=Array();
    var k,AA,BB,CC,DD,a,b,c,d;
    var S11=7, S12=12, S13=17, S14=22;
    var S21=5, S22=9 , S23=14, S24=20;
    var S31=4, S32=11, S33=16, S34=23;
    var S41=6, S42=10, S43=15, S44=21;
 
    string = Utf8Encode(string);
 
    x = ConvertToWordArray(string);
 
    a = 0x67452301; b = 0xEFCDAB89; c = 0x98BADCFE; d = 0x10325476;
 
    for (k=0;k<x.length;k+=16) {
        AA=a; BB=b; CC=c; DD=d;
        a=FF(a,b,c,d,x[k+0], S11,0xD76AA478);
        d=FF(d,a,b,c,x[k+1], S12,0xE8C7B756);
        c=FF(c,d,a,b,x[k+2], S13,0x242070DB);
        b=FF(b,c,d,a,x[k+3], S14,0xC1BDCEEE);
        a=FF(a,b,c,d,x[k+4], S11,0xF57C0FAF);
        d=FF(d,a,b,c,x[k+5], S12,0x4787C62A);
        c=FF(c,d,a,b,x[k+6], S13,0xA8304613);
        b=FF(b,c,d,a,x[k+7], S14,0xFD469501);
        a=FF(a,b,c,d,x[k+8], S11,0x698098D8);
        d=FF(d,a,b,c,x[k+9], S12,0x8B44F7AF);
        c=FF(c,d,a,b,x[k+10],S13,0xFFFF5BB1);
        b=FF(b,c,d,a,x[k+11],S14,0x895CD7BE);
        a=FF(a,b,c,d,x[k+12],S11,0x6B901122);
        d=FF(d,a,b,c,x[k+13],S12,0xFD987193);
        c=FF(c,d,a,b,x[k+14],S13,0xA679438E);
        b=FF(b,c,d,a,x[k+15],S14,0x49B40821);
        a=GG(a,b,c,d,x[k+1], S21,0xF61E2562);
        d=GG(d,a,b,c,x[k+6], S22,0xC040B340);
        c=GG(c,d,a,b,x[k+11],S23,0x265E5A51);
        b=GG(b,c,d,a,x[k+0], S24,0xE9B6C7AA);
        a=GG(a,b,c,d,x[k+5], S21,0xD62F105D);
        d=GG(d,a,b,c,x[k+10],S22,0x2441453);
        c=GG(c,d,a,b,x[k+15],S23,0xD8A1E681);
        b=GG(b,c,d,a,x[k+4], S24,0xE7D3FBC8);
        a=GG(a,b,c,d,x[k+9], S21,0x21E1CDE6);
        d=GG(d,a,b,c,x[k+14],S22,0xC33707D6);
        c=GG(c,d,a,b,x[k+3], S23,0xF4D50D87);
        b=GG(b,c,d,a,x[k+8], S24,0x455A14ED);
        a=GG(a,b,c,d,x[k+13],S21,0xA9E3E905);
        d=GG(d,a,b,c,x[k+2], S22,0xFCEFA3F8);
        c=GG(c,d,a,b,x[k+7], S23,0x676F02D9);
        b=GG(b,c,d,a,x[k+12],S24,0x8D2A4C8A);
        a=HH(a,b,c,d,x[k+5], S31,0xFFFA3942);
        d=HH(d,a,b,c,x[k+8], S32,0x8771F681);
        c=HH(c,d,a,b,x[k+11],S33,0x6D9D6122);
        b=HH(b,c,d,a,x[k+14],S34,0xFDE5380C);
        a=HH(a,b,c,d,x[k+1], S31,0xA4BEEA44);
        d=HH(d,a,b,c,x[k+4], S32,0x4BDECFA9);
        c=HH(c,d,a,b,x[k+7], S33,0xF6BB4B60);
        b=HH(b,c,d,a,x[k+10],S34,0xBEBFBC70);
        a=HH(a,b,c,d,x[k+13],S31,0x289B7EC6);
        d=HH(d,a,b,c,x[k+0], S32,0xEAA127FA);
        c=HH(c,d,a,b,x[k+3], S33,0xD4EF3085);
        b=HH(b,c,d,a,x[k+6], S34,0x4881D05);
        a=HH(a,b,c,d,x[k+9], S31,0xD9D4D039);
        d=HH(d,a,b,c,x[k+12],S32,0xE6DB99E5);
        c=HH(c,d,a,b,x[k+15],S33,0x1FA27CF8);
        b=HH(b,c,d,a,x[k+2], S34,0xC4AC5665);
        a=II(a,b,c,d,x[k+0], S41,0xF4292244);
        d=II(d,a,b,c,x[k+7], S42,0x432AFF97);
        c=II(c,d,a,b,x[k+14],S43,0xAB9423A7);
        b=II(b,c,d,a,x[k+5], S44,0xFC93A039);
        a=II(a,b,c,d,x[k+12],S41,0x655B59C3);
        d=II(d,a,b,c,x[k+3], S42,0x8F0CCC92);
        c=II(c,d,a,b,x[k+10],S43,0xFFEFF47D);
        b=II(b,c,d,a,x[k+1], S44,0x85845DD1);
        a=II(a,b,c,d,x[k+8], S41,0x6FA87E4F);
        d=II(d,a,b,c,x[k+15],S42,0xFE2CE6E0);
        c=II(c,d,a,b,x[k+6], S43,0xA3014314);
        b=II(b,c,d,a,x[k+13],S44,0x4E0811A1);
        a=II(a,b,c,d,x[k+4], S41,0xF7537E82);
        d=II(d,a,b,c,x[k+11],S42,0xBD3AF235);
        c=II(c,d,a,b,x[k+2], S43,0x2AD7D2BB);
        b=II(b,c,d,a,x[k+9], S44,0xEB86D391);
        a=AddUnsigned(a,AA);
        b=AddUnsigned(b,BB);
        c=AddUnsigned(c,CC);
        d=AddUnsigned(d,DD);
    }
 
    var temp = WordToHex(a)+WordToHex(b)+WordToHex(c)+WordToHex(d);
 
    return temp.toLowerCase();
}