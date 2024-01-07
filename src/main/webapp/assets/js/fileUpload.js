window.onload = function() {
    document.getElementById('e-book').addEventListener('change', function() {
        // Change the class of the element here
        document.getElementById('e-book-icon').src = 'assets/images/pdfUploaded.png';
        document.getElementById('upload-phrase-file').innerText = document.getElementById("e-book").files[0].name;
        document.getElementById("e-book-button").style.display = 'none';
        document.getElementById('upload-check-file').style.display="flex";
    });

    document.getElementById('cover').addEventListener('change', function() {
        // Change the class of the element here
        document.getElementById('cover-icon').src = 'assets/images/pngUploaded.png';
        document.getElementById('upload-phrase-cover').innerText = document.getElementById("cover").files[0].name;
        document.getElementById("cover-button").style.display = 'none';
        document.getElementById('upload-check-cover').style.display="flex";
    });
}
