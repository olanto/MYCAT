/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.mysqd.util;

public class HTML4Type {

    /**
     * reference: W3C - Character entity in HTML 4
     * http://www.w3.org/TR/html401/sgml/entities.html" .
     */
    public static final Object[][] html4 = {
        {new String("&Aacute;"), new Integer(193), new Boolean(false)}, //symbol: &Aacute; = Á
        {new String("&aacute;"), new Integer(225), new Boolean(false)}, //symbol: &aacute; = á
        {new String("&Acirc;"), new Integer(194), new Boolean(false)}, //symbol: &Acirc; = Â
        {new String("&acirc;"), new Integer(226), new Boolean(false)}, //symbol: &acirc; = â
        {new String("&acute;"), new Integer(180), new Boolean(true)}, //symbol: &acute; = ´
        {new String("&AElig;"), new Integer(198), new Boolean(false)}, //symbol: &AElig; = Æ
        {new String("&aelig;"), new Integer(230), new Boolean(false)}, //symbol: &aelig; = æ
        {new String("&Agrave;"), new Integer(192), new Boolean(false)}, //symbol: &Agrave; = À
        {new String("&agrave;"), new Integer(224), new Boolean(false)}, //symbol: &agrave; = à
        {new String("&alefsym;"), new Integer(8501), new Boolean(false)}, //symbol: &alefsym; = ℵ
        {new String("&Alpha;"), new Integer(913), new Boolean(false)}, //symbol: &Alpha; = Α
        {new String("&alpha;"), new Integer(945), new Boolean(false)}, //symbol: &alpha; = α
        {new String("&amp;"), new Integer(38), new Boolean(true)}, //symbol: &amp; = &
        {new String("&and;"), new Integer(8743), new Boolean(true)}, //symbol: &and; = ∧
        {new String("&ang;"), new Integer(8736), new Boolean(true)}, //symbol: &ang; = ∠
        {new String("&apos;"), new Integer(39), new Boolean(true)}, //symbol: &apos; = '
        {new String("&Aring;"), new Integer(197), new Boolean(false)}, //symbol: &Aring; = Å
        {new String("&aring;"), new Integer(229), new Boolean(false)}, //symbol: &aring; = å
        {new String("&asymp;"), new Integer(8776), new Boolean(true)}, //symbol: &asymp; = ≈
        {new String("&Atilde;"), new Integer(195), new Boolean(false)}, //symbol: &Atilde; = Ã
        {new String("&atilde;"), new Integer(227), new Boolean(false)}, //symbol: &atilde; = ã
        {new String("&Auml;"), new Integer(196), new Boolean(false)}, //symbol: &Auml; = Ä
        {new String("&auml;"), new Integer(228), new Boolean(false)}, //symbol: &auml; = ä
        {new String("&bdquo;"), new Integer(8222), new Boolean(true)}, //symbol: &bdquo; = „
        {new String("&Beta;"), new Integer(914), new Boolean(false)}, //symbol: &Beta; = Β
        {new String("&beta;"), new Integer(946), new Boolean(false)}, //symbol: &beta; = β
        {new String("&brvbar;"), new Integer(166), new Boolean(true)}, //symbol: &brvbar; = ¦
        {new String("&bull;"), new Integer(8226), new Boolean(true)}, //symbol: &bull; = •
        {new String("&cap;"), new Integer(8745), new Boolean(true)}, //symbol: &cap; = ∩
        {new String("&Ccedil;"), new Integer(199), new Boolean(false)}, //symbol: &Ccedil; = Ç
        {new String("&ccedil;"), new Integer(231), new Boolean(false)}, //symbol: &ccedil; = ç
        {new String("&cedil;"), new Integer(184), new Boolean(true)}, //symbol: &cedil; = ¸
        {new String("&cent;"), new Integer(162), new Boolean(false)}, //symbol: &cent; = ¢
        {new String("&Chi;"), new Integer(935), new Boolean(false)}, //symbol: &Chi; = Χ
        {new String("&chi;"), new Integer(967), new Boolean(false)}, //symbol: &chi; = χ
        {new String("&circ;"), new Integer(710), new Boolean(true)}, //symbol: &circ; = ˆ
        {new String("&clubs;"), new Integer(9827), new Boolean(true)}, //symbol: &clubs; = ♣
        {new String("&cong;"), new Integer(8773), new Boolean(true)}, //symbol: &cong; = ≅
        {new String("&copy;"), new Integer(169), new Boolean(true)}, //symbol: &copy; = ©
        {new String("&crarr;"), new Integer(8629), new Boolean(true)}, //symbol: &crarr; = ↵
        {new String("&cup;"), new Integer(8746), new Boolean(true)}, //symbol: &cup; = ∪
        {new String("&curren;"), new Integer(164), new Boolean(true)}, //symbol: &curren; = ¤
        {new String("&dagger;"), new Integer(8224), new Boolean(true)}, //symbol: &dagger; = †
        {new String("&Dagger;"), new Integer(8225), new Boolean(true)}, //symbol: &Dagger; = ‡
        {new String("&darr;"), new Integer(8595), new Boolean(true)}, //symbol: &darr; = ↓
        {new String("&dArr;"), new Integer(8659), new Boolean(true)}, //symbol: &dArr; = ⇓
        {new String("&deg;"), new Integer(176), new Boolean(true)}, //symbol: &deg; = °
        {new String("&Delta;"), new Integer(916), new Boolean(false)}, //symbol: &Delta; = Δ
        {new String("&delta;"), new Integer(948), new Boolean(false)}, //symbol: &delta; = δ
        {new String("&diams;"), new Integer(9830), new Boolean(true)}, //symbol: &diams; = ♦
        {new String("&divide;"), new Integer(247), new Boolean(true)}, //symbol: &divide; = ÷
        {new String("&Eacute;"), new Integer(201), new Boolean(false)}, //symbol: &Eacute; = É
        {new String("&eacute;"), new Integer(233), new Boolean(false)}, //symbol: &eacute; = é
        {new String("&Ecirc;"), new Integer(202), new Boolean(false)}, //symbol: &Ecirc; = Ê
        {new String("&ecirc;"), new Integer(234), new Boolean(false)}, //symbol: &ecirc; = ê
        {new String("&Egrave;"), new Integer(200), new Boolean(false)}, //symbol: &Egrave; = È
        {new String("&egrave;"), new Integer(232), new Boolean(false)}, //symbol: &egrave; = è
        {new String("&empty;"), new Integer(8709), new Boolean(true)}, //symbol: &empty; = ∅
        {new String("&emsp;"), new Integer(8195), new Boolean(true)}, //symbol: &emsp; =  
        {new String("&ensp;"), new Integer(8194), new Boolean(true)}, //symbol: &ensp; =  
        {new String("&Epsilon;"), new Integer(917), new Boolean(false)}, //symbol: &Epsilon; = Ε
        {new String("&epsilon;"), new Integer(949), new Boolean(false)}, //symbol: &epsilon; = ε
        {new String("&equiv;"), new Integer(8801), new Boolean(true)}, //symbol: &equiv; = ≡
        {new String("&Eta;"), new Integer(919), new Boolean(false)}, //symbol: &Eta; = Η
        {new String("&eta;"), new Integer(951), new Boolean(false)}, //symbol: &eta; = η
        {new String("&ETH;"), new Integer(208), new Boolean(false)}, //symbol: &ETH; = Ð
        {new String("&eth;"), new Integer(240), new Boolean(false)}, //symbol: &eth; = ð
        {new String("&Euml;"), new Integer(203), new Boolean(false)}, //symbol: &Euml; = Ë
        {new String("&euml;"), new Integer(235), new Boolean(false)}, //symbol: &euml; = ë
        {new String("&euro;"), new Integer(8364), new Boolean(false)}, //symbol: &euro; = €
        {new String("&exist;"), new Integer(8707), new Boolean(true)}, //symbol: &exist; = ∃
        {new String("&fnof;"), new Integer(402), new Boolean(true)}, //symbol: &fnof; = ƒ
        {new String("&forall;"), new Integer(8704), new Boolean(true)}, //symbol: &forall; = ∀
        {new String("&frac12;"), new Integer(189), new Boolean(false)}, //symbol: &frac12; = ½
        {new String("&frac14;"), new Integer(188), new Boolean(false)}, //symbol: &frac14; = ¼
        {new String("&frac34;"), new Integer(190), new Boolean(false)}, //symbol: &frac34; = ¾
        {new String("&frasl;"), new Integer(8260), new Boolean(true)}, //symbol: &frasl; = ⁄
        {new String("&Gamma;"), new Integer(915), new Boolean(false)}, //symbol: &Gamma; = Γ
        {new String("&gamma;"), new Integer(947), new Boolean(false)}, //symbol: &gamma; = γ
        {new String("&ge;"), new Integer(8805), new Boolean(true)}, //symbol: &ge; = ≥
        {new String("&gt;"), new Integer(62), new Boolean(true)}, //symbol: &gt; = >
        {new String("&harr;"), new Integer(8596), new Boolean(true)}, //symbol: &harr; = ↔
        {new String("&hArr;"), new Integer(8660), new Boolean(true)}, //symbol: &hArr; = ⇔
        {new String("&hearts;"), new Integer(9829), new Boolean(true)}, //symbol: &hearts; = ♥
        {new String("&hellip;"), new Integer(8230), new Boolean(true)}, //symbol: &hellip; = …
        {new String("&Iacute;"), new Integer(205), new Boolean(false)}, //symbol: &Iacute; = Í
        {new String("&iacute;"), new Integer(237), new Boolean(false)}, //symbol: &iacute; = í
        {new String("&Icirc;"), new Integer(206), new Boolean(false)}, //symbol: &Icirc; = Î
        {new String("&icirc;"), new Integer(238), new Boolean(false)}, //symbol: &icirc; = î
        {new String("&iexcl;"), new Integer(161), new Boolean(true)}, //symbol: &iexcl; = ¡
        {new String("&Igrave;"), new Integer(204), new Boolean(false)}, //symbol: &Igrave; = Ì
        {new String("&igrave;"), new Integer(236), new Boolean(false)}, //symbol: &igrave; = ì
        {new String("&image;"), new Integer(8465), new Boolean(true)}, //symbol: &image; = ℑ
        {new String("&infin;"), new Integer(8734), new Boolean(true)}, //symbol: &infin; = ∞
        {new String("&int;"), new Integer(8747), new Boolean(true)}, //symbol: &int; = ∫
        {new String("&Iota;"), new Integer(921), new Boolean(false)}, //symbol: &Iota; = Ι
        {new String("&iota;"), new Integer(953), new Boolean(false)}, //symbol: &iota; = ι
        {new String("&iquest;"), new Integer(191), new Boolean(true)}, //symbol: &iquest; = ¿
        {new String("&isin;"), new Integer(8712), new Boolean(true)}, //symbol: &isin; = ∈
        {new String("&Iuml;"), new Integer(207), new Boolean(false)}, //symbol: &Iuml; = Ï
        {new String("&iuml;"), new Integer(239), new Boolean(false)}, //symbol: &iuml; = ï
        {new String("&Kappa;"), new Integer(922), new Boolean(false)}, //symbol: &Kappa; = Κ
        {new String("&kappa;"), new Integer(954), new Boolean(false)}, //symbol: &kappa; = κ
        {new String("&Lambda;"), new Integer(923), new Boolean(false)}, //symbol: &Lambda; = Λ
        {new String("&lambda;"), new Integer(955), new Boolean(false)}, //symbol: &lambda; = λ
        {new String("&lang;"), new Integer(9001), new Boolean(true)}, //symbol: &lang; = 〈
        {new String("&laquo;"), new Integer(171), new Boolean(false)}, //symbol: &laquo; = «
        {new String("&larr;"), new Integer(8592), new Boolean(true)}, //symbol: &larr; = ←
        {new String("&lArr;"), new Integer(8656), new Boolean(true)}, //symbol: &lArr; = ⇐
        {new String("&lceil;"), new Integer(8968), new Boolean(true)}, //symbol: &lceil; = ⌈
        {new String("&ldquo;"), new Integer(8220), new Boolean(true)}, //symbol: &ldquo; = “
        {new String("&le;"), new Integer(8804), new Boolean(true)}, //symbol: &le; = ≤
        {new String("&lfloor;"), new Integer(8970), new Boolean(true)}, //symbol: &lfloor; = ⌊
        {new String("&lowast;"), new Integer(8727), new Boolean(true)}, //symbol: &lowast; = ∗
        {new String("&loz;"), new Integer(9674), new Boolean(true)}, //symbol: &loz; = ◊
        {new String("&lrm;"), new Integer(8206), new Boolean(true)}, //symbol: &lrm; = ‎
        {new String("&lsaquo;"), new Integer(8249), new Boolean(true)}, //symbol: &lsaquo; = ‹
        {new String("&lsquo;"), new Integer(8216), new Boolean(true)}, //symbol: &lsquo; = ‘
        {new String("&lt;"), new Integer(60), new Boolean(true)}, //symbol: &lt; = <
        {new String("&macr;"), new Integer(175), new Boolean(true)}, //symbol: &macr; = ¯
        {new String("&mdash;"), new Integer(8212), new Boolean(true)}, //symbol: &mdash; = —
        {new String("&micro;"), new Integer(181), new Boolean(false)}, //symbol: &micro; = µ
        {new String("&middot;"), new Integer(183), new Boolean(true)}, //symbol: &middot; = ·
        {new String("&minus;"), new Integer(8722), new Boolean(true)}, //symbol: &minus; = −
        {new String("&Mu;"), new Integer(924), new Boolean(false)}, //symbol: &Mu; = Μ
        {new String("&mu;"), new Integer(956), new Boolean(false)}, //symbol: &mu; = μ
        {new String("&nabla;"), new Integer(8711), new Boolean(true)}, //symbol: &nabla; = ∇
        {new String("&nbsp;"), new Integer(160), new Boolean(true)}, //symbol: &nbsp; =  
        {new String("&ndash;"), new Integer(8211), new Boolean(false)}, //symbol: &ndash; = –
        {new String("&ne;"), new Integer(8800), new Boolean(true)}, //symbol: &ne; = ≠
        {new String("&ni;"), new Integer(8715), new Boolean(true)}, //symbol: &ni; = ∋
        {new String("&not;"), new Integer(172), new Boolean(true)}, //symbol: &not; = ¬
        {new String("&notin;"), new Integer(8713), new Boolean(true)}, //symbol: &notin; = ∉
        {new String("&nsub;"), new Integer(8836), new Boolean(true)}, //symbol: &nsub; = ⊄
        {new String("&Ntilde;"), new Integer(209), new Boolean(false)}, //symbol: &Ntilde; = Ñ
        {new String("&ntilde;"), new Integer(241), new Boolean(false)}, //symbol: &ntilde; = ñ
        {new String("&Nu;"), new Integer(925), new Boolean(false)}, //symbol: &Nu; = Ν
        {new String("&nu;"), new Integer(957), new Boolean(false)}, //symbol: &nu; = ν
        {new String("&Oacute;"), new Integer(211), new Boolean(false)}, //symbol: &Oacute; = Ó
        {new String("&oacute;"), new Integer(243), new Boolean(false)}, //symbol: &oacute; = ó
        {new String("&Ocirc;"), new Integer(212), new Boolean(false)}, //symbol: &Ocirc; = Ô
        {new String("&ocirc;"), new Integer(244), new Boolean(false)}, //symbol: &ocirc; = ô
        {new String("&OElig;"), new Integer(338), new Boolean(false)}, //symbol: &OElig; = Œ
        {new String("&oelig;"), new Integer(339), new Boolean(false)}, //symbol: &oelig; = œ
        {new String("&Ograve;"), new Integer(210), new Boolean(false)}, //symbol: &Ograve; = Ò
        {new String("&ograve;"), new Integer(242), new Boolean(false)}, //symbol: &ograve; = ò
        {new String("&oline;"), new Integer(8254), new Boolean(true)}, //symbol: &oline; = ‾
        {new String("&Omega;"), new Integer(937), new Boolean(false)}, //symbol: &Omega; = Ω
        {new String("&omega;"), new Integer(969), new Boolean(false)}, //symbol: &omega; = ω
        {new String("&Omicron;"), new Integer(927), new Boolean(false)}, //symbol: &Omicron; = Ο
        {new String("&omicron;"), new Integer(959), new Boolean(false)}, //symbol: &omicron; = ο
        {new String("&oplus;"), new Integer(8853), new Boolean(true)}, //symbol: &oplus; = ⊕
        {new String("&or;"), new Integer(8744), new Boolean(true)}, //symbol: &or; = ∨
        {new String("&ordf;"), new Integer(170), new Boolean(false)}, //symbol: &ordf; = ª
        {new String("&ordm;"), new Integer(186), new Boolean(false)}, //symbol: &ordm; = º
        {new String("&Oslash;"), new Integer(216), new Boolean(false)}, //symbol: &Oslash; = Ø
        {new String("&oslash;"), new Integer(248), new Boolean(false)}, //symbol: &oslash; = ø
        {new String("&Otilde;"), new Integer(213), new Boolean(false)}, //symbol: &Otilde; = Õ
        {new String("&otilde;"), new Integer(245), new Boolean(false)}, //symbol: &otilde; = õ
        {new String("&otimes;"), new Integer(8855), new Boolean(true)}, //symbol: &otimes; = ⊗
        {new String("&Ouml;"), new Integer(214), new Boolean(false)}, //symbol: &Ouml; = Ö
        {new String("&ouml;"), new Integer(246), new Boolean(false)}, //symbol: &ouml; = ö
        {new String("&para;"), new Integer(182), new Boolean(true)}, //symbol: &para; = ¶
        {new String("&part;"), new Integer(8706), new Boolean(true)}, //symbol: &part; = ∂
        {new String("&permil;"), new Integer(8240), new Boolean(false)}, //symbol: &permil; = ‰
        {new String("&perp;"), new Integer(8869), new Boolean(true)}, //symbol: &perp; = ⊥
        {new String("&Phi;"), new Integer(934), new Boolean(false)}, //symbol: &Phi; = Φ
        {new String("&phi;"), new Integer(966), new Boolean(false)}, //symbol: &phi; = φ
        {new String("&Pi;"), new Integer(928), new Boolean(false)}, //symbol: &Pi; = Π
        {new String("&pi;"), new Integer(960), new Boolean(false)}, //symbol: &pi; = π
        {new String("&piv;"), new Integer(982), new Boolean(false)}, //symbol: &piv; = ϖ
        {new String("&plusmn;"), new Integer(177), new Boolean(true)}, //symbol: &plusmn; = ±
        {new String("&pound;"), new Integer(163), new Boolean(false)}, //symbol: &pound; = £
        {new String("&prime;"), new Integer(8242), new Boolean(true)}, //symbol: &prime; = ′
        {new String("&Prime;"), new Integer(8243), new Boolean(true)}, //symbol: &Prime; = ″
        {new String("&prod;"), new Integer(8719), new Boolean(true)}, //symbol: &prod; = ∏
        {new String("&prop;"), new Integer(8733), new Boolean(true)}, //symbol: &prop; = ∝
        {new String("&Psi;"), new Integer(936), new Boolean(false)}, //symbol: &Psi; = Ψ
        {new String("&psi;"), new Integer(968), new Boolean(false)}, //symbol: &psi; = ψ
        {new String("&radic;"), new Integer(8730), new Boolean(true)}, //symbol: &radic; = √
        {new String("&quot;"), new Integer(43), new Boolean(true)}, //symbol: &quot; = "
        {new String("&rang;"), new Integer(9002), new Boolean(true)}, //symbol: &rang; = 〉
        {new String("&raquo;"), new Integer(187), new Boolean(true)}, //symbol: &raquo; = »
        {new String("&rarr;"), new Integer(8594), new Boolean(true)}, //symbol: &rarr; = →
        {new String("&rArr;"), new Integer(8658), new Boolean(true)}, //symbol: &rArr; = ⇒
        {new String("&rceil;"), new Integer(8969), new Boolean(true)}, //symbol: &rceil; = ⌉
        {new String("&rdquo;"), new Integer(8221), new Boolean(true)}, //symbol: &rdquo; = ”
        {new String("&real;"), new Integer(8476), new Boolean(true)}, //symbol: &real; = ℜ
        {new String("&reg;"), new Integer(174), new Boolean(true)}, //symbol: &reg; = ®
        {new String("&rfloor;"), new Integer(8971), new Boolean(true)}, //symbol: &rfloor; = ⌋
        {new String("&Rho;"), new Integer(929), new Boolean(false)}, //symbol: &Rho; = Ρ
        {new String("&rho;"), new Integer(961), new Boolean(false)}, //symbol: &rho; = ρ
        {new String("&rlm;"), new Integer(8207), new Boolean(true)}, //symbol: &rlm; = ‏
        {new String("&rsaquo;"), new Integer(8250), new Boolean(true)}, //symbol: &rsaquo; = ›
        {new String("&rsquo;"), new Integer(8217), new Boolean(true)}, //symbol: &rsquo; = ’
        {new String("&sbquo;"), new Integer(8218), new Boolean(true)}, //symbol: &sbquo; = ‚
        {new String("&Scaron;"), new Integer(352), new Boolean(false)}, //symbol: &Scaron; = Š
        {new String("&scaron;"), new Integer(353), new Boolean(false)}, //symbol: &scaron; = š
        {new String("&sdot;"), new Integer(8901), new Boolean(true)}, //symbol: &sdot; = ⋅
        {new String("&sect;"), new Integer(167), new Boolean(true)}, //symbol: &sect; = §
        {new String("&shy;"), new Integer(173), new Boolean(true)}, //symbol: &shy; = ­
        {new String("&Sigma;"), new Integer(931), new Boolean(false)}, //symbol: &Sigma; = Σ
        {new String("&sigma;"), new Integer(963), new Boolean(false)}, //symbol: &sigma; = σ
        {new String("&sigmaf;"), new Integer(962), new Boolean(false)}, //symbol: &sigmaf; = ς
        {new String("&sim;"), new Integer(8764), new Boolean(true)}, //symbol: &sim; = ∼
        {new String("&spades;"), new Integer(9824), new Boolean(true)}, //symbol: &spades; = ♠
        {new String("&sub;"), new Integer(8834), new Boolean(true)}, //symbol: &sub; = ⊂
        {new String("&sube;"), new Integer(8838), new Boolean(true)}, //symbol: &sube; = ⊆
        {new String("&sum;"), new Integer(8721), new Boolean(true)}, //symbol: &sum; = ∑
        {new String("&sup1;"), new Integer(185), new Boolean(false)}, //symbol: &sup1; = ¹
        {new String("&sup2;"), new Integer(178), new Boolean(false)}, //symbol: &sup2; = ²
        {new String("&sup3;"), new Integer(179), new Boolean(false)}, //symbol: &sup3; = ³
        {new String("&sup;"), new Integer(8835), new Boolean(true)}, //symbol: &sup; = ⊃
        {new String("&supe;"), new Integer(8839), new Boolean(true)}, //symbol: &supe; = ⊇
        {new String("&szlig;"), new Integer(223), new Boolean(false)}, //symbol: &szlig; = ß
        {new String("&Tau;"), new Integer(932), new Boolean(false)}, //symbol: &Tau; = Τ
        {new String("&tau;"), new Integer(964), new Boolean(false)}, //symbol: &tau; = τ
        {new String("&there4;"), new Integer(8756), new Boolean(true)}, //symbol: &there4; = ∴
        {new String("&Theta;"), new Integer(920), new Boolean(false)}, //symbol: &Theta; = Θ
        {new String("&theta;"), new Integer(952), new Boolean(false)}, //symbol: &theta; = θ
        {new String("&thetasym;"), new Integer(977), new Boolean(false)}, //symbol: &thetasym; = ϑ
        {new String("&thinsp;"), new Integer(8201), new Boolean(true)}, //symbol: &thinsp; =  
        {new String("&THORN;"), new Integer(222), new Boolean(false)}, //symbol: &THORN; = Þ
        {new String("&thorn;"), new Integer(254), new Boolean(false)}, //symbol: &thorn; = þ
        {new String("&tilde;"), new Integer(732), new Boolean(true)}, //symbol: &tilde; = ˜
        {new String("&times;"), new Integer(215), new Boolean(true)}, //symbol: &times; = ×
        {new String("&trade;"), new Integer(8482), new Boolean(true)}, //symbol: &trade; = ™
        {new String("&Uacute;"), new Integer(218), new Boolean(false)}, //symbol: &Uacute; = Ú
        {new String("&uacute;"), new Integer(250), new Boolean(false)}, //symbol: &uacute; = ú
        {new String("&uarr;"), new Integer(8593), new Boolean(true)}, //symbol: &uarr; = ↑
        {new String("&uArr;"), new Integer(8657), new Boolean(true)}, //symbol: &uArr; = ⇑
        {new String("&Ucirc;"), new Integer(219), new Boolean(false)}, //symbol: &Ucirc; = Û
        {new String("&ucirc;"), new Integer(251), new Boolean(false)}, //symbol: &ucirc; = û
        {new String("&Ugrave;"), new Integer(217), new Boolean(false)}, //symbol: &Ugrave; = Ù
        {new String("&ugrave;"), new Integer(249), new Boolean(false)}, //symbol: &ugrave; = ù
        {new String("&uml;"), new Integer(168), new Boolean(true)}, //symbol: &uml; = ¨
        {new String("&upsih;"), new Integer(978), new Boolean(false)}, //symbol: &upsih; = ϒ
        {new String("&Upsilon;"), new Integer(933), new Boolean(false)}, //symbol: &Upsilon; = Υ
        {new String("&upsilon;"), new Integer(965), new Boolean(false)}, //symbol: &upsilon; = υ
        {new String("&Uuml;"), new Integer(220), new Boolean(false)}, //symbol: &Uuml; = Ü
        {new String("&uuml;"), new Integer(252), new Boolean(false)}, //symbol: &uuml; = ü
        {new String("&weierp;"), new Integer(8472), new Boolean(true)}, //symbol: &weierp; = ℘
        {new String("&Xi;"), new Integer(926), new Boolean(false)}, //symbol: &Xi; = Ξ
        {new String("&xi;"), new Integer(958), new Boolean(false)}, //symbol: &xi; = ξ
        {new String("&Yacute;"), new Integer(221), new Boolean(false)}, //symbol: &Yacute; = Ý
        {new String("&yacute;"), new Integer(253), new Boolean(false)}, //symbol: &yacute; = ý
        {new String("&yen;"), new Integer(165), new Boolean(false)}, //symbol: &yen; = ¥
        {new String("&yuml;"), new Integer(255), new Boolean(false)}, //symbol: &yuml; = ÿ
        {new String("&Yuml;"), new Integer(376), new Boolean(false)}, //symbol: &Yuml; = Ÿ
        {new String("&Zeta;"), new Integer(918), new Boolean(false)}, //symbol: &Zeta; = Ζ
        {new String("&zeta;"), new Integer(950), new Boolean(false)}, //symbol: &zeta; = ζ
        {new String("&zwj;"), new Integer(8205), new Boolean(true)}, //symbol: &zwj; = ‍
        {new String("&zwnj;"), new Integer(8204), new Boolean(true)} //symbol: &zwnj; = ‌
    };

    public static HTMLEntity html4get(int n) {
        return new HTMLEntity((String) html4[n][0], (Integer) html4[n][1], (Boolean) html4[n][2]);
    }

    public static int html4Size() {
        return html4.length;
    }
}
