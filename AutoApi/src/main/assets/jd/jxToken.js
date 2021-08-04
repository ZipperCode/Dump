/**
 * 使用京东cookie获取京喜token
 */
var _0xod8 = 'jsjiami.com.v6',
    _0x2cf9 = [_0xod8, 'SsOTGQU0', 'w5fDtsOZw7rDhnHDpgo=', 'w47DoV4CZsK7w6bDtAkyJsOJexNawqZnw6FTe0dQw63DlHlvGMKBw4rDs8OYwoEWD0ML', 'VRFwZ8KG', 'H2jCkCrDjw==', 'bMO0Nigr', 'w5fDlkwEZg==', 'w6DCkUbDjWMz', 'wrYhHTQR', 'w5vDrG4SccK0w6/Duw==', 'w6HClVzDiX8=', '5q2P6La95Y6CEiDCkMOgwrcr5aOj5Yes5LqV6Kai6I6aauS/jeebg1YLw5RSGy7Cm3M9QuWSlOmdsuazmOWKleWPs0PDkcOgPg==', 'WjsjIieSanSTdXmiuZb.EncDom.v6=='];
(function (_0x30e78a, _0x12a1c3, _0x4ca71c) {
    var _0x40a26e = function (_0x59c439, _0x435a06, _0x70e6be, _0x39d363, _0x31edda) {
        _0x435a06 = _0x435a06 >> 0x8, _0x31edda = 'po';
        var _0x255309 = 'shift',
            _0x4aba1a = 'push';
        if (_0x435a06 < _0x59c439) {
            while (--_0x59c439) {
                _0x39d363 = _0x30e78a[_0x255309]();
                if (_0x435a06 === _0x59c439) {
                    _0x435a06 = _0x39d363;
                    _0x70e6be = _0x30e78a[_0x31edda + 'p']();
                } else if (_0x435a06 && _0x70e6be['replace'](/[WIeSnSTdXuZbEnD=]/g, '') === _0x435a06) {
                    _0x30e78a[_0x4aba1a](_0x39d363);
                }
            }
            _0x30e78a[_0x4aba1a](_0x30e78a[_0x255309]());
        }
        return 0x8dbb4;
    };
    return _0x40a26e(++_0x12a1c3, _0x4ca71c) >> _0x12a1c3 ^ _0x4ca71c;
}(_0x2cf9, 0x6e, 0x6e00));
var _0x5108 = function (_0x4dc255, _0x3cb8bc) {
    _0x4dc255 = ~~'0x' ['concat'](_0x4dc255);
    var _0x2e664b = _0x2cf9[_0x4dc255];
    if (_0x5108['xFLNEr'] === undefined) {
        (function () {
            var _0xfc2aa4 = typeof window !== 'undefined' ? window : typeof process === 'object' && typeof require === 'function' && typeof global === 'object' ? global : this;
            var _0x26458d = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
            _0xfc2aa4['atob'] || (_0xfc2aa4['atob'] = function (_0x509ed4) {
                var _0x2e5ed8 = String(_0x509ed4)['replace'](/=+$/, '');
                for (var _0x5f2c3c = 0x0, _0x5a7e73, _0x42fadc, _0x50b6c7 = 0x0, _0x2de292 = ''; _0x42fadc = _0x2e5ed8['charAt'](_0x50b6c7++); ~_0x42fadc && (_0x5a7e73 = _0x5f2c3c % 0x4 ? _0x5a7e73 * 0x40 + _0x42fadc : _0x42fadc, _0x5f2c3c++ % 0x4) ? _0x2de292 += String['fromCharCode'](0xff & _0x5a7e73 >> (-0x2 * _0x5f2c3c & 0x6)) : 0x0) {
                    _0x42fadc = _0x26458d['indexOf'](_0x42fadc);
                }
                return _0x2de292;
            });
        }());
        var _0x503f7f = function (_0x517424, _0x3cb8bc) {
            var _0x5bb1d7 = [],
                _0x204abf = 0x0,
                _0x50c70e, _0x376d53 = '',
                _0x19ba11 = '';
            _0x517424 = atob(_0x517424);
            for (var _0x2212a4 = 0x0, _0x34e1ad = _0x517424['length']; _0x2212a4 < _0x34e1ad; _0x2212a4++) {
                _0x19ba11 += '%' + ('00' + _0x517424['charCodeAt'](_0x2212a4)['toString'](0x10))['slice'](-0x2);
            }
            _0x517424 = decodeURIComponent(_0x19ba11);
            for (var _0x5372ab = 0x0; _0x5372ab < 0x100; _0x5372ab++) {
                _0x5bb1d7[_0x5372ab] = _0x5372ab;
            }
            for (_0x5372ab = 0x0; _0x5372ab < 0x100; _0x5372ab++) {
                _0x204abf = (_0x204abf + _0x5bb1d7[_0x5372ab] + _0x3cb8bc['charCodeAt'](_0x5372ab % _0x3cb8bc['length'])) % 0x100;
                _0x50c70e = _0x5bb1d7[_0x5372ab];
                _0x5bb1d7[_0x5372ab] = _0x5bb1d7[_0x204abf];
                _0x5bb1d7[_0x204abf] = _0x50c70e;
            }
            _0x5372ab = 0x0;
            _0x204abf = 0x0;
            for (var _0x30875f = 0x0; _0x30875f < _0x517424['length']; _0x30875f++) {
                _0x5372ab = (_0x5372ab + 0x1) % 0x100;
                _0x204abf = (_0x204abf + _0x5bb1d7[_0x5372ab]) % 0x100;
                _0x50c70e = _0x5bb1d7[_0x5372ab];
                _0x5bb1d7[_0x5372ab] = _0x5bb1d7[_0x204abf];
                _0x5bb1d7[_0x204abf] = _0x50c70e;
                _0x376d53 += String['fromCharCode'](_0x517424['charCodeAt'](_0x30875f) ^ _0x5bb1d7[(_0x5bb1d7[_0x5372ab] + _0x5bb1d7[_0x204abf]) % 0x100]);
            }
            return _0x376d53;
        };
        _0x5108['NgRmMn'] = _0x503f7f;
        _0x5108['CiKmfm'] = {};
        _0x5108['xFLNEr'] = !![];
    }
    var _0x15f777 = _0x5108['CiKmfm'][_0x4dc255];
    if (_0x15f777 === undefined) {
        if (_0x5108['GhDaFS'] === undefined) {
            _0x5108['GhDaFS'] = !![];
        }
        _0x2e664b = _0x5108['NgRmMn'](_0x2e664b, _0x3cb8bc);
        _0x5108['CiKmfm'][_0x4dc255] = _0x2e664b;
    } else {
        _0x2e664b = _0x15f777;
    }
    return _0x2e664b;
};

_0xod8 = 'jsjiami.com.v6';
var md5 = (function (n) {
    "use strict";

    function t(n, t) {
        var r = (65535 & n) + (65535 & t);
        return (n >> 16) + (t >> 16) + (r >> 16) << 16 | 65535 & r
    }

    function r(n, t) {
        return n << t | n >>> 32 - t
    }

    function e(n, e, o, u, c, f) {
        return t(r(t(t(e, n), t(u, f)), c), o)
    }

    function o(n, t, r, o, u, c, f) {
        return e(t & r | ~t & o, n, t, u, c, f)
    }

    function u(n, t, r, o, u, c, f) {
        return e(t & o | r & ~o, n, t, u, c, f)
    }

    function c(n, t, r, o, u, c, f) {
        return e(t ^ r ^ o, n, t, u, c, f)
    }

    function f(n, t, r, o, u, c, f) {
        return e(r ^ (t | ~o), n, t, u, c, f)
    }

    function i(n, r) {
        n[r >> 5] |= 128 << r % 32, n[14 + (r + 64 >>> 9 << 4)] = r;
        var e, i, a, d, h, l = 1732584193,
            g = -271733879,
            v = -1732584194,
            m = 271733878;
        for (e = 0; e < n.length; e += 16) i = l, a = g, d = v, h = m, g = f(g = f(g = f(g = f(g = c(g = c(g = c(g = c(g = u(g = u(g = u(g = u(g = o(g = o(g = o(g = o(g, v = o(v, m = o(m, l = o(l, g, v, m, n[e], 7, -680876936), g, v, n[e + 1], 12, -389564586), l, g, n[e + 2], 17, 606105819), m, l, n[e + 3], 22, -1044525330), v = o(v, m = o(m, l = o(l, g, v, m, n[e + 4], 7, -176418897), g, v, n[e + 5], 12, 1200080426), l, g, n[e + 6], 17, -1473231341), m, l, n[e + 7], 22, -45705983), v = o(v, m = o(m, l = o(l, g, v, m, n[e + 8], 7, 1770035416), g, v, n[e + 9], 12, -1958414417), l, g, n[e + 10], 17, -42063), m, l, n[e + 11], 22, -1990404162), v = o(v, m = o(m, l = o(l, g, v, m, n[e + 12], 7, 1804603682), g, v, n[e + 13], 12, -40341101), l, g, n[e + 14], 17, -1502002290), m, l, n[e + 15], 22, 1236535329), v = u(v, m = u(m, l = u(l, g, v, m, n[e + 1], 5, -165796510), g, v, n[e + 6], 9, -1069501632), l, g, n[e + 11], 14, 643717713), m, l, n[e], 20, -373897302), v = u(v, m = u(m, l = u(l, g, v, m, n[e + 5], 5, -701558691), g, v, n[e + 10], 9, 38016083), l, g, n[e + 15], 14, -660478335), m, l, n[e + 4], 20, -405537848), v = u(v, m = u(m, l = u(l, g, v, m, n[e + 9], 5, 568446438), g, v, n[e + 14], 9, -1019803690), l, g, n[e + 3], 14, -187363961), m, l, n[e + 8], 20, 1163531501), v = u(v, m = u(m, l = u(l, g, v, m, n[e + 13], 5, -1444681467), g, v, n[e + 2], 9, -51403784), l, g, n[e + 7], 14, 1735328473), m, l, n[e + 12], 20, -1926607734), v = c(v, m = c(m, l = c(l, g, v, m, n[e + 5], 4, -378558), g, v, n[e + 8], 11, -2022574463), l, g, n[e + 11], 16, 1839030562), m, l, n[e + 14], 23, -35309556), v = c(v, m = c(m, l = c(l, g, v, m, n[e + 1], 4, -1530992060), g, v, n[e + 4], 11, 1272893353), l, g, n[e + 7], 16, -155497632), m, l, n[e + 10], 23, -1094730640), v = c(v, m = c(m, l = c(l, g, v, m, n[e + 13], 4, 681279174), g, v, n[e], 11, -358537222), l, g, n[e + 3], 16, -722521979), m, l, n[e + 6], 23, 76029189), v = c(v, m = c(m, l = c(l, g, v, m, n[e + 9], 4, -640364487), g, v, n[e + 12], 11, -421815835), l, g, n[e + 15], 16, 530742520), m, l, n[e + 2], 23, -995338651), v = f(v, m = f(m, l = f(l, g, v, m, n[e], 6, -198630844), g, v, n[e + 7], 10, 1126891415), l, g, n[e + 14], 15, -1416354905), m, l, n[e + 5], 21, -57434055), v = f(v, m = f(m, l = f(l, g, v, m, n[e + 12], 6, 1700485571), g, v, n[e + 3], 10, -1894986606), l, g, n[e + 10], 15, -1051523), m, l, n[e + 1], 21, -2054922799), v = f(v, m = f(m, l = f(l, g, v, m, n[e + 8], 6, 1873313359), g, v, n[e + 15], 10, -30611744), l, g, n[e + 6], 15, -1560198380), m, l, n[e + 13], 21, 1309151649), v = f(v, m = f(m, l = f(l, g, v, m, n[e + 4], 6, -145523070), g, v, n[e + 11], 10, -1120210379), l, g, n[e + 2], 15, 718787259), m, l, n[e + 9], 21, -343485551), l = t(l, i), g = t(g, a), v = t(v, d), m = t(m, h);
        return [l, g, v, m]
    }

    function a(n) {
        var t, r = "",
            e = 32 * n.length;
        for (t = 0; t < e; t += 8) r += String.fromCharCode(n[t >> 5] >>> t % 32 & 255);
        return r
    }

    function d(n) {
        var t, r = [];
        for (r[(n.length >> 2) - 1] = void 0, t = 0; t < r.length; t += 1) r[t] = 0;
        var e = 8 * n.length;
        for (t = 0; t < e; t += 8) r[t >> 5] |= (255 & n.charCodeAt(t / 8)) << t % 32;
        return r
    }

    function h(n) {
        return a(i(d(n), 8 * n.length))
    }

    function l(n, t) {
        var r, e, o = d(n),
            u = [],
            c = [];
        for (u[15] = c[15] = void 0, o.length > 16 && (o = i(o, 8 * n.length)), r = 0; r < 16; r += 1) u[r] = 909522486 ^ o[r], c[r] = 1549556828 ^ o[r];
        return e = i(u.concat(d(t)), 512 + 8 * t.length), a(i(c.concat(e), 640))
    }

    function g(n) {
        var t, r, e = "";
        for (r = 0; r < n.length; r += 1) t = n.charCodeAt(r), e += "0123456789abcdef".charAt(t >>> 4 & 15) + "0123456789abcdef".charAt(15 & t);
        return e
    }

    function v(n) {
        return unescape(encodeURIComponent(n))
    }

    function m(n) {
        return h(v(n))
    }

    function p(n) {
        return g(m(n))
    }

    function s(n, t) {
        return l(v(n), v(t))
    }

    function C(n, t) {
        return g(s(n, t))
    }

    function A(n, t, r) {
        return t ? r ? s(t, n) : C(t, n) : r ? m(n) : p(n)
    }
    console.log("aaa", A)
    return A
}(this));

function getJxToken(cookie) {
    var _0x3565bd = {
        'AShns': _0x5108('0', 'U*Pv'),
        'ehytr': function (_0x50bf17, _0x53078a) {
            return _0x50bf17 < _0x53078a;
        },
        'GoCYd': function (_0x136745, _0x5686db) {
            return _0x136745(_0x5686db);
        },
        'xUqbe': function (_0x1ea9c8, _0x5b6c4e) {
            return _0x1ea9c8 * _0x5b6c4e;
        }
    };

    function _0x23cb77(_0x378208) {
        let _0x36ad34 = _0x3565bd[_0x5108('1', 'cqej')];
        let _0x3ba0b7 = '';
        for (let _0x24b162 = 0x0; _0x3565bd[_0x5108('2', '1#C#')](_0x24b162, _0x378208); _0x24b162++) {
            _0x3ba0b7 += _0x36ad34[_0x3565bd[_0x5108('3', 'Hq%O')](parseInt, _0x3565bd[_0x5108('4', 'U*Pv')](Math['random'](), _0x36ad34[_0x5108('5', '8QnT')]))];
        }
        return _0x3ba0b7;
    }
    return new Promise(_0x2ef875 => {
        let _0x9ac908 = _0x3565bd[_0x5108('6', 'x)1A')](_0x23cb77, 0x28);
        let _0x256650 = (+new Date())[_0x5108('7', 'U*Pv')]();
        if (!cookie[_0x5108('8', '8QnT')](/pt_pin=([^; ]+)(?=;?)/)) {
            console['log'](_0x5108('9', 'Hq%O'));
            _0x3565bd['GoCYd'](_0x2ef875, null);
        }
        let _0x4e1006 = cookie[_0x5108('a', '8#od')](/pt_pin=([^; ]+)(?=;?)/)[0x1];
        let _0x57bff6 = md5('' + decodeURIComponent(_0x4e1006) + _0x256650 + _0x9ac908 + 'tPOamqCuk9NLgVPAljUyIHcPRmKlVxDy')[_0x5108('b', ']OsH')]();
        _0x3565bd['GoCYd'](_0x2ef875, {
            'timestamp': _0x256650,
            'phoneid': _0x9ac908,
            'farm_jstoken': _0x57bff6
        });
    });
};

let cookie = "pt_key=AAJgpa_TADAnLASb8a82ll1e07tXWSPnnYietfCF6PS1v8TEiftg0NfrtUj-vJG8bPOkDMcefW0;pt_pin=jd_59739e7a7a296;"
console.log(getJxToken(cookie).then((resp) => {
    console.log("res = ", resp)
}).catch(error => {
    console.log(error)
}))
console.log("jx", getJxToken(cookie))