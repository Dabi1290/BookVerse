window.onload = function() {
    document.getElementById('e-book').addEventListener('change', function() {
        // Change the class of the element here
        document.getElementById('e-book-icon').className = 'icon-uploaded-pdf';
        document.getElementById('upload-phrase').innerText = document.getElementById("e-book").files[0].name;
    });
}
