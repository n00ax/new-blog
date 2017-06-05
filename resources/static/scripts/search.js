bindSearchHandlers()
// I would have implemented this in Clojurescript, but I was lazy and it was 2am
// JQUERY Galore, I would have used react, but it seemed too heavy weight..

function bindSearchHandlers(){
    $("#searchInput").on("keyup", function(i){
        lookupQuery($("#searchInput").val())
    });
}
function lookupQuery(lookupID){
    $.get("/lookup?lookup-id="+lookupID, function(i){
        $.get("/lookup-assoc-date?lookup-id="+lookupID, function(dateData){
            var keypressElementsDate = dateData.split(',')
            var keypressElements = i.split(',')
            for(a=0;a<keypressElements.length;a++){
                modifySearchResultEntry(a, keypressElements[a], 
                                            keypressElementsDate[a], keypressElements[a])
            }
            for(b=a;b<8;b++){
                nullifySearchResultEntry(b)
            }
    })});
}
function modifySearchResultEntry(position, entryName, entryDate, entryLink){
    var entrySelected = $("div.search-result").toArray();
    entrySelected[position].children[0].innerText = entryName;
    entrySelected[position].children[1].innerText = entryDate;
    entrySelected[position].onclick = function(e){
        window.location = "entries/" + entryLink + "/post";
    }
}
function nullifySearchResultEntry(position){
    var entrySelected = $("div.search-result").toArray();
    entrySelected[position].children[0].innerText = ""
    entrySelected[position].children[1].innerText = ""
    entrySelected[position].onclick = null;
}