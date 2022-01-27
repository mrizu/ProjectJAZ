function getCookie(name) {
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    }
    else
    {
        begin += 2;
        var end = document.cookie.indexOf(";", begin);
        if (end == -1) {
            end = dc.length;
        }
    }
    return decodeURI(dc.substring(begin + prefix.length, end));
}


window.addEventListener('DOMContentLoaded', (event) => {
    if (getCookie("error_authenticating") == null || getCookie("error_authenticating") == "false") {
        document.getElementById("error").style.display = "none";
    } else {
        document.getElementById("error").style.visibility = "visible";
        document.cookie = "error_authenticating=false"
    }
});