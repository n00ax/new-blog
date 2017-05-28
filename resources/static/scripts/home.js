bindLinks();
function bindLinks(){
    $('#first-post').click(function(e){
        window.location = $('#first-post-url').text()
    })
    $('#second-post').click(function(e){
        window.location = ($('#second-post-url').text())
    })
    $('#third-post').click(function(e){
        window.location = ($('#third-post-url').text())
    })
}