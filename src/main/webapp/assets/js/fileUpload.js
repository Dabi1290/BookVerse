window.onload = function() {
    document.getElementById('e-book').addEventListener('change', function() {
        let fileInput = document.getElementById("e-book");
        let fileName = fileInput.files[0].name;
        let uploadTextImage=document.getElementById('upload-check-file');
        let uploadText=document.getElementById('upload-text-file');
        let image=uploadTextImage.getElementsByTagName('img')[0];


        document.getElementById('upload-phrase-file').innerText = fileName;
        document.getElementById("e-book-button").style.display = 'none';
        uploadTextImage.style.display="flex";
        if(fileName.endsWith('.pdf')) {
            document.getElementById('e-book-icon').src = 'assets/images/pdfUploaded.png';
            uploadText.style.color="#32E886"
            image.src="assets/images/greenCheck.png";
            uploadText.innerText="Upload complete";
        }
        else{
            document.getElementById('e-book-icon').src = 'assets/images/upload.png';
            uploadText.style.color="#EE1515"
            image.src="assets/images/redCheck.png";
            uploadText.innerText="The file format must \nbe in pdf";
        }

    });

    document.getElementById('cover').addEventListener('change', function() {
        let fileInput = document.getElementById("cover");
        let fileName = fileInput.files[0].name;
        let uploadTextImage=document.getElementById('upload-check-cover');
        let uploadText=document.getElementById('upload-text-cover');
        let image=uploadTextImage.getElementsByTagName('img')[0];




        document.getElementById('upload-phrase-cover').innerText = fileName;
        document.getElementById("cover-button").style.display = 'none';
        uploadTextImage.style.display="flex";
        if(fileName.endsWith('.png')) {
            document.getElementById('cover-icon').src = 'assets/images/pngUploaded.png';
            uploadText.style.color="#32E886"
            image.src="assets/images/greenCheck.png";
            uploadText.innerText="Upload complete";
        }
        else{
            document.getElementById('cover-icon').src = 'assets/images/upload.png';
            uploadText.style.color="#EE1515"
            image.src="assets/images/redCheck.png";
            uploadText.innerText="The file format must \nbe in png";
        }
    });
}
