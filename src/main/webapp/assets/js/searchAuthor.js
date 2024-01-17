let clickedValues = new Set();
function searchProducts(pippo) {
    let searchValue = document.getElementById("searchBox").value;

    if (searchValue.length >= 2) {
        $.get('SearchAuthor', {"query": searchValue},
            function(resp) { // on sucess
                displayResults(resp,pippo);
            })
            .fail(function() { // on failure
                alert("Request failed.");
            });
    } else {
        $("#suggestions").empty();
    }
}

function displayResults(authors,pippo) {

    $("#suggestions").empty();

    if (authors.length > 0) {

        $.each(authors, function(id,author) {
            if(!pairExists(clickedValues,author.id,author.email) && author.email!==pippo) {


                $("#suggestions").append($('<li/>', {
                    class: 'riga-tabella',
                    html: '<div class="pippo"><input type="hidden" value="' + author.id + '"><b>' + author.email + '</b></div>'
                })).on("click", ".riga-tabella", function () {
                    let id = $(this).find('input').val();
                    let email = $(this).find('b').text();
                    if (!pairExists(clickedValues, id, email)) {
                        clickedValues.add([id, email]);
                        checkAdded(clickedValues);
                    }



                });
            }
        });
    } else {
        $("#suggestions").append("No results found.");
    }
}
function checkAdded(clickedValues){

    let resultList = $("#effective-authors");
    let authors= $("#authors");
    resultList.empty();
    authors.empty();
    clickedValues.forEach(pair => {
        let [id, email] = pair;

        // Create a new <li> element for each pair
        let newLi = $('<li/>', {
            html: '<div>'+email+' <img src="assets/images/x-mark.png" onclick="deleteAuthor(\''+id+'\',\''+email+'\')" class="icona"></div>'
        });
        let newop=$('<option/>',
        {
            value: id
        });
        // Append the <li> to the specific <ul> (resultList)
        resultList.append(newLi);
        authors.append(newop);
        newop.prop('selected', true);



    })


}
function pairExists(set, id, email) {
    for (let existingPair of set) {
        if (existingPair[0] === id && existingPair[1] === email) {
            return true;
        }
    }
    return false;
}

function deleteAuthor(id, email) {
    let resultList = $("#effective-authors");
    resultList.find('li').each(function() {
        if ($(this).text().trim() === email) {
            $(this).remove();
        }
    });
    deletePair(clickedValues,id,email);
    checkAdded(clickedValues);
    console.log(clickedValues);
}

function deletePair(set, id, email) {
    for (let existingPair of set) {
        if (existingPair[0] === id && existingPair[1] === email) {
            set.delete(existingPair);
            break;
        }
    }
}



