function ToCn(a) {
	var b = 9.999999999999E10, f = "\u96f6", h = "\u58f9", g = "\u8d30", e = "\u53c1", k = "\u8086", p = "\u4f0d", q = "\u9646", r = "\u67d2", s = "\u634c", t = "\u7396", l = "\u62fe", d = "\u4f70", i = "\u4edf", m = "\u4e07", j = "\u4ebf", u = "", o = "\u5143", c = "\u89d2", n = "\u5206", v = "\u6574";
	a = a.toString();
	if (a == "") {
		alert("\u8bf7\u8f93\u5165\u6570\u5b57!");
		return "";
	}
	if (a.match(/[^,.\d]/) != null) {
		alert("\u8bf7\u4e0d\u8981\u8f93\u5165\u5176\u4ed6\u5b57\u7b26\uff01");
		return "";
	}
	if (a.match(/^((\d{1,3}(,\d{3})*(.((\d{3},)*\d{1,3}))?)|(\d+(.\d+)?))$/) == null) {
		alert("\u975e\u6cd5\u683c\u5f0f\uff01");
		return "";
	}
	a = a.replace(/,/g, "");
	a = a.replace(/^0+/, "");
	if (Number(a) > b) {
		return "数字太大无法显示,不能大于"+b;
	}
	b = a.split(".");
	if (b.length > 1) {
		a = b[0];
		b = b[1];
		b = b.substr(0, 2);
	} else {
		a = b[0];
		b = "";
	}
	h = new Array(f, h, g, e, k, p, q, r, s, t);
	l = new Array("", l, d, i);
	m = new Array("", m, j);
	n = new Array(c, n);
	c = "";
	if (Number(a) > 0) {
		for (d = j = 0; d < a.length; d++) {
			e = a.length - d - 1;
			i = a.substr(d, 1);
			g = e / 4;
			e = e % 4;
			if (i == "0")
				j++;
			else {
				if (j > 0)
					c += h[0];
				j = 0;
				c += h[Number(i)] + l[e];
			}
			if (e == 0 && j < 4)
				c += m[g];
		}
		c += o;
	}
	if (b != "")
		for (d = 0; d < b.length; d++) {
			i = b.substr(d, 1);
			if (i != "0")
				c += h[Number(i)] + n[d];
		}
	if (c == "")
		c = f + o;
	if (b == "")
		c += v;
	return c = u + c;
}
