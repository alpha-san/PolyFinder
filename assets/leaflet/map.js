var map = L.map('map').setView([5, 5], 2);
        L.tileLayer('cmap/{z}/{x}/{y}.png', {
            minZoom: 2,
            maxZoom: 5,
            attribution: 'PolyFinder',
			continuousWorld: true,
			unloadInvisibleTiles: true,
            tms: true
        }).addTo(map);
	map.panTo(new L.LatLng(-65.737, -73.923));
	
// Firebase stuff
var polyfinderData = new Firebase('https://polyfindertest.firebaseio.com/events');
polyfinderData.on('value', function(snapshot) {
	console.log(snapshot.val());
	var events = snapshot.val();
	for (var i = 0; i < events.length; i++) {
		createEvent(events[i].message, new L.LatLng(events[i].coordinates.x, events[i].coordinates.y), events[i].id);
	}
});

map.on('click', onMapClick);  

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
    alert("You clicked the map at " + e.latlng);
}

function addButtons() {
	var btn=document.createElement("BUTTON");
	var t=document.createTextNode("CLICK ME");
	btn.appendChild(t);
}
