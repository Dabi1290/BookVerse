let error = document.getElementById("error");
function checkEbook(){
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
        return true;
    }
    else{
        document.getElementById('e-book-icon').src = 'assets/images/upload.png';
        uploadText.style.color="#EE1515"
        image.src="assets/images/redCheck.png";
        uploadText.innerText="The file format must \nbe in pdf";
        return false;
    }
}
function checkCover(){
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
        return true;
    }
    else{
        document.getElementById('cover-icon').src = 'assets/images/upload.png';
        uploadText.style.color="#EE1515"
        image.src="assets/images/redCheck.png";
        uploadText.innerText="The file format must \nbe in png";
        return false;
    }
}
window.onload = function() {
    document.getElementById('e-book').addEventListener('change', function() {


    });

    document.getElementById('cover').addEventListener('change', function() {

    });
}

function checkTitle(){
    let title = document.getElementById("title");
    if(title.value.length>30){
        error.value = "The title should have a maximum size of 30 characters";
        return false;
    }
    return true;
}
function checkPrice(){
    let price = document.getElementById("price");
    if(price.value<0 || price.value>500){
        error.value = "The price must be in a range between 0 and 500";
        return false;
    }
    return true;
}

