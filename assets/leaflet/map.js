var userCoord, tempMarker;

var map = L.map('map').setView([5, 5], 4);
        L.tileLayer('cmap/{z}/{x}/{y}.png', {
            minZoom: 2,
            maxZoom: 5,
            attribution: 'PolyFinder',
			continuousWorld: true,
			unloadInvisibleTiles: true,
            tms: true
        }).addTo(map);


//setbounds
var southWest = new L.LatLng(-85.02, -178.24),
    northEast = new L.LatLng(-.75, 31.64),
    bounds = new L.LatLngBounds(southWest, northEast);
map.setMaxBounds(bounds);
map.panTo(new L.LatLng(-65.737, -73.923));

// Firebase stuff
var polyfinderData = new Firebase('https://polyfindertest.firebaseio.com');
polyfinderData.on('value', function(snapshot) {
	console.log(snapshot.val());
	var events = snapshot.val();
	for (var i = 0; i < events.length; i++) {
		createEvent(events[i].message, new L.LatLng(events[i].coordinates.x, events[i].coordinates.y), events[i].id);
	}
});


function addButton(){
	map.on('click', onMapClick); 
}

function yesButton(){
	map.off('click', onMapClick);
	tempMarker = null;
}

function noButton(){
	if (tempMarker != null) {
   		map.removeLayer(tempMarker);
   	}
	map.off('click', onMapClick);
}


function createEvent(txt, lat, id){
	var marker = L.marker(lat).addTo(map);
	marker.bindPopup(txt);
    marker.eventID = id;
    console.log(marker);
}

// Get coordinates
//-1 - top, -179 - left, 31 - right, -89 - bottom  
// b8: (-44.80912, -121.37695), b94: (-41.40978, -106.30371)

function onMapClick(e) {
   	console.log(e.latlng);
   	if (tempMarker != null) {
   		map.removeLayer(tempMarker);
   	}
   	tempMarker = L.marker(e.latlng).addTo(map);
	tempMarker.on('click', openDialog);
	//tempMarker.bindPopup("Test");
}

function openDialog(){
	WebViewInterface.eventDialog("Developer", "Marti came 1 hour late. Raymond came 1 hour and 30 minutes late and Allan came 2 hours late");
}

function alertMe(){
	alert("hey");
}
