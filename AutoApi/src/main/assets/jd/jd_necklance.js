/*
点点券，可以兑换无门槛红包（1元，5元，10元，100元，部分红包需抢购）
Last Modified time: 2021-05-28 17:27:14
活动入口：京东APP-领券中心/券后9.9-领点点券 [活动地址](https://h5.m.jd.com/babelDiy/Zeus/41Lkp7DumXYCFmPYtU3LTcnTTXTX/index.html)
脚本兼容: QuantumultX, Surge, Loon, JSBox, Node.js
===============Quantumultx===============
[task_local]
#点点券
20 0,20 * * * jd_necklace.js, tag=点点券, img-url=https://raw.githubusercontent.com/Orz-3/mini/master/Color/jd.png, enabled=true

 */
/*****************************[Function]*************************************/
/* Hash constant words K: */
var K256 = new Array(
  0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5,
  0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
  0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3,
  0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
  0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
  0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
  0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
  0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
  0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
  0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
  0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
  0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
  0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
  0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
  0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
  0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
);

/* global arrays */
var ihash, count, buffer;
var sha256_hex_digits = "0123456789abcdef";

/*****************************[Function]*************************************/


/**********************************[ZooFaker]****************************************/
const ZooFaker = {
  getDefaultVal: function (e) {
    try {
      return {
        undefined: "u",
        false: "f",
        true: "t"
      } [e] || e
    } catch (t) {
      return e
    }
  },
  requestUrl: {
    gettoken: "".concat("https://", "bh.m.jd.com/gettoken"),
    bypass: "".concat("https://blackhole", ".m.jd.com/bypass")
  },
  getTouchSession: function () {
    var e = (new Date).getTime(),
      t = this.getRandomInt(1e3, 9999);
    return String(e) + String(t)
  },
  sha256: function (data) {
    sha256_init();
    sha256_update(data, data.length);
    sha256_final();
    return sha256_encode_hex().toUpperCase();
  },
  atobPolyfill: function (e) {
    return function (e) {
      var t = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
      if (e = String(e).replace(/[\t\n\f\r ]+/g, ""), !/^(?:[A-Za-z\d+\/]{4})*?(?:[A-Za-z\d+\/]{2}(?:==)?|[A-Za-z\d+\/]{3}=?)?$/.test(e)) throw new TypeError("解密错误");
      e += "==".slice(2 - (3 & e.length));
      for (var n, r, i, o = "", a = 0; a < e.length;) n = t.indexOf(e.charAt(a++)) << 18 | t.indexOf(e.charAt(a++)) << 12 | (r = t.indexOf(e.charAt(a++))) << 6 | (i = t.indexOf(e.charAt(a++))), o += 64 === r ? String.fromCharCode(n >> 16 & 255) : 64 === i ? String.fromCharCode(n >> 16 & 255, n >> 8 & 255) : String.fromCharCode(n >> 16 & 255, n >> 8 & 255, 255 & n);
      return o
    }(e)
  },
  btoaPolyfill: function (e) {
    var t = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    return function (e) {
      for (var n, r, i, o, a = "", s = 0, u = (e = String(e)).length % 3; s < e.length;) {
        if ((r = e.charCodeAt(s++)) > 255 || (i = e.charCodeAt(s++)) > 255 || (o = e.charCodeAt(s++)) > 255) throw new TypeError("Failed to execute 'btoa' on 'Window': The string to be encoded contains characters outside of the Latin1 range.");
        a += t.charAt((n = r << 16 | i << 8 | o) >> 18 & 63) + t.charAt(n >> 12 & 63) + t.charAt(n >> 6 & 63) + t.charAt(63 & n)
      }
      return u ? a.slice(0, u - 3) + "===".substring(u) : a
    }(unescape(encodeURIComponent(e)))
  },
  xorEncrypt: function (e, t) {
    for (var n = t.length, r = "", i = 0; i < e.length; i++) r += String.fromCharCode(e[i].charCodeAt() ^ t[i % n].charCodeAt());
    return r
  },
  encrypt1: function (e, t) {
    for (var n = e.length, r = t.toString(), i = [], o = "", a = 0, s = 0; s < r.length; s++) a >= n && (a %= n), o = (r.charCodeAt(s) ^ e.charCodeAt(a)) % 10, i.push(o), a += 1;
    return i.join().replace(/,/g, "")
  },
  len_Fun: function (e, t) {
    return "".concat(e.substring(t, e.length)) + "".concat(e.substring(0, t))
  },
  encrypt2: function (e, t) {
    var n = t.toString(),
      r = t.toString().length,
      i = parseInt((r + e.length) / 3),
      o = "",
      a = "";
    return r > e.length ? (o = this.len_Fun(n, i), a = this.encrypt1(e, o)) : (o = this.len_Fun(e, i), a = this.encrypt1(n, o)), a
  },
  addZeroFront: function (e) {
    return e && e.length >= 5 ? e : ("00000" + String(e)).substr(-5)
  },
  addZeroBack: function (e) {
    return e && e.length >= 5 ? e : (String(e) + "00000").substr(0, 5)
  },
  encrypt3: function (e, t) {
    var n = this.addZeroBack(t).toString().substring(0, 5),
      r = this.addZeroFront(e).substring(e.length - 5),
      i = n.length,
      o = encrypt_3(Array(i).keys()),
      a = [];
    return o.forEach(function (e) {
      a.push(Math.abs(n.charCodeAt(e) - r.charCodeAt(e)))
    }), a.join().replace(/,/g, "")
  },
  getCurrentDate: function () {
    return new Date
  },
  getCurrentTime: function () {
    return this.getCurrentDate().getTime()
  },
  getRandomInt: function () {
    var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : 0,
      t = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : 9;
    return e = Math.ceil(e), t = Math.floor(t), Math.floor(Math.random() * (t - e + 1)) + e
  },
  getRandomWord: function (e) {
    for (var t = "", n = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", r = 0; r < e; r++) {
      var i = Math.round(Math.random() * (n.length - 1));
      t += n.substring(i, i + 1)
    }
    return t
  },
  getNumberInString: function (e) {
    return Number(e.replace(/[^0-9]/gi, ""))
  },
  getSpecialPosition: function (e) {
    for (var t = !(arguments.length > 1 && void 0 !== arguments[1]) || arguments[1], n = ((e = String(e)).length, t ? 1 : 0), r = "", i = 0; i < e.length; i++) i % 2 === n && (r += e[i]);
    return r
  },
  getLastAscii: function (e) {
    var t = e.charCodeAt(0).toString();
    return t[t.length - 1]
  },
  toAscii: function (e) {
    var t = "";
    for (var n in e) {
      var r = e[n],
        i = /[a-zA-Z]/.test(r);
      e.hasOwnProperty(n) && (t += i ? this.getLastAscii(r) : r)
    }
    return t
  },
  add0: function (e, t) {
    return (Array(t).join("0") + e).slice(-t)
  },
  minusByByte: function (e, t) {
    var n = e.length,
      r = t.length,
      i = Math.max(n, r),
      o = this.toAscii(e),
      a = this.toAscii(t),
      s = "",
      u = 0;
    for (n !== r && (o = this.add0(o, i), a = this.add0(a, i)); u < i;) s += Math.abs(o[u] - a[u]), u++;
    return s
  },
  Crc32: function (str) {
    var table = "00000000 77073096 EE0E612C 990951BA 076DC419 706AF48F E963A535 9E6495A3 0EDB8832 79DCB8A4 E0D5E91E 97D2D988 09B64C2B 7EB17CBD E7B82D07 90BF1D91 1DB71064 6AB020F2 F3B97148 84BE41DE 1ADAD47D 6DDDE4EB F4D4B551 83D385C7 136C9856 646BA8C0 FD62F97A 8A65C9EC 14015C4F 63066CD9 FA0F3D63 8D080DF5 3B6E20C8 4C69105E D56041E4 A2677172 3C03E4D1 4B04D447 D20D85FD A50AB56B 35B5A8FA 42B2986C DBBBC9D6 ACBCF940 32D86CE3 45DF5C75 DCD60DCF ABD13D59 26D930AC 51DE003A C8D75180 BFD06116 21B4F4B5 56B3C423 CFBA9599 B8BDA50F 2802B89E 5F058808 C60CD9B2 B10BE924 2F6F7C87 58684C11 C1611DAB B6662D3D 76DC4190 01DB7106 98D220BC EFD5102A 71B18589 06B6B51F 9FBFE4A5 E8B8D433 7807C9A2 0F00F934 9609A88E E10E9818 7F6A0DBB 086D3D2D 91646C97 E6635C01 6B6B51F4 1C6C6162 856530D8 F262004E 6C0695ED 1B01A57B 8208F4C1 F50FC457 65B0D9C6 12B7E950 8BBEB8EA FCB9887C 62DD1DDF 15DA2D49 8CD37CF3 FBD44C65 4DB26158 3AB551CE A3BC0074 D4BB30E2 4ADFA541 3DD895D7 A4D1C46D D3D6F4FB 4369E96A 346ED9FC AD678846 DA60B8D0 44042D73 33031DE5 AA0A4C5F DD0D7CC9 5005713C 270241AA BE0B1010 C90C2086 5768B525 206F85B3 B966D409 CE61E49F 5EDEF90E 29D9C998 B0D09822 C7D7A8B4 59B33D17 2EB40D81 B7BD5C3B C0BA6CAD EDB88320 9ABFB3B6 03B6E20C 74B1D29A EAD54739 9DD277AF 04DB2615 73DC1683 E3630B12 94643B84 0D6D6A3E 7A6A5AA8 E40ECF0B 9309FF9D 0A00AE27 7D079EB1 F00F9344 8708A3D2 1E01F268 6906C2FE F762575D 806567CB 196C3671 6E6B06E7 FED41B76 89D32BE0 10DA7A5A 67DD4ACC F9B9DF6F 8EBEEFF9 17B7BE43 60B08ED5 D6D6A3E8 A1D1937E 38D8C2C4 4FDFF252 D1BB67F1 A6BC5767 3FB506DD 48B2364B D80D2BDA AF0A1B4C 36034AF6 41047A60 DF60EFC3 A867DF55 316E8EEF 4669BE79 CB61B38C BC66831A 256FD2A0 5268E236 CC0C7795 BB0B4703 220216B9 5505262F C5BA3BBE B2BD0B28 2BB45A92 5CB36A04 C2D7FFA7 B5D0CF31 2CD99E8B 5BDEAE1D 9B64C2B0 EC63F226 756AA39C 026D930A 9C0906A9 EB0E363F 72076785 05005713 95BF4A82 E2B87A14 7BB12BAE 0CB61B38 92D28E9B E5D5BE0D 7CDCEFB7 0BDBDF21 86D3D2D4 F1D4E242 68DDB3F8 1FDA836E 81BE16CD F6B9265B 6FB077E1 18B74777 88085AE6 FF0F6A70 66063BCA 11010B5C 8F659EFF F862AE69 616BFFD3 166CCF45 A00AE278 D70DD2EE 4E048354 3903B3C2 A7672661 D06016F7 4969474D 3E6E77DB AED16A4A D9D65ADC 40DF0B66 37D83BF0 A9BCAE53 DEBB9EC5 47B2CF7F 30B5FFE9 BDBDF21C CABAC28A 53B39330 24B4A3A6 BAD03605 CDD70693 54DE5729 23D967BF B3667A2E C4614AB8 5D681B02 2A6F2B94 B40BBE37 C30C8EA1 5A05DF1B 2D02EF8D";
    crc = 0 ^ (-1);
    var n = 0; //a number between 0 and 255
    var x = 0; //an hex number

    for (var i = 0, iTop = str.length; i < iTop; i++) {
      n = (crc ^ str.charCodeAt(i)) & 0xFF;
      x = "0x" + table.substr(n * 9, 8);
      crc = (crc >>> 8) ^ x;
    }
    return (crc ^ (-1)) >>> 0;
  },
  getCrcCode: function (e) {
    var t = "0000000",
      n = "";
    try {
      n = this.Crc32(e).toString(36), t = this.addZeroToSeven(n)
    } catch (e) {}
    return t
  },
  addZeroToSeven: function (e) {
    return e && e.length >= 7 ? e : ("0000000" + String(e)).substr(-7)
  },
  getInRange: function (e, t, n) {
    var r = [];
    return e.map(function (e, i) {
      e >= t && e <= n && r.push(e)
    }), r
  },
  RecursiveSorting: function () {
    var e = this,
      t = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {},
      n = {},
      r = t;
    if ("[object Object]" == Object.prototype.toString.call(r)) {
      var i = Object.keys(r).sort(function (e, t) {
        return e < t ? -1 : e > t ? 1 : 0
      });
      i.forEach(function (t) {
        var i = r[t];
        if ("[object Object]" === Object.prototype.toString.call(i)) {
          var o = e.RecursiveSorting(i);
          n[t] = o
        } else if ("[object Array]" === Object.prototype.toString.call(i)) {
          for (var a = [], s = 0; s < i.length; s++) {
            var u = i[s];
            if ("[object Object]" === Object.prototype.toString.call(u)) {
              var c = e.RecursiveSorting(u);
              a[s] = c
            } else a[s] = u
          }
          n[t] = a
        } else n[t] = i
      })
    } else n = t;
    return n
  },
  objToString2: function () {
    var e = arguments.length > 0 && void 0 !== arguments[0] ? arguments[0] : {},
      t = "";
    return Object.keys(e).forEach(function (n) {
      var r = e[n];
      null != r && (t += r instanceof Object || r instanceof Array ? "".concat("" === t ? "" : "&").concat(n, "=").concat(JSON.stringify(r)) : "".concat("" === t ? "" : "&").concat(n, "=").concat(r))
    }), t
  },
  getKey: function (e, t, n) {
    let r = this;
    return {
      1: function () {
        var e = r.getNumberInString(t),
          i = r.getSpecialPosition(n);
        return Math.abs(e - i)
      },
      2: function () {
        var e = r.getSpecialPosition(t, !1),
          i = r.getSpecialPosition(n);
        return r.minusByByte(e, i)
      },
      3: function () {
        var e = t.slice(0, 5),
          i = String(n).slice(-5);
        return r.minusByByte(e, i)
      },
      4: function () {
        return r.encrypt1(t, n)
      },
      5: function () {
        return r.encrypt2(t, n)
      },
      6: function () {
        return r.encrypt3(t, n)
      }
    } [e]()
  },
  decipherJoyToken: function (e, t) {
    let m = this;
    var n = {
      jjt: "a",
      expire: m.getCurrentTime(),
      outtime: 3,
      time_correction: !1
    };
    var r = "",
      i = e.indexOf(t) + t.length,
      o = e.length;
    if ((r = (r = e.slice(i, o).split(".")).map(function (e) {
        return m.atobPolyfill(e)
      }))[1] && r[0] && r[2]) {
      var a = r[0].slice(2, 7),
        s = r[0].slice(7, 9),
        u = m.xorEncrypt(r[1] || "", a).split("~");
      n.outtime = u[3] - 0, n.encrypt_id = u[2], n.jjt = "t";
      var c = u[0] - 0 || 0;
      c && "number" == typeof c && (n.time_correction = !0, n.expire = c);
      var l = c - m.getCurrentTime() || 0;
      return n.q = l, n.cf_v = s, n
    }
    return n
  },
  sha1: function (s) {
    var data = new Uint8Array(this.encodeUTF8(s))
    var i, j, t;
    var l = ((data.length + 8) >>> 6 << 4) + 16,
      s = new Uint8Array(l << 2);
    s.set(new Uint8Array(data.buffer)), s = new Uint32Array(s.buffer);
    for (t = new DataView(s.buffer), i = 0; i < l; i++) s[i] = t.getUint32(i << 2);
    s[data.length >> 2] |= 0x80 << (24 - (data.length & 3) * 8);
    s[l - 1] = data.length << 3;
    var w = [],
      f = [
        function () {
          return m[1] & m[2] | ~m[1] & m[3];
        },
        function () {
          return m[1] ^ m[2] ^ m[3];
        },
        function () {
          return m[1] & m[2] | m[1] & m[3] | m[2] & m[3];
        },
        function () {
          return m[1] ^ m[2] ^ m[3];
        }
      ],
      rol = function (n, c) {
        return n << c | n >>> (32 - c);
      },
      k = [1518500249, 1859775393, -1894007588, -899497514],
      m = [1732584193, -271733879, null, null, -1009589776];
    m[2] = ~m[0], m[3] = ~m[1];
    for (var i = 0; i < s.length; i += 16) {
      var o = m.slice(0);
      for (j = 0; j < 80; j++)
        w[j] = j < 16 ? s[i + j] : rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1),
        t = rol(m[0], 5) + f[j / 20 | 0]() + m[4] + w[j] + k[j / 20 | 0] | 0,
        m[1] = rol(m[1], 30), m.pop(), m.unshift(t);
      for (j = 0; j < 5; j++) m[j] = m[j] + o[j] | 0;
    };
    t = new DataView(new Uint32Array(m).buffer);
    for (var i = 0; i < 5; i++) m[i] = t.getUint32(i << 2);

    var hex = Array.prototype.map.call(new Uint8Array(new Uint32Array(m).buffer), function (e) {
      return (e < 16 ? "0" : "") + e.toString(16);
    }).join("");
    return hex.toString().toUpperCase();
  },
  encodeUTF8: function (s) {
    var i, r = [],
      c, x;
    for (i = 0; i < s.length; i++)
      if ((c = s.charCodeAt(i)) < 0x80) r.push(c);
      else if (c < 0x800) r.push(0xC0 + (c >> 6 & 0x1F), 0x80 + (c & 0x3F));
    else {
      if ((x = c ^ 0xD800) >> 10 == 0) //对四字节UTF-16转换为Unicode
        c = (x << 10) + (s.charCodeAt(++i) ^ 0xDC00) + 0x10000,
        r.push(0xF0 + (c >> 18 & 0x7), 0x80 + (c >> 12 & 0x3F));
      else r.push(0xE0 + (c >> 12 & 0xF));
      r.push(0x80 + (c >> 6 & 0x3F), 0x80 + (c & 0x3F));
    };
    return r;
  },
  gettoken: function (UA) {
    const https = require('https');
    var body = `content={"appname":"50082","whwswswws":"","jdkey":"","body":{"platform":"1"}}`;
    return new Promise((resolve, reject) => {
      let options = {
        hostname: "bh.m.jd.com",
        port: 443,
        path: "/gettoken",
        method: "POST",
        rejectUnauthorized: false,
        headers: {
          "Content-Type": "text/plain;charset=UTF-8",
          "Host": "bh.m.jd.com",
          "Origin": "https://h5.m.jd.com",
          "X-Requested-With": "com.jingdong.app.mall",
          "Referer": "https://h5.m.jd.com/babelDiy/Zeus/41Lkp7DumXYCFmPYtU3LTcnTTXTX/index.html",
          "User-Agent": UA,
        }
      }
      const req = https.request(options, (res) => {
        res.setEncoding('utf-8');
        let rawData = '';
        res.on('error', reject);
        res.on('data', chunk => rawData += chunk);
        res.on('end', () => resolve(rawData));
      });
      req.write(body);
      req.on('error', reject);
      req.end();
    });
  },
  get_blog: function (pin) {
    let encrypefun = {
      "z": function (p1, p2) {
        var str = "";
        for (var vi = 0; vi < p1.length; vi++) {
          str += (p1.charCodeAt(vi) ^ p2.charCodeAt(vi % p2.length)).toString("16");
        }
        return str;
      },
      "y": function (p1, p2) {
        var str = "";
        for (var vi = 0; vi < p1.length; vi++) {
          str += (p1.charCodeAt(vi) & p2.charCodeAt(vi % p2.length)).toString("16");
        }
        return str;
      },
      "x": function (p1, p2) {
        p1 = p1.substring(1) + p1.substring(0, 1);
        p2 = p2.substring((p2.length - 1)) + p2.substring(0, (p2.length - 1));
        var str = "";
        for (var vi = 0; vi < p1.length; vi++) {
          str += (p1.charCodeAt(vi) ^ p2.charCodeAt(vi % p2.length)).toString("16");
        }
        return str;
      },
      "jiami": function (po, p1) {
        var str = "";
        for (vi = 0; vi < po.length; vi++) {
          str += String.fromCharCode(po.charCodeAt(vi) ^ p1.charCodeAt(vi % p1.length));
        }
        return new Buffer.from(str).toString('base64');
      }
    }
    const ids = ["x", "y", "z"];
    var encrypeid = ids[Math.floor(Math.random() * 1e8) % ids.length];
    var timestamp = this.getCurrentTime();
    var nonce_str = this.getRandomWord(10);
    var isDefaultKey = "B";
    // timestamp = 1627139784174;
    refer = "com.miui.home";
    encrypeid = "x";
    //nonce_str = "jNN40H0elF";
    var json = {
      r: refer,
      a: "",
      c: "a",
      v: "2.5.8",
      t: timestamp.toString().substring(timestamp.toString().length - 4)
    }
    var token = md5(pin);
    var key = encrypefun[encrypeid](timestamp.toString(), nonce_str);
    //console.log(key);
    var cipher = encrypefun["jiami"](JSON.stringify(json), key);
    //sOf+"~1"+sa1+sb+"~"+sb1+"~~~"+str+"~"+sa+"~"+sa2;
    //"1627139784174~1jNN40H0elF14e91ebb633928c23d5afbaa8f947952~x~~~B~TBJHGg0bVAlaF1oPTVwfXQtaVBdJFQcVChcaGxtURA0bVkQUF0cXXhUDG1AZXhUcF0wVAxVSBg4DREU=~0v3u0bq",
    return `${timestamp}~1${nonce_str + token}~${encrypeid}~~~${isDefaultKey}~${cipher}~${this.getCrcCode(cipher)}`;
  },
  get_risk_result: async function ($) {
    var appid = "50082";
    var TouchSession = this.getTouchSession();
    if (!$.joyytoken || $.joyytoken_count > 18) {
      $.joyytoken = JSON.parse(await this.gettoken($.UA))["joyytoken"];
      //console.log("第一次请求joyytoken");
      $.joyytoken_count = 0;
    }
    $.joyytoken_count++;
    let riskData;
    switch ($.action) {
      case 'startTask':
        riskData = {
          taskId: $.id
        };
        break;
      case 'chargeScores':
        riskData = {
          bubleId: $.id
        };
        break;
      case 'sign':
        riskData = {};
      default:
        break;
    }

    var random = Math.floor(1e+6 * Math.random()).toString().padEnd(6, '8');
    var senddata = this.objToString2(this.RecursiveSorting({
      pin: $.UserName,
      random,
      ...riskData
    }));
    var time = this.getCurrentTime();
    // time = 1626970587918;
    var encrypt_id = this.decipherJoyToken(appid + $.joyytoken, appid)["encrypt_id"].split(",");
    var nonce_str = this.getRandomWord(10);
    // nonce_str="iY8uFBbYX7";
    var key = this.getKey(encrypt_id[2], nonce_str, time.toString());

    var str1 = `${senddata}&token=${$.joyytoken}&time=${time}&nonce_str=${nonce_str}&key=${key}&is_trust=1`;
    //console.log(str1);
    str1 = this.sha1(str1);
    var outstr = [time, "1" + nonce_str + $.joyytoken, encrypt_id[2] + "," + encrypt_id[3]];
    outstr.push(str1);
    outstr.push(this.getCrcCode(str1));
    outstr.push("C");
    var data = {}
    data = {
      tm: [],
      tnm: [],
      grn: $.joyytoken_count,
      ss: TouchSession,
      wed: 'ttttt',
      wea: 'ffttttua',
      pdn: [7, (Math.floor(Math.random() * 1e8) % 180) + 1, 6, 11, 1, 5],
      jj: 1,
      cs: hexMD5("Object.P.<computed>=&HTMLDocument.Ut.<computed>=https://storage.360buyimg.com/babel/00750963/1942873/production/dev/main.e5d1c436.js"),
      // cs:"4d8853ea4e4f10d8bde7071dd7fb0ed4",
      np: 'iPhone',
      t: time,
      jk: `${$.UUID}`,
      fpb: '',
      nv: 'Apple Computer, Inc.',
      nav: '167741',
      scr: [896, 414],
      ro: [
        'iPhone12,1',
        'iOS',
        '14.3',
        '10.0.8',
        '167741',
        `${$.UUID}`,
        'a'
      ],
      ioa: 'fffffftt',
      aj: 'u',
      ci: 'w3.1.0',
      cf_v: '01',
      bd: senddata,
      mj: [1, 0, 0],
      blog: "a",
      msg: ''
    }
    // console.log(data);
    //console.log(JSON.stringify(data));
    data = new Buffer.from(this.xorEncrypt(JSON.stringify(data), key)).toString('base64');
    //console.log(data);
    outstr.push(data);
    outstr.push(this.getCrcCode(data));
    //console.log(outstr.join("~"));
    return {
      extraData: {
        log: outstr.join("~"),
        sceneid: "DDhomePageh5"
      },
      ...riskData,
      random,
    };
  }
};

/**********************************[ZooFaker]****************************************/


/*************************************[Main]****************************************/


function main(cookieJson){
  const $ = new Env('点点券');

  let allMessage = ``;
  const notify = '';
  //Node.js用户请在jdCookie.js处填写京东ck;
  const jdCookieNode = cookieJson;
  const openUrl = `openjd://virtual?params=%7B%20%22category%22:%20%22jump%22,%20%22des%22:%20%22m%22,%20%22url%22:%20%22https://h5.m.jd.com/babelDiy/Zeus/41Lkp7DumXYCFmPYtU3LTcnTTXTX/index.html%22%20%7D`

  $.UA = ``;
  $.UUID = ``;
  $.joyytoken_count = 1
  getUA()
  let message = '';
  let nowTimes = new Date(new Date().getTime() + new Date().getTimezoneOffset() * 60 * 1000 + 8 * 60 * 60 * 1000);

  let cookie = '';

  const JD_API_HOST = 'https://api.m.jd.com/api';

  !(async () => {
    // console.log(`\n通知：京东已在领取任务、签到、领取点点券三个添加了log做了校验，暂时无可解决\n`);
    $.cookie = cookiesArr[i];
    $.UserName = decodeURIComponent(cookie.match(/pt_pin=([^; ]+)(?=;?)/) && cookie.match(/pt_pin=([^; ]+)(?=;?)/)[1])      
    $.index = i + 1;
    $.isLogin = true;
    $.nickName = '';
    console.log(`\n开始【京东账号${$.index}】${$.nickName || $.UserName}\n`);
    await jd_necklace();
  })().catch((e) => {
    $.log('', `❌ ${$.name}, 失败! 原因: ${e}!`, '')
  })
  .finally(() => {
    $.done();
  })

}


/*************************************[Main]****************************************/

async function jd_necklace() {
  try {
    await necklace_homePage();
    await $.wait(2000)
    await doTask();
    await $.wait(2000)
    await sign();
    await $.wait(2000)
    await necklace_homePage();
    await receiveBubbles();
    await necklace_homePage();
    // // await necklace_exchangeGift($.totalScore);//自动兑换多少钱的无门槛红包，1000代表1元，默认兑换全部点点券
    await showMsg();
    await $.wait(2000)
  } catch (e) {
    $.logErr(e)
  }
}

function showMsg() {
  return new Promise(async resolve => {
    $.msg($.name, '', `京东账号${$.index} ${$.nickName || $.UserName}\n${(errorMsgLllegal > 0 && "当前有" + errorMsgLllegal + "个[非法请求]任务\n") || ""}当前${$.name}：${$.totalScore}个\n可兑换无门槛红包：${$.totalScore / 1000}元\n点击弹窗即可去兑换(注：此红包具有时效性)`, {
      'open-url': openUrl
    });
    // 云端大于10元无门槛红包时进行通知推送
    // if ($.isNode() && $.totalScore >= 20000 && nowTimes.getHours() >= 20) await notify.sendNotify(`${$.name} - 京东账号${$.index}`, `京东账号${$.index}\n当前${$.name}：${$.totalScore}个\n可兑换无门槛红包：${$.totalScore / 1000}元\n点击链接即可去兑换(注：此红包具有时效性)\n↓↓↓ \n\n ${openUrl} \n\n ↑↑↑`, { url: openUrl })
    if ($.isNode() && (nowTimes.getHours() >= 20 || errorMsgLllegal > 0) && (process.env.DDQ_NOTIFY_CONTROL ? process.env.DDQ_NOTIFY_CONTROL === 'false' : !!1)) {
      allMessage += `京东账号${$.index} ${$.nickName || $.UserName}\n${(errorMsgLllegal > 0 && "当前有" + errorMsgLllegal + "个[非法请求]任务\n") || ""}当前${$.name}：${$.totalScore}个\n可兑换无门槛红包：${$.totalScore / 1000}元\n(京东APP->领券->左上角点点券.注：此红包具有时效性)${$.index !== cookiesArr.length ? '\n\n' : `\n↓↓↓ \n\n "https://h5.m.jd.com/babelDiy/Zeus/41Lkp7DumXYCFmPYtU3LTcnTTXTX/index.html" \n\n ↑↑↑`}`
    }
    resolve()
  })
}
async function doTask() {
  for (let item of $.taskConfigVos) {
    if (item.taskStage === 0) {
      console.log(`【${item.taskName}】 任务未领取,开始领取此任务`);
      let res = await necklace_startTask(item.id);
      if (res && res.rtn_code == 0) {
        console.log(`【${item.taskName}】 任务领取成功,开始完成此任务`);
        await $.wait(2000);
        await reportTask(item);
        await $.wait(2000)
      }
    } else if (item.taskStage === 2) {
      console.log(`【${item.taskName}】 任务已做完,奖励未领取`);
    } else if (item.taskStage === 3) {
      console.log(`${item.taskName}奖励已领取`);
    } else if (item.taskStage === 1) {
      console.log(`\n【${item.taskName}】 任务已领取但未完成,开始完成此任务`);
      await reportTask(item);
      await $.wait(2000)
    }
  }
}
async function receiveBubbles() {
  for (let item of $.bubbles) {
    console.log(`\n开始领取点点券`);
    await necklace_chargeScores(item.id)
    await $.wait(2000)
  }
}
async function sign() {
  if ($.signInfo.todayCurrentSceneSignStatus === 1) {
    console.log(`\n开始每日签到`)
    await necklace_sign();
  } else {
    console.log(`当前${new Date(new Date().getTime() + new Date().getTimezoneOffset() * 60 * 1000 + 8 * 60 * 60 * 1000).toLocaleString()}已签到`)
  }
}
async function reportTask(item = {}) {
  //普通任务
  if (item['taskType'] === 2) await necklace_startTask(item.id, 'necklace_reportTask');
  //逛很多商品店铺等等任务
  if (item['taskType'] === 6 || item['taskType'] === 8 || item['taskType'] === 5 || item['taskType'] === 9) {
    //浏览精选活动任务
    await necklace_getTask(item.id);
    $.taskItems = $.taskItems.filter(value => !!value && value['status'] === 0);
    for (let vo of $.taskItems) {
      console.log(`浏览精选活动 【${vo['title']}】`);
      await necklace_startTask(item.id, 'necklace_reportTask', vo['id']);
    }
  }
  //首页浏览XX秒的任务
  // console.log(item)
  if (item['taskType'] === 3) await doAppTask('3', item.id);
  if (item['taskType'] === 4) await doAppTask('4', item.id);
}
//每日签到福利
function necklace_sign() {
  return new Promise(async resolve => {
    $.action = 'sign'
    const body = await ZooFaker.get_risk_result($)
    // const body = {
    //   currentDate: $.lastRequestTime.replace(/:/g, "%3A"),
    // }
    $.post(taskPostUrl("necklace_sign", body), async (err, resp, data) => {
      try {
        if (err) {
          console.log(`${JSON.stringify(err)}`)
          console.log(`${$.name} API请求失败，请检查网路重试`)
        } else {
          if (safeGet(data)) {
            data = JSON.parse(data);
            if (data.rtn_code === 0) {
              if (data.data.biz_code === 0) {
                console.log(`签到成功，时间：${new Date(new Date().getTime() + new Date().getTimezoneOffset() * 60 * 1000 + 8 * 60 * 60 * 1000).toLocaleString()}`)
                // $.taskConfigVos = data.data.result.taskConfigVos;
                // $.exchangeGiftConfigs = data.data.result.exchangeGiftConfigs;
              }
            } else if (data.rtn_code === 403 || data.rtn_msg.indexOf('非法请求') > -1) {
              console.log(`每日签到失败：${data.rtn_msg}\n`)
              errorMsgLllegal += 1
              getUA()
            } else {
              console.log(`每日签到失败：${JSON.stringify(data)}\n`);
            }
          }
        }
      } catch (e) {
        $.logErr(e, resp)
      } finally {
        resolve(data);
      }
    })
  })
}
//兑换无门槛红包
function necklace_exchangeGift(scoreNums) {
  return new Promise(resolve => {
    const body = {
      scoreNums,
      "giftConfigId": 31,
      currentDate: $.lastRequestTime.replace(/:/g, "%3A"),
    }
    $.post(taskPostUrl("necklace_exchangeGift", body), async (err, resp, data) => {
      try {
        if (err) {
          console.log(`${JSON.stringify(err)}`)
          console.log(`${$.name} API请求失败，请检查网路重试`)
        } else {
          if (safeGet(data)) {
            data = JSON.parse(data);
            if (data.rtn_code === 0) {
              if (data.data.biz_code === 0) {
                const {
                  result
                } = data.data;
                message += `${result.redpacketTitle}：${result.redpacketAmount}元兑换成功\n`;
                message += `红包有效期：${new Date(result.endTime + new Date().getTimezoneOffset() * 60 * 1000 + 8 * 60 * 60 * 1000).toLocaleString('zh', { hour12: false })}`;
                console.log(message)
              }
            }
          }
        }
      } catch (e) {
        $.logErr(e, resp)
      } finally {
        resolve(data);
      }
    })
  })
}
//领取奖励
function necklace_chargeScores(bubleId) {
  return new Promise(async resolve => {
    $.id = bubleId
    $.action = 'chargeScores'
    const body = await ZooFaker.get_risk_result($);
    // const body = {
    //   bubleId,
    //   currentDate: $.lastRequestTime.replace(/:/g, "%3A"),
    // }
    $.post(taskPostUrl("necklace_chargeScores", body), async (err, resp, data) => {
      try {
        if (err) {
          console.log(`${JSON.stringify(err)}`)
          console.log(`${$.name} API请求失败，请检查网路重试`)
        } else {
          if (safeGet(data)) {
            data = JSON.parse(data);
            if (data.rtn_code === 0) {
              if (data.data.biz_code === 0) {
                // $.taskConfigVos = data.data.result.taskConfigVos;
                // $.exchangeGiftConfigs = data.data.result.exchangeGiftConfigs;
              }
            } else if (data.rtn_code === 403 || data.rtn_msg.indexOf('非法请求') > -1) {
              console.log(`领取奖励失败：${data.rtn_msg}\n`)
              errorMsgLllegal += 1
              getUA()
            } else {
              console.log(`领取奖励失败：${JSON.stringify(data)}\n`)
            }
          }
        }
      } catch (e) {
        $.logErr(e, resp)
      } finally {
        resolve(data);
      }
    })
  })
}

function necklace_startTask(taskId, functionId = 'necklace_startTask', itemId = "") {
  return new Promise(async resolve => {
    let body = {
      taskId,
      currentDate: $.lastRequestTime.replace(/:/g, "%3A"),
    }
    if (functionId == 'necklace_startTask') {
      $.id = taskId
      $.action = 'startTask'
      body = await ZooFaker.get_risk_result($)
    } else {
      if (itemId) body['itemId'] = itemId;
    }
    $.post(taskPostUrl(functionId, body), async (err, resp, data) => {
      try {
        if (err) {
          console.log(`${JSON.stringify(err)}`)
          console.log(`${$.name} API请求失败，请检查网路重试`)
        } else {
          console.log(`${functionId === 'necklace_startTask' ? '领取任务结果' : '做任务结果'}：${data}`);
          if (safeGet(data)) {
            data = JSON.parse(data);
            if (data.rtn_code === 0) {
              if (data.data.biz_code === 0) {
                // $.taskConfigVos = data.data.result.taskConfigVos;
                // $.exchangeGiftConfigs = data.data.result.exchangeGiftConfigs;
              }
            } else if (data.rtn_code === 403 || data.rtn_msg.indexOf('非法请求') > -1) {
              console.log(`${functionId === 'necklace_startTask' ? '领取任务失败' : '做任务结果'}：${data.rtn_msg}\n`)
              errorMsgLllegal += 1
              getUA()
            } else {
              console.log(`${functionId === 'necklace_startTask' ? '领取任务失败' : '做任务结果'}：${JSON.stringify(data)}\n`)
            }
          }
        }
      } catch (e) {
        $.logErr(e, resp)
      } finally {
        resolve(data);
      }
    })
  })
}

function necklace_getTask(taskId) {
  return new Promise(resolve => {
    const body = {
      taskId,
      currentDate: $.lastRequestTime.replace(/:/g, "%3A"),
    }
    $.taskItems = [];
    $.post(taskPostUrl("necklace_getTask", body), async (err, resp, data) => {
      try {
        if (err) {
          console.log(`${JSON.stringify(err)}`)
          console.log(`${$.name} API请求失败，请检查网路重试`)
        } else {
          if (safeGet(data)) {
            data = JSON.parse(data);
            if (data.rtn_code === 0) {
              if (data.data.biz_code === 0) {
                $.taskItems = data.data.result && data.data.result.taskItems;
              }
            } else if (data.rtn_code === 403 || data.rtn_msg.indexOf('非法请求') > -1) {
              console.log(`浏览精选活动失败：${data.rtn_msg}\n`)
              errorMsgLllegal += 1
              getUA()
            } else {
              console.log(`浏览精选活动失败：${JSON.stringify(data)}\n`)
            }
          }
        }
      } catch (e) {
        $.logErr(e, resp)
      } finally {
        resolve();
      }
    })
  })
}

function necklace_homePage() {
  $.taskConfigVos = [];
  $.bubbles = [];
  $.signInfo = {};
  return new Promise(resolve => {
    $.post(taskPostUrl('necklace_homePage'), async (err, resp, data) => {
      try {
        if (err) {
          console.log(`${JSON.stringify(err)}`)
          console.log(`${$.name} API请求失败，请检查网路重试`)
        } else {
          if (safeGet(data)) {
            data = JSON.parse(data);
            if (data.rtn_code === 0) {
              if (data.data.biz_code === 0) {
                $.taskConfigVos = data.data.result.taskConfigVos;
                $.exchangeGiftConfigs = data.data.result.exchangeGiftConfigs;
                $.lastRequestTime = data.data.result.lastRequestTime;
                $.bubbles = data.data.result.bubbles;
                $.signInfo = data.data.result.signInfo;
                $.totalScore = data.data.result.totalScore;
              }
            }
          }
        }
      } catch (e) {
        $.logErr(e, resp)
      } finally {
        resolve();
      }
    })
  })
}

async function doAppTask(type = '3', id) {
  console.log(id)
  let functionId = 'getCcTaskList'
  let body = "area=16_1315_3486_59648&body=%7B%22pageClickKey%22%3A%22CouponCenter%22%2C%22shshshfpb%22%3A%22dPH6zeJy%5C/HFogCIf0ZGFYqSDOShGwmpjVOPM%5C/ViCGC5fgBLL9JoI9mjgUt46vjSFeSkmU9DZLEjFaeFTWBj4Axg%3D%3D%22%2C%22eid%22%3A%22eidIeb54812323sf%2BAJEbj5LR0Kf6GUzM9DKXvgCReTpKTRyRwiuxY%5C/uvRHBqebAAKCAXkJFzhWtPj5uoHxNeK3DjTumb%2BrfXOt1w0%5C/dGmOJzfbLuyNo%22%2C%22childActivityUrl%22%3A%22openapp.jdmobile%253a%252f%252fvirtual%253fparams%253d%257b%255c%2522category%255c%2522%253a%255c%2522jump%255c%2522%252c%255c%2522des%255c%2522%253a%255c%2522couponCenter%255c%2522%257d%22%2C%22lat%22%3A%2224.49441271645999%22%2C%22globalLat%22%3A%2224.49335%22%2C%22lng%22%3A%22118.1447713674174%22%2C%22globalLng%22%3A%22118.1423%22%7D&build=167707&client=apple&clientVersion=10.0.4&d_brand=apple&d_model=iPhone12%2C1&eid=eidIeb54812323sf%2BAJEbj5LR0Kf6GUzM9DKXvgCReTpKTRyRwiuxY/uvRHBqebAAKCAXkJFzhWtPj5uoHxNeK3DjTumb%2BrfXOt1w0/dGmOJzfbLuyNo&isBackground=N&joycious=70&lang=zh_CN&networkType=wifi&networklibtype=JDNetworkBaseAF&openudid=8a0d1837f803a12eb217fcf5e1f8769cbb3f898d&osVersion=14.3&partner=apple&rfs=0000&scope=11&screen=828%2A1792&sign=75afd018b5751e9ac4cba0b51b8adb3c&st=1624535152771&sv=101&uemps=0-0&uts=0f31TVRjBStsz%2BC9YKuTtbGZPv/xrvQQdSUKvavez1nEbzXO4dLo%2BXEvUHAXAd0VPmZqkUNAf2yO/fBM7ImhPYnyBrotzw06Kk7qP6mG42fhA1t5BkW3ZGLaQgPtiuosYOHPMyHpg%2BJ9ZQBP4g3zsSFq2DUWsTOZbb85I4ThMCgqvymyLl48ebUg7aQTle9CfTJVWu5gx0YZ/ScklgN9Pg%3D%3D&uuid=hjudwgohxzVu96krv/T6Hg%3D%3D&wifiBssid=796606e8e181aa5865ec20728a27238b"
  await getCcTaskList(functionId, body, type);
  if (Number(id) == 229) {
    body = `area=16_1315_3486_59648&body=%7B%22shshshfpb%22%3A%22dPH6zeJy%5C/HFogCIf0ZGFYqSDOShGwmpjVOPM%5C/ViCGC5fgBLL9JoI9mjgUt46vjSFeSkmU9DZLEjFaeFTWBj4Axg%3D%3D%22%2C%22globalLng%22%3A%22118.1423%22%2C%22globalLat%22%3A%2224.49335%22%2C%22monitorSource%22%3A%22ccgroup_ios_index_task%22%2C%22monitorRefer%22%3A%22%22%2C%22taskType%22%3A%222%22%2C%22childActivityUrl%22%3A%22openapp.jdmobile%253a%252f%252fvirtual%253fparams%253d%257b%255c%2522category%255c%2522%253a%255c%2522jump%255c%2522%252c%255c%2522des%255c%2522%253a%255c%2522couponCenter%255c%2522%257d%22%2C%22pageClickKey%22%3A%22CouponCenter%22%2C%22lat%22%3A%2224.49441271645999%22%2C%22taskId%22%3A%22necklace_229%22%2C%22lng%22%3A%22118.1447713674174%22%2C%22eid%22%3A%22eidIeb54812323sf%2BAJEbj5LR0Kf6GUzM9DKXvgCReTpKTRyRwiuxY%5C/uvRHBqebAAKCAXkJFzhWtPj5uoHxNeK3DjTumb%2BrfXOt1w0%5C/dGmOJzfbLuyNo%22%7D&build=167707&client=apple&clientVersion=10.0.4&d_brand=apple&d_model=iPhone12%2C1&eid=eidIeb54812323sf%2BAJEbj5LR0Kf6GUzM9DKXvgCReTpKTRyRwiuxY/uvRHBqebAAKCAXkJFzhWtPj5uoHxNeK3DjTumb%2BrfXOt1w0/dGmOJzfbLuyNo&isBackground=N&joycious=70&lang=zh_CN&networkType=wifi&networklibtype=JDNetworkBaseAF&openudid=8a0d1837f803a12eb217fcf5e1f8769cbb3f898d&osVersion=14.3&partner=apple&rfs=0000&scope=11&screen=828%2A1792&sign=57453a76ffe9440d7961b05405fb4f13&st=1624535165882&sv=110&uemps=0-0&uts=0f31TVRjBStsz%2BC9YKuTtbGZPv/xrvQQdSUKvavez1nEbzXO4dLo%2BXEvUHAXAd0VPmZqkUNAf2yO/fBM7ImhPYnyBrotzw06Kk7qP6mG42fhA1t5BkW3ZGLaQgPtiuosYOHPMyHpg%2BJ9ZQBP4g3zsSFq2DUWsTOZbb85I4ThMCgqvymyLl48ebUg7aQTle9CfTJVWu5gx0YZ/ScklgN9Pg%3D%3D&uuid=hjudwgohxzVu96krv/T6Hg%3D%3D&wifiBssid=796606e8e181aa5865ec20728a27238b`
  } else if (Number(id) == 260) {
    body = `area=16_1315_3486_59648&body=%7B%22shshshfpb%22%3A%22hRRVbEkLST%2BoqUB6fhir%2BfMoJS814u0eqASGoy8xq0vV1m9X9zKoAVYtaZjcO4UsQaWNyUrMVkZK5HBZ5aJo5zQ%3D%3D%22%2C%22globalLng%22%3A%22118.1423%22%2C%22globalLat%22%3A%2224.49335%22%2C%22monitorSource%22%3A%22ccgroup_ios_index_task%22%2C%22monitorRefer%22%3A%22%22%2C%22taskType%22%3A%222%22%2C%22childActivityUrl%22%3A%22openapp.jdmobile%253a%252f%252fvirtual%253fparams%253d%257b%255c%2522category%255c%2522%253a%255c%2522jump%255c%2522%252c%255c%2522des%255c%2522%253a%255c%2522couponCenter%255c%2522%257d%22%2C%22pageClickKey%22%3A%22CouponCenter%22%2C%22lat%22%3A%2224.49435886957707%22%2C%22taskId%22%3A%22necklace_260%22%2C%22lng%22%3A%22118.144791637343%22%2C%22eid%22%3A%22eidI0faa812328s1AvGqBpW%2BSouJeXiZIORi9gLxq36FvXhk6SQPmtUFPglIaTIxGXnVzVAwHm%5C/QEwj14SR2vxSgH5tw4rWGdLJtHzSh8bloRLoX8mFY%22%7D&build=167568&client=apple&clientVersion=9.4.2&d_brand=apple&d_model=iPhone12%2C1&eid=eidI0faa812328s1AvGqBpW%2BSouJeXiZIORi9gLxq36FvXhk6SQPmtUFPglIaTIxGXnVzVAwHm/QEwj14SR2vxSgH5tw4rWGdLJtHzSh8bloRLoX8mFY&isBackground=N&joycious=51&lang=zh_CN&networkType=wifi&networklibtype=JDNetworkBaseAF&openudid=ebf4ce8ecbb641054b00c00483b1cee85660d196&osVersion=14.3&partner=apple&rfs=0000&scope=11&screen=828%2A1792&sign=93249982ced7ec850c69de8b3e859dab&st=1624610691429&sv=110&uts=0f31TVRjBSsqndu4/jgUPz6uymy50MQJSTfJm3Nbyn7GqB7OtrJRuHoZMYV%2Bs0mkEZsSwjxzwlDPXLeepml5BnM5XPZQmPVomYBHlkSfLJWR5D1y0Ovgf60fpjMS2gXL5aLh50cNO3cmx2GvVTaTeYxvRUl%2BpaW7HXsuBhxJgA6pUzd01tBX9yiFih8xvToesg91Nl8KcWGYzXJ2/hWKXg%3D%3D&uuid=hjudwgohxzVu96krv/T6Hg%3D%3D&wifiBssid=796606e8e181aa5865ec20728a27238b`
  } else if (Number(id) == 267) {
    body = `area=16_1315_3486_59648&body=%7B%22shshshfpb%22%3A%22dPH6zeJy%5C/HFogCIf0ZGFYqSDOShGwmpjVOPM%5C/ViCGC5fgBLL9JoI9mjgUt46vjSFeSkmU9DZLEjFaeFTWBj4Axg%3D%3D%22%2C%22globalLng%22%3A%22118.1423%22%2C%22globalLat%22%3A%2224.49335%22%2C%22monitorSource%22%3A%22ccgroup_ios_index_task%22%2C%22monitorRefer%22%3A%22%22%2C%22taskType%22%3A%222%22%2C%22childActivityUrl%22%3A%22openapp.jdmobile%253a%252f%252fvirtual%253fparams%253d%257b%255c%2522category%255c%2522%253a%255c%2522jump%255c%2522%252c%255c%2522des%255c%2522%253a%255c%2522couponCenter%255c%2522%257d%22%2C%22pageClickKey%22%3A%22CouponCenter%22%2C%22lat%22%3A%2224.49437467152672%22%2C%22taskId%22%3A%22necklace_267%22%2C%22lng%22%3A%22118.1447981202065%22%2C%22eid%22%3A%22eidIeb54812323sf%2BAJEbj5LR0Kf6GUzM9DKXvgCReTpKTRyRwiuxY%5C/uvRHBqebAAKCAXkJFzhWtPj5uoHxNeK3DjTumb%2BrfXOt1w0%5C/dGmOJzfbLuyNo%22%7D&build=167707&client=apple&clientVersion=10.0.4&d_brand=apple&d_model=iPhone12%2C1&eid=eidIeb54812323sf%2BAJEbj5LR0Kf6GUzM9DKXvgCReTpKTRyRwiuxY/uvRHBqebAAKCAXkJFzhWtPj5uoHxNeK3DjTumb%2BrfXOt1w0/dGmOJzfbLuyNo&isBackground=N&joycious=70&lang=zh_CN&networkType=wifi&networklibtype=JDNetworkBaseAF&openudid=8a0d1837f803a12eb217fcf5e1f8769cbb3f898d&osVersion=14.3&partner=apple&rfs=0000&scope=11&screen=828%2A1792&sign=64e2361aa2a81068930c0c3325fd45ef&st=1624950832218&sv=111&uemps=0-0&uts=0f31TVRjBSsMGLCxYS3UIqlZl8dbXmnuZ4ayfhN43Ot1QaV41onc66czNm7agt5ZxuI/ZiEjTyLMd9C68bu6j250BhqFBz9aHYMZHRsZRt99av4Tsia77GOWxlDaSYf5ixm0pZhBRR4OQ%2BUBD6%2BPW4wCMOS5CO3/VI2cFHPfi%2BdGNinbfncIha86vGUGuGKiHSAf4rUFY4wrafX6Rksw7g%3D%3D&uuid=hjudwgohxzVu96krv/T6Hg%3D%3D&wifiBssid=796606e8e181aa5865ec20728a27238b`
  } else if (Number(id) == 273) {
    body = `area=16_1315_3486_59648&body=%7B%22shshshfpb%22%3A%22dPH6zeJy%5C/HFogCIf0ZGFYqSDOShGwmpjVOPM%5C/ViCGC5fgBLL9JoI9mjgUt46vjSFeSkmU9DZLEjFaeFTWBj4Axg%3D%3D%22%2C%22globalLng%22%3A%22118.1423%22%2C%22globalLat%22%3A%2224.49335%22%2C%22monitorSource%22%3A%22ccgroup_ios_index_task%22%2C%22monitorRefer%22%3A%22%22%2C%22taskType%22%3A%222%22%2C%22childActivityUrl%22%3A%22openapp.jdmobile%253a%252f%252fvirtual%253fparams%253d%257b%255c%2522category%255c%2522%253a%255c%2522jump%255c%2522%252c%255c%2522des%255c%2522%253a%255c%2522couponCenter%255c%2522%257d%22%2C%22pageClickKey%22%3A%22CouponCenter%22%2C%22lat%22%3A%2224.494383110087%22%2C%22taskId%22%3A%22necklace_273%22%2C%22lng%22%3A%22118.1447767134287%22%2C%22eid%22%3A%22eidIeb54812323sf%2BAJEbj5LR0Kf6GUzM9DKXvgCReTpKTRyRwiuxY%5C/uvRHBqebAAKCAXkJFzhWtPj5uoHxNeK3DjTumb%2BrfXOt1w0%5C/dGmOJzfbLuyNo%22%7D&build=167741&client=apple&clientVersion=10.0.8&d_brand=apple&d_model=iPhone12%2C1&eid=eidIeb54812323sf%2BAJEbj5LR0Kf6GUzM9DKXvgCReTpKTRyRwiuxY/uvRHBqebAAKCAXkJFzhWtPj5uoHxNeK3DjTumb%2BrfXOt1w0/dGmOJzfbLuyNo&isBackground=N&joycious=71&lang=zh_CN&networkType=wifi&networklibtype=JDNetworkBaseAF&openudid=8a0d1837f803a12eb217fcf5e1f8769cbb3f898d&osVersion=14.3&partner=apple&rfs=0000&scope=11&screen=828%2A1792&sign=c5f1773c699259a32596629ff17c77af&st=1627034890276&sv=101&uemps=0-0&uts=0f31TVRjBSuc9dw/M%2Bj%2BYjMPuoLDUbUPjPag%2BZ5OSbdXPyIGbVBxfPOWG8Z24KZdpryfyfoAUE5oYfYi1SuqGZ5atF1ARqzdFrPeo%2BZQVMmuwn/nYDGsLdj0Q9HcidhJXAaY1ti0j023Mv4f/ls51fJl5ypecBgw2sWtd8KiGQncYOe9GxCz6tlkHuSHDk3zN6hF%2BN0deRJOqJP8OOrJog%3D%3D&uuid=hjudwgohxzVu96krv/T6Hg%3D%3D&wifiBssid=796606e8e181aa5865ec20728a27238b`
  } else if (Number(id) == 281) {
    body = `area=16_1332_42932_43102&body=%7B%22shshshfpb%22%3A%22hRRVbEkLST%2BoqUB6fhir%2BfMoJS814u0eqASGoy8xq0vV1m9X9zKoAVYtaZjcO4UsQaWNyUrMVkZK5HBZ5aJo5zQ%3D%3D%22%2C%22globalLng%22%3A%22118.541458%22%2C%22globalLat%22%3A%2224.609455%22%2C%22monitorSource%22%3A%22ccgroup_ios_index_task%22%2C%22monitorRefer%22%3A%22%22%2C%22taskType%22%3A%222%22%2C%22childActivityUrl%22%3A%22openapp.jdmobile%253a%252f%252fvirtual%253fparams%253d%257b%255c%2522category%255c%2522%253a%255c%2522jump%255c%2522%252c%255c%2522des%255c%2522%253a%255c%2522couponCenter%255c%2522%257d%22%2C%22pageClickKey%22%3A%22CouponCenter%22%2C%22lat%22%3A%2224.49440185204448%22%2C%22taskId%22%3A%22necklace_281%22%2C%22lng%22%3A%22118.1448096802756%22%2C%22eid%22%3A%22eidI0faa812328s1AvGqBpW%2BSouJeXiZIORi9gLxq36FvXhk6SQPmtUFPglIaTIxGXnVzVAwHm%5C/QEwj14SR2vxSgH5tw4rWGdLJtHzSh8bloRLoX8mFY%22%7D&build=167741&client=apple&clientVersion=10.0.8&d_brand=apple&d_model=iPhone12%2C1&eid=eidI0faa812328s1AvGqBpW%2BSouJeXiZIORi9gLxq36FvXhk6SQPmtUFPglIaTIxGXnVzVAwHm/QEwj14SR2vxSgH5tw4rWGdLJtHzSh8bloRLoX8mFY&isBackground=N&joycious=51&lang=zh_CN&networkType=wifi&networklibtype=JDNetworkBaseAF&openudid=ebf4ce8ecbb641054b00c00483b1cee85660d196&osVersion=14.3&partner=apple&rfs=0000&scope=11&screen=828%2A1792&sign=6bf1da7e3c218998ae5bd34a5b9b0d5c&st=1627088377408&sv=122&uemps=0-1&uts=0f31TVRjBSsqndu4/jgUPz6uymy50MQJPuQXd3Iw2YAKsnsGHXGtpI6DTtbcnaz7p7QeCmsoL2Cl/BMWopi0bEL/HBdhfK3iH/oMP6POfCzGYqGUp9HjUx/7lG%2BGpzuUJ%2B7ZrAQF4UMuG2/9epLOLCkpw4w6EgF4FqamAtXxTBCJZ82M%2Bkm26wJx996BKm7JCzdQfT6pJ0aFbovPOlp71A%3D%3D&uuid=hjudwgohxzVu96krv/T6Hg%3D%3D&wifiBssid=796606e8e181aa5865ec20728a27238b`
  }
  if (type === '4') {
    // https://h5.m.jd.com/babelDiy/Zeus/2fDwtAwAQX1PJh51f3UXzLhKiD86/index.html
    console.log('需等待30秒')
    functionId = 'reportSinkTask'
    body = `&appid=XPMSGC2019&monitorSource=&uuid=16245525345801334814959&body=%7B%22platformType%22%3A%221%22%2C%22taskId%22%3A%22necklace_${id}%22%7D&client=m&clientVersion=4.6.0&area=16_1315_1316_59175&geo=%5Bobject%20Object%5D`
    await $.wait(15000);
  } else {
    // https://h5.m.jd.com/babelDiy/Zeus/3TcqzbLKXwyiGDzrn5nKV7sSEC8N/index.html
    console.log('需等待15秒')
    functionId = 'reportCcTask'
  }
  await $.wait(1600);
  await getCcTaskList(functionId, body, type);
}

function getCcTaskList(functionId, body, type = '3') {
  let url = `https://api.m.jd.com/client.action?functionId=${functionId}`
  return new Promise(resolve => {
    if (functionId === 'getCcTaskList') {

    }
    if (functionId === 'reportCcTask') {

    }
    if (functionId === 'reportSinkTask') {
      url += body
      body = ''
    }
    // if (type === '4' && functionId === 'reportCcTask'){
    // url = `https://api.m.jd.com/client.action?functionId=${functionId}&body=${escape(JSON.stringify(body))}&uuid=8888888&client=apple&clientVersion=9.4.1&st=1622193986049&sign=f5abd9fd7b9b8abaa25b34088f9e8a54&sv=102`
    // body = `body=${escape(JSON.stringify(body))}`
    // }
    const options = {
      url,
      body,
      headers: {
        "Accept": "application/json, text/plain, */*",
        "Accept-Encoding": "gzip, deflate, br",
        "Accept-Language": "zh-cn",
        "Connection": "keep-alive",
        "Content-Length": "63",
        "Content-Type": "application/x-www-form-urlencoded",
        "Host": "api.m.jd.com",
        "Origin": "https://h5.m.jd.com",
        "Cookie": cookie + `joyytoken=${"50082" + $.joyytoken};`,
        "Referer": "https://h5.m.jd.com/babelDiy/Zeus/4ZK4ZpvoSreRB92RRo8bpJAQNoTq/index.html",
        "User-Agent": $.UA,
      }
    }
    $.post((options), async (err, resp, data) => {
      try {
        if (err) {
          console.log(`${JSON.stringify(err)}`)
          console.log(`${$.name} API请求失败，请检查网路重试`)
        } else {
          if (safeGet(data)) {
            if (type === '3' && functionId === 'reportCcTask') console.log(`点击首页领券图标(进入领券中心浏览15s)任务:${data}`)
            if (type === '4' && functionId === 'reportSinkTask') console.log(`点击“券后9.9”任务:${data}`)
            // data = JSON.parse(data);
          }
        }
      } catch (e) {
        $.logErr(e, resp)
      } finally {
        resolve();
      }
    })
  })
}

function taskPostUrl(function_id, body = {}) {
  const time = new Date().getTime() + new Date().getTimezoneOffset() * 60 * 1000 + 8 * 60 * 60 * 1000;
  return {
    url: `${JD_API_HOST}?functionId=${function_id}&appid=coupon-necklace&loginType=2&client=coupon-necklace&t=${Date.now()}`,
    body: `body=${escape(JSON.stringify(body))}`,
    headers: {
      'Host': 'api.m.jd.com',
      'accept': 'application/json, text/plain, */*',
      'content-type': 'application/x-www-form-urlencoded',
      'origin': 'https://h5.m.jd.com',
      'accept-language': 'zh-cn',
      'User-Agent': $.UA,
      'referer': 'https://h5.m.jd.com/',
      'cookie': cookie + `joyytoken=${"50082" + $.joyytoken};`
    }
  }
}

function safeGet(data) {
  try {
    if (typeof JSON.parse(data) == "object") {
      return true;
    }
  } catch (e) {
    console.log(e);
    console.log(`京东服务器访问数据为空，请检查自身设备网络情况`);
    return false;
  }
}

function jsonParse(str) {
  if (typeof str == "string") {
    try {
      return JSON.parse(str);
    } catch (e) {
      console.log(e);
      $.msg($.name, '', '请勿随意在BoxJs输入框修改内容\n建议通过脚本去获取cookie')
      return [];
    }
  }
}

function getUA() {
  $.UA = `jdapp;iPhone;10.0.8;14.3;${randomString(40)};network/wifi;model/iPhone12,1;addressid/4199175193;appBuild/167741;jdSupportDarkMode/0;Mozilla/5.0 (iPhone; CPU iPhone OS 14_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/1`
  $.UUID = $.UA.split(';') && $.UA.split(';')[4] || ''
  $.joyytoken = ''
}

function randomString(e) {
  e = e || 32;
  let t = "abcdefhijkmnprstwxyz2345678",
    a = t.length,
    n = "";
  for (i = 0; i < e; i++)
    n += t.charAt(Math.floor(Math.random() * a));
  return n
}
/************************************************************/

function safeAdd(x, y) {
  var lsw = (x & 0xffff) + (y & 0xffff)
  var msw = (x >> 16) + (y >> 16) + (lsw >> 16)
  return (msw << 16) | (lsw & 0xffff)
}

/*
 * Bitwise rotate a 32-bit number to the left.
 */
function bitRotateLeft(num, cnt) {
  return (num << cnt) | (num >>> (32 - cnt))
}

function md5(string, key, raw) {
  if (!key) {
    if (!raw) {
      return hexMD5(string)
    }
    return rawMD5(string)
  }
  if (!raw) {
    return hexHMACMD5(key, string)
  }
  return rawHMACMD5(key, string)
}

/*
 * Convert a raw string to a hex string
 */
function rstr2hex(input) {
  var hexTab = '0123456789abcdef'
  var output = ''
  var x
  var i
  for (i = 0; i < input.length; i += 1) {
    x = input.charCodeAt(i)
    output += hexTab.charAt((x >>> 4) & 0x0f) + hexTab.charAt(x & 0x0f)
  }
  return output
}
/*
 * Encode a string as utf-8
 */
function str2rstrUTF8(input) {
  return unescape(encodeURIComponent(input))
}
/*
 * Calculate the MD5 of a raw string
 */
function rstrMD5(s) {
  return binl2rstr(binlMD5(rstr2binl(s), s.length * 8))
}

function hexMD5(s) {
  return rstr2hex(rawMD5(s))
}

function rawMD5(s) {
  return rstrMD5(str2rstrUTF8(s))
}

/*
 * These functions implement the four basic operations the algorithm uses.
 */
function md5cmn(q, a, b, x, s, t) {
  return safeAdd(bitRotateLeft(safeAdd(safeAdd(a, q), safeAdd(x, t)), s), b)
}

function md5ff(a, b, c, d, x, s, t) {
  return md5cmn((b & c) | (~b & d), a, b, x, s, t)
}

function md5gg(a, b, c, d, x, s, t) {
  return md5cmn((b & d) | (c & ~d), a, b, x, s, t)
}

function md5hh(a, b, c, d, x, s, t) {
  return md5cmn(b ^ c ^ d, a, b, x, s, t)
}

function md5ii(a, b, c, d, x, s, t) {
  return md5cmn(c ^ (b | ~d), a, b, x, s, t)
}

/*
 * Calculate the MD5 of an array of little-endian words, and a bit length.
 */
function binlMD5(x, len) {
  /* append padding */
  x[len >> 5] |= 0x80 << (len % 32)
  x[((len + 64) >>> 9 << 4) + 14] = len

  var i
  var olda
  var oldb
  var oldc
  var oldd
  var a = 1732584193
  var b = -271733879
  var c = -1732584194
  var d = 271733878

  for (i = 0; i < x.length; i += 16) {
    olda = a
    oldb = b
    oldc = c
    oldd = d

    a = md5ff(a, b, c, d, x[i], 7, -680876936)
    d = md5ff(d, a, b, c, x[i + 1], 12, -389564586)
    c = md5ff(c, d, a, b, x[i + 2], 17, 606105819)
    b = md5ff(b, c, d, a, x[i + 3], 22, -1044525330)
    a = md5ff(a, b, c, d, x[i + 4], 7, -176418897)
    d = md5ff(d, a, b, c, x[i + 5], 12, 1200080426)
    c = md5ff(c, d, a, b, x[i + 6], 17, -1473231341)
    b = md5ff(b, c, d, a, x[i + 7], 22, -45705983)
    a = md5ff(a, b, c, d, x[i + 8], 7, 1770035416)
    d = md5ff(d, a, b, c, x[i + 9], 12, -1958414417)
    c = md5ff(c, d, a, b, x[i + 10], 17, -42063)
    b = md5ff(b, c, d, a, x[i + 11], 22, -1990404162)
    a = md5ff(a, b, c, d, x[i + 12], 7, 1804603682)
    d = md5ff(d, a, b, c, x[i + 13], 12, -40341101)
    c = md5ff(c, d, a, b, x[i + 14], 17, -1502002290)
    b = md5ff(b, c, d, a, x[i + 15], 22, 1236535329)

    a = md5gg(a, b, c, d, x[i + 1], 5, -165796510)
    d = md5gg(d, a, b, c, x[i + 6], 9, -1069501632)
    c = md5gg(c, d, a, b, x[i + 11], 14, 643717713)
    b = md5gg(b, c, d, a, x[i], 20, -373897302)
    a = md5gg(a, b, c, d, x[i + 5], 5, -701558691)
    d = md5gg(d, a, b, c, x[i + 10], 9, 38016083)
    c = md5gg(c, d, a, b, x[i + 15], 14, -660478335)
    b = md5gg(b, c, d, a, x[i + 4], 20, -405537848)
    a = md5gg(a, b, c, d, x[i + 9], 5, 568446438)
    d = md5gg(d, a, b, c, x[i + 14], 9, -1019803690)
    c = md5gg(c, d, a, b, x[i + 3], 14, -187363961)
    b = md5gg(b, c, d, a, x[i + 8], 20, 1163531501)
    a = md5gg(a, b, c, d, x[i + 13], 5, -1444681467)
    d = md5gg(d, a, b, c, x[i + 2], 9, -51403784)
    c = md5gg(c, d, a, b, x[i + 7], 14, 1735328473)
    b = md5gg(b, c, d, a, x[i + 12], 20, -1926607734)

    a = md5hh(a, b, c, d, x[i + 5], 4, -378558)
    d = md5hh(d, a, b, c, x[i + 8], 11, -2022574463)
    c = md5hh(c, d, a, b, x[i + 11], 16, 1839030562)
    b = md5hh(b, c, d, a, x[i + 14], 23, -35309556)
    a = md5hh(a, b, c, d, x[i + 1], 4, -1530992060)
    d = md5hh(d, a, b, c, x[i + 4], 11, 1272893353)
    c = md5hh(c, d, a, b, x[i + 7], 16, -155497632)
    b = md5hh(b, c, d, a, x[i + 10], 23, -1094730640)
    a = md5hh(a, b, c, d, x[i + 13], 4, 681279174)
    d = md5hh(d, a, b, c, x[i], 11, -358537222)
    c = md5hh(c, d, a, b, x[i + 3], 16, -722521979)
    b = md5hh(b, c, d, a, x[i + 6], 23, 76029189)
    a = md5hh(a, b, c, d, x[i + 9], 4, -640364487)
    d = md5hh(d, a, b, c, x[i + 12], 11, -421815835)
    c = md5hh(c, d, a, b, x[i + 15], 16, 530742520)
    b = md5hh(b, c, d, a, x[i + 2], 23, -995338651)

    a = md5ii(a, b, c, d, x[i], 6, -198630844)
    d = md5ii(d, a, b, c, x[i + 7], 10, 1126891415)
    c = md5ii(c, d, a, b, x[i + 14], 15, -1416354905)
    b = md5ii(b, c, d, a, x[i + 5], 21, -57434055)
    a = md5ii(a, b, c, d, x[i + 12], 6, 1700485571)
    d = md5ii(d, a, b, c, x[i + 3], 10, -1894986606)
    c = md5ii(c, d, a, b, x[i + 10], 15, -1051523)
    b = md5ii(b, c, d, a, x[i + 1], 21, -2054922799)
    a = md5ii(a, b, c, d, x[i + 8], 6, 1873313359)
    d = md5ii(d, a, b, c, x[i + 15], 10, -30611744)
    c = md5ii(c, d, a, b, x[i + 6], 15, -1560198380)
    b = md5ii(b, c, d, a, x[i + 13], 21, 1309151649)
    a = md5ii(a, b, c, d, x[i + 4], 6, -145523070)
    d = md5ii(d, a, b, c, x[i + 11], 10, -1120210379)
    c = md5ii(c, d, a, b, x[i + 2], 15, 718787259)
    b = md5ii(b, c, d, a, x[i + 9], 21, -343485551)

    a = safeAdd(a, olda)
    b = safeAdd(b, oldb)
    c = safeAdd(c, oldc)
    d = safeAdd(d, oldd)
  }
  return [a, b, c, d]
}
/*
 * Convert an array of little-endian words to a string
 */
function binl2rstr(input) {
  var i
  var output = ''
  var length32 = input.length * 32
  for (i = 0; i < length32; i += 8) {
    output += String.fromCharCode((input[i >> 5] >>> (i % 32)) & 0xff)
  }
  return output
}


/*
 * Convert a raw string to an array of little-endian words
 * Characters >255 have their high-byte silently ignored.
 */
function rstr2binl(input) {
  var i
  var output = []
  output[(input.length >> 2) - 1] = undefined
  for (i = 0; i < output.length; i += 1) {
    output[i] = 0
  }
  var length8 = input.length * 8
  for (i = 0; i < length8; i += 8) {
    output[i >> 5] |= (input.charCodeAt(i / 8) & 0xff) << (i % 32)
  }
  return output
}


function encrypt_3(e) {
  return function (e) {
    if (Array.isArray(e)) return encrypt_3_3(e)
  }(e) || function (e) {
    if ("undefined" != typeof Symbol && Symbol.iterator in Object(e)) return Array.from(e)
  }(e) || function (e, t) {
    if (e) {
      if ("string" == typeof e) return encrypt_3_3(e, t);
      var n = Object.prototype.toString.call(e).slice(8, -1);
      return "Object" === n && e.constructor && (n = e.constructor.name), "Map" === n || "Set" === n ? Array.from(e) : "Arguments" === n || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n) ? encrypt_3_3(e, t) : void 0
    }
  }(e) || function () {
    throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")
  }()
}

function encrypt_3_3(e, t) {
  (null == t || t > e.length) && (t = e.length);
  for (var n = 0, r = new Array(t); n < t; n++) r[n] = e[n];
  return r
}

function rotateRight(n, x) {
  return ((x >>> n) | (x << (32 - n)));
}

function choice(x, y, z) {
  return ((x & y) ^ (~x & z));
}

function majority(x, y, z) {
  return ((x & y) ^ (x & z) ^ (y & z));
}

function sha256_Sigma0(x) {
  return (rotateRight(2, x) ^ rotateRight(13, x) ^ rotateRight(22, x));
}

function sha256_Sigma1(x) {
  return (rotateRight(6, x) ^ rotateRight(11, x) ^ rotateRight(25, x));
}

function sha256_sigma0(x) {
  return (rotateRight(7, x) ^ rotateRight(18, x) ^ (x >>> 3));
}

function sha256_sigma1(x) {
  return (rotateRight(17, x) ^ rotateRight(19, x) ^ (x >>> 10));
}

function sha256_expand(W, j) {
  return (W[j & 0x0f] += sha256_sigma1(W[(j + 14) & 0x0f]) + W[(j + 9) & 0x0f] +
    sha256_sigma0(W[(j + 1) & 0x0f]));
}


/* Add 32-bit integers with 16-bit operations (bug in some JS-interpreters: 
overflow) */
function safe_add(x, y) {
  var lsw = (x & 0xffff) + (y & 0xffff);
  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
  return (msw << 16) | (lsw & 0xffff);
}

/* Initialise the SHA256 computation */
function sha256_init() {
  ihash = new Array(8);
  count = new Array(2);
  buffer = new Array(64);
  count[0] = count[1] = 0;
  ihash[0] = 0x6a09e667;
  ihash[1] = 0xbb67ae85;
  ihash[2] = 0x3c6ef372;
  ihash[3] = 0xa54ff53a;
  ihash[4] = 0x510e527f;
  ihash[5] = 0x9b05688c;
  ihash[6] = 0x1f83d9ab;
  ihash[7] = 0x5be0cd19;
}

/* Transform a 512-bit message block */
function sha256_transform() {
  var a, b, c, d, e, f, g, h, T1, T2;
  var W = new Array(16);

  /* Initialize registers with the previous intermediate value */
  a = ihash[0];
  b = ihash[1];
  c = ihash[2];
  d = ihash[3];
  e = ihash[4];
  f = ihash[5];
  g = ihash[6];
  h = ihash[7];

  /* make 32-bit words */
  for (var i = 0; i < 16; i++)
    W[i] = ((buffer[(i << 2) + 3]) | (buffer[(i << 2) + 2] << 8) | (buffer[(i << 2) + 1] <<
      16) | (buffer[i << 2] << 24));

  for (var j = 0; j < 64; j++) {
    T1 = h + sha256_Sigma1(e) + choice(e, f, g) + K256[j];
    if (j < 16) T1 += W[j];
    else T1 += sha256_expand(W, j);
    T2 = sha256_Sigma0(a) + majority(a, b, c);
    h = g;
    g = f;
    f = e;
    e = safe_add(d, T1);
    d = c;
    c = b;
    b = a;
    a = safe_add(T1, T2);
  }

  /* Compute the current intermediate hash value */
  ihash[0] += a;
  ihash[1] += b;
  ihash[2] += c;
  ihash[3] += d;
  ihash[4] += e;
  ihash[5] += f;
  ihash[6] += g;
  ihash[7] += h;
}

/* Read the next chunk of data and update the SHA256 computation */
function sha256_update(data, inputLen) {
  var i, index, curpos = 0;
  /* Compute number of bytes mod 64 */
  index = ((count[0] >> 3) & 0x3f);
  var remainder = (inputLen & 0x3f);

  /* Update number of bits */
  if ((count[0] += (inputLen << 3)) < (inputLen << 3)) count[1]++;
  count[1] += (inputLen >> 29);

  /* Transform as many times as possible */
  for (i = 0; i + 63 < inputLen; i += 64) {
    for (var j = index; j < 64; j++)
      buffer[j] = data.charCodeAt(curpos++);
    sha256_transform();
    index = 0;
  }

  /* Buffer remaining input */
  for (var j = 0; j < remainder; j++)
    buffer[j] = data.charCodeAt(curpos++);
}

/* Finish the computation by operations such as padding */
function sha256_final() {
  var index = ((count[0] >> 3) & 0x3f);
  buffer[index++] = 0x80;
  if (index <= 56) {
    for (var i = index; i < 56; i++)
      buffer[i] = 0;
  } else {
    for (var i = index; i < 64; i++)
      buffer[i] = 0;
    sha256_transform();
    for (var i = 0; i < 56; i++)
      buffer[i] = 0;
  }
  buffer[56] = (count[1] >>> 24) & 0xff;
  buffer[57] = (count[1] >>> 16) & 0xff;
  buffer[58] = (count[1] >>> 8) & 0xff;
  buffer[59] = count[1] & 0xff;
  buffer[60] = (count[0] >>> 24) & 0xff;
  buffer[61] = (count[0] >>> 16) & 0xff;
  buffer[62] = (count[0] >>> 8) & 0xff;
  buffer[63] = count[0] & 0xff;
  sha256_transform();
}

/* Split the internal hash values into an array of bytes */
function sha256_encode_bytes() {
  var j = 0;
  var output = new Array(32);
  for (var i = 0; i < 8; i++) {
    output[j++] = ((ihash[i] >>> 24) & 0xff);
    output[j++] = ((ihash[i] >>> 16) & 0xff);
    output[j++] = ((ihash[i] >>> 8) & 0xff);
    output[j++] = (ihash[i] & 0xff);
  }
  return output;
}

/* Get the internal hash as a hex string */
function sha256_encode_hex() {
  var output = new String();
  for (var i = 0; i < 8; i++) {
    for (var j = 28; j >= 0; j -= 4)
      output += sha256_hex_digits.charAt((ihash[i] >>> j) & 0x0f);
  }
  return output;
}

// prettier-ignore
function Env(t, e) {
  "undefined" != typeof process && JSON.stringify(process.env).indexOf("GITHUB") > -1 && process.exit(0);
  class s {
    constructor(t) {
      this.env = t
    }
    send(t, e = "GET") {
      t = "string" == typeof t ? {
        url: t
      } : t;
      let s = this.get;
      return "POST" === e && (s = this.post), new Promise((e, i) => {
        s.call(this, t, (t, s, r) => {
          t ? i(t) : e(s)
        })
      })
    }
    get(t) {
      return this.send.call(this.env, t)
    }
    post(t) {
      return this.send.call(this.env, t, "POST")
    }
  }
  return new class {
    constructor(t, e) {
      this.name = t, this.http = new s(this), this.data = null, this.dataFile = "box.dat", this.logs = [], this.isMute = !1, this.isNeedRewrite = !1, this.logSeparator = "\n", this.startTime = (new Date).getTime(), Object.assign(this, e), this.log("", `🔔${this.name}, 开始!`)
    }
    isNode() {
      return "undefined" != typeof module && !!module.exports
    }
    isQuanX() {
      return "undefined" != typeof $task
    }
    isSurge() {
      return "undefined" != typeof $httpClient && "undefined" == typeof $loon
    }
    isLoon() {
      return "undefined" != typeof $loon
    }
    toObj(t, e = null) {
      try {
        return JSON.parse(t)
      } catch {
        return e
      }
    }
    toStr(t, e = null) {
      try {
        return JSON.stringify(t)
      } catch {
        return e
      }
    }
    getjson(t, e) {
      let s = e;
      const i = this.getdata(t);
      if (i) try {
        s = JSON.parse(this.getdata(t))
      } catch {}
      return s
    }
    setjson(t, e) {
      try {
        return this.setdata(JSON.stringify(t), e)
      } catch {
        return !1
      }
    }
    getScript(t) {
      return new Promise(e => {
        this.get({
          url: t
        }, (t, s, i) => e(i))
      })
    }
    runScript(t, e) {
      return new Promise(s => {
        let i = this.getdata("@chavy_boxjs_userCfgs.httpapi");
        i = i ? i.replace(/\n/g, "").trim() : i;
        let r = this.getdata("@chavy_boxjs_userCfgs.httpapi_timeout");
        r = r ? 1 * r : 20, r = e && e.timeout ? e.timeout : r;
        const [o, h] = i.split("@"), n = {
          url: `http://${h}/v1/scripting/evaluate`,
          body: {
            script_text: t,
            mock_type: "cron",
            timeout: r
          },
          headers: {
            "X-Key": o,
            Accept: "*/*"
          }
        };
        this.post(n, (t, e, i) => s(i))
      }).catch(t => this.logErr(t))
    }
    loaddata() {
      if (!this.isNode()) return {}; {
        this.fs = this.fs ? this.fs : require("fs"), this.path = this.path ? this.path : require("path");
        const t = this.path.resolve(this.dataFile),
          e = this.path.resolve(process.cwd(), this.dataFile),
          s = this.fs.existsSync(t),
          i = !s && this.fs.existsSync(e);
        if (!s && !i) return {}; {
          const i = s ? t : e;
          try {
            return JSON.parse(this.fs.readFileSync(i))
          } catch (t) {
            return {}
          }
        }
      }
    }
    writedata() {
      if (this.isNode()) {
        this.fs = this.fs ? this.fs : require("fs"), this.path = this.path ? this.path : require("path");
        const t = this.path.resolve(this.dataFile),
          e = this.path.resolve(process.cwd(), this.dataFile),
          s = this.fs.existsSync(t),
          i = !s && this.fs.existsSync(e),
          r = JSON.stringify(this.data);
        s ? this.fs.writeFileSync(t, r) : i ? this.fs.writeFileSync(e, r) : this.fs.writeFileSync(t, r)
      }
    }
    lodash_get(t, e, s) {
      const i = e.replace(/\[(\d+)\]/g, ".$1").split(".");
      let r = t;
      for (const t of i)
        if (r = Object(r)[t], void 0 === r) return s;
      return r
    }
    lodash_set(t, e, s) {
      return Object(t) !== t ? t : (Array.isArray(e) || (e = e.toString().match(/[^.[\]]+/g) || []), e.slice(0, -1).reduce((t, s, i) => Object(t[s]) === t[s] ? t[s] : t[s] = Math.abs(e[i + 1]) >> 0 == +e[i + 1] ? [] : {}, t)[e[e.length - 1]] = s, t)
    }
    getdata(t) {
      let e = this.getval(t);
      if (/^@/.test(t)) {
        const [, s, i] = /^@(.*?)\.(.*?)$/.exec(t), r = s ? this.getval(s) : "";
        if (r) try {
          const t = JSON.parse(r);
          e = t ? this.lodash_get(t, i, "") : e
        } catch (t) {
          e = ""
        }
      }
      return e
    }
    setdata(t, e) {
      let s = !1;
      if (/^@/.test(e)) {
        const [, i, r] = /^@(.*?)\.(.*?)$/.exec(e), o = this.getval(i), h = i ? "null" === o ? null : o || "{}" : "{}";
        try {
          const e = JSON.parse(h);
          this.lodash_set(e, r, t), s = this.setval(JSON.stringify(e), i)
        } catch (e) {
          const o = {};
          this.lodash_set(o, r, t), s = this.setval(JSON.stringify(o), i)
        }
      } else s = this.setval(t, e);
      return s
    }
    getval(t) {
      return this.isSurge() || this.isLoon() ? $persistentStore.read(t) : this.isQuanX() ? $prefs.valueForKey(t) : this.isNode() ? (this.data = this.loaddata(), this.data[t]) : this.data && this.data[t] || null
    }
    setval(t, e) {
      return this.isSurge() || this.isLoon() ? $persistentStore.write(t, e) : this.isQuanX() ? $prefs.setValueForKey(t, e) : this.isNode() ? (this.data = this.loaddata(), this.data[e] = t, this.writedata(), !0) : this.data && this.data[e] || null
    }
    initGotEnv(t) {
      this.got = this.got ? this.got : require("got"), this.cktough = this.cktough ? this.cktough : require("tough-cookie"), this.ckjar = this.ckjar ? this.ckjar : new this.cktough.CookieJar, t && (t.headers = t.headers ? t.headers : {}, void 0 === t.headers.Cookie && void 0 === t.cookieJar && (t.cookieJar = this.ckjar))
    }
    get(t, e = (() => {})) {
      t.headers && (delete t.headers["Content-Type"], delete t.headers["Content-Length"]), this.isSurge() || this.isLoon() ? (this.isSurge() && this.isNeedRewrite && (t.headers = t.headers || {}, Object.assign(t.headers, {
        "X-Surge-Skip-Scripting": !1
      })), $httpClient.get(t, (t, s, i) => {
        !t && s && (s.body = i, s.statusCode = s.status), e(t, s, i)
      })) : this.isQuanX() ? (this.isNeedRewrite && (t.opts = t.opts || {}, Object.assign(t.opts, {
        hints: !1
      })), $task.fetch(t).then(t => {
        const {
          statusCode: s,
          statusCode: i,
          headers: r,
          body: o
        } = t;
        e(null, {
          status: s,
          statusCode: i,
          headers: r,
          body: o
        }, o)
      }, t => e(t))) : this.isNode() && (this.initGotEnv(t), this.got(t).on("redirect", (t, e) => {
        try {
          if (t.headers["set-cookie"]) {
            const s = t.headers["set-cookie"].map(this.cktough.Cookie.parse).toString();
            s && this.ckjar.setCookieSync(s, null), e.cookieJar = this.ckjar
          }
        } catch (t) {
          this.logErr(t)
        }
      }).then(t => {
        const {
          statusCode: s,
          statusCode: i,
          headers: r,
          body: o
        } = t;
        e(null, {
          status: s,
          statusCode: i,
          headers: r,
          body: o
        }, o)
      }, t => {
        const {
          message: s,
          response: i
        } = t;
        e(s, i, i && i.body)
      }))
    }
    post(t, e = (() => {})) {
      if (t.body && t.headers && !t.headers["Content-Type"] && (t.headers["Content-Type"] = "application/x-www-form-urlencoded"), t.headers && delete t.headers["Content-Length"], this.isSurge() || this.isLoon()) this.isSurge() && this.isNeedRewrite && (t.headers = t.headers || {}, Object.assign(t.headers, {
        "X-Surge-Skip-Scripting": !1
      })), $httpClient.post(t, (t, s, i) => {
        !t && s && (s.body = i, s.statusCode = s.status), e(t, s, i)
      });
      else if (this.isQuanX()) t.method = "POST", this.isNeedRewrite && (t.opts = t.opts || {}, Object.assign(t.opts, {
        hints: !1
      })), $task.fetch(t).then(t => {
        const {
          statusCode: s,
          statusCode: i,
          headers: r,
          body: o
        } = t;
        e(null, {
          status: s,
          statusCode: i,
          headers: r,
          body: o
        }, o)
      }, t => e(t));
      else if (this.isNode()) {
        this.initGotEnv(t);
        const {
          url: s,
          ...i
        } = t;
        this.got.post(s, i).then(t => {
          const {
            statusCode: s,
            statusCode: i,
            headers: r,
            body: o
          } = t;
          e(null, {
            status: s,
            statusCode: i,
            headers: r,
            body: o
          }, o)
        }, t => {
          const {
            message: s,
            response: i
          } = t;
          e(s, i, i && i.body)
        })
      }
    }
    time(t, e = null) {
      const s = e ? new Date(e) : new Date;
      let i = {
        "M+": s.getMonth() + 1,
        "d+": s.getDate(),
        "H+": s.getHours(),
        "m+": s.getMinutes(),
        "s+": s.getSeconds(),
        "q+": Math.floor((s.getMonth() + 3) / 3),
        S: s.getMilliseconds()
      };
      /(y+)/.test(t) && (t = t.replace(RegExp.$1, (s.getFullYear() + "").substr(4 - RegExp.$1.length)));
      for (let e in i) new RegExp("(" + e + ")").test(t) && (t = t.replace(RegExp.$1, 1 == RegExp.$1.length ? i[e] : ("00" + i[e]).substr(("" + i[e]).length)));
      return t
    }
    msg(e = t, s = "", i = "", r) {
      const o = t => {
        if (!t) return t;
        if ("string" == typeof t) return this.isLoon() ? t : this.isQuanX() ? {
          "open-url": t
        } : this.isSurge() ? {
          url: t
        } : void 0;
        if ("object" == typeof t) {
          if (this.isLoon()) {
            let e = t.openUrl || t.url || t["open-url"],
              s = t.mediaUrl || t["media-url"];
            return {
              openUrl: e,
              mediaUrl: s
            }
          }
          if (this.isQuanX()) {
            let e = t["open-url"] || t.url || t.openUrl,
              s = t["media-url"] || t.mediaUrl;
            return {
              "open-url": e,
              "media-url": s
            }
          }
          if (this.isSurge()) {
            let e = t.url || t.openUrl || t["open-url"];
            return {
              url: e
            }
          }
        }
      };
      if (this.isMute || (this.isSurge() || this.isLoon() ? $notification.post(e, s, i, o(r)) : this.isQuanX() && $notify(e, s, i, o(r))), !this.isMuteLog) {
        let t = ["", "==============📣系统通知📣=============="];
        t.push(e), s && t.push(s), i && t.push(i), console.log(t.join("\n")), this.logs = this.logs.concat(t)
      }
    }
    log(...t) {
      t.length > 0 && (this.logs = [...this.logs, ...t]), console.log(t.join(this.logSeparator))
    }
    logErr(t, e) {
      const s = !this.isSurge() && !this.isQuanX() && !this.isLoon();
      s ? this.log("", `❗️${this.name}, 错误!`, t.stack) : this.log("", `❗️${this.name}, 错误!`, t)
    }
    wait(t) {
      return new Promise(e => setTimeout(e, t))
    }
    done(t = {}) {
      const e = (new Date).getTime(),
        s = (e - this.startTime) / 1e3;
      this.log("", `🔔${this.name}, 结束! 🕛 ${s} 秒`), this.log(), (this.isSurge() || this.isQuanX() || this.isLoon()) && $done(t)
    }
  }(t, e)
}