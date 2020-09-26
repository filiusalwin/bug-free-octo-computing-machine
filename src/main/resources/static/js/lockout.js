/*
var doc, bod, htm, C, E, T; // for use on other loads
addEventListener('load', function(){

    doc = document; bod = doc.body; htm = doc.documentElement;
    C = function(tag){
        return doc.createElement(tag);
    }
    E = function(id){
        return doc.getElementById(id);
    }
    T = function(tag){
        return doc.getElementByTagName(tag);
    }
    var form = E('form'), fS = form.style, sub = E('sub'), tO;
    function tF(){
        // code within this func inside logout ajax
        fS.display = 'block';
    }
    form.addEventListener('submit', function(ev){
        ev.preventDefault();
    });
    sub.addEventListener('click', function(){
        // run code within this func inside login ajax
        fS.display = 'none'; E('un').value = E('pw').value = '';
        tO = setTimeout(tF, 10000);
        addEventListener('mousemove', function(){
            if(tO)clearTimeout(tO);
            tO = setTimeout(tF, 10000);
        });
    });
    form.addEventListener('keydown', function(ev){
        if(ev.keyCode === 13)sub.click();
    });

});*/
window.history.forward();
function noBack() {
    window.history.forward();
}

