function textFixer(athis, a, b) {
    athis.value = athis.value.replace(/\,/g, ".").replace(/[^\d\.\-]/g, "");
    while (athis.value.lastIndexOf('-') > 0) {
        athis.value = athis.value.substr(0, athis.value.lastIndexOf('-'));
    }
    let x = parseFloat(athis.value);
    while (x > b || x < a) {
        athis.value = athis.value.substr(0, athis.value.length - 1);
        x = parseFloat(athis.value);
    }
    while ((athis.value[0] == '-' && (athis.value[1] == '.' || athis.value.lastIndexOf('.') > 2)) || (athis.value[0] != '-' && (athis.value[0] == '.' || athis.value.lastIndexOf('.') > 1))) {
        athis.value = athis.value.substr(0, athis.value.lastIndexOf('.'));
    }
}

function fixY() {
    textFixer(this, -5, 5);
}

function fixR() {
    textFixer(this, 2, 5);
}

document.querySelector("#y-textinput").onkeyup = fixY;
document.querySelector("#r-textinput").onkeyup = fixR;
for (let i = 1; i < 10; i++) {
    document.querySelector("#x-button"+i).onclick = function() {
        document.querySelector("#xlabel").innerHTML = "X = "+(i-5)+":";
        document.querySelector("#x-value").value = i-5;
    }
}
document.querySelector("#resetter").onclick = function() {
    document.querySelector("#xlabel").innerHTML = "X:";
    document.querySelector("#x-value").value = "";
}
document.querySelector("#grofik").onclick = function(event) {
    let r = document.querySelector("#r-textinput").value;
    if (r === "") {
        alert("Сперва надо ввести радиус!");
        return;
    }
    let x = event.pageX-$("#grofik").offset().left;
    let y = event.pageY-$("#grofik").offset().top;
    const cx = 105.25;
    const cy = 107.328125;
    const cr = 80;
    x = (x-cx);
    y = (y-cx);
    const dx = -1;
    const dy = -112.328125;
    document.querySelector("#tochka").setAttribute("style", `position:relative;left:${dx+x}px;top:${dy+y}px;`);
    x /= cr/r;
    y /= -cr/r;
    document.querySelector("#xlabel").innerHTML = "X = "+(x)+":";
    document.querySelector("#x-value").value = x;
    document.querySelector("#y-textinput").value = y;
    $.ajax({
        method: 'post',
        dataType: 'text',
        data: `x-value=${x}&y-value=${y}&rg-value=${r}`,
        success: function (data) {
            $('#result-table').append(data);
            if (data.includes('Есть')) {
                document.querySelector("#img-tochka").setAttribute("src", "green.png");
            } else {
                document.querySelector("#img-tochka").setAttribute("src", "red.png");
            }
        }
    });
}

function validateX() {
    let thex = document.querySelector("#x-value").value;
    return (["-4", "-3", "-2", "-1", "0", "1", "2", "3", "4"]).includes(thex);
}

function validateR() {
    let val = document.getElementById('r-textinput').value;
    console.log("r-textinput: "+val);
    if (val === null || val === undefined || val === "") {
        return false;
    }
    return true;
}

function validateY() {
    let val = document.getElementById('y-textinput').value;
    console.log("y-textinput: "+val);
    if (val === null || val === undefined || val === "") {
        return false;
    }
    return true;
}

document.querySelectorAll('input[value="Всё удалить"]')[0].addEventListener('click', function() {
    $.ajax({
        method: 'post',
        dataType: 'text',
        data: `totalreset`,
        success: function () {
            location.reload();
        }
    });
});

$('#input-form').on('submit', function (event) {
    if (!(validateX() & validateY() & validateR())) {
        alert("Не все данные введены.");
        event.preventDefault();
        return;
    }
});