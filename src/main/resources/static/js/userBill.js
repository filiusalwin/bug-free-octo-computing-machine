$(document).ready(_ => {
    fillReceipts();
});

function fillReceipts() {
    var entries = $(".paymentEntry");
    for (let elem of entries) {
        let entry = JSON.parse(elem.innerHTML);
        elem.innerHTML = "";
        for (let item of entry) {
            let div = document.createElement("div");
            div.classList.add("row");
            appendSpan(div, item.amount, 50, "text");
            appendSpan(div, item.name, 200, "text");
            appendSpan(div, item.price, 100, "currency");
            appendSpan(div, item.subtotal, 100, "currency");
            elem.appendChild(div);
        }
        let total = document.createElement("div");
        total.classList.add("row");
        total.style.fontWeight = "bold";
        appendSpan(total, "", 50, "text");
        appendSpan(total, "Total", 300, "text");
        appendSpan(total, elem.getAttribute("total"), 100, "currency");
        elem.appendChild(total);
    }
}

function appendSpan(parent, content, width, type) {
    var span = document.createElement("div");
    span.style.width = width + "px";
    if (type == "currency") {
        content = formatCurrencyString(content, true);
        span.classList.add("text-right");
    }
    span.innerHTML = content;
    parent.appendChild(span);
}

function deductPayment() {
    $("#error").hide();
    $.ajax({
        type: "POST",
        url: "/payment/credit/clear/" + $("#userId").val()
    }).done(data => {
        location.href = "/user";
    }).fail(jqXHR => {
        console.table(jqXHR.responseText);
        $("#error").show();
    });
}

function printPage() {
    var buttons = $(".hideOnPrint");
    buttons.hide();
    $("#error").hide();
    print();
    buttons.show();
}