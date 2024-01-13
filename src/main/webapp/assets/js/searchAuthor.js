function searchProducts() {
    let searchValue = document.getElementById("searchBox").value;

    if (searchValue.length >= 2) {
        $.get('SearchAuthor', {"query": searchValue},
            function(resp) { // on sucess
                displayResults(resp);
            })
            .fail(function() { // on failure
                alert("Request failed.");
            });
    } else {
        $("#suggestions").empty();
    }
}

function displayResults(authors) {
    $("#suggestions").empty();

    if (authors.length > 0) {

        $.each(authors, function(id,author) {



            $("#suggestions").append($('<li />', {html: '<div class="pippo"><input type="hidden" value="'+author.id+'"><b>' + author.email + '</b></div>'}));
            // And then for every band add a list of their albums.

        });
    } else {
        $("#suggestions").append("No results found.");
    }
}