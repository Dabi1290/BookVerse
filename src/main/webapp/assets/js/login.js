window.onload = function() {
//Reader
    let r = document.getElementById("r");
//Validator
    let v = document.getElementById("v");
//Author
    let a = document.getElementById("a");
//CManager
    let c = document.getElementById("c");
    let role = document.getElementById("role");
    let form = document.getElementById("form");
r.addEventListener("click", function() {
        role.value="Reader";
        form.submit();
    });
a.addEventListener("click", function() {
        role.value="Author";
        form.submit();
    });
v.addEventListener("click", function() {
        role.value="Validator";
        form.submit();
    });
}

