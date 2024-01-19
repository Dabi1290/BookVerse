
window.onload = function() {
    let error = document.getElementById("error");
}
let fileSize = 500 * 1048576
function checkEbook(){
    let fileInput = document.getElementById("e-book");
    if (fileInput.files.length > 0) {
        let fileName = fileInput.files[0].name;
        let uploadTextImage=document.getElementById('upload-check-file');
        let uploadText=document.getElementById('upload-text-file');
        let image=uploadTextImage.getElementsByTagName('img')[0];
        document.getElementById('upload-phrase-file').innerText = fileName;
        document.getElementById("e-book-button").style.display = 'none';
        uploadTextImage.style.display="flex";

        if(fileName.endsWith('.pdf') && fileInput.files[0].size<fileSize) {
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
            uploadText.innerText="The file format must \nbe in pdf and a maximum size of 500 mb";
            return false;
        }
    } else {

        error.innerText = "You must upload a file for the e-book";
        return false;
    }
}
function checkCover(){
    let fileInput = document.getElementById("cover");
    if (fileInput.files.length > 0) {
    let fileName = fileInput.files[0].name;
    let uploadTextImage=document.getElementById('upload-check-cover');
    let uploadText=document.getElementById('upload-text-cover');
    let image=uploadTextImage.getElementsByTagName('img')[0];




    document.getElementById('upload-phrase-cover').innerText = fileName;
    document.getElementById("cover-button").style.display = 'none';
    uploadTextImage.style.display="flex";
    if(fileName.endsWith('.png')  && fileInput.files[0].size<fileSize) {
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
        uploadText.innerText="The file format must \nbe in png and a maximum size of 500 mb";
        return false;
    }
    }
    else{
        error.innerText = "You must upload a file for the cover";
        return false;
    }
}

function checkTitle(){
    let title = document.getElementById("title");
    if(title.value.length>30){
        error.innerText = "The title should have a maximum size of 30 characters";
        return false;
    }
    error.innerText ="";
    return true;
}
function checkPrice(){
    let price = document.getElementById("price");
    if(price.value<0 || price.value>500){
        error.innerText = "The price must be in a range between 0 and 500";
        return false;
    }
    error.innerText ="";
    return true;
}
function checkDescription(){
    let description = document.getElementById("description");
    console.log(description.value);
    if(description.value.length>500 || description.value.length===0 ){
        error.innerText = "The description should have a maximum size of 500 characters";
        return false;
    }
    error.innerText ="";
    return true;
}

function  checkGenres(){


        var checkboxes = document.querySelectorAll('input[name="genres"][type="checkbox"]');


        for (var i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                return true;
            }
        }
        error.innerText = "At least one genre must be selected";
        return false;

}

function send(){
    if(checkEbook() && checkCover() && checkTitle() && checkPrice() && checkDescription() && checkGenres()){
        let form=document.getElementById("form-pippo");
        form.submit();
    }
}
function sendValidator(){
    if(checkEbook()){
        let form=document.getElementById("form-report");
        form.submit();
    }
}


