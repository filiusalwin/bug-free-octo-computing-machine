// This script is from https://codepen.io/559wade/pen/LRzEjj

$("input[data-type='currency']").on({
    keyup: function() {
        formatCurrency($(this));
    },
    blur: function() {
        formatCurrency($(this), "blur");
    }
});


function formatNumber(n) {
  // format number 1000000 to 1,234,567
  return n.replace(/\D/g, "")
}


function formatCurrency(input, blur) {
  // appends $ to value, validates decimal side
  // and puts cursor back in right position.

  // get input value
  var input_val = input.val();

  // don't validate empty input
  if (input_val === "") { return; }

  // original length
  var original_len = input_val.length;

  // initial caret position
  var caret_pos = input.prop("selectionStart");

  var decimal_pos = input_val.indexOf(".");
  var comma_pos = input_val.indexOf(",");
  if (decimal_pos == -1) {
    decimal_pos = comma_pos;
  }

  // check for decimal
  if (decimal_pos >= 0) {

    // get position of first decimal
    // this prevents multiple decimals from
    // being entered

    // split number by decimal point
    var left_side = input_val.substring(0, decimal_pos);
    var right_side = input_val.substring(decimal_pos);

    // add commas to left side of number
    left_side = formatNumber(left_side);

    // validate right side
    right_side = formatNumber(right_side);

    // On blur make sure 2 numbers after decimal
    if (blur === "blur") {
      right_side += "00";
    }

    // Limit decimal to only 2 digits
    right_side = right_side.substring(0, 2);

    // join number by .
    input_val = "€" + left_side + "." + right_side;

  } else {
    // no decimal entered
    // add commas to number
    // remove all non-digits
    input_val = formatNumber(input_val);
    input_val = "€" + input_val;

    // final formatting
    if (blur === "blur") {
      input_val += ".00";
    }
  }

  // send updated string to input
  input.val(input_val);

  // put caret back in the right position
  var updated_len = input_val.length;
  caret_pos = updated_len - original_len + caret_pos;
  input[0].setSelectionRange(caret_pos, caret_pos);
}

function formatCurrencyString(input_val, fullDisplay) {
    input_val = input_val / 100;
    input_val = input_val.toString();

   // original length
     var original_len = input_val.length;

     var decimal_pos = input_val.indexOf(".");
     var comma_pos = input_val.indexOf(",");
     if (decimal_pos == -1) {
       decimal_pos = comma_pos;
     }

     // check for decimal
     if (decimal_pos >= 0) {

       // get position of first decimal
       // this prevents multiple decimals from
       // being entered

       // split number by decimal point
       var left_side = input_val.substring(0, decimal_pos);
       var right_side = input_val.substring(decimal_pos);

       // add commas to left side of number
       left_side = formatNumber(left_side);

       // validate right side
       right_side = formatNumber(right_side);

       // On blur make sure 2 numbers after decimal
       if (blur === "blur") {
         right_side += "00";
       }

       // Limit decimal to only 2 digits
       right_side = right_side.substring(0, 2);
       if (right_side.length == 1) {
           right_side += "0";
       }

       // join number by .
       input_val = "€" + left_side + "." + right_side;

     } else {
       // no decimal entered
       // add commas to number
       // remove all non-digits
       input_val = formatNumber(input_val);
       input_val = "€" + input_val;

       // final formatting
       if (blur === "blur" || fullDisplay) {
         input_val += ".00";
       }
     }
   return input_val;
}
